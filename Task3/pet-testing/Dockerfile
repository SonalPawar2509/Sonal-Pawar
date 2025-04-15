FROM maven:3.8-openjdk-17-slim

# Set working directory
WORKDIR /app

# Create a non-root user to run Maven
RUN groupadd -r testuser && useradd -r -g testuser testuser

# Create and set permissions for Maven repository
RUN mkdir -p /home/testuser/.m2/repository && \
    chown -R testuser:testuser /home/testuser

# Copy the project files
COPY . .

# Set proper permissions
RUN mkdir -p target && \
    chown -R testuser:testuser /app && \
    chmod -R 755 /app

# Switch to non-root user
USER testuser

# Download dependencies during container build
RUN mvn dependency:go-offline

# Default command to run tests (skip clean phase)
CMD ["mvn", "test"] 