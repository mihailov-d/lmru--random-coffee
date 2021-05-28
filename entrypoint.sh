#!/bin/sh

DEFAULT_JAVA_OPTS="-server"
DEFAULT_JAVA_OPTS="${DEFAULT_JAVA_OPTS} -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/logs"
DEFAULT_JAVA_OPTS="${DEFAULT_JAVA_OPTS} -Djava.security.egd=file:/dev/urandom"

java $DEFAULT_JAVA_OPTS $JAVA_OPTS -cp /application/ org.springframework.boot.loader.JarLauncher $PROG_OPTS "$@"