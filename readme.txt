How build fat jar file of the project:
1) Go to root project: cd transfer
2) Install maven in system environment and call command in console:
 > mvn clean package spring-boot:repackage
3) Fat jar file is folder: .../transfer/target/transfer-0.0.1-SNAPSHOT.jar

How create composed container docker:
1) Go to root project: cd transfer
2) Install docker and and call command in console:
> docker-compose up --build
3) If you want stop container that typing Ctrl+C and call command in console:
> docker-compose stop