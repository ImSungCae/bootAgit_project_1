# JDK 17 기반
FROM openjdk:17-jdk-slim

CMD ["./gradlew", "clean", "build", "-x", "test"]

# build할 jar 파일 변수 선언
ARG JAR_FILE=build/libs/*.jar

# build/libs에 있는 JAR파일을 Docker 이미지의 app.jar로 복사
COPY ${JAR_FILE} app.jar

# wait-for-it.sh 사용시 하단의 ENTRYPOINT는 주석처리나 지워줘야함
#RUN apt-get update && apt-get install -y bash
#COPY wait-for-it.sh /wait-for-it.sh
#RUN chmod +x /wait-for-it.sh

# Docker 컨테이너가 실행되면 java -jar app.jar 명령어를 실행하여 Spring boot 실행
ENTRYPOINT ["java", "-jar", "app.jar"]