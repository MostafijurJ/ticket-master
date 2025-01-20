FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/discovery-server-0.1.jar discovery-server.jar

EXPOSE 8761

# Set the command to run the JAR file
CMD ["java", "-jar", "discovery-server.jar"]
