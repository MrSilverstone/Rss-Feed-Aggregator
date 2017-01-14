FROM tomcat:8-jre8

MAINTAINER Louis

RUN ["rm", "-fr", "/usr/local/tomcat/webapps/ROOT"]
COPY aggregator-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war
CMD ["catalina.sh", "run"]