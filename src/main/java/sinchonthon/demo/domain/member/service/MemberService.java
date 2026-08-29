package sinchonthon.demo.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sinchonthon.demo.domain.member.dto.LoginRequest;
import sinchonthon.demo.domain.member.dto.LoginResponse;
import sinchonthon.demo.domain.member.dto.StoreSignupRequest;
import sinchonthon.demo.domain.member.dto.StudentSignupRequest;
import sinchonthon.demo.domain.member.entity.Member;
import sinchonthon.demo.domain.member.entity.MemberRole;
import sinchonthon.demo.domain.member.repository.MemberRepository;
import sinchonthon.demo.global.exception.BusinessException;
import sinchonthon.demo.global.response.GeneralErrorCode;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void signupStore(StoreSignupRequest request) {
        validateDuplicateLoginId(request.getLoginId());

        Member member = Member.builder()
                .loginId(request.getLoginId())
                .password(passwordEncoder.encode(request.getPassword()))
                .phoneNumber(request.getPhoneNumber())
                .role(MemberRole.STORE)
                .storeName(request.getStoreName())
                .build();

        memberRepository.save(member);
    }

    @Transactional
    public void signupStudent(StudentSignupRequest request) {
        validateDuplicateLoginId(request.getLoginId());

        Member member = Member.builder()
                .loginId(request.getLoginId())
                .password(passwordEncoder.encode(request.getPassword()))
                .phoneNumber(request.getPhoneNumber())
                .role(MemberRole.STUDENT)
                .university(request.getUniversity())
                .nickname(request.getNickname())
                .build();

        memberRepository.save(member);
    }

    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {
        Member member = memberRepository.findByLoginId(request.getLoginId())
                .orElseThrow(() -> new BusinessException(GeneralErrorCode.LOGIN_FAILED));

        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new BusinessException(GeneralErrorCode.LOGIN_FAILED);
        }

        return new LoginResponse(
                member.getId(),
                member.getLoginId(),
                member.getRole(),
                getRedirectPath(member.getRole())
        );
    }

    private void validateDuplicateLoginId(String loginId) {
        if (memberRepository.existsByLoginId(loginId)) {
            throw new BusinessException(GeneralErrorCode.DUPLICATE_LOGIN_ID);
        }
    }

    private String getRedirectPath(MemberRole role) {
        if (role == MemberRole.STORE) {
            return "/store";
        }
        return "/student";
    }
}
