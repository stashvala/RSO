FROM openjdk:8-jre-alpine

RUN mkdir /app

WORKDIR /app

ADD ./player-api/target/player-api-1.0-SNAPSHOT.jar /app

EXPOSE 8089

CMD ["java", "-jar", "player-api-1.0-SNAPSHOT.jar"]
