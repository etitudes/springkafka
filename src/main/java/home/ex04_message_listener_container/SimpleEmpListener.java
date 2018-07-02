package home.ex04_message_listener_container;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
class SimpleEmpListener implements GenericMessageListener<EmpDto> {

	@Override
	public void onMessage(EmpDto message) {
		log.warn("SimpleEmpListener.onMessage() - received: " + message);
	}
}