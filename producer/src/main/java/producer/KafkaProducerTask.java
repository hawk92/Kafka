package producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import property.KafkaProperties;
import utils.FileUtils;
import valueobject.InstrumentFile;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class KafkaProducerTask implements Runnable {

    private KafkaProducer<String, InstrumentFile> kafkaProducer;
    private String topic;
    private String instrumentType;
    private String filePath;

    public KafkaProducerTask(KafkaProperties consumerProperties, String instrumentType, String filePath) {
        this.kafkaProducer = new KafkaProducer<>(consumerProperties.getProperties());
        this.topic = consumerProperties.getTopicName();
        this.instrumentType = instrumentType;
        this.filePath = filePath;
    }

    public void sendAndForget(ProducerRecord<String, InstrumentFile> record) {
        //Send and forget
        kafkaProducer.send(record);
    }

    public RecordMetadata syncSend(ProducerRecord<String, InstrumentFile> record) {
        //Sync send
        RecordMetadata metadata = null;
        try {
            metadata = kafkaProducer.send(record).get();
            System.out.println(String.format("Partition %d,Offset %d", metadata.partition(), metadata.offset()));
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return metadata;
    }

    private void asyncSend(ProducerRecord<String, InstrumentFile> record) {
        //Async send
        kafkaProducer.send(record, ((recordMetadata, e) -> {
            if (e == null) {
                System.out.println(String.format("Partition %d,Offset %d", recordMetadata.partition(), recordMetadata.offset()));
            } else {
                e.printStackTrace();
            }
        }));
    }

    @Override
    public void run() {
        try {
            InstrumentFile instrumentFile = new InstrumentFile(instrumentType, FileUtils.getFileName(filePath), FileUtils.readFileBytes(filePath));
            ProducerRecord<String, InstrumentFile> record = new ProducerRecord<>(topic, instrumentType, instrumentFile);
            asyncSend(record);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            kafkaProducer.close();
        }
    }
}