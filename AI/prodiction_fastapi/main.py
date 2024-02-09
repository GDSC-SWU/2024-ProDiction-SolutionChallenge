from fastapi import FastAPI, File, UploadFile
from google.cloud import speech_v1p1beta1 as speech
from google.cloud import texttospeech
from google.oauth2 import service_account
from starlette.responses import FileResponse
from pydub import AudioSegment
import itertools
import os
import librosa
import noisereduce as nr
import numpy as np
import soundfile as sf

app = FastAPI()

key_path = 'pro-diction-413410-a8e94d966bfb.json'

credentials = service_account.Credentials.from_service_account_file(
        key_path,
        scopes=["https://www.googleapis.com/auth/cloud-platform"],
    )

__all__ = ["split_syllable_char", "split_syllables",
           "join_jamos", "join_jamos_char",
           "CHAR_INITIALS", "CHAR_MEDIALS", "CHAR_FINALS"]

INITIAL = 0x001
MEDIAL = 0x010
FINAL = 0x100
CHAR_LISTS = {
    INITIAL: list(map(chr, [
        0x3131, 0x3132, 0x3134, 0x3137, 0x3138, 0x3139,
        0x3141, 0x3142, 0x3143, 0x3145, 0x3146, 0x3147,
        0x3148, 0x3149, 0x314a, 0x314b, 0x314c, 0x314d,
        0x314e
    ])),
    MEDIAL: list(map(chr, [
        0x314f, 0x3150, 0x3151, 0x3152, 0x3153, 0x3154,
        0x3155, 0x3156, 0x3157, 0x3158, 0x3159, 0x315a,
        0x315b, 0x315c, 0x315d, 0x315e, 0x315f, 0x3160,
        0x3161, 0x3162, 0x3163
    ])),
    FINAL: list(map(chr, [
        0x3131, 0x3132, 0x3133, 0x3134, 0x3135, 0x3136,
        0x3137, 0x3139, 0x313a, 0x313b, 0x313c, 0x313d,
        0x313e, 0x313f, 0x3140, 0x3141, 0x3142, 0x3144,
        0x3145, 0x3146, 0x3147, 0x3148, 0x314a, 0x314b,
        0x314c, 0x314d, 0x314e
    ]))
}

CHAR_INITIALS = CHAR_LISTS[INITIAL]
CHAR_MEDIALS = CHAR_LISTS[MEDIAL]
CHAR_FINALS = CHAR_LISTS[FINAL]
CHAR_SETS = {k: set(v) for k, v in CHAR_LISTS.items()}
CHARSET = set(itertools.chain(*CHAR_SETS.values()))
CHAR_INDICES = {k: {c: i for i, c in enumerate(v)}
                for k, v in CHAR_LISTS.items()}


@app.get("/")
async def root():
    return {"message": "Hello World"}


@app.post("/noisereduce/")
async def noisereduce(file: UploadFile = File(...)):
    # 임시 파일 경로 설정
    file_path = f"temp_{file.filename}"
    # 파일 저장
    with open(file_path, "wb") as f:
        f.write(file.file.read())

    # 오디오 파일 로드
    audio, sr = sf.read(file_path)

    # 잡음 제거
    reduced_noise = nr.reduce_noise(audio, sr)

    # 출력 파일 이름 설정
    output_file = "noise_reduced.wav"
    # 잡음이 제거된 파일 저장
    sf.write(output_file, reduced_noise, sr)

    # 임시 파일 삭제
    os.remove(file_path)

    # 잡음이 제거된 wav 파일 반환
    return FileResponse(output_file, media_type="audio/wav", filename=output_file)


@app.post("/speechtotext/")
async def speechtotext(file: UploadFile = File(...)):
    return process_audio_file(file)


@app.post("/speechtotext_syllables/")
async def speechtotext_syllables(file: UploadFile = File(...)):
    # 파일 저장
    file_path = f"temp_{file.filename}"
    with open(file_path, "wb") as f:
        f.write(file.file.read())

    result = process_audio(file_path, amplitude_threshold=0.05, merge_distance=0.2, front_padding=0.5, back_padding=0.5)

    # 파일 삭제 (옵션)
    os.remove(file_path)

    return result


