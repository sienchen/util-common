#!/bin/bash
APP_NAME=uitl-common-1.0.jar
BASE_HOME=$(dirname $(readlink -f "$0"))
CONF_HOME=$BASE_HOME/conf
nohup java -Dfile.encoding=utf-8 -Duser.timezone=GMT+08 -jar -Dspring.config.location=$CONF_HOME/  -Xmx2048M  $APP_NAME  --logging.config=$CONF_HOME/logback-spring.xml > /dev/null 2>&1 &
tail -f /dev/null