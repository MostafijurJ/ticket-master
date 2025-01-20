FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/search-service-0.1.jar search-service.jar

EXPOSE 1122

# Set the command to run the JAR file
CMD ["java", "-jar", "search-service.jar"]