@app.get("/texttospeech/{text}")
async def text_to_speech(text: str):
    client = texttospeech.TextToSpeechClient(credentials=credentials)

    synthesis_input = texttospeech.SynthesisInput(text=text)
    voice = texttospeech.VoiceSelectionParams(
        language_code="ko-KR",
        name="ko-KR-Wavenet-D",
        ssml_gender=texttospeech.SsmlVoiceGender.NEUTRAL,
    )
    audio_config = texttospeech.AudioConfig(
        audio_encoding=texttospeech.AudioEncoding.LINEAR16
    )
    response = client.synthesize_speech(
        input=synthesis_input, voice=voice, audio_config=audio_config
    )
    output_file = "processed_audio.wav"
    with open(output_file, "wb") as out_file:
        out_file.write(response.audio_content)

    return FileResponse(output_file, media_type="audio/wav")


@app.get("/joinjamos/{text}")
async def joinjamos(text: str):
    return join_jamos(text)


@app.get("/splitjamos/{text}")
async def splitjamos(text: str):
    return split_syllables(text)


def process_audio_file(file: UploadFile):
    client = speech.SpeechClient(credentials=credentials)
    content = file.file.read()

    audio = speech.RecognitionAudio(content=content)

    config = speech.RecognitionConfig(
        encoding=speech.RecognitionConfig.AudioEncoding.LINEAR16,
        sample_rate_hertz=44100,
        language_code="ko-KR",
        enable_word_confidence=True,
    )

    response = client.recognize(config=config, audio=audio)

    # 결과가 있을 때만 처리
    if response.results:
        alternative = response.results[0].alternatives[0]
        # print("-" * 20)
        # print(f"First alternative of result 0")
        # print(f"Transcript: {alternative.transcript}")
        # print(
        #     "First Word and Confidence: ({}, {})".format(
        #         alternative.words[0].word, alternative.words[0].confidence
        #     )
        # )
        return alternative.transcript
    else:
        return "No speech detected."


def process_audio_file_syllable(file_path):
    client = speech.SpeechClient(credentials=credentials)

    # 파일 내용을 읽어오기
    with open(file_path, "rb") as audio_file:
        content = audio_file.read()

    audio = speech.RecognitionAudio(content=content)

    config = speech.RecognitionConfig(
        encoding=speech.RecognitionConfig.AudioEncoding.LINEAR16,
        sample_rate_hertz=44100,
        language_code="ko-KR",
        enable_word_confidence=True,
    )

    response = client.recognize(config=config, audio=audio)

    # 결과가 있을 때만 처리
    if response.results:
        alternative = response.results[0].alternatives[0]
        # print("-" * 20)
        # print(f"First alternative of result 0")
        # print(f"Transcript: {alternative.transcript}")
        # print(
        #     "First Word and Confidence: ({}, {})".format(
        #         alternative.words[0].word, alternative.words[0].confidence
        #     )
        # )
        return alternative.transcript
    else:
        return "No speech detected"


def is_hangul_syllable(c):
    return 0xac00 <= ord(c) <= 0xd7a3  # Hangul Syllables


def is_hangul_jamo(c):
    return 0x1100 <= ord(c) <= 0x11ff  # Hangul Jamo


def is_hangul_compat_jamo(c):
    return 0x3130 <= ord(c) <= 0x318f  # Hangul Compatibility Jamo


def is_hangul_jamo_exta(c):
    return 0xa960 <= ord(c) <= 0xa97f  # Hangul Jamo Extended-A


def is_hangul_jamo_extb(c):
    return 0xd7b0 <= ord(c) <= 0xd7ff  # Hangul Jamo Extended-B


def is_hangul(c):
    return (is_hangul_syllable(c) or
            is_hangul_jamo(c) or
            is_hangul_compat_jamo(c) or
            is_hangul_jamo_exta(c) or
            is_hangul_jamo_extb(c))


def is_supported_hangul(c):
    return is_hangul_syllable(c) or is_hangul_compat_jamo(c)


def check_hangul(c, jamo_only=False):
    if not ((jamo_only or is_hangul_compat_jamo(c)) or is_supported_hangul(c)):
        raise ValueError(f"'{c}' is not a supported hangul character. "
                         f"'Hangul Syllables' (0xac00 ~ 0xd7a3) and "
                         f"'Hangul Compatibility Jamos' (0x3130 ~ 0x318f) are "
                         f"supported at the moment.")


def get_jamo_type(c):
    check_hangul(c)
    assert is_hangul_compat_jamo(c), f"not a jamo: {ord(c):x}"

    return sum(t for t, s in CHAR_SETS.items() if c in s)


