FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/notification-service-0.1.jar notification-service.jar

EXPOSE 1144

# Set the command to run the JAR file
CMD ["java", "-jar", "notification-service.jar"]
