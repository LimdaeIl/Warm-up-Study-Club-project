package inflearn.com.corporation.vacation.controller;

import inflearn.com.corporation.vacation.dto.resquest.VacationRoleRequest;
import inflearn.com.corporation.vacation.service.VacationRegistrationRuleService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VacationRegistrationRuleController {

    private final VacationRegistrationRuleService vacationRegistrationRuleService;

    public VacationRegistrationRuleController(VacationRegistrationRuleService vacationRegistrationRuleService) {
        this.vacationRegistrationRuleService = vacationRegistrationRuleService;
    }

    @PostMapping("/api/v1/vacation/role")
    public void registeredVacationRole(@RequestBody VacationRoleRequest request) {
        vacationRegistrationRuleService.registeredVacationRole(request);
    }
}
