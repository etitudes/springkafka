package home.ex04_message_listener_container;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AppConfig.class})
public class SendReceiverSpringTest {

	@Autowired
	private KafkaTemplate<Integer, String> template;

	@Autowired
	private KafkaTemplate<Long, EmpDto> templateEmp;

	@Test
	public void testSimple() throws Exception {
		// container 기동을 기다려줌. 최초의 테스트에만 필요
		// latest로 consume 하므로, 메시지 발송 후 container가 kafka에 가면 latest 이후의 메시지가 없는 상황이 발생할 수 있음
		Thread.sleep(2000);

		template.send("annotated1", 0, "foo1" + new Date());
		template.flush();
		template.send("annotated1", 1, "foo2" + new Date());
		template.flush();

		templateEmp.send("emp-topic", 0L, newEmpJson(1L));
		templateEmp.flush();
		templateEmp.send("emp-topic", 1L, newEmpJson(2L));
		templateEmp.flush();
	}

	private EmpDto newEmpJson(Long empNo) {
		return EmpDto.builder()
			.empNo(empNo)
			.name("직원" + String.valueOf(empNo))
			.hireDate(new Date())
			.build();
	}
}
