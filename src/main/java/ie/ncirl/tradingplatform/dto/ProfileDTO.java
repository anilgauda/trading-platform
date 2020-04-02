package ie.ncirl.tradingplatform.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProfileDTO {
    private String username;
    private String displayName;
}
