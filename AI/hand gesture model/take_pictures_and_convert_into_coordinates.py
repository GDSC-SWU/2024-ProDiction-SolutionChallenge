import cv2
import mediapipe as mp
import numpy as np
import keyboard
import time

from PIL import Image, ImageDraw, ImageFont

from unicode import join_jamos


font_path = "NanumGothic.ttf"
font_size = 30
max_num_hands = 1

gesture = {
    0: 'ㄱ', 1: 'ㄴ', 2: 'ㄷ', 3: 'ㄹ', 4: 'ㅁ', 5: 'ㅂ', 6: 'ㅅ', 7: 'ㅇ', 8: 'ㅈ', 9: 'ㅊ',
    10: 'ㅋ', 11: 'ㅌ', 12: 'ㅍ', 13: 'ㅎ',
    14: 'ㅏ', 15: 'ㅑ', 16: 'ㅓ', 17: 'ㅕ', 18: 'ㅗ', 19: 'ㅛ', 20: 'ㅜ', 21: 'ㅠ', 22: 'ㅡ', 23: 'ㅣ',
    24: 'ㅐ', 25: 'ㅒ', 26: 'ㅔ', 27: 'ㅖ', 28: 'ㅚ', 29: 'ㅟ', 30: 'ㅢ',
    31: 'spacing', 32: 'clear_one', 33: 'all_clear', 34: 'add'
}

mp_hands = mp.solutions.hands
mp_drawing = mp.solutions.drawing_utils
hands = mp_hands.Hands(
    max_num_hands=max_num_hands,
    min_detection_confidence=0.5,
    min_tracking_confidence=0.5
)

f = open('test.txt', 'w')
file = np.genfromtxt('dataSet.txt', delimiter=',')
angleFile = file[:, : -1]
labelFile = file[:, -1]
angle = angleFile.astype(np.float32)
label = labelFile.astype(np.float32)
knn = cv2.ml.KNearest_create()
knn.train(angle, cv2.ml.ROW_SAMPLE, label)
cap = cv2.VideoCapture(0)

startTime = time.time()
prev_index = 0
sentence = ''
recognizeDelay = 1

while True:
    ret, img = cap.read()

    if not ret:
        continue
    imgRGB = cv2.cvtColor(img, cv2.COLOR_BGR2RGB)
    result = hands.process(imgRGB)

    if result.multi_hand_landmarks is not None:
        for res in result.multi_hand_landmarks:
            joint = np.zeros((21, 3))
            for j, lm in enumerate(res.landmark):
                joint[j] = [lm.x, lm.y, lm.z]

            v1 = joint[[0, 1, 2, 3, 0, 5, 6, 7, 0, 9, 10, 11, 0, 13, 14, 15, 0, 17, 18, 19], :]
            v2 = joint[[1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20], :]

            v = v2 - v1
            v = v / np.linalg.norm(v, axis=1)[:, np.newaxis]
            compareV1 = v[[0, 1, 2, 4, 5, 6, 7, 8, 9, 10, 12, 13, 14, 16, 17], :]
            compareV2 = v[[1, 2, 3, 5, 6, 7, 9, 10, 11, 13, 14, 15, 17, 18, 19], :]
            angle = np.arccos(np.einsum('nt,nt->n', compareV1, compareV2))

            angle = np.degrees(angle)
            # a 누르면 현재 angle이 txt 파일에 저장
            if keyboard.is_pressed('a'):
                for num in angle:
                    num = round(num, 6)
                    f.write(str(num))
                    f.write(',')
                # 데이터 저장 label 번호
                f.write("0.000000")
                f.write('\n')
                print("next")
            data = np.array([angle], dtype=np.float32)
            ret, results, neighbours, dist = knn.findNearest(data, 3)
            index = int(results[0][0])

            if index in gesture.keys():
                if index != prev_index:
                    startTime = time.time()
                    prev_index = index
                else:
                    if time.time() - startTime > recognizeDelay:
                        if index == 31:
                            sentence += ' '
                        elif index == 32:
                            sentence = sentence[:-1]
                        elif index == 33:
                            sentence = ''
                        elif index == 34:
                            if sentence[-1] == 'ㄱ' and sentence[-2] == 'ㄱ':
                                sentence = sentence[:-2]
                                sentence += 'ㄲ'
                            elif sentence[-1] == 'ㄷ' and sentence[-2] == 'ㄷ':
                                sentence = sentence[:-2]
                                sentence += 'ㄸ'
                            elif sentence[-1] == 'ㅂ' and sentence[-2] == 'ㅂ':
                                sentence = sentence[:-2]
                                sentence += 'ㅃ'
                            elif sentence[-1] == 'ㅅ' and sentence[-2] == 'ㅅ':
                                sentence = sentence[:-2]
                                sentence += 'ㅆ'
                            elif sentence[-1] == 'ㅈ' and sentence[-2] == 'ㅈ':
                                sentence = sentence[:-2]
                                sentence += 'ㅉ'
                            elif sentence[-1] == 'ㅅ' and sentence[-2] == 'ㄱ':
                                sentence = sentence[:-2]
                                sentence += 'ㄳ'
                            elif sentence[-1] == 'ㅈ' and sentence[-2] == 'ㄴ':
                                sentence = sentence[:-2]
                                sentence += 'ㄵ'
                            elif sentence[-1] == 'ㅎ' and sentence[-2] == 'ㄴ':
                                sentence = sentence[:-2]
                                sentence += 'ㄶ'
                            elif sentence[-1] == 'ㄱ' and sentence[-2] == 'ㄹ':
                                sentence = sentence[:-2]
                                sentence += 'ㄺ'
                            elif sentence[-1] == 'ㅁ' and sentence[-2] == 'ㄹ':
                                sentence = sentence[:-2]
                                sentence += 'ㄻ'
                            elif sentence[-1] == 'ㅂ' and sentence[-2] == 'ㄹ':
                                sentence = sentence[:-2]
                                sentence += 'ㄼ'
                            elif sentence[-1] == 'ㅅ' and sentence[-2] == 'ㄹ':
                                sentence = sentence[:-2]
                                sentence += 'ㄽ'
                            elif sentence[-1] == 'ㅌ' and sentence[-2] == 'ㄹ':
                                sentence = sentence[:-2]
                                sentence += 'ㄾ'
                            elif sentence[-1] == 'ㅍ' and sentence[-2] == 'ㄹ':
                                sentence = sentence[:-2]
                                sentence += 'ㄿ'
                            elif sentence[-1] == 'ㅎ' and sentence[-2] == 'ㄹ':
                                sentence = sentence[:-2]
                                sentence += 'ㅀ'
                            elif sentence[-1] == 'ㅅ' and sentence[-2] == 'ㅂ':
                                sentence = sentence[:-2]
                                sentence += 'ㅄ'
                            elif sentence[-1] == 'ㅏ' and sentence[-2] == 'ㅗ':
                                sentence = sentence[:-2]
                                sentence += 'ㅘ'
                            elif sentence[-1] == 'ㅐ' and sentence[-2] == 'ㅗ':
                                sentence = sentence[:-2]
                                sentence += 'ㅙ'
                            elif sentence[-1] == 'ㅓ' and sentence[-2] == 'ㅜ':
                                sentence = sentence[:-2]
                                sentence += 'ㅝ'
                            elif sentence[-1] == 'ㅔ' and sentence[-2] == 'ㅜ':
                                sentence = sentence[:-2]
                                sentence += 'ㅞ'
                        else:
                            sentence += gesture[index]
                        startTime = time.time()

                pil_img = Image.fromarray(img)
                draw = ImageDraw.Draw(pil_img)
                font = ImageFont.truetype(font_path, font_size)

                text_position = (int(res.landmark[0].x * img.shape[1] - 10),
                                 int(res.landmark[0].y * img.shape[0] + 40))
                draw.text(text_position, gesture[index].upper(), font=font, fill=(255, 255, 255))

                img = np.array(pil_img)

            mp_drawing.draw_landmarks(img, res, mp_hands.HAND_CONNECTIONS)

    pil_img = Image.fromarray(np.uint8(img))
    draw = ImageDraw.Draw(pil_img)
    font = ImageFont.truetype(font_path, font_size)

    text_position = (20, 440)
    draw.text(text_position, join_jamos(sentence), font=font, fill=(255, 255, 255))

    img = np.array(pil_img)

    cv2.imshow('HandTracking', img)
    cv2.waitKey(1)

    # b 누르면 프로그램 종료
    if keyboard.is_pressed('b'):
        break
f.close()
