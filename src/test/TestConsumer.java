package test;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;

import java.nio.ByteBuffer;
import java.util.*;

import kafka.javaapi.message.ByteBufferMessageSet;
import kafka.message.MessageAndOffset;
import java.io.UnsupportedEncodingException;


/**
 * Created by user on 8/4/14.
 */
public class TestConsumer extends  Thread {

    final static String clientId = "SimpleConsumerDemoClient";
    final static String TOPIC = "testa";
    ConsumerConnector consumerConnector;

    public static void main(String[] argv) throws UnsupportedEncodingException {
        TestConsumer helloKafkaConsumer = new TestConsumer();
        helloKafkaConsumer.start();
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
        helloKafkaConsumer.shutdown();
    }

    public TestConsumer(){
        Properties properties = new Properties();
        properties.put("zookeeper.connect", "localhost:2181");
        properties.put("group.id","testtest");
        ConsumerConfig consumerConfig = new ConsumerConfig(properties);
        consumerConnector = Consumer.createJavaConsumerConnector(consumerConfig);
    }

    public void shutdown() {
        consumerConnector.shutdown();
    }

    @Override
    public void run() {
        Map<String, Integer> topicCountMap = new HashMap<>();
        topicCountMap.put(TOPIC, new Integer(1));
        Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumerConnector.createMessageStreams(topicCountMap);
        KafkaStream<byte[], byte[]> stream =  consumerMap.get(TOPIC).get(0);
        ConsumerIterator<byte[], byte[]> it = stream.iterator();
        while(it.hasNext())
            System.out.println(new String(it.next().message()));
    }

    private static void printMessages(ByteBufferMessageSet messageSet) throws UnsupportedEncodingException {
        for(MessageAndOffset messageAndOffset: messageSet) {
            ByteBuffer payload = messageAndOffset.message().payload();
            byte[] bytes = new byte[payload.limit()];
            payload.get(bytes);
            System.out.println(new String(bytes, "UTF-8"));
        }
    }
}
