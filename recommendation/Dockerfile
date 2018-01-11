FROM openjdk:8-jre-alpine

RUN mkdir /app

WORKDIR /app

ADD ./recommendation-api/target/recommendation-api-1.0-SNAPSHOT.jar /app

EXPOSE 8086

CMD ["java", "-jar", "recommendation-api-1.0-SNAPSHOT.jar"]
