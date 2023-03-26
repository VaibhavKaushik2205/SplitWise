package com.project.splitwise.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ContainerProperties.AckMode;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.SeekToCurrentErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

@Configuration
@EnableKafka
@Slf4j
@ComponentScan({"com.project.splitwise.kafka"})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class KafkaConfig {

    public static final String SECURITY_UTILS = "security.utils";
    @Value("${kafka.servers}")
    private String bootstrapServers;
    @Value("${consumer.retry.attempts:3}")
    private int consumerRetryAttempts;
    @Value("${consumer.max.poll.size:50}")
    private int maxPollSize;
    @Value("${consumer.max.poll.interval.ms:300000}")
    private int maxPollIntervalMs;
    @Value("${max.request.size:3145728}")
    private int maxRequestSize;
    @Value("${producer.max.in.flight.requests:1}")
    private int maxInFlightRequestPerConnection;
    @Value("${consumer.auto.offset:latest}")
    private String autoOffsetRest;
    @Value("${producer.enable.idempotency:true}")
    private boolean enableIdempotency;
    @Value("${kafka.cluster.api.key}")
    private String clusterApiKey;
    @Value("${kafka.cluster.api.secret}")
    private String clusterApiSecret;
    @Value("${ENVIRONMENT:test}")
    private String environment;

    @Bean({"kafkaTemplate"})
    @Primary
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate(this.producerFactory(), true);
    }


    @Bean(
        name = {"kafkaListenerContainerFactory"}
    )
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory();
        factory.setConsumerFactory(this.consumerFactory());
        factory.getContainerProperties().setAckMode(AckMode.RECORD);
        return factory;
    }


    @Bean(
        name = {"producerFactory"}
    )
    public ProducerFactory<String, String> producerFactory() {
        Map<String, Object> props = new HashMap();
        props.put("bootstrap.servers", this.bootstrapServers);
        props.put("key.serializer", StringSerializer.class);
        props.put("value.serializer", StringSerializer.class);
        props.put("max.in.flight.requests.per.connection", this.maxInFlightRequestPerConnection);
        props.put("max.request.size", this.maxRequestSize);
        props.put("acks", "all");
        props.put("enable.idempotence", true);
        props.putAll(this.credentialsConfig());
        return new DefaultKafkaProducerFactory(props);
    }

    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        Map<String, Object> props = new HashMap();
        props.put("bootstrap.servers", this.bootstrapServers);
        props.put("key.deserializer", StringDeserializer.class);
        props.put("value.deserializer", StringDeserializer.class);
        props.put("auto.offset.reset", this.autoOffsetRest);
        props.put("max.poll.records", this.maxPollSize);
        props.put("max.poll.interval.ms", this.maxPollIntervalMs);
        props.putAll(this.credentialsConfig());
        return new DefaultKafkaConsumerFactory(props);
    }


    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> eventListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory();
        factory.setConsumerFactory(this.eventConsumerFactory());
        factory.setStatefulRetry(true);
        factory.setErrorHandler(
            new SeekToCurrentErrorHandler(new DeadLetterPublishingRecoverer(this.kafkaTemplate()), new FixedBackOff(1L, (long) this.consumerRetryAttempts)));
        return factory;
    }

    @Bean
    public ConsumerFactory<String, String> eventConsumerFactory() {
        Map<String, Object> props = new HashMap();
        props.put("bootstrap.servers", this.bootstrapServers);
        props.put("key.deserializer", StringDeserializer.class);
        props.put("value.deserializer", StringDeserializer.class);
        props.put("max.poll.interval.ms", this.maxPollIntervalMs);
        props.putAll(this.credentialsConfig());
        return new DefaultKafkaConsumerFactory(props);
    }

    @Bean
    public List<Class<? extends Exception>> nonRetryableExceptions() {
        return List.of(JsonProcessingException.class, RuntimeException.class);
    }

    private Map<String, String> credentialsConfig() {
        Map<String, String> credentialsConfig = new HashMap();
        if (this.environment.equalsIgnoreCase("prod")) {
            //log.info("Running in {} environment, Applying confluent credentials", this.environment);
            credentialsConfig.put("security.protocol", "SASL_SSL");
            credentialsConfig.put("sasl.jaas.config",
                String.format("org.apache.kafka.common.security.plain.PlainLoginModule required username=\"%s\" password=\"%s\";", this.clusterApiKey,
                    this.clusterApiSecret));
            credentialsConfig.put("ssl.endpoint.identification.algorithm", "https");
            credentialsConfig.put("sasl.mechanism", "PLAIN");
        } else if (!this.environment.equals("test")) {
            log.info("Running in {} environment, Applying confluent credentials", this.environment);
            credentialsConfig.put("security.protocol", "SASL_SSL");
            credentialsConfig.put("sasl.jaas.config",
                String.format("org.apache.kafka.common.security.scram.ScramLoginModule required username=\"%s\" password=\"%s\";",
                    this.clusterApiKey, this.clusterApiSecret));
            credentialsConfig.put("ssl.endpoint.identification.algorithm", "https");
            credentialsConfig.put("sasl.mechanism", "SCRAM-SHA-512");
        } else {
            log.warn("Running in {} environment, Ignoring confluent credentials", this.environment);
        }

        return credentialsConfig;
    }
}