def split_syllable_char(c):
    """
    Splits a given korean syllable into its components. Each component is
    represented by Unicode in 'Hangul Compatibility Jamo' range.

    Arguments:
        c: A Korean character.

    Returns:
        A triple (initial, medial, final) of Hangul Compatibility Jamos.
        If no jamo corresponds to a position, `None` is returned there.

    Example:
        >>> split_syllable_char("안")
        ("ㅇ", "ㅏ", "ㄴ")
        >>> split_syllable_char("고")
        ("ㄱ", "ㅗ", None)
        >>> split_syllable_char("ㅗ")
        (None, "ㅗ", None)
        >>> split_syllable_char("ㅇ")
        ("ㅇ", None, None)
    """
    check_hangul(c)
    if len(c) != 1:
        raise ValueError("Input string must have exactly one character.")

    init, med, final = None, None, None
    if is_hangul_syllable(c):
        offset = ord(c) - 0xac00
        x = (offset - offset % 28) // 28
        init, med, final = x // 21, x % 21, offset % 28
        if not final:
            final = None
        else:
            final -= 1
    else:
        pos = get_jamo_type(c)
        if pos & INITIAL == INITIAL:
            pos = INITIAL
        elif pos & MEDIAL == MEDIAL:
            pos = MEDIAL
        elif pos & FINAL == FINAL:
            pos = FINAL
        idx = CHAR_INDICES[pos][c]
        if pos == INITIAL:
            init = idx
        elif pos == MEDIAL:
            med = idx
        else:
            final = idx

    return tuple(CHAR_LISTS[pos][idx] if idx is not None else None
                 for pos, idx in
                 zip([INITIAL, MEDIAL, FINAL], [init, med, final]))


def split_syllables(s, ignore_err=True, pad=None):
    """
    Performs syllable-split on a string.

    Arguments:
        s (str): A string (possibly mixed with non-Hangul characters).
        ignore_err (bool): If set False, it ensures that all characters in
            the string are Hangul-splittable and throws a ValueError otherwise.
            (default: True)
        pad (str): Pad empty jamo positions (initial, medial, or final) with
            `pad` character. This is useful for cases where fixed-length
            strings are needed. (default: None)

    Returns:
        Hangul-split string

    Example:
        >>> split_syllables("안녕하세요")
        "ㅇㅏㄴㄴㅕㅇㅎㅏㅅㅔㅇㅛ"
        >>> split_syllables("안녕하세요~~", ignore_err=False)
        ValueError: encountered an unsupported character: ~ (0x7e)
        >>> split_syllables("안녕하세요ㅛ", pad="x")
        'ㅇㅏㄴㄴㅕㅇㅎㅏxㅅㅔxㅇㅛxxㅛx'
    """

    def try_split(c):
        try:
            return split_syllable_char(c)
        except ValueError:
            if ignore_err:
                return (c,)
            raise ValueError(f"encountered an unsupported character: "
                             f"{c} (0x{ord(c):x})")

    s = map(try_split, s)
    if pad is not None:
        tuples = map(lambda x: tuple(pad if y is None else y for y in x), s)
    else:
        tuples = map(lambda x: filter(None, x), s)

    return "".join(itertools.chain(*tuples))


def join_jamos_char(init, med, final=None):
    """
    Combines jamos into a single syllable.

    Arguments:
        init (str): Initial jao.
        med (str): Medial jamo.
        final (str): Final jamo. If not supplied, the final syllable is made
            without the final. (default: None)

    Returns:
        A Korean syllable.
    """
    chars = (init, med, final)
    for c in filter(None, chars):
        check_hangul(c, jamo_only=True)

    idx = tuple(CHAR_INDICES[pos][c] if c is not None else c
                for pos, c in zip((INITIAL, MEDIAL, FINAL), chars))
    init_idx, med_idx, final_idx = idx
    # final index must be shifted once as
    # final index with 0 points to syllables without final
    final_idx = 0 if final_idx is None else final_idx + 1

    return chr(0xac00 + 28 * 21 * init_idx + 28 * med_idx + final_idx)


