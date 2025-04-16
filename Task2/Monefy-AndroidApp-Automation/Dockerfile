# Use an official Maven image with Java 11 (align with project requirements)
FROM maven:3.9-eclipse-temurin-11 AS builder

# Set the working directory in the container
WORKDIR /app

# Copy the pom.xml first to leverage Docker cache for dependencies
COPY pom.xml .

# Download dependencies (optional, but can speed up subsequent builds)
# RUN mvn dependency:go-offline

# Copy the entire project source code
COPY src ./src

# Compile the project and test classes
RUN mvn compile test-compile

# Set the default command to run the tests using the default testng.xml (which runs E2E)
# Ensure Appium server is running externally and config.properties points to it correctly.
CMD ["mvn", "test"] 