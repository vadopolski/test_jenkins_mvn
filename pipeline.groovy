pipeline {
    agent any

    parameters {
        string(name: 'BRANCH', defaultValue: 'with_test', description: 'Name of branch.')
    }

    tools {
        maven "maven 3.8.9"
    }

    stages {

        stage('Build common libs') {
            steps {
                git 'https://github.com/vadopolski/common-libs.git'

                sh "git checkout ${params.BRANCH}"

                sh "mvn clean package"

            }
        }

        stage('Test common libs') {
            steps {
                sh "mvn test compile"
            }
        }

        stage('parallel-build-and-deploy-projects') {
            parallel {
                stage('streaming') {
                    stages {

                        stage('build streaming') {
                            steps {
                                git 'https://github.com/vadopolski/streaming.git'

                                sh "git checkout ${params.BRANCH}"

                                sh "mvn clean package"

                            }
                        }

                        stage('deploy streaming') {
                            steps {
                                script {
                                    project_version = sh script: 'mvn help:evaluate -Dexpression=project.version -q -DforceStdout', returnStdout: true
                                    echo project_version
                                }

                                sh """cd target
                                      curl -i -X PUT "http://namenode:9870/webhdfs/v1/streaming_${params.BRANCH}/common-libs-1.0-SNAPSHOT.jar?op=CREATE&overwrite=true"
                                      curl -i -X PUT -T test_jenkins_mvn-1.0-SNAPSHOT.jar "http://datanode:9864/webhdfs/v1/streaming_${params.BRANCH}/common-libs-1.0-SNAPSHOT.jar?op=CREATE&namenoderpcaddress=namenode:9000&createflag=&createparent=true&overwrite=true"
                                   """
                            }
                        }
                    }
                }

                stage('batch') {
                    stages {

                        stage('build batching') {
                            steps {
                                git 'https://github.com/vadopolski/batching.git'

                                sh "git checkout ${params.BRANCH}"

                                sh "mvn clean package"
                            }
                        }

                        stage('deploy batching') {
                            steps {
                                sh """cd target
                              curl -i -X PUT "http://namenode:9870/webhdfs/v1/batch_${params.BRANCH}/batching-1.0-SNAPSHOT.jar?op=CREATE&overwrite=true"
                              curl -i -X PUT -T test_jenkins_mvn-1.0-SNAPSHOT.jar "http://datanode:9864/webhdfs/v1/batch_${params.BRANCH}/batching-1.0-SNAPSHOT.jar?op=CREATE&namenoderpcaddress=namenode:9000&createflag=&createparent=true&overwrite=true"
                                   """
                            }
                        }

                    }
                }
            }
        }
    }
}