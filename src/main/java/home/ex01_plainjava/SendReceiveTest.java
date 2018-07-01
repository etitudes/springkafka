package home.ex01_plainjava;

import home.ex00_props.SimpleConsumerProperties;
import home.ex00_props.SimpleProducerProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.listener.config.ContainerProperties;

import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Slf4j
public class SendReceiveTest {

	public static void main(String[] args) {
		SendReceiveTest test = new SendReceiveTest();
		try {
			test.testAutoCommit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void testAutoCommit() throws Exception {
		log.info("Start auto");
		ContainerProperties containerProps = new ContainerProperties("topic1", "topic2");
		final CountDownLatch latch = new CountDownLatch(4);
		containerProps.setMessageListener(new MessageListener<Integer, String>() {
			@Override
			public void onMessage(ConsumerRecord<Integer, String> message) {
				log.info("received: " + message);
				latch.countDown();
			}
		});

		KafkaMessageListenerContainer<Integer, String> listenerContainer = createListenerContainer(containerProps);
		listenerContainer.setBeanName("testAuto");
		listenerContainer.start();
		Thread.sleep(1000);

		KafkaTemplate<Integer, String> template = createTemplate();
		template.setDefaultTopic("topic1");
		template.sendDefault(0, "foo");
		template.sendDefault(2, "bar");
		template.sendDefault(0, "baz");
		template.sendDefault(2, "qux");
		template.flush();

		boolean await = latch.await(60, TimeUnit.SECONDS);
		log.error("latch.await: " + await);

		listenerContainer.stop();
		log.info("Stop auto");
	}

	private KafkaMessageListenerContainer<Integer, String> createListenerContainer(ContainerProperties containerProps) {
		Map<String, Object> props = SimpleConsumerProperties.make();
		DefaultKafkaConsumerFactory<Integer, String> cf = new DefaultKafkaConsumerFactory<>(props);
		return new KafkaMessageListenerContainer<>(cf, containerProps);
	}

	private KafkaTemplate<Integer, String> createTemplate() {
		Map<String, Object> producerProps = SimpleProducerProperties.make();
		ProducerFactory<Integer, String> pf = new DefaultKafkaProducerFactory<>(producerProps);
		return new KafkaTemplate<>(pf);
	}
}
