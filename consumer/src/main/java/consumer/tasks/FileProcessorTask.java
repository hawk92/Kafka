package consumer.tasks;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import utils.FileUtils;
import valueobject.InstrumentFile;

import java.io.IOException;
import java.util.Properties;

public class FileProcessorTask implements Runnable {

    private KafkaProducer<String, InstrumentFile> kafkaProducer;
    private ConsumerRecord<String, InstrumentFile> record;
    private Properties properties;

    public FileProcessorTask(Properties properties, ConsumerRecord<String, InstrumentFile> record) {
        this.kafkaProducer = new KafkaProducer<>(properties);
        this.record = record;
        this.properties = properties;
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

        String processedFilePath = properties.getProperty("file.path") + record.value().getFileName();
        try {
            FileUtils.writeFile(record.value().getFileData(), processedFilePath);
            InstrumentFile processedInstrumentFile = new InstrumentFile(
                    record.value().getInstrumentType(), record.value().getFileName(), FileUtils.readFileBytes(processedFilePath));
            ProducerRecord<String, InstrumentFile> record = new ProducerRecord<>(
                    properties.getProperty("producer.topic"), processedInstrumentFile);
            asyncSend(record);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            kafkaProducer.close();
        }
    }
}