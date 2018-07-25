#!/bin/bash
getServiceTime(){
    echo "received serviceName: $1"
    if [ -f "/data/test" ];then
        echo `stat -c %Y /data/test`
    else
        echo 0
    fi
}

case $1 in
   "getServiceTime") eval $@ ;;
   *) echo "invalid command $1" ;;
esac
