package home.ex05_message_listener_container_general_template.client;

import home.ex05_message_listener_container_general_template.MessageReceiver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SimpleEmpReceiver implements MessageReceiver<EmpDto> {

	@Override
	public void receive(EmpDto message) {
		log.warn("SimpleEmpReceiver.receive() ===============> received: " + message);
	}
}