def join_jamos(s, ignore_err=True):
    """
    Combines a sequence of jamos to produce a sequence of syllables.

    Arguments:
        s (str): A string (possible mixed with non-jamo characters).
        ignore_err (bool): If set False, it will ensure that all characters
            will be consumed for the making of syllables. It will throw a
            ValueError when it fails to do so. (default: True)

    Returns:
        A string

    Example:
        >>> join_jamos("ㅇㅏㄴㄴㅕㅇㅎㅏㅅㅔㅇㅛ")
        "안녕하세요"
        >>> join_jamos("ㅇㅏㄴㄴㄴㅕㅇㅎㅏㅅㅔㅇㅛ")
        "안ㄴ녕하세요"
        >>> join_jamos()
    """
    last_t = 0
    queue = []
    new_string = ""

    def flush(n=0):
        new_queue = []
        while len(queue) > n:
            new_queue.append(queue.pop())
        if len(new_queue) == 1:
            if not ignore_err:
                raise ValueError(f"invalid jamo character: {new_queue[0]}")
            result = new_queue[0]
        elif len(new_queue) >= 2:
            try:
                result = join_jamos_char(*new_queue)
            except (ValueError, KeyError):
                # Invalid jamo combination
                if not ignore_err:
                    raise ValueError(f"invalid jamo characters: {new_queue}")
                result = "".join(new_queue)
        else:
            result = None

        return result

    for c in s:
        if c not in CHARSET:
            if queue:
                new_c = flush() + c
            else:
                new_c = c
            last_t = 0
        else:
            t = get_jamo_type(c)
            new_c = None
            if t & FINAL == FINAL:
                if not (last_t == MEDIAL):
                    new_c = flush()
            elif t == INITIAL:
                new_c = flush()
            elif t == MEDIAL:
                if last_t & INITIAL == INITIAL:
                    new_c = flush(1)
                else:
                    new_c = flush()
            last_t = t
            queue.insert(0, c)
        if new_c:
            new_string += new_c
    if queue:
        new_string += flush()

    return new_string


def find_high_amplitude_segments(y, threshold):
    high_amp_indices = np.where(np.abs(y) > threshold)[0]
    high_amp_segments = librosa.samples_to_time(high_amp_indices)

    return high_amp_segments


def merge_segments(segments, merge_distance=0.5):
    merged_segments = []
    current_segment = [segments[0]]

    for segment in segments[1:]:
        if segment - current_segment[-1][-1] <= merge_distance:
            # 인접한 덩어리를 합치기
            current_segment.append(segment)
        else:
            # 새로운 덩어리 시작
            merged_segments.append(np.concatenate(current_segment))
            current_segment = [segment]

    # 마지막 덩어리 추가
    merged_segments.append(np.concatenate(current_segment))

    return merged_segments


def cut_audio_segments(y, segments, front_padding=0.1, back_padding=0.1):
    cut_segments = []
    for segment in segments:
        start_idx = librosa.time_to_samples(segment[0] - front_padding)
        end_idx = librosa.time_to_samples(segment[-1] + back_padding)
        cut_segment = y[start_idx:end_idx]
        cut_segments.append(cut_segment)
    return cut_segments


def add_silence(audio, duration_ms):
    silence = AudioSegment.silent(duration=duration_ms)
    return silence + audio + silence


def process_audio(input_path, amplitude_threshold=0.1, merge_distance=0.5, front_padding=0.1, back_padding=0.1):
    # 오디오 파일 로드
    audio, sr = librosa.load(input_path, sr=None)

    # 잡음 제거
    reduced_noise = nr.reduce_noise(audio, sr)

    # 일정 진폭 이상인 덩어리 찾기
    high_amp_segments = find_high_amplitude_segments(reduced_noise, amplitude_threshold)

    # 작은 덩어리들 합치기
    merged_segments = merge_segments([np.array([segment]) for segment in high_amp_segments], merge_distance)

    # 찾은 덩어리를 기준으로 오디오를 자르기
    cut_segments = cut_audio_segments(reduced_noise, merged_segments, front_padding, back_padding)

    syllables_recognized = ""

    # 자른 음성 파일 저장
    for i, segment in enumerate(cut_segments):
        # 저장
        output_path = f"segment_{i}.wav"
        sf.write(output_path, segment, sr)

        # 원본 파일 로드
        original_audio = AudioSegment.from_file(output_path)

        # 시작 전 2초, 끝난 후 2초에 각각 2초의 silence 추가
        modified_audio = add_silence(original_audio, 2000)

        # 수정된 오디오를 파일로 저장
        modified_audio.export(output_path, format="wav")

        syllables_recognized += process_audio_file_syllable(output_path)
        syllables_recognized += " "

        # 파일 삭제 (옵션)
        os.remove(output_path)

    return syllables_recognized
