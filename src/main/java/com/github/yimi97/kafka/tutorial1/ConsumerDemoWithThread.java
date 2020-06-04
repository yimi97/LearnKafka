package com.github.yimi97.kafka.tutorial1;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class ConsumerDemoWithThread {
    public static void main(String[] args) {

        Logger logger = LoggerFactory.getLogger(ConsumerDemoWithThread.class.getName());

        String bootstrapServers = "vcm-15078.vm.duke.edu:9092";
        String groupId = "my-fourth-application";
        String topic = "first_topic";

        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);

        /*
        * StringDeserializer: when a producer creates a string and serialize to bytes and sends it to Kafka,
        * and when Kafka sends theses bytes right back to our consumer, our consumer has to take these bytes
        * and create a string from it,and that process is call deserialization
        * */
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId);

        /*
        * "earliest": read from the very beginning of the topic, equal to --from-the-beginning
        * "latest": read only the new msg
        * "none": throw an error
        * */
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        // create consumer
        KafkaConsumer<String, String> consumer =
                new KafkaConsumer<String, String>(properties);

        // subscribe consumer to our topics
        // Collections.singleton: only subscribe to one topic
        // could subscribe more by using Arrays.asList("first_topic", "second_topic")
        consumer.subscribe(Collections.singleton(topic));

        // poll for new data
        while (true) {
            ConsumerRecords<String, String> records =
                    consumer.poll(Duration.ofMillis(100)); // poll: the non-deprecated version uses a duration

            for (ConsumerRecord<String, String> record: records) {
                logger.info("Key: " + record.key() + ", Value: " + record.value());
                logger.info("Partition: " + record.partition() + ", Offset: " + record.offset());

            }

        }

    }
}
