package home.ex05_message_listener_container_general_template;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AppConfig.class})
public class SendReceiverSpringTest {

	@Resource(name = "simpleKafkaTemplate")
	private KafkaTemplate<String, EmpDto> kafkaTemplate;

	@Test
	public void testSimple() throws Exception {
		// container 기동을 기다려줌. 최초의 테스트에만 필요
		// latest로 consume 하므로, 메시지 발송 후 container가 kafka에 가면 latest 이후의 메시지가 없는 상황이 발생할 수 있음
		Thread.sleep(2000);

		kafkaTemplate.send("emp-topic", newEmpJson(1L));
		kafkaTemplate.flush();
		kafkaTemplate.send("emp-topic", newEmpJson(2L));
		kafkaTemplate.flush();
	}

	private EmpDto newEmpJson(Long empNo) {
		return EmpDto.builder()
			.empNo(empNo)
			.name("직원" + String.valueOf(empNo))
			.hireDate(new Date())
			.build();
	}
}
