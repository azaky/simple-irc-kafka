package com.client;

import kafka.consumer.ConsumerConfig;
import kafka.producer.ProducerConfig;

import java.io.FileInputStream;
import java.io.IOError;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by azaky on 14/10/15.
 */
public class Configs {

    public static String ZOOKEEPER = "localhost:2181";
    public static String BROKER = "localhost:9092";
    private static final Properties PRODUCER_PROPERTIES = new Properties();

    static {
        FileInputStream input = null;
        try {
            input = new FileInputStream("config.properties");
            Properties properties = new Properties();
            properties.load(input);
            ZOOKEEPER = properties.getProperty("zookeeper", ZOOKEEPER);
            BROKER = properties.getProperty("broker", BROKER);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        PRODUCER_PROPERTIES.put("metadata.broker.list", BROKER);
        PRODUCER_PROPERTIES.put("serializer.class","kafka.serializer.StringEncoder");
    }


    private Configs() {
        // utility class
    }

    public static Properties getConsumerProperties(String groupId) {
        Properties properties = new Properties();
        properties.put("zookeeper.connect", ZOOKEEPER);
        properties.put("group.id", groupId);
        return properties;
    }

    public static ConsumerConfig getConsumerConfig(String groupId) {
        return new ConsumerConfig(getConsumerProperties(groupId));
    }

    public static Properties getProducerProperties() {
        return PRODUCER_PROPERTIES;
    }

    public static ProducerConfig getProducerConfig() {
        return new ProducerConfig(getProducerProperties());
    }

}
