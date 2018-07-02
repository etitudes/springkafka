package home.ex04_message_listener_container;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Builder
@Data
public class EmpDto {
	private Long empNo;
	private String name;
	private Date hireDate;
}
