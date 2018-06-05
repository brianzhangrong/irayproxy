FROM codenvy/jdk8_maven3_tomcat8

ADD target/irayproxy-0.0.1.jar /irayproxy.jar

ENV JAVA_HOME /opt/jdk1.8.0_51 CLASSPATH=$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar PATH=$PATH:$JAVA_HOME/bin

WORKDIR /

CMD java -server -Xmx4g -Xms4g -Xmn256m -Xss256k -XX:+DisableExplicitGC -XX:+UseConcMarkSweepGC -XX:+CMSParallelRemarkEnabled -XX:LargePageSizeInBytes=128m -XX:+UseFastAccessorMethods -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=70 -Duser.timezone=GMT+8 -jar irayproxy.jar

EXPOSE 8888