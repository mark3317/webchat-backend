FROM openjdk:21-jdk-slim
WORKDIR /app
EXPOSE 8081
COPY /build/libs/*dev.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
LABEL authors="mark3317"