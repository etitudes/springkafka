package home.ex03_producer_listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;

import java.util.concurrent.CountDownLatch;

@Slf4j
class SimpleListener {
	final CountDownLatch latch1 = new CountDownLatch(1);

	@KafkaListener(id = "foo", topics = "annotated1")
	private void listen1(String foo) {
		log.warn("received: " + foo);
		this.latch1.countDown();
	}
}