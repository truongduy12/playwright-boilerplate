pipeline {
    agent any

    environment {
        // Ensure Java 17 is used if available in standard paths or configured in Jenkins
        // JAVA_HOME = tool name: 'jdk-17', type: 'jdk' // Uncomment if you use Global Tool Configuration
        HEADLESS = 'true'
    }

    stages {
        stage('Initialize') {
            steps {
                echo 'Initializing Project...'
                sh 'chmod +x mvnw'
                sh './mvnw -version'
            }
        }

        stage('Install Dependencies & Browsers') {
            steps {
                echo 'Installing Playwright Browsers...'
                // Install project dependencies
                sh './mvnw -B clean compile'
                // Install Playwright browsers
                sh './mvnw exec:java -e -Dexec.mainClass=com.microsoft.playwright.CLI -Dexec.args="install"'
            }
        }

        stage('Run Tests') {
            steps {
                echo 'Running Tests...'
                // Run tests with Maven Wrapper
                sh './mvnw -B test'
            }
        }
    }

    post {
        always {
             // 1. Publish JUnit Report (Standard in Jenkins)
            junit 'target/surefire-reports/*.xml'

            // 2. Publish Allure Report (Requires Allure Jenkins Plugin)
            script {
                if (fileExists('target/allure-results')) {
                     allure([
                        includeProperties: false,
                        jdk: '',
                        properties: [],
                        reportBuildPolicy: 'ALWAYS',
                        results: [[path: 'target/allure-results']]
                    ])
                }
            }
        }
    }
}
