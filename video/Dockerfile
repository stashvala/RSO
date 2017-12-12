FROM openjdk:8-jre-alpine

RUN mkdir /app

WORKDIR /app

ADD ./video-api/target/video-api-1.0-SNAPSHOT.jar /app

EXPOSE 8081

CMD ["java", "-jar", "video-api-1.0-SNAPSHOT.jar"]