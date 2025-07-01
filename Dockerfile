# Use a slim OpenJDK base image for smaller size
FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/AI_habit_track-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]