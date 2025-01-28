FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/event-service-0.1.jar event-service.jar

EXPOSE 1122

# Set the command to run the JAR file
CMD ["java", "-jar", "event-service.jar"]
