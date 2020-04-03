package ie.ncirl.tradingplatform.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountDetailsDTO {
    private Double balance;
    private Double totalEarnings;
    private Integer numStocks;
}
