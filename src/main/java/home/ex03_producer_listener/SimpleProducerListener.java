package home.ex03_producer_listener;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.support.ProducerListenerAdapter;

@Slf4j
public class SimpleProducerListener extends ProducerListenerAdapter<Integer, String> {
	@Override
	public boolean isInterestedInSuccess() {
		// true 를 반환해야 onSuccess()가 호출된다.
		return true;
	}

	@Override
	public void onSuccess(
		String topic, Integer partition,
		Integer key, String value,
		RecordMetadata recordMetadata
	) {
		log.error("Producer에서 정상 처리됨\n- topic: {}\n- key: {}\n- value: {}",
			topic, key, value);
	}

	@Override
	public void onError(
		String topic, Integer partition,
		Integer key, String value,
		Exception exception
	) {
		log.error("Producer에서 에러가 발생함\n- message: {}\n- topic: {}\n- key: {}\n- value: {}",
			exception.getMessage(), topic, key, value);
	}
}
