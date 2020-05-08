#FROM maven:3.6.2-jdk-8 
FROM openjdk:11
COPY . /
WORKDIR /target
CMD ["java", "-jar", "explanation-service-1.0-SNAPSHOT-runner.jar"]
#CMD ["./mvnw", "compile", "quarkus:dev"]
#CMD ["sleep", "10000"] 
