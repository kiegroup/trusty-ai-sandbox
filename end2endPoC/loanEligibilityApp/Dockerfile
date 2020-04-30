#FROM maven:3.6.2-jdk-8 
FROM openjdk:11
COPY app/ /app/
WORKDIR app/target
CMD ["java", "-jar", "loanEligibilityApp-runner.jar"]
#CMD ["./mvnw", "compile", "quarkus:dev"]
#CMD ["sleep", "10000"] 
