FROM openjdk:11-jdk

ARG JAR_NAME=build/libs/*.jar

COPY ${JAR_NAME} viewment_server.jar

EXPOSE 8080

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/viewment_server.jar"]
