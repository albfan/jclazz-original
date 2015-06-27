#!/bin/sh

./setenv.sh

$JAVA_HOME/bin/java -classpath ./jclazz.jar ru.andrew.jclazz.apps.decomp.Decomp $@