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
                    sh "./gradlew sonarqube -Dsonar.login=8cae112e16a890fe03410604aa2a1ef724d1ead3"
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