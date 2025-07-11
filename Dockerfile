FROM openjdk:17-jdk-buster

WORKDIR /app

RUN apt-get update && apt-get install -y curl \
  && curl -o .env https://storelabs.s3.ap-northeast-2.amazonaws.com/configs/.env \
  && curl -o application.yml https://storelabs.s3.ap-northeast-2.amazonaws.com/configs/application.yml

COPY build/libs/*.jar ./app.jar

CMD export $(cat .env | grep -v '^#' | xargs) && \
    java -jar app.jar --spring.config.location=file:/app/application.yml
