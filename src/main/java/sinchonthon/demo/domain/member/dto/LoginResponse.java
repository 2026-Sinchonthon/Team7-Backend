package sinchonthon.demo.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import sinchonthon.demo.domain.member.entity.MemberRole;

@Getter
@AllArgsConstructor
public class LoginResponse {

    private Long memberId;
    private String loginId;
    private MemberRole role;
    private String redirectPath;
    private Long storeId;
}
