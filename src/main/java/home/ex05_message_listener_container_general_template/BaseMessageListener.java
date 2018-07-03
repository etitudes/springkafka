package home.ex05_message_listener_container_general_template;

import home.serializer.JsonDeserializer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.listener.MessageListener;

public class BaseMessageListener<T> implements MessageListener<String, String> {

	private MessageReceiver<T> messageReceiver;
	private Class<T> clazz;

	public BaseMessageListener(MessageReceiver<T> messageReceiver, Class<T> clazz) {
		this.messageReceiver = messageReceiver;
		this.clazz = clazz;
	}

	@Override
	public void onMessage(ConsumerRecord<String, String> record) {
		messageReceiver.receive(JsonDeserializer.deserialize(record.value(), clazz));
	}
}
