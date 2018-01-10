FROM openjdk:8-jre-alpine

RUN mkdir /app

WORKDIR /app

ADD ./subscription-api/target/subscription-api-1.0-SNAPSHOT.jar /app

EXPOSE 8084

CMD ["java", "-jar", "subscription-api-1.0-SNAPSHOT.jar"]
