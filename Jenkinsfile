pipeline {
    agent any
    
    tools {
        maven 'maven 3.9.12' // Tên phải khớp với cấu cấu hình trong Manage Jenkins -> Tools
        jdk 'corretto-17.0.14'    // Tên phải khớp với cấu cấu hình trong Manage Jenkins -> Tools
    }

    environment {
        HEADLESS = 'true'
    }

    stages {
        stage('Initialize') {
            steps {
                echo 'Initializing Project...'
                sh 'mvn -version'
            }
        }

        stage('Install Dependencies & Browsers') {
            steps {
                echo 'Installing Playwright Browsers...'
                // Install project dependencies
                sh 'mvn -B clean compile'
                // Install Playwright browsers
                sh 'mvn exec:java -e -Dexec.mainClass=com.microsoft.playwright.CLI -Dexec.args="install"'
            }
        }

        stage('Run Tests') {
            steps {
                echo 'Running Tests...'
                // Run tests with Maven Wrapper
                sh 'mvn -B test'
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
