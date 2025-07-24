FROM openjdk:17-jdk-slim-buster
LABEL maintainer="samael1414"
WORKDIR /app
COPY target/ticket-terminal-api-0.0.1-SNAPSHOT.jar ./ticket-terminal.jar
ENTRYPOINT ["java", "-jar", "ticket-terminal.jar"]
