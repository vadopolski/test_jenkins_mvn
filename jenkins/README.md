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


# Sonar
1) Добавляем webhook, логинимся, меняем пароль логин на user

2) Добавляем webHook Sonar -> Configuration -> Webhooks
   http://jenkins:8080/sonarqube-webhook/

3) Создаем токен в Сонаре - Security -> Users -> Tokens -> Generate
добавляем в строчку
   sh "./gradlew sonarqube -Dsonar.login=c750434e7764c7e2ce18e66cb673f77ce212e644"
   
4) Разрешаем пермишны вс в  Security -> Users -> Global Permission

5) Создаем quality gate rule - 


1) Сетапим плагин Jenkins SonarQube Scanner и Sonar Quality Gates


2) Конфигурим Сонар в настройках, создаем креденшл с секретным словом
и урлом - http://sonarqube:9000/

3) Устанавливаем Сонар в конфигурации