package ie.ncirl.tradingplatform.vo;

import java.util.List;

import lombok.Data;

@Data
public class GoalVo {
	private Long id;
	private String name;
	private Integer percent;
	private List<Double> amounts;
	
}
