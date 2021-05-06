package serializer;

import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Serializer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Map;

public class InstrumentFileSerializer implements Serializer {
    @Override
    public void close() {

    }

    @Override
    public void configure(Map configs, boolean isKey) {

    }

    @Override
    public byte[] serialize(String s, Object o) {
        try {
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            ObjectOutputStream objectStream = new ObjectOutputStream(byteStream);
            objectStream.writeObject(o);
            objectStream.flush();
            objectStream.close();
            return byteStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalStateException("Can't serialize object: " + o, e);
        }
    }

    @Override
    public byte[] serialize(String topic, Headers headers, Object data) {
        return new byte[0];
    }
}
