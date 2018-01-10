FROM openjdk:8-jre-alpine

RUN mkdir /app

WORKDIR /app

ADD ./comment-api/target/comment-api-1.0-SNAPSHOT.jar /app

EXPOSE 8087

CMD ["java", "-jar", "comment-api-1.0-SNAPSHOT.jar"]
