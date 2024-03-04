package inflearn.com.corporation.team.service;

import inflearn.com.corporation.team.dto.request.TeamCreateRequest;
import inflearn.com.corporation.team.dto.response.TeamFindAllResponse;
import inflearn.com.corporation.team.entity.Team;
import inflearn.com.corporation.team.repository.TeamRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeamService {

    private final TeamRepository teamRepository;

    public TeamService(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    @Transactional
    public void savedTeam(TeamCreateRequest request) {
        if (teamRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException(String.format("(%s)는 이미 존재하는 팀 입니다.", request.getName()));
        }
        Team team = new Team(request.getName());
        teamRepository.save(team);
    }

    public List<TeamFindAllResponse> teamFindAllResponses() {
        List<Team> teamList = teamRepository.findAll();
        return teamList.stream()
                .map(team -> new TeamFindAllResponse(
                        team.getName(),
                        team.getManager(),
                        team.getMemberCount()
                )).collect(Collectors.toList());
    }
}
