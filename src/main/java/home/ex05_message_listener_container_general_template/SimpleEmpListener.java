package home.ex05_message_listener_container_general_template;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
class SimpleEmpListener implements GenericMessageListener<EmpDto> {

	@Override
	public void onMessage(EmpDto message) {
		log.warn("SimpleEmpListener.onMessage() ===============> received: " + message);
	}
}