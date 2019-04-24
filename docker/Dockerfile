FROM harbor.today36524.td/base/tomcat:8-jdk8

COPY ./catalina.sh /usr/local/tomcat/bin/
RUN rm -rf /usr/local/tomcat/webapps && mkdir -p /usr/local/tomcat/webapps && chmod 744 /usr/local/tomcat/bin/catalina.sh

COPY  ./apps/ROOT.war /usr/local/tomcat/webapps/
WORKDIR /usr/local/tomcat/bin/
CMD sh  /usr/local/tomcat/bin/catalina.sh run
