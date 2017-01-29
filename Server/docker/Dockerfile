FROM tomcat:8-jre8

MAINTAINER Louis Mondesir

RUN ["rm", "-rf", "/usr/local/tomcat/webapps/aggregator.war"]
COPY aggregator-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/aggregator.war
ENV CATALINA_OPTS="-Dmongo.db.server=172.17.0.2"
ENV JAVA_OPTS="-Djava.security.egd=file:/dev/./urandom"

CMD ["catalina.sh", "run"]
