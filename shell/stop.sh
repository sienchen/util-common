#!/bin/bash
APP_NAME=uitl-common-1.0.jar
#检查程序是否在运行
is_exist(){
  pid=`ps -ef|grep $APP_NAME|grep -v grep|awk '{print $2}' `
  #如果不存在返回1，存在返回0     
  if [ -z "${pid}" ]; then
   return 1
  else
    return 0
  fi
}

#停止方法
stop(){
  is_exist
  if [ $? -eq "0" ]; then
  	echo "Stopping $APP_NAME(pid=${pid})..."
    kill -9 $pid
    if [ $? -eq 0 ]; then
      echo "[stop Success]"
      echo "================================================================================================================"
    else
      echo "[stop Failed]"
      echo "================================================================================================================"
    fi
  else
    echo "${APP_NAME} is not running"
  fi  
}

stop




