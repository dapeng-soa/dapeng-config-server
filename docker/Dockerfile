FROM dapengsoa/openjdk:java8-with-arthas

COPY ./catalina.sh /usr/local/tomcat/bin/
RUN rm -rf /usr/local/tomcat/webapps && mkdir -p /usr/local/tomcat/webapps && chmod 744 /usr/local/tomcat/bin/catalina.sh

COPY  ./apps/ROOT.war /usr/local/tomcat/webapps/
WORKDIR /usr/local/tomcat/bin/
CMD sh  /usr/local/tomcat/bin/catalina.sh run
