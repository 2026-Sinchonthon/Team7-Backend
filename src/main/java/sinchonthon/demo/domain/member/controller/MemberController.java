package sinchonthon.demo.domain.member.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sinchonthon.demo.domain.member.dto.LoginRequest;
import sinchonthon.demo.domain.member.dto.LoginResponse;
import sinchonthon.demo.domain.member.dto.StoreSignupRequest;
import sinchonthon.demo.domain.member.dto.StudentSignupRequest;
import sinchonthon.demo.domain.member.service.MemberService;
import sinchonthon.demo.global.response.ApiResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signup/store")
    public ApiResponse<?> signupStore(@Valid @RequestBody StoreSignupRequest request) {
        memberService.signupStore(request);
        return ApiResponse.success();
    }

    @PostMapping("/signup/student")
    public ApiResponse<?> signupStudent(@Valid @RequestBody StudentSignupRequest request) {
        memberService.signupStudent(request);
        return ApiResponse.success();
    }

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ApiResponse.success(memberService.login(request));
    }
}
