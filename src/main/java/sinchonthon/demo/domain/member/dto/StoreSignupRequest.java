package sinchonthon.demo.domain.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class StoreSignupRequest {

    @NotBlank(message = "상점명은 필수입니다.")
    private String storeName;

    @NotBlank(message = "아이디는 필수입니다.")
    private String loginId;

    @NotBlank(message = "비밀번호는 필수입니다.")
    private String password;

    @NotBlank(message = "전화번호는 필수입니다.")
    @Pattern(regexp = "^[0-9-]{9,20}$", message = "전화번호는 숫자와 하이픈만 사용할 수 있습니다.")
    private String phoneNumber;
}
