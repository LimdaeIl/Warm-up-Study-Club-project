package inflearn.com.corporation.team.controller;

import inflearn.com.corporation.team.dto.request.TeamCreateRequest;
import inflearn.com.corporation.team.dto.response.TeamFindAllResponse;
import inflearn.com.corporation.team.service.TeamService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TeamController {

    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @PutMapping("/api/v1/team")
    public void savedTeam(@RequestBody TeamCreateRequest request) {
        teamService.savedTeam(request);
    }

    @GetMapping("/api/v1/team")
    public List<TeamFindAllResponse> teamFindAllResponses() {
        return teamService.teamFindAllResponses();
    }
}
