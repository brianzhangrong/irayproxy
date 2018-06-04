FROM openjdk:8-jdk-alpine
ADD target/irayproxy-0.0.1.jar /irayproxy.jar

#COPY ${JAR_FILE} /irayproxy.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/irayproxy.jar"]

EXPOSE 8888