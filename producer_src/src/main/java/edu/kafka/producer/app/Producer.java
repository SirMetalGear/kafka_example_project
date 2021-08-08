package edu.kafka.producer.app;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.IntegerSerializer;

public class Producer {
    public static void main(String[] args) {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                IntegerSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                IntegerSerializer.class.getName());
        String topicName = "fizzBuzz";
        KafkaProducer<Integer, Integer> producer = new KafkaProducer<>(props);
        for(int i = 1; i <= 1_000_000; i++)
            producer.send(new ProducerRecord<>(topicName,
                    i - 1, i));
        System.out.println("Message sent successfully");
        producer.close();
    }
}
