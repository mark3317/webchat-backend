# Stage 1: Build the application
FROM gradle:8.8-jdk21 AS build
WORKDIR /app
COPY . .
RUN gradle clean build --no-daemon

# Stage 2: Run the application
FROM openjdk:21-jdk-slim
WORKDIR /app
EXPOSE 8081
COPY --from=build /app/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]