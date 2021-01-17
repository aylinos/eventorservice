FROM openjdk:11-jdk
VOLUME /tmp
COPY /*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]

