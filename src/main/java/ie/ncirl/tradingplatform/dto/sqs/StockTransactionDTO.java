package ie.ncirl.tradingplatform.dto.sqs;

import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@ToString
public class StockTransactionDTO implements Serializable, SQSDTO {
    private String symbol;

    private Double buyPrice;

    private Double sellPrice;

    private Integer quantity;

    private Long accountId;
}
