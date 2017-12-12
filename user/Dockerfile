FROM openjdk:8-jre-alpine

RUN mkdir /app

WORKDIR /app

ADD ./user-api/target/user-api-1.0-SNAPSHOT.jar /app

EXPOSE 8080

CMD ["java", "-jar", "user-api-1.0-SNAPSHOT.jar"]
