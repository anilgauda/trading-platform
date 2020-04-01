package ie.ncirl.tradingplatform.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MyStockVo {
    String name;
    String symbol;
    Double currPrice;
    Double open;
    Double myAvgBuyPrice;
    Double myAvgSellPrice;
    Integer quantity;
    @Builder.Default
    Integer trade = 0;
}
