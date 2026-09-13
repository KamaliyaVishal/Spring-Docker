FROM eclipse-temurin:26-jre-alpine

WORKDIR /app

COPY /target/docker-service-1.jar .

ENTRYPOINT ["java", "-jar", "docker-service-1.jar"]