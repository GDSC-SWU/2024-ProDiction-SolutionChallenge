# Pro-Diction

# ⛳️ Target UN SDGs
<img src="https://raw.githubusercontent.com/suucong/Github-User-Content/main/sdg4.png" width=200/> <img src="https://raw.githubusercontent.com/suucong/Github-User-Content/main/sdg10.png" width=200/>

# ⛳️ Our Solutions
Hearing impaired often face difficulties in communicating effectively with non-disabled individuals, leading to discrimination in various aspects of society such as education and employment.

### 🚀 Future Fluent Communication
Hearing impaired learn pronunciation to ultimately communicate seamlessly with non-disabled individuals without relying on anyone else's assistance.

### 🚀 Current Fluent Communication:
Even without pronunciation training and correction, real-time communication with non-disabled individuals is facilitated through sign language or text.

# 🎥 Our Demo Video
[![demo video](https://raw.githubusercontent.com/suucong/Github-User-Content/main/prodiction.png)](https://youtu.be/NOwo06cDY_U)

# 📱 How to use
<img src="https://raw.githubusercontent.com/suucong/Github-User-Content/main/splash&login.gif"/>
<img src="https://raw.githubusercontent.com/suucong/Github-User-Content/main/onboarding.gif"/>
<img src="https://raw.githubusercontent.com/suucong/Github-User-Content/main/study1.gif"/>
<img src="https://raw.githubusercontent.com/suucong/Github-User-Content/main/study2.gif"/>
<img src="https://raw.githubusercontent.com/suucong/Github-User-Content/main/study3.gif" width=100%>
<img src="https://raw.githubusercontent.com/suucong/Github-User-Content/main/communication2.gif"/>
<img src="https://raw.githubusercontent.com/suucong/Github-User-Content/main/communication.gif"/>
<img src="https://raw.githubusercontent.com/suucong/Github-User-Content/main/mypage_voca.gif"/>
<img src="https://raw.githubusercontent.com/suucong/Github-User-Content/main/mypage_call.gif"/>

# 🛠️ Project Architecture
<img src="https://raw.githubusercontent.com/suucong/Github-User-Content/main/TechStack.png"/>

## Backend
### 1. Tech Stack
- Java 17
- Springboot, Spring Security
- Spring Data JPA
- MySQL
- Redis
- Docker, Docker-compose
- GCP Compute Engine

### 2. Architecture
- I built a Docker image for our Spring Boot project using a Dockerfile and pushed it to Dockerhub.
- Next, I used the Vim text editor within a GCP Compute Engine instance to write the [docker-compose.yml](https://github.com/GDSC-SWU/2024-ProDiction-SolutionChallenge/issues/37#issuecomment-1948326979).
- After that, I pulled the Spring Boot image, along with MySQL and Redis images. 
- Running 'docker-compose up -d' created the necessary containers. 
- Subsequently, I utilized a Docker MySQL container to create a user, schema, and database. 
- Backend communicated with the frontend using the server container we created. 
- To connect to an external API for pronunciation testing, the frontend sent recorded audio files to the server using Multipart. 
- The server then handled the encoding to Base64 and sent the encoded values to the external API. 
- Additionally, we stored Refresh Tokens in the Redis container for user authentication and authorization.
- The external API processed the audio, returned the pronunciation scores to the server, which in turn forwarded them to the frontend.
- Furthermore, to play pronunciation practice videos, the backend sends the string that needs to be pronounced to an AI server. The AI server then separates the consonants and vowels in the string.

### 3. ERD
<img src="https://raw.githubusercontent.com/suucong/Github-User-Content/main/erd2.png"/>

## Frontend
### 1. Tech Stack
- Kotlin plugin version 1.9.0

### 2. Architecture
- We used Kotlin and XML to develop the Android app. We implemented Google OAuth within Android Studio to allow users to use the app after signing in with Google. 
- We also used Google's built-in Speech Recognizer and Text to Speech feature as speed is important for the real-time communication menu. 
- We imported the mouth shape and tongue position images for the learning menu from Google Cloud Storage.

## AI
### 1. Tech Stack
- ffmpeg 6.1.1
- fastapi 0.109.0
- uvicorn 0.26.0
- gunicorn 21.2.0
- python-multipart 0.0.6
- audiosegment 0.23.0
- google-cloud-speech 2.23.0
- pydub 0.25.1
- librosa 0.10.1
- soundfile 0.12.1

### 2. Architecture
- To implement the communication function, I used Colab to train Google MediaPipe's Gesture Recognizer model through manually collected sign language images, and then saved them as task files and handed them over to Android.
- In order to learn how the user's pronunciation differs from the standard pronunciation by syllable, we used libraries such as FFmpeg and librosa to cut the voice file delivered by Android through the server into syllables, and then passed it to the Google Cloud Speech API to get the syllable-by-syllable pronunciation.
- I converted all functions to FastAPI, and then dockerized them, and pushed them to Docker Hub. After that, we deployed the server by pulling the docker image to the Virtual Machine inside Google's Compute Engine and deployed the server to communicate with Android.

# Member
|Member|[이서현 (Lee Seohyun)](https://github.com/bimeon)|[노수진 (Sujin Roh)](https://github.com/suucong)|[김하연 (Kim Hayeon)](https://github.com/hyhy0623)|[김예솔 (Kim Yesol)](https://github.com/yesolthee)|
|:--:|:--:|:--:|:--:|:--:|
|Role|PM / AI Developer|Backend Developer|Frontend Developer|UX-UI Designer|
|Profile|<img src="https://raw.githubusercontent.com/suucong/Github-User-Content/main/sh.png" width="100" height="100"/>|<img src="https://raw.githubusercontent.com/suucong/Github-User-Content/main/sujin.jpg" width="100" height="100"/>|<img src="https://raw.githubusercontent.com/suucong/Github-User-Content/main/hy.png" width="100" height="100"/>|<img src="https://raw.githubusercontent.com/suucong/Github-User-Content/main/yesol.jpg" width="100" height="100">|
