FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
COPY application/target/application-1.0.0-SNAPSHOT.jar application.jar
ENTRYPOINT ["java","-jar","/application.jar"]
