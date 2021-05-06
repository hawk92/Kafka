import exception.KafkaException;
import producer.KafkaProducerTask;
import property.KafkaProperties;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProducerApp {

    public static void main(String args[]) {

        Map<String, String> instrumentFileMap = new HashMap<>();

        instrumentFileMap.put("BG", "F:\\kafka-testing\\source\\A.txt");
        instrumentFileMap.put("FD", "F:\\kafka-testing\\source\\B.txt");

        ExecutorService executorService = null;
        try {
            KafkaProperties producerProperties = new KafkaProperties("producer.properties");
            executorService = Executors.newFixedThreadPool(producerProperties.getThreadCount());

            for (String instrumentType : instrumentFileMap.keySet()) {
                KafkaProducerTask kafkaProducerTask = new KafkaProducerTask(producerProperties, instrumentType, instrumentFileMap.get(instrumentType));
                executorService.submit(kafkaProducerTask);
            }
        } catch (KafkaException e) {
            e.printStackTrace();
        } finally {
            executorService.shutdown();
        }
    }
}
