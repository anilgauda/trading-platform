package ie.ncirl.tradingplatform.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SharesTableVo {
	String name;
	String symbol;
	Double mcap;
	Double price;
	Double open;
	Double prevClose;
	Double vol;
	Double eps;
	Double pe;
	Double bookVal;
	

}
