import consumer.BasicConsumer;
import exception.KafkaException;
import property.KafkaProperties;

public class ConsumerApp {

    public static void main(String args[]) {
        try {
            KafkaProperties consumerProperties = new KafkaProperties("consumers/basic.properties");
            BasicConsumer basicConsumer = new BasicConsumer(consumerProperties);
            basicConsumer.consume();
        } catch (KafkaException e) {
            e.printStackTrace();
        }
    }
}
