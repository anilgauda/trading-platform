package ie.ncirl.tradingplatform.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name = "goals")
public class Goal {
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	private Long id;
	
	@NotNull
	private String name;
	@NotNull
	private Date startDate;
	@NotNull
	private Date endDate;
	@NotNull
	private Double targetAmount;
	@NotNull
	private Integer percent;
	@ManyToOne
	private Account account;
	
	

}
