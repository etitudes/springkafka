package home.ex05_message_listener_container_general_template;

import home.ex05_message_listener_container_general_template.client.EmpDto;
import home.ex05_message_listener_container_general_template.client.SimpleEmpListener;
import home.serializer.SimpleJsonSerializer;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@ComponentScan(basePackageClasses = { AppConfig.class })
@Slf4j
class AppConfig {

	@Bean
	public ConcurrentMessageListenerContainer simpleEmpMessageListenerContainer(@Autowired SimpleEmpListener listener) {
		ContainerProperty property = new ContainerProperty();
		property.setTopic("emp-topic");
		property.setConsumerGroupId("foo_consumer_ex04");

		return KafkaConsumerContainerFactory.newContainer(EmpDto.class, listener, property);
	}

	@Bean("simpleKafkaTemplate")
	public KafkaTemplate<String, Object> simpleKafkaTemplate() {
		return new KafkaTemplate<>(simpleProducerFactory());
	}

	private ProducerFactory<String, Object> simpleProducerFactory() {
		Map<String, Object> producerProps = new HashMap<>();
		producerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
		producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, SimpleJsonSerializer.class);
		return new DefaultKafkaProducerFactory<>(producerProps);
	}
}
