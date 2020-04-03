package ie.ncirl.tradingplatform.dto;

import java.util.Date;

import lombok.Data;

@Data
public class GoalsRequest {
	private String name;

	private Date startDate;

	private Date endDate;
	
	private Double targetAmount;
	
	private Integer percent;
	}
