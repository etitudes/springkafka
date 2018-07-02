package home.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

@Slf4j
public abstract class JsonDeserializer {

	private JsonDeserializer() {
	}

	private static ObjectMapper objectMapper;

	static {
		objectMapper = new ObjectMapper();
		objectMapper.configure(FAIL_ON_UNKNOWN_PROPERTIES, false);
	}

	public static <T> T deserialize(String message, Class<T> clazz) {
		log.error("received message : [{}]", message);
		try {
			return objectMapper.readValue(message, clazz);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
