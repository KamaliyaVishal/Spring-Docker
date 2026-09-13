# Stage 1: Build using the project's own Maven Wrapper
FROM eclipse-temurin:26-jdk-alpine AS builder
WORKDIR /app

# Copy wrapper configuration files first for caching
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline -B

# Copy code and compile the binary
COPY src ./src
RUN ./mvnw clean package -DskipTests

# Stage 2: Production execution environment
FROM eclipse-temurin:26-jre-alpine
WORKDIR /app

# SECURITY: Run as a non-root user instead of root
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copy binary from the compiler stage
COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
