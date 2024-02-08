package com.example.pro_diction

import android.util.Log

object HangulConverter {
    private val CHOSUNG_START_UNICODE = 0x001
    private val JUNGSUNG_START_UNICODE = 0x010
    private val JONGSUNG_START_UNICODE = 0x100

    // 자음 리스트
    private val CHOSUNG_LIST = listOf(
        'ㄱ', 'ㄲ', 'ㄴ', 'ㄷ', 'ㄸ',
        'ㄹ', 'ㅁ', 'ㅂ', 'ㅃ', 'ㅅ',
        'ㅆ', 'ㅇ', 'ㅈ', 'ㅉ', 'ㅊ',
        'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ'
    )

    // 모음 리스트
    private val JUNGSUNG_LIST = listOf(
        'ㅏ', 'ㅐ', 'ㅑ', 'ㅒ', 'ㅓ',
        'ㅔ', 'ㅕ', 'ㅖ', 'ㅗ', 'ㅘ',
        'ㅙ', 'ㅚ', 'ㅛ', 'ㅜ', 'ㅝ',
        'ㅞ', 'ㅟ', 'ㅠ', 'ㅡ', 'ㅢ', 'ㅣ'
    )

    // 종성 리스트
    private val JONGSUNG_LIST = listOf(
        '\u0000', 'ㄱ', 'ㄲ', 'ㄳ', 'ㄴ',
        'ㄵ', 'ㄶ', 'ㄷ', 'ㄹ', 'ㄺ', 'ㄻ',
        'ㄼ', 'ㄽ', 'ㄾ', 'ㄿ', 'ㅀ', 'ㅁ',
        'ㅂ', 'ㅄ', 'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ',
        'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ'
    )

    // 한글 문자를 만드는 함수
    fun convertHangul(chosungIndex: Int, jungsungIndex: Int, jongsungIndex: Int): Char {
        require(chosungIndex in 0 until CHOSUNG_LIST.size) { "Invalid chosung index" }
        require(jungsungIndex in 0 until JUNGSUNG_LIST.size) { "Invalid jungsung index" }
        require(jongsungIndex in 0 until JONGSUNG_LIST.size) { "Invalid jongsung index" }

        val chosung = CHOSUNG_START_UNICODE.toInt() + chosungIndex
        val jungsung = JUNGSUNG_START_UNICODE.toInt() + jungsungIndex
        val jongsung = JONGSUNG_START_UNICODE.toInt() + jongsungIndex

        return if (jongsungIndex == 0) {
            // 종성이 없는 경우
            (chosung + jungsung).toChar()
        } else {
            // 종성이 있는 경우
            (chosung + jungsung + jongsung).toChar()
        }
    }

    fun joinHangul(chosung: Char, jungsung: Char): Char {
        val choIndex = CHOSUNG_LIST.indexOf(chosung)
        val jungIndex = JUNGSUNG_LIST.indexOf(jungsung)
        return (0xac00 + choIndex * 28 * 21 + jungIndex * 28).toChar()
    }

    fun joinHangulJongsung(chojung: Char, jongsung: Char): Char {
        val chojungUni = chojung.code
        val jongsungIndex = JONGSUNG_LIST.indexOf(jongsung)
        return (chojungUni + jongsungIndex).toChar()
    }


    // 주어진 문자가 한글 문자인지 확인하는 확장 함수
    private fun Char.isHangul(): Boolean {
        return this in '\uAC00'..'\uD7AF' || this in '\u1100'..'\u11FF' || this in '\u3130'..'\u318F'
    }
}
