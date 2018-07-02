package home.ex04_message_listener_container;

import home.ex00_props.SimpleConsumerProperties;
import home.ex00_props.SimpleProducerProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.listener.config.ContainerProperties;

import java.util.Map;

@Configuration
@ComponentScan(basePackageClasses = { AppConfig.class })
//@EnableKafka
@Slf4j
class AppConfig {

	@Bean
	public ConcurrentMessageListenerContainer<Integer, String> simpleMessageListenerContainer(@Autowired SimpleListener simpleListener) {
		ContainerProperties containerProps = new ContainerProperties("annotated1");
		containerProps.setGroupId("foo_consumer_ex04");
		containerProps.setMessageListener((MessageListener<Integer, String>) message -> {
			log.warn("MessageListener.onMessage() - received: " + message);
			simpleListener.listen1(message.value());
		});

		ConcurrentMessageListenerContainer<Integer, String> container =
			new ConcurrentMessageListenerContainer<>(
				consumerFactory(),
				containerProps
			);
		container.start();
		return container;
	}

	@Bean
	public ConcurrentMessageListenerContainer<Long, EmpDto> simpleEmpMessageListenerContainer(
		@Autowired SimpleEmpListener listener
	) {
		ContainerProperties containerProps = new ContainerProperties("emp-topic");
		containerProps.setGroupId("foo_consumer_ex04");
		containerProps.setMessageListener((MessageListener<Long, EmpDto>) message -> {
			log.warn("MessageListener.onMessage() - received: " + message);
			listener.onMessage(message.value());
		});

		Map<String, Object> configs = consumerConfigs();
		configs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class);
		configs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

		ConcurrentMessageListenerContainer<Long, EmpDto> container =
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

	@Bean
	public ProducerFactory<Integer, String> producerFactory() {
		return new DefaultKafkaProducerFactory<>(producerConfigs());
	}

	@Bean
	public Map<String, Object> producerConfigs() {
		return SimpleProducerProperties.make();
	}

	@Bean
	public KafkaTemplate kafkaTemplate() {
		return new KafkaTemplate<>(producerFactory());
	}
}
