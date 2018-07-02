package home.ex05_message_listener_container_general_template.client;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class EmpDto {
	private Long empNo;
	private String name;
	private Date hireDate;
}
