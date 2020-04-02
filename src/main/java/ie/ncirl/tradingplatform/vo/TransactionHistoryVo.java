package ie.ncirl.tradingplatform.vo;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class TransactionHistoryVo {
    String name;
    String symbol;
    String type;
    Double price;
    Integer quantity;
    LocalDateTime tradedOn;
}
