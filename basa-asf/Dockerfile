FROM frolvlad/alpine-java:jdk8-full
VOLUME /tmp
ADD target/basa-1.6.0.jar app.jar
RUN sh -c 'touch /app.jar'
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar" ]
EXPOSE 8090


#
#FROM ubuntu:latest
#RUN apt-get -y update && apt-get -y upgrade
#RUN apt-get -y install openjdk-8-jdk wget
#RUN mkdir /usr/local/tomcat
#EXPOSE 8080
#CMD /usr/local/tomcat/bin/catalina.sh run

# Second stage, add only our minimal "JRE" distr and our app
#FROM debian:stretch-slim

#ENV JAVA_OPTS=""
#ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar" ]

#COPY "target/basa-1.6.0.jar" "/app.jar"

#CMD [ "-jar", "/app.jar" ]
#ENTRYPOINT [ "Java" ]
#EXPOSE 8080
