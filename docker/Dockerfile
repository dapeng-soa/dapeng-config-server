FROM harbor.today36524.td/base/openjdk:server-jre8
CMD mkdir -h /dapeng-config-server
COPY ./apps/dapeng-config-server.jar /dapeng-config-server/
WORKDIR /dapeng-config-server
CMD java -jar /dapeng-config-server/dapeng-config-server.jar
