FROM openjdk:8-jre-alpine

RUN mkdir /app

WORKDIR /app

ADD ./target/config-1.0-SNAPSHOT.jar /app

EXPOSE 8083

CMD ["java", "-jar", "config-1.0-SNAPSHOT.jar"]
