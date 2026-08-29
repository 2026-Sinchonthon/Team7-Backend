package sinchonthon.demo.global.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private final boolean isSuccess;
    private final String code;
    private final String message;
    private final T result;

    public static <T> ApiResponse<T> success(T result) {
        return new ApiResponse<>(true, "COMMON200", "요청이 성공했습니다.", result);
    }

    public static ApiResponse<Void> success() {
        return success(null);
    }

    public static ApiResponse<Void> failure(GeneralErrorCode errorCode) {
        return new ApiResponse<>(false, errorCode.getCode(), errorCode.getMessage(), null);
    }
}
