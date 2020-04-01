package ie.ncirl.tradingplatform.dto;

import lombok.Data;

@Data
public class ResponseDTO<T> {
    private T data;
    private boolean hasError = false;
    private String errorMessage;

    public ResponseDTO<T> withData(T data) {
        this.data = data;
        return this;
    }

    public ResponseDTO<T> withError(String error) {
        this.hasError = true;
        this.errorMessage = error;
        return this;
    }
}
