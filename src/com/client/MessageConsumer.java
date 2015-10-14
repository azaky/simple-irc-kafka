package com.client;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by azaky on 14/10/15.
 */
public class MessageConsumer implements Runnable {

    private final String groupId;
    private final String topic;
    private final ConsumerConnector connector;

    public MessageConsumer(String groupId, String topic) {
        this.groupId =  groupId;
        this.topic = topic;
        connector = Consumer.createJavaConsumerConnector(Configs.getConsumerConfig(groupId));
    }

    @Override
    public void run() {
        Map<String, Integer> topicCountMap = new HashMap<>();
        topicCountMap.put(topic, new Integer(1));
        Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = connector.createMessageStreams(topicCountMap);
        KafkaStream<byte[], byte[]> stream = consumerMap.get(topic).get(0);
        ConsumerIterator<byte[], byte[]> it = stream.iterator();
        while(it.hasNext()) {
            System.out.println(new String(it.next().message()));
        }
    }

    public void shutdown() {
        connector.shutdown();
    }
}
