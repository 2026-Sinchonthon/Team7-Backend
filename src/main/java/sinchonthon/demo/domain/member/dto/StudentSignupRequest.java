package sinchonthon.demo.domain.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class StudentSignupRequest {

    @NotBlank(message = "대학교는 필수입니다.")
    @Pattern(
            regexp = "연세대학교|이화여자대학교|서강대학교|홍익대학교|명지대학교",
            message = "대학교는 연세대학교, 이화여자대학교, 서강대학교, 홍익대학교, 명지대학교 중 하나여야 합니다."
    )
    private String university;

    @NotBlank(message = "닉네임은 필수입니다.")
    private String nickname;

    @NotBlank(message = "아이디는 필수입니다.")
    private String loginId;

    @NotBlank(message = "비밀번호는 필수입니다.")
    private String password;
}
