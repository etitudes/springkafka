package home.ex05_message_listener_container_general_template;

public interface GenericMessageListener<T> {
	void onMessage(T message);
}
