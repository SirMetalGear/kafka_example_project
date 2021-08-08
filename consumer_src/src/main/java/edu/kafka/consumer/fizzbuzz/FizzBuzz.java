package edu.kafka.consumer.fizzbuzz;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;

public class FizzBuzz extends Thread {

    private volatile boolean working;
    private volatile boolean newTask;
    private final StringBuilder stringBuilder;
    private ConsumerRecords<Integer, Integer> records;

    public FizzBuzz() {
        stringBuilder = new StringBuilder();
        this.working = true;
        this.newTask = false;
    }

    public void shutdown() {
        this.working = false;
    }

    public void newWork(ConsumerRecords<Integer, Integer> records) {
        this.records = records;
        newTask = true;
    }

    public void print() {
        if (stringBuilder.length() != 0) {
            System.out.println(stringBuilder);
            stringBuilder.delete(0, stringBuilder.length());
        }
    }

    public boolean isBusy() {
        return newTask;
    }

    @Override
    public void run() {
        int value;
        while (working) {
            if (newTask) {
                for (ConsumerRecord<Integer, Integer> record : records) {
                    value = record.value();
                    if (value % 15 == 0)
                        stringBuilder.append("FizzBuzz\n");
                    else if (value % 3 == 0)
                        stringBuilder.append("Fizz\n");
                    else if (value % 5 == 0)
                        stringBuilder.append("Buzz\n");
                    else
                        stringBuilder.append(value).append("\n");
                }
                newTask = false;
            }
        }
    }
}
