package consumer;

import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import property.KafkaProperties;

import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

public class ReadFromStartConsumer implements Runnable {

    private KafkaConsumer<String, String> kafkaConsumer;
    private String topic;

    public ReadFromStartConsumer(KafkaProperties consumerProperties) {
        this.kafkaConsumer = new KafkaConsumer<>(consumerProperties.getProperties());
        this.topic = consumerProperties.getTopicName();
    }

    @Override
    public void run() {
        try {
            System.out.println("Reading from start");
            kafkaConsumer.subscribe(Collections.singletonList(topic), new ConsumerRebalanceListener() {

                @Override
                public void onPartitionsRevoked(Collection<TopicPartition> collection) {

                }

                @Override
                public void onPartitionsAssigned(Collection<TopicPartition> collection) {
                    kafkaConsumer.seekToBeginning(collection);
                }
            });
            kafkaConsumer.poll(Duration.ZERO);

            while (true) {
                ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ZERO);
                for (ConsumerRecord<String, String> record : records) {
                    System.out.println(String.format("Received key %s and value %s from partition %d and offset %d by thread %s",
                            record.key(), record.value(), record.partition(), record.offset(), Thread.currentThread().getName()));
                }
            }
        } finally {
            kafkaConsumer.close();
        }
    }
}
