package home.ex05_message_listener_container_general_template;

import home.serializer.JsonDeserializer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.listener.config.ContainerProperties;

import java.util.HashMap;
import java.util.Map;

public abstract class KafkaConsumerContainerFactory {
	private KafkaConsumerContainerFactory() {}

	public static <T> ConcurrentMessageListenerContainer<String, String> newContainer(Class<T> clazz, GenericMessageListener<T> listener, ContainerProperty property) {
		ContainerProperties containerProps = new ContainerProperties(property.getTopic());
		containerProps.setGroupId(property.getConsumerGroupId());
		containerProps.setMessageListener((MessageListener<String, String>) message ->
			listener.onMessage(JsonDeserializer.deserialize(message.value(), clazz))
		);

		DefaultKafkaConsumerFactory<String, String> consumerFactory = new DefaultKafkaConsumerFactory<>(consumerConfigs());
		ConcurrentMessageListenerContainer<String, String> container = new ConcurrentMessageListenerContainer<>(consumerFactory, containerProps);
		container.start();
		return container;
	}

	private static Map<String, Object> consumerConfigs() {
		Map<String, Object> consumerProps = new HashMap<>();
		consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
		consumerProps.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
		consumerProps.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "100");
		consumerProps.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "15000");
		consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		return consumerProps;
	}
}
