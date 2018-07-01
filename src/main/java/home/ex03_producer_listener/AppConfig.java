package home.ex03_producer_listener;

import home.ex00_props.SimpleConsumerProperties;
import home.ex00_props.SimpleProducerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;

import java.util.Map;

@Configuration
@EnableKafka
class AppConfig {

	@Bean
	public ConcurrentKafkaListenerContainerFactory<Integer, String> kafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<Integer, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(consumerFactory());
		return factory;
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
	public SimpleListener simpleListener() {
		return new SimpleListener();
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
	public KafkaTemplate<Integer, String> kafkaTemplate() {
		KafkaTemplate<Integer, String> template = new KafkaTemplate<>(producerFactory());
		template.setProducerListener(new SimpleProducerListener());
		return template;
	}
}
