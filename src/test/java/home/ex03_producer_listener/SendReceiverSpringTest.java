package home.ex03_producer_listener;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AppConfig.class})
public class SendReceiverSpringTest {

	@Autowired
	private SimpleListener simpleListener;

	@Autowired
	private KafkaTemplate<Integer, String> template;

	@Test
	public void testSimple() throws Exception {
		template.send("annotated1", 0, "foo1");
		template.flush();

		// 이 라인에서 break point 걸고, debug로 실행한 다음
		// 이 라인에 진입 직전에 토픽을 삭제 -> 라인 진입하면 에러 발생됨
		template.send("annotated1", 1, "foo2");
		template.flush();
		assertTrue(this.simpleListener.latch1.await(20, TimeUnit.SECONDS));
	}
}
