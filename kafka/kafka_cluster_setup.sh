#!/bin/bash

if [ -z "$KAFKA_HOME" ]
then
      echo "\$Variable KAFKA_HOME not set. Using default /opt/kafka..."
      export KAFKA_HOME=/opt/kafka
fi

cd $KAFKA_HOME || exit 1
bin/zookeeper-server-start.sh config/zookeeper.properties  &>/dev/null

sleep 2
bin/kafka-server-start.sh config/server.properties &>/dev/null

sleep 2
TOPIC="index_app_topic"
bin/kafka-topics.sh --create --topic $TOPIC --bootstrap-server localhost:9092 --config retention.ms=1000 &>/dev/null

