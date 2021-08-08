package edu.kafka.consumer.app;

import edu.kafka.consumer.fizzbuzz.FizzBuzz;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.IntegerDeserializer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;


public class Consumer {

    static final int THREAD_COUNT = 2;

    public static boolean nobodyWorking(FizzBuzz[] workers) {
        for (FizzBuzz worker : workers)
            if (worker.isBusy())
                return false;
        return true;
    }

    public static void main(String[] args) {
        Properties props = new Properties();
        props.put(ConsumerConfig.GROUP_ID_CONFIG,
                "test-consumer-group");
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                IntegerDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                IntegerDeserializer.class.getName());

        final KafkaConsumer<Integer, Integer> consumer =
                new KafkaConsumer<>(props);

        FizzBuzz[] workers = new FizzBuzz[THREAD_COUNT];
        for (int i = 0; i < THREAD_COUNT; ++i) {
            workers[i] = new FizzBuzz();
            workers[i].start();
        }

        int workerTurn = 0;
        try {
            consumer.subscribe(Collections.singletonList("fizzBuzz"));
            while (true) {
                ConsumerRecords<Integer, Integer> records = consumer.poll(Duration.ofMillis(100));
                if (!records.isEmpty() && workerTurn + 1 != THREAD_COUNT) {
                    workers[workerTurn++].newWork(records);
                }
                if (workerTurn + 1 == THREAD_COUNT) {
                    while (!nobodyWorking(workers))
                        continue;
                    for (FizzBuzz worker : workers)
                        worker.print();
                    workerTurn = 0;
                }
                else if (nobodyWorking(workers)) {
                    for (FizzBuzz worker : workers)
                        worker.print();
                    workerTurn = 0;
                }
            }
        } finally {
            for (FizzBuzz worker : workers)
                worker.shutdown();
            consumer.close();
        }
    }
}
