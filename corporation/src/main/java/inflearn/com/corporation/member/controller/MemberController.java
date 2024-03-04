package inflearn.com.corporation.member.controller;

import inflearn.com.corporation.member.dto.request.MemberCreateRequest;
import inflearn.com.corporation.member.dto.response.MemberFindAllResponse;
import inflearn.com.corporation.member.service.MemberService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PutMapping("/api/v1/members")
    public void savedMember(@RequestBody MemberCreateRequest request) {
        memberService.savedMember(request);
    }

    @GetMapping("/api/v1/members")
    public List<MemberFindAllResponse> findAllMember() {
        return memberService.findAllMember();
    }
}
