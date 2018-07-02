package home.ex05_message_listener_container_general_template;

import home.serializer.JsonDeserializer;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.listener.config.ContainerProperties;

public abstract class KafkaConsumerContainerFactory {
	private KafkaConsumerContainerFactory() {}

	public static <T> ConcurrentMessageListenerContainer<String, String> newContainer(Class<T> clazz, GenericMessageListener<T> listener, ContainerProperty property) {
		ContainerProperties containerProps = new ContainerProperties(property.getTopic());
		containerProps.setGroupId(property.getConsumerGroupId());
		containerProps.setMessageListener((MessageListener<String, String>) message ->
			listener.onMessage(JsonDeserializer.deserialize(message.value(), clazz))
		);

		DefaultKafkaConsumerFactory<String, String> consumerFactory = new DefaultKafkaConsumerFactory<>(property.getConsumerConfigs());
		ConcurrentMessageListenerContainer<String, String> container = new ConcurrentMessageListenerContainer<>(consumerFactory, containerProps);
		container.setConcurrency(property.getConcurrency());
		container.start();
		return container;
	}
}
