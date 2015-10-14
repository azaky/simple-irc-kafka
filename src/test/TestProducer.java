package test;

import com.client.Configs;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

/**
 * Created by user on 8/4/14.
 */
public class TestProducer {
    final static String TOPIC = "testa";

    public static void main(String[] argv) {
        Properties properties = new Properties();
        properties.put("metadata.broker.list","localhost:9092");
        properties.put("serializer.class","kafka.serializer.StringEncoder");
        ProducerConfig producerConfig = new ProducerConfig(Configs.getProducerProperties());
        kafka.javaapi.producer.Producer<String,String> producer = new kafka.javaapi.producer.Producer<>(producerConfig);
        SimpleDateFormat sdf = new SimpleDateFormat();
        KeyedMessage<String, String> message =new KeyedMessage<>(TOPIC,"Test message from java program " + sdf.format(new Date()));
        producer.send(message);
        producer.close();
    }
}
