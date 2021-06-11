pipeline {
    agent any

    parameters {
        string(name: 'BRANCH', defaultValue: 'with_test', description: 'Name of branch.')
    }

    tools {
        // Install the Maven version configured as "M3" and add it to the path.
        maven "M3"
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

        stage('build deploy target') {
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
                                      curl -i -X PUT "http://namenode:9870/webhdfs/v1/streaming_${params.BRANCH}/common-libs-${project_version}.jar?op=CREATE&overwrite=true"
                                      curl -i -X PUT -T test_jenkins_mvn-1.0-SNAPSHOT.jar "http://datanode:9864/webhdfs/v1/streaming_${params.BRANCH}/common-libs-${project_version}.jar?op=CREATE&namenoderpcaddress=namenode:9000&createflag=&createparent=true&overwrite=true"
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

                stage('rest') {
                    stages {

                        stage('update DB') {
                            steps {
                                echo 'Starting cassandra loader'
                            }
                        }

                        stage('build Rest') {
                            steps {
                                echo 'cloned and built dao'
                            }
                        }

                        stage('deploy Rest into Tomcat') {
                            steps {
                                echo 'test dao'
                            }
                        }

                        stage('start Jmeter') {
                            steps {
                                echo '.... JMeter Test OK'
                            }
                        }
                    }
                }
            }
        }
    }
}