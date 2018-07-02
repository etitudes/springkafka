package home.serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

@Slf4j
public class SimpleJsonSerializer<T> implements Serializer<T> {
	private static final ObjectMapper MAPPER;

	static {
		MAPPER = new ObjectMapper();
		MAPPER.configure(FAIL_ON_UNKNOWN_PROPERTIES, false);
	}

	@Override
	public byte[] serialize(String topic, T data) {
		try {
			return MAPPER.writeValueAsBytes(data);
		} catch (JsonProcessingException e) {
			log.error("kafka serialization failed. data:{}, topic: {}", data, topic, e);
			throw new SerializationException("Can't serialize data [" + data + "] for topic [" + topic + "]", e);
		}
	}

	@Override
	public void configure(Map<String, ?> configs, boolean isKey) {
	}

	@Override
	public void close() {
	}

}
