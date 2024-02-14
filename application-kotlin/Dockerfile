FROM openjdk:19
LABEL authors="Vadim Taratonov"
EXPOSE 8082
ARG JAR_FILE=/target/application-0.0.1-SNAPSHOT.jar

COPY ${JAR_FILE} application-kotlin

CMD ["java","-jar","application-kotlin"]