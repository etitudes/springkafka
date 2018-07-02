package home.ex05_message_listener_container_general_template;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor
@Data
public class ContainerProperty {
	private String topic;
	private String consumerGroupId;
	private int concurrency = 2;
	private Map<String, Object> consumerConfigs;
}
