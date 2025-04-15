# Pet Store API Test Automation Framework

A robust and scalable test automation framework for the Swagger Pet Store API, designed to ensure API reliability through comprehensive test coverage of all CRUD operations.

## Setup

### Prerequisites
- JDK 17 or higher
- Maven 3.8+
- Git
- Docker (optional, for containerized execution)

### Installation

1. Clone the repository:
   ```
   git clone <repository-url>
   cd pet-testing
   ```

2. Install dependencies:
   ```
   mvn clean install -DskipTests
   ```

## How to Run Tests

### Running Tests Locally

Run all tests:
```
mvn clean test
```

Run specific test class:
```
mvn clean test -Dtest=CreatePetTests
```

Run specific test method:
```
mvn clean test -Dtest=CreatePetTests#createPetWithValidData
```

Run tests with specific environment:
```
mvn clean test -Denv=staging
```

### Generating Reports

Generate Allure report:
```
mvn allure:report
```

View report in browser:
```
mvn allure:serve
```

### Running Tests in Docker

Build and run tests in Docker:
```
docker-compose build
docker-compose up
```

The Docker setup uses host network mode to connect to the API running on the host machine. This configuration:
- Skips image upload tests that may require special handling
- Uses TestNG XML configuration to control test execution
- Provides optimized container permissions for test execution

Run with specific test or environment:
```
docker run pet-store-tests mvn clean test -Dtest=CreatePetTests -Denv=staging
```

### CI/CD Integration

Tests run automatically through GitHub Actions:
- On push to main branch
- On pull requests to main branch
- Daily scheduled runs

## Test Configuration

### TestNG XML Configuration

The framework uses TestNG XML configuration (`src/test/resources/testng.xml`) to:
- Control parallel test execution
- Handle test groups and dependencies
- Exclude problematic tests (e.g., image upload tests)

You can modify this file to include or exclude specific tests based on your needs.

## Approach and Tech Stack

### Tech Stack Selection

This framework was built using:

- **Java 17+**: Modern language features provide robust, maintainable code
- **Maven**: Industry-standard build tool with excellent dependency management
- **TestNG**: Powerful test framework supporting parallel execution and data-driven testing
- **Rest Assured**: De facto standard for API testing, with fluent assertion capabilities
- **Jackson**: High-performance JSON processing
- **Allure Reports**: Rich reporting with detailed test steps and attachments
- **SLF4J with Logback**: Flexible logging with configurable outputs
- **AssertJ**: Fluent assertion library for readable validations
- **JavaFaker**: Generates realistic test data to ensure robust test scenarios
- **Docker**: Containerized test execution with host network mode for API connectivity

### Framework Architecture

The framework follows a layered architecture:

1. **API Client Layer**: Encapsulates all HTTP operations
2. **Model Layer**: POJO representations of API entities
3. **Test Layer**: Organized by CRUD operations
4. **Configuration Layer**: Environment-specific settings
5. **Utilities**: Helper functions and test data generation
6. **Reporting**: Test result capture and visualization

### Design Principles

This framework was designed with these principles:

#### Scalability
- Modular structure allows easy extension to other API endpoints
- New test cases can be added with minimal code duplication
- Pattern-based approach enables consistent implementation

#### Maintainability
- Clear separation of concerns with specialized components
- Consistent coding patterns throughout the codebase
- Centralized configuration management
- Strong typing with model classes

#### Reliability
- Comprehensive error handling and logging
- Detailed test reporting
- Consistent test environment through containerization
- CI/CD integration ensures continuous validation

### Testing Approach

- **Complete CRUD Coverage**: Tests for all Create, Read, Update, Delete operations
- **Edge Cases**: Testing boundary conditions and error scenarios
- **Data-Driven Tests**: Parameterized tests with different data sets
- **API Response Validation**: Complete validation of response structure and content
- **Independent Tests**: Each test is self-contained and can run in isolation 