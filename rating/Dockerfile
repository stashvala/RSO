FROM openjdk:8-jre-alpine

RUN mkdir /app

WORKDIR /app

ADD ./rating-api/target/rating-api-1.0-SNAPSHOT.jar /app

EXPOSE 8085

CMD ["java", "-jar", "rating-api-1.0-SNAPSHOT.jar"]
