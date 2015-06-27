#!/bin/sh

if ! [ -v JCLAZZ_HOME ]
then
   echo please define "JCLAZZ_HOME" environment variable
   exit 1
fi

export LIB_PATH="$JCLAZZ_HOME/build"
export JAVA="java"

if [ "$JAVA_HOME" != "" ]
then
    export JAVA="$JAVA_HOME/bin/java"
fi

if [ "$JDK_HOME" != "" ]
then
    export JAVA="$JDK_HOME/bin/java"
fi

$JAVA -jar $LIB_PATH/jclazz-gui.jar
