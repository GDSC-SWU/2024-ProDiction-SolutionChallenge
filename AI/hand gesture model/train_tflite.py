import tensorflow as tf
from tensorflow import keras
import numpy as np

# 예시 데이터 생성
file = np.genfromtxt('dataSet.txt', delimiter=',')
angleFile = file[:, :-1]
labelFile = file[:, -1]
angle = angleFile.astype(np.float32)
label = labelFile.astype(np.float32)

# 모델 정의
model = keras.Sequential([
    keras.layers.Dense(64, activation='relu', input_shape=(angle.shape[1],)),
    keras.layers.Dense(10, activation='softmax')
])

# 모델 컴파일
model.compile(optimizer='adam',
              loss='sparse_categorical_crossentropy',
              metrics=['accuracy'])

# 모델 훈련
model.fit(angle, label, epochs=10)

# 모델 저장
model.save("hand_gesture_model.h5")

# TensorFlow Lite Converter 생성
converter = tf.lite.TFLiteConverter.from_keras_model(model)

# TensorFlow Lite 모델로 변환
tflite_model = converter.convert()

# TensorFlow Lite 모델 저장
with open("hand_gesture_model.tflite", "wb") as f:
    f.write(tflite_model)