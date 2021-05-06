package consumer;

import consumer.tasks.FileProcessorTask;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import property.KafkaProperties;
import valueobject.InstrumentFile;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BasicConsumer {

    private KafkaConsumer<String, InstrumentFile> kafkaConsumer;
    private Properties properties;
    private ExecutorService executorService;

    public BasicConsumer(KafkaProperties consumerProperties) {
        this.kafkaConsumer = new KafkaConsumer<>(consumerProperties.getProperties());
        this.properties = consumerProperties.getProperties();
    }

    public void consume() {

        Runtime.getRuntime().addShutdownHook(new Thread(
                () -> {
                    if (kafkaConsumer != null) {
                        kafkaConsumer.close();
                    }
                    if (executorService != null) {
                        executorService.shutdown();
                    }
                }
        ));

        try {
            executorService = Executors.newFixedThreadPool(Integer.parseInt(properties.getProperty("thread.count")));
            kafkaConsumer.subscribe(Collections.singletonList(properties.getProperty("consumer.topic")));
            while (true) {
                ConsumerRecords<String, InstrumentFile> records = kafkaConsumer.poll(Duration.ZERO);
                for (ConsumerRecord<String, InstrumentFile> record : records) {
                    System.out.println(String.format("Received key %s and value %s from partition %d and offset %d by thread %s",
                            record.key(), record.value(), record.partition(), record.offset(), Thread.currentThread().getName()));
                    FileProcessorTask fileProcessorTask = new FileProcessorTask(properties, record);
                    executorService.submit(fileProcessorTask);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            kafkaConsumer.close();
            if (executorService != null) {
                executorService.shutdown();
            }
        }

    }

}
