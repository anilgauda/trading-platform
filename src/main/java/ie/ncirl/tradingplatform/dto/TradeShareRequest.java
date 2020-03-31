package ie.ncirl.tradingplatform.dto;

import lombok.Data;

@Data
public class TradeShareRequest {
    private String symbol;
    private Double price;
    private Integer quantity;
}
