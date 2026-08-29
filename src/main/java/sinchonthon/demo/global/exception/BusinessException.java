package sinchonthon.demo.global.exception;

import lombok.Getter;
import sinchonthon.demo.global.response.GeneralErrorCode;

@Getter
public class BusinessException extends RuntimeException {
    private final GeneralErrorCode errorCode;

    public BusinessException(GeneralErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
