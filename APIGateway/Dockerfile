FROM openjdk:23-jdk-slim
WORKDIR /app
COPY target/APIGateway-0.0.1-SNAPSHOT.jar /app/app.jar
EXPOSE 9090
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
