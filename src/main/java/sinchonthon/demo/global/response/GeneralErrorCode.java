package sinchonthon.demo.global.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum GeneralErrorCode implements BaseErrorCode {
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "COMMON400", "잘못된 요청입니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "COMMON401", "인증이 필요합니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "접근 권한이 없습니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "COMMON404", "요청한 리소스를 찾을 수 없습니다."),
    LOGIN_FAILED(HttpStatus.UNAUTHORIZED, "MEMBER401", "아이디 또는 비밀번호가 올바르지 않습니다."),
    DUPLICATE_LOGIN_ID(HttpStatus.CONFLICT, "MEMBER409", "이미 사용 중인 아이디입니다."),
    STORE_ALREADY_EXISTS(HttpStatus.CONFLICT, "STORE4091", "이미 등록된 상점이 있습니다."),
    INVALID_REPRESENTATIVE_MENU(HttpStatus.BAD_REQUEST, "STORE4001", "대표 메뉴는 정확히 1개여야 합니다."),
    STORE_NOT_FOUND(HttpStatus.NOT_FOUND, "STORE4041", "상점을 찾을 수 없습니다."),
    SLOT_NOT_FOUND(HttpStatus.NOT_FOUND, "SLOT4041", "모집 슬롯을 찾을 수 없습니다."),
    ACTIVE_SLOT_ALREADY_EXISTS(HttpStatus.CONFLICT, "SLOT4091", "이미 모집 중인 슬롯이 있습니다."),
    INVALID_SLOT_TIME(HttpStatus.BAD_REQUEST, "SLOT4001", "수령 시간은 00분 또는 30분이어야 합니다."),
    SLOT_CANCEL_NOT_ALLOWED(HttpStatus.CONFLICT, "SLOT4092", "현재 상태에서는 모집을 취소할 수 없습니다."),
    SLOT_NOT_RECRUITING(HttpStatus.CONFLICT, "SLOT4093", "현재 모집 중인 슬롯이 아닙니다."),
    SLOT_ALREADY_FULL(HttpStatus.CONFLICT, "SLOT4094", "모집 인원이 이미 가득 찼습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 오류가 발생했습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
