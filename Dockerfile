FROM adoptopenjdk/openjdk11:alpine-jre

COPY target/transfer-*.jar /transfer.jar

CMD ["java", "-jar", "/transfer.jar"]
