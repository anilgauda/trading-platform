package ie.ncirl.tradingplatform.dto;

import lombok.Data;

@Data
public class JwtResponse {
    private final String jwttoken;

    public JwtResponse(String token) {
        this.jwttoken = token;
    }
}
