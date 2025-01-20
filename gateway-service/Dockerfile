FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/gateway-service-0.1.jar gateway-service.jar

EXPOSE 1122

# Set the command to run the JAR file
CMD ["java", "-jar", "gateway-service.jar"]
