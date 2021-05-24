# По Jenkins:

docker-compose up -d

curl -i -X PUT "http://namenode:9870/webhdfs/v1/deployed/test_jenkins_mvn-1.0-SNAPSHOT.jar?op=CREATE&overwrite=true"

curl -i -X PUT -T test_jenkins_mvn-1.0-SNAPSHOT.jar "http://datanode:9864/webhdfs/v1/deployed/test_jenkins_mvn-1.0-SNAPSHOT.jar?op=CREATE&namenoderpcaddress=namenode:9000&createflag=&createparent=true&overwrite=true"


http://localhost:8080/

http://localhost:9870/

https://registry.hub.docker.com/r/mdouchement/hdfs



