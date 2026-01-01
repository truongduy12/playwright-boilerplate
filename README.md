# Playwright Java Boilerplate

This project is a comprehensive boilerplate for testing web applications using Playwright and Java. It supports both standalone testing of public URLs and integration testing of local applications using Docker Compose.

> [!NOTE]
> 👉 **[Xem Hướng Dẫn Tiếng Việt (Vietnamese)](README_VN.md)**

## Features

- **Java 17 & Maven**
- **Playwright** for browser automation
- **JUnit 5** for test execution
- **Allure** for reporting
- **Strict Page Object Model**
- **Environment Variable Overrides** for configuration
- **Smart Polling/Health Check** in `BaseTest`

---

## 1. Prerequisites

- **Java JDK 17+**
- **Maven** (or use the included `mvnw` wrapper)

---

## 2. Configuration

You can configure the test execution via `src/test/resources/config.properties` or by passing System Environment Variables.

Key settings:
- `base.url`: The URL to test.
- `headless`: `true` or `false`.
- `browser`: `chromium`, `firefox`, or `webkit`.

**Priority:** Environment Variables > System Properties > Config File.

---

## 3. How to Run

### Mode A: Standalone (Testing Public URLs)

Run tests directly on your local machine against public URLs or localhost.

```bash
# Run all tests
./mvnw test

# Run a specific test
./mvnw test -Dtest=ExampleTest

# Run with custom configuration
./mvnw test -Dbase.url=https://other-site.com

### Mode B: Docker Execution

Run tests inside a Docker container to ensure a consistent environment.

```bash
# Build the image
docker build -t playwright-tests .

# Run the container (executes ./mvnw test by default)
docker run --rm --ipc=host playwright-tests
```

**Note:** `--ipc=host` is recommended for Chrome/Playwright to avoid memory issues.

---

## 4. Reporting

Run the following command to serve the Allure report locally (requires Allure command line):

```bash
allure serve target/allure-results
```

---

## 5. CI/CD Integration (Jenkins)

This project includes a `Jenkinsfile` for easy integration.

### Requirements on Jenkins Agent:
1. **Java 17**: Must be installed and available either via `JAVA_HOME` or Global Tool Configuration.
2. **Allure Plugin**: (Optional) For viewing rich reports in Jenkins.

### Pipeline Steps:
1. **Initialize**: Sets permissions for `mvnw`.
2. **Install Browsers**: Automatically runs Playwright's browser installation command.
3. **Run Tests**: Executes tests using Maven.
4. **Publish Reports**: Publishes JUnit XML and Allure results.

---

## Project Structure

```text
playwright-java-boilerplate/
├── src/main/java       # Page Objects, Config, Utils
├── src/test/java       # Tests, BaseTest
├── src/test/resources  # Config properties
├── Jenkinsfile         # CI pipeline
├── Dockerfile          # Test container definition
└── pom.xml             # Dependencies
```

