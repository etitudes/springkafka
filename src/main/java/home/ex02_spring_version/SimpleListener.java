package home.ex02_spring_version;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;

import java.util.concurrent.CountDownLatch;

@Slf4j
class SimpleListener {
	final CountDownLatch latch1 = new CountDownLatch(1);

	@KafkaListener(id = "foo_consumer_ex02", topics = "annotated1")
	private void listen1(String foo) {
		log.warn("received: " + foo);
		this.latch1.countDown();
	}
}