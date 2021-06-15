pipeline {
    agent any

    stages {
        stage("Clone sources") {
            steps {
                git branch: 'bad-code', url: 'https://github.com/tkgregory/sonarqube-jacoco-code-coverage.git'
            }
        }

        stage("SonarQube analyses") {
            steps {
                withSonarQubeEnv('SonarQube') {
                    sh "./gradlew sonarqube -Dsonar.login=c750434e7764c7e2ce18e66cb673f77ce212e644"
                }
            }
        }

        stage("Quality gate") {
            steps {
                waitForQualityGate abortPipeline: true
            }
        }
    }
}