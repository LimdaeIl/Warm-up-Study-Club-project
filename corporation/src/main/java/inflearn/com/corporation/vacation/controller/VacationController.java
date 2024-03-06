package inflearn.com.corporation.vacation.controller;

import inflearn.com.corporation.vacation.dto.resquest.VacationRegisterRequest;
import inflearn.com.corporation.vacation.service.VacationService;
import org.springframework.web.bind.annotation.*;

@RestController
public class VacationController {

    private final VacationService vacationService;

    public VacationController(VacationService vacationService) {
        this.vacationService = vacationService;
    }

    @PostMapping("/api/v1/vacation")
    public void registeredVacation(@RequestBody VacationRegisterRequest request) {
        vacationService.registeredVacation(request);
    }

    @GetMapping("/api/v1/vacation")
    public int countVacation(@RequestParam Long id) {
        return vacationService.countVacation(id);
    }
}
