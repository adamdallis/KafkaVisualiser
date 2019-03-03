package com.finallytrycatch.kafkaVisualiser;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.PartitionInfo;

public class KafkaVisualiser {
    public static void main(String[] args) {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("group.id", "test-consumer-group");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("bootstrap.servers", "localhost:9092");
        props.put("acks", "all");
        props.put("retries", 10);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");

        props.put("value.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");
        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);
        Map<String, List<PartitionInfo>> topics = consumer.listTopics();

        Iterator<String> iterator = topics.keySet().iterator();

        while(iterator.hasNext()) {
            String topic = iterator.next();
            if(!topic.startsWith("_")){
                List<PartitionInfo> partitionInfos = topics.get(topic);
                for (PartitionInfo partition:partitionInfos) {
                    System.out.println(partition);
                }
            }
        }
        consumer.close();
        sendMessagesToKafka(props);

    }

    private static void sendMessagesToKafka(Properties props) {

        String topicName = "test";
        Producer<String, String> producer = new KafkaProducer<String, String>(props);

        for (int i = 0; i < 10; i++)
            producer.send(new ProducerRecord<String, String>(topicName,
                    Integer.toString(i), Integer.toString(i)));
        System.out.println("Message sent successfully");
        producer.close();
    }
}
