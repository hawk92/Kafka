package property;

import exception.KafkaException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class KafkaProperties {

    private InputStream inputStream;
    private Properties properties = new Properties();

    public KafkaProperties(String propertyFileName) throws KafkaException {
        if (propertyFileName == null || propertyFileName.isEmpty()) {
            throw new KafkaException("Property file name not defined");
        }
        this.inputStream = KafkaProperties.class.getClassLoader().getResourceAsStream(propertyFileName);
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Properties getProperties() {
        return properties;
    }

    public String getTopicName() {
        return properties.getProperty("topic.name");
    }

    public int getThreadCount() {
        return Integer.parseInt(properties.getProperty("thread.count"));
    }
}
