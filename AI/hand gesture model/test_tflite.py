import cv2
import mediapipe as mp
import numpy as np
import time
from PIL import Image, ImageDraw, ImageFont
from unicode import join_jamos
import tensorflow as tf

# Load the TensorFlow Lite model.
interpreter = tf.lite.Interpreter(model_path="hand_gesture_model.tflite")
interpreter.allocate_tensors()

# Input and output details of the model.
input_details = interpreter.get_input_details()
output_details = interpreter.get_output_details()

# Camera setup
cap = cv2.VideoCapture(0)
width, height = int(cap.get(3)), int(cap.get(4))

# Mediapipe Hands setup
mp_hands = mp.solutions.hands
mp_drawing = mp.solutions.drawing_utils
hands = mp_hands.Hands(
    max_num_hands=1,
    min_detection_confidence=0.5,
    min_tracking_confidence=0.5
)

font_path = "NanumGothic.ttf"
font_size = 30
gesture = {
    0: 'ㄱ', 1: 'ㄴ', 2: 'ㄷ', 3: 'ㄹ', 4: 'ㅁ', 5: 'ㅂ', 6: 'ㅅ', 7: 'ㅇ', 8: 'ㅈ', 9: 'ㅊ',
    10: 'ㅋ', 11: 'ㅌ', 12: 'ㅍ', 13: 'ㅎ',
    14: 'ㅏ', 15: 'ㅑ', 16: 'ㅓ', 17: 'ㅕ', 18: 'ㅗ', 19: 'ㅛ', 20: 'ㅜ', 21: 'ㅠ', 22: 'ㅡ', 23: 'ㅣ',
    24: 'ㅐ', 25: 'ㅒ', 26: 'ㅔ', 27: 'ㅖ', 28: 'ㅚ', 29: 'ㅟ', 30: 'ㅢ',
    31: 'spacing', 32: 'clear_one', 33: 'all_clear', 34: 'add'
}

startTime = time.time()
prev_index = 0
sentence = ''
recognizeDelay = 1

while True:
    ret, img = cap.read()

    if not ret:
        continue

    # Convert the BGR image to RGB
    imgRGB = cv2.cvtColor(img, cv2.COLOR_BGR2RGB)

    # Use Mediapipe Hands to process the image
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

            # Process the angles using the TensorFlow Lite model
            data = np.array([angle], dtype=np.float32)
            interpreter.set_tensor(input_details[0]['index'], data)
            interpreter.invoke()
            output_data = interpreter.get_tensor(output_details[0]['index'])
            index = np.argmax(output_data)

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
                            # Add your custom logic for combining characters
                            pass
                        else:
                            sentence += gesture[index]
                        startTime = time.time()

                pil_img = Image.fromarray(img)
                draw = ImageDraw.Draw(pil_img)
                font = ImageFont.truetype(font_path, font_size)

                text_position = (int(res.landmark[0].x * width - 10),
                                 int(res.landmark[0].y * height + 40))
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
    if cv2.waitKey(1) & 0xFF == ord('q'):
        break

# Release the camera
cap.release()
cv2.destroyAllWindows()