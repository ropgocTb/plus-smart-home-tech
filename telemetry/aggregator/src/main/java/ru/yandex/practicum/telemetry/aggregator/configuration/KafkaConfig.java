package ru.yandex.practicum.telemetry.aggregator.configuration;

import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class KafkaConfig {

    @Value("${aggregator.kafka.bootstrap-servers}")
    private String server;

    @Value("${aggregator.kafka.producer.properties.key.serializer}")
    private String keySerializer;

    @Value("${aggregator.kafka.consumer.properties.key.deserializer}")
    private String keyDeserializer;

    @Value("${aggregator.kafka.producer.properties.value.serializer}")
    private String valueSerializer;

    @Value("${aggregator.kafka.consumer.properties.value.deserializer}")
    private String valueDeserializer;

    @Value("${aggregator.kafka.consumer.properties.group.id}")
    private String groupId;

    @Value("${aggregator.kafka.consumer.properties.client.id}")
    private String clientId;

    @Value("${aggregator.kafka.consumer.properties.enable.auto.commit}")
    private String autoCommit;

    @Value("${aggregator.kafka.consumer.properties.auto.offset.reset}")
    private String autoOffset;

    @Bean
    public Producer<String, SpecificRecordBase> getProducer() {
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, server);
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, keySerializer);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, valueSerializer);
        return new KafkaProducer<>(properties);
    }

    @Bean
    public KafkaConsumer<String, SpecificRecordBase> getConsumer() {
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, server);
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, keyDeserializer);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, valueDeserializer);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        properties.put(ConsumerConfig.CLIENT_ID_CONFIG, clientId);
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, autoCommit);
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffset);
        return new KafkaConsumer<>(properties);
    }
}
