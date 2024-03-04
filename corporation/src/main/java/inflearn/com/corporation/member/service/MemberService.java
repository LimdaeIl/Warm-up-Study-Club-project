package inflearn.com.corporation.member.service;

import inflearn.com.corporation.member.dto.request.MemberCreateRequest;
import inflearn.com.corporation.member.dto.response.MemberFindAllResponse;
import inflearn.com.corporation.member.entity.Member;
import inflearn.com.corporation.member.entity.type.Role;
import inflearn.com.corporation.member.repository.MemberRepository;
import inflearn.com.corporation.team.entity.Team;
import inflearn.com.corporation.team.repository.TeamRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final TeamRepository teamRepository;

    public MemberService(MemberRepository memberRepository, TeamRepository teamRepository) {
        this.memberRepository = memberRepository;
        this.teamRepository = teamRepository;
    }

    @Transactional
    public void savedMember(MemberCreateRequest request) {
        Team team = null;
        if (request.getTeamName() != null) {
            team = teamRepository.findByName(request.getTeamName())
                    .orElseThrow(IllegalArgumentException::new);
        }

        // 만약 회원의 역할이 MANAGER 이고, 팀에 이미 MANAGER 가 존재한다면 예외 발생
        if (request.getRole() == Role.MANAGER && team != null && team.hasManager()) {
            throw new IllegalArgumentException("팀에 이미 매니저가 존재합니다.");
        }

        Member member = new Member(
                request.getName(),
                team,
                request.getRole(),
                request.getBirthday(),
                request.getWorkStartDate()
        );
        memberRepository.save(member);

        // 만약 회원의 역할이 MANAGER 라면 팀의 managerName 필드를 업데이트
        if (member.getRole() == Role.MANAGER && team != null) {
            team.setManagerName(member.getName());
            teamRepository.save(team);
        }

        // 팀에 멤버 추가
        if (team != null) {
            team.addMember(member);
            teamRepository.save(team);
        }
    }

    @Transactional
    public List<MemberFindAllResponse> findAllMember() {
        List<Member> memberList = memberRepository.findAll();

        return memberList.stream()
                .map(member -> {
                    String teamName = member.getTeam() != null ? member.getTeam().getName() : null;
                    return new MemberFindAllResponse(
                            member.getName(),
                            teamName,
                            member.getRole(),
                            member.getBirthday(),
                            member.getWorkStartDate()
                    );
                }).collect(Collectors.toList());
    }


}
