FROM openjdk:8-jre-alpine

RUN mkdir /app

WORKDIR /app

ADD ./uploader-api/target/uploader-api-1.0-SNAPSHOT.jar /app

EXPOSE 8088

CMD ["java", "-jar", "uploader-api-1.0-SNAPSHOT.jar"]
