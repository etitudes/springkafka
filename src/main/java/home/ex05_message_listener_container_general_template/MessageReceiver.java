package home.ex05_message_listener_container_general_template;

public interface MessageReceiver<T> {
	void receive(T message);
}
