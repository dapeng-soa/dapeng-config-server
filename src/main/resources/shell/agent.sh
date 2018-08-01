#!/bin/bash
getServerTime(){
   #echo "received serviceName: $1"
   ip=$(ifconfig en0|grep "inet "|awk '{print $2}')
    if [ -f "/data/test" ];then
        echo "$ip:"`stat -c %Y /data/test`
    else
        echo "$ip:"0
    fi
}

deploy() {
    docker-compose $1 $2 $3 $4
}

case $1 in
   "getServerTime" | "deploy") eval $@ ;;
   *) echo "invalid command $1" ;;
esac
