# По Jenkins:

docker-compose up -d

curl -i -X PUT "http://namenode:9870/webhdfs/v1/deployed/test_jenkins_mvn-1.0-SNAPSHOT.jar?op=CREATE&overwrite=true"

curl -i -X PUT -T test_jenkins_mvn-1.0-SNAPSHOT.jar "http://datanode:9864/webhdfs/v1/deployed/test_jenkins_mvn-1.0-SNAPSHOT.jar?op=CREATE&namenoderpcaddress=namenode:9000&createflag=&createparent=true&overwrite=true"

1) Jenkins
http://localhost:8080/
login: user
passw: bitnami

2) HDFS -> Utilities -> Browse Directory
http://localhost:9870/


https://registry.hub.docker.com/r/mdouchement/hdfs