FROM openjdk:8-jre-alpine

RUN mkdir /app

WORKDIR /app

ADD ./demo-api/target/demo-api-1.0-SNAPSHOT.jar /app

EXPOSE 8081

CMD ["java", "-jar", "demo-api-1.0-SNAPSHOT.jar"]
