package home.ex05_message_listener_container_general_template;

import home.ex05_message_listener_container_general_template.client.EmpDto;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.config.ContainerProperties;

public abstract class KafkaConsumerContainerFactory {
	private KafkaConsumerContainerFactory() {}

	public static <T> ConcurrentMessageListenerContainer<String, String> newContainer(Class<T> clazz, MessageReceiver<T> receiver, ContainerProperty property) {
		ContainerProperties containerProps = new ContainerProperties(property.getTopic());
		containerProps.setGroupId(property.getConsumerGroupId());
		containerProps.setMessageListener(new BaseMessageListener(receiver, EmpDto.class));

		DefaultKafkaConsumerFactory<String, String> consumerFactory = new DefaultKafkaConsumerFactory<>(property.getConsumerConfigs());
		ConcurrentMessageListenerContainer<String, String> container = new ConcurrentMessageListenerContainer<>(consumerFactory, containerProps);
		container.setConcurrency(property.getConcurrency());
		container.start();
		return container;
	}
}
