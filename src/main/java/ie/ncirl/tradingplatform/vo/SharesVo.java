package ie.ncirl.tradingplatform.vo;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SharesVo {
	
	String name;
	ArrayList<String> date;
	ArrayList<Double> open;
	ArrayList<Double> low;
	ArrayList<Double> high;
	ArrayList<Double> volume;
	ArrayList<Double> close;
}
