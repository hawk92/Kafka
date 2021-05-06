package consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import property.KafkaProperties;

import java.time.Duration;
import java.util.Collections;

public class NoCommitConsumer implements Runnable {

    private KafkaConsumer<String, String> kafkaConsumer;
    private String topic;

    public NoCommitConsumer(KafkaProperties consumerProperties) {
        this.kafkaConsumer = new KafkaConsumer<String, String>(consumerProperties.getProperties());
        this.topic = consumerProperties.getTopicName();
    }

    @Override
    public void run() {
        try {
            kafkaConsumer.subscribe(Collections.singletonList(topic));
            while (true) {
                ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofMillis(1000));
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
