# Stage 1: Build application (Image with JDK and Maven)
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Copy Maven configuration files and dependencies
COPY pom.xml .
# Download dependencies offline (optimizes build cache if dependencies don't change)
RUN mvn dependency:go-offline

# Copy source code and package (skipping tests for faster image generation)
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Create final image (Only the JRE needed to run)
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copy final JAR file from build stage
COPY --from=build /app/target/autoflex-0.0.1-SNAPSHOT.jar app.jar

# Set the port the application runs on (8080 by default in Spring)
EXPOSE 8080

# Command to start the application
ENTRYPOINT ["java", "-jar", "app.jar"]
