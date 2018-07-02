package home.ex04_message_listener_container;

public interface GenericMessageListener<T> {
	void onMessage(T message);
}
