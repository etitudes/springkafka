package home.ex02_spring_version;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
public class SendReceiverSpringTest {

	@Autowired
	private SimpleListener simpleListener;

	@Autowired
	private KafkaTemplate<Integer, String> template;

	@Test
	public void testSimple() throws Exception {
		template.send("annotated1", 0, "foo");
		template.flush();
		assertTrue(this.simpleListener.latch1.await(10, TimeUnit.SECONDS));
	}
}
