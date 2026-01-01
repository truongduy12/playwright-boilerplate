FROM mcr.microsoft.com/playwright/java:v1.57.0-jammy

WORKDIR /app

# Copy Maven Wrapper and Pom
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Resolve dependencies (go offline)
RUN ./mvnw dependency:go-offline

# Copy Source Code
COPY src src

# Default command: Run all tests
CMD ["./mvnw", "test"]
