# Kafka


Kafka commands

kafka-consumer-groups.bat --bootstrap-server localhost:9092 --list
kafka-consumer-groups.bat --bootstrap-server localhost:9092 --describe --group ConsumerGroup2

kafka-topics.bat -zookeeper localhost:2181 --list
kafka-topics.bat -zookeeper localhost:2181 --describe TOPIC-1


kafka-topics.bat --zookeeper localhost:2181 --create --topic TOPIC-1 --partitions 3 --replication-factor 1
