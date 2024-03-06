package inflearn.com.corporation.vacation.service;

import inflearn.com.corporation.team.entity.Team;
import inflearn.com.corporation.team.repository.TeamRepository;
import inflearn.com.corporation.vacation.dto.resquest.VacationRoleRequest;
import inflearn.com.corporation.vacation.repository.VacationRegistrationRuleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VacationRegistrationRuleService {

    final private VacationRegistrationRuleRepository vacationRegistrationRuleRepository;
    final private TeamRepository teamRepository;

    public VacationRegistrationRuleService(VacationRegistrationRuleRepository vacationRegistrationRuleRepository, TeamRepository teamRepository) {
        this.vacationRegistrationRuleRepository = vacationRegistrationRuleRepository;
        this.teamRepository = teamRepository;
    }

    @Transactional
    public void registeredVacationRole(VacationRoleRequest request) {
        Team team = getByTeamName(request.getName());
        team.updateRegistrationRule(request.getMinDay());
        vacationRegistrationRuleRepository.save(team.getVacationRegistrationRule());
    }



    private Team getByTeamName(String teamName) {
        return teamRepository.findByName(teamName)
                .orElseThrow(() -> new IllegalArgumentException(String.format("팀 (%s)가 존재하지 않습니다.", teamName)));
    }
}
