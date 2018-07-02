package home.ex04_message_listener_container;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
class SimpleListener {

	public void listen1(String foo) {
		log.warn("SimpleListener.listen1() - received: " + foo);
	}
}