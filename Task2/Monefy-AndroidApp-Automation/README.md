# Monefy Android App Automation Framework

This is an Appium automation framework for testing the Monefy Android app on a real device.

## Prerequisites

- Java JDK 11 or higher
- Maven
- Appium Server 2.0 or higher
- Android SDK
- A real Android device with Monefy app installed

## Setup

1. Clone the repository
2. Update the `config.properties` file in `src/test/resources` with your device details:
   ```
   device.name=YOUR_DEVICE_NAME
   device.udid=YOUR_DEVICE_UDID
   device.platformVersion=YOUR_ANDROID_VERSION
   ```
3. Make sure your Appium server is running on the URL specified in the config file (default: http://127.0.0.1:4723)
4. Ensure that the Monefy app is installed on your device

## Project Structure

```
├── src
│   ├── main
│   │   └── java
│   │       └── com
│   │           └── monefy
│   │               ├── pages        # Page Object Model classes
│   │               └── utils        # Utility classes
│   └── test
│       ├── java
│       │   └── com
│       │       └── monefy
│       │           └── tests        # Test classes
│       └── resources
│           ├── config.properties    # Configuration properties
│           ├── log4j2.xml           # Logging configuration
│           └── testng.xml           # TestNG configuration
```

## Approach and Technology Stack

This framework utilizes the following technologies and design patterns:

*   **Appium 2.0 (with UiAutomator2 Driver):** The industry standard for mobile application automation. Appium allows testing native, hybrid, and mobile web apps, providing flexibility. We use the UiAutomator2 driver, which is Google's standard framework for Android UI testing.
*   **Java:** A robust, object-oriented programming language widely used in test automation with strong community support and excellent integration with Appium and Maven.
*   **Maven:** Used for project build management and dependency resolution. It standardizes the build process, manages external libraries (like Appium, TestNG, Selenium), and makes it easy to compile code and run tests.
*   **TestNG:** A powerful testing framework for Java. It provides features like test annotations (`@Test`, `@BeforeSuite`, etc.), test grouping, parallel execution capabilities, detailed reporting, and listeners (used here for logging and screenshots), making test management more organized and flexible.
*   **Page Object Model (POM):** A design pattern used to enhance test maintenance and reduce code duplication. Each screen or significant component of the application is represented by a Page Object class, encapsulating the elements and interactions specific to that page. This separates test logic from UI interaction logic.

This combination provides a robust, maintainable, and scalable framework for automating tests for the Monefy Android application.

## Page Object Model

The framework follows the Page Object Model pattern:

- `BasePage`: Contains common methods for all pages
- `HomePage`: Contains elements and methods specific to the Monefy home page

## Utilities

- `DriverManager`: Manages the Appium driver instance
- `TestUtils`: Contains helper methods for test execution
- `Listeners`: TestNG listeners for reporting and screenshot capture

## Running Tests

To run the tests, use the following Maven command:

```
mvn clean test
```

## Adding New Tests

1. Create a new test class that extends `BaseTest`
2. Create new page objects if needed
3. Add the test class to the `testng.xml` file
4. Implement your test methods using TestNG annotations

## Screenshots

Screenshots are automatically taken when a test fails and stored in the `screenshots` directory.

## Running Tests with Docker

This project includes a `Dockerfile` to run the end-to-end tests in a containerized environment.

**Prerequisites:**

*   Docker installed and running.
*   Appium server running on your **host machine** (outside the container).
*   An Android device connected to your host machine (and recognized by `adb`) or an Android emulator running on your host.

**Setup:**

1.  **Configure Appium URL:** Before building the image, you **must** update the `appium.server.url` in `src/test/resources/config.properties` to point to your host machine's Appium server from the container's perspective:
    *   **Docker Desktop (Mac/Windows):** Use `http://host.docker.internal:4723`.
    *   **Docker on Linux:** Use your host machine's IP address on the Docker bridge network (e.g., `http://172.17.0.1:4723`. You can typically find this using `ip addr show docker0`).

**Build the Image:**

Navigate to the project root directory in your terminal and run:

```bash
docker build -t monefy-e2e-tests .
```

**Run the Tests:**

Once the image is built, run the tests using:

```bash
docker run --rm monefy-e2e-tests
```

This command will start a container, execute `mvn test` (which defaults to using `testng.xml`, running the `EndToEndFlowTest`), and connect to the Appium server running on your host machine to interact with the device/emulator.