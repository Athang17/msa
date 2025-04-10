FROM openjdk:23-jdk-slim
WORKDIR /app
COPY target/ConfigServer-0.0.1-SNAPSHOT.jar /app/app.jar
EXPOSE 8888
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
