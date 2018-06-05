FROM openjdk:8-jdk-alpine
ADD target/irayproxy-0.0.1.jar /irayproxy.jar

#COPY ${JAR_FILE} /irayproxy.jar
ENTRYPOINT ["java","-server -Xmx4g -Xms4g -Xmn256m -Xss256k -XX:+DisableExplicitGC  -XX:+UseConcMarkSweepGC -XX:+CMSParallelRemarkEnabled -XX:LargePageSizeInBytes=128m -XX:+UseFastAccessorMethods -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=70 -Duser.timezone=GMT+8 -Djava.security.egd=file:/dev/./urandom","-jar","/irayproxy.jar"]

EXPOSE 8888