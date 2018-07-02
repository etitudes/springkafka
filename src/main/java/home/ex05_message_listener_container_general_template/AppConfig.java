package home.ex05_message_listener_container_general_template;

import com.fasterxml.jackson.databind.ObjectMapper;
import home.ex00_props.SimpleConsumerProperties;
import home.serializer.SimpleJsonSerializer;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.listener.config.ContainerProperties;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

@Configuration
@ComponentScan(basePackageClasses = { AppConfig.class })
@Slf4j
class AppConfig {

	private static ObjectMapper objectMapper;
	static {
		objectMapper = new ObjectMapper();
		objectMapper.configure(FAIL_ON_UNKNOWN_PROPERTIES, false);
	}

	@Bean
	public ConcurrentMessageListenerContainer<Integer, String> simpleEmpMessageListenerContainer(
		@Autowired SimpleEmpListener listener
	) {
		ContainerProperties containerProps = new ContainerProperties("emp-topic");
		containerProps.setGroupId("foo_consumer_ex04");
		Class<EmpDto> valueType = EmpDto.class;
		containerProps.setMessageListener((MessageListener<Integer, String>) message -> {
			log.warn("MessageListener.onMessage() - received: " + message);
			try {
				listener.onMessage(objectMapper.readValue(message.value(), valueType));
			} catch (IOException e) {
				e.printStackTrace();
			}
		});

		Map<String, Object> configs = consumerConfigs();
		configs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, IntegerDeserializer.class);

		ConcurrentMessageListenerContainer<Integer, String> container =
			new ConcurrentMessageListenerContainer<>(
				new DefaultKafkaConsumerFactory<>(configs),
				containerProps
			);
		container.start();
		return container;
	}

	@Bean
	public ConsumerFactory<Integer, String> consumerFactory() {
		return new DefaultKafkaConsumerFactory<>(consumerConfigs());
	}

	@Bean
	public Map<String, Object> consumerConfigs() {
		return SimpleConsumerProperties.make();
	}

	public ProducerFactory<String, Object> simpleProducerFactory() {
		Map<String, Object> props = new HashMap<>();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, SimpleJsonSerializer.class);
		return new DefaultKafkaProducerFactory<>(props);
	}

	@Bean("simpleKafkaTemplate")
	public KafkaTemplate<String, Object> simpleKafkaTemplate() {
		return new KafkaTemplate<>(simpleProducerFactory());
	}
}
