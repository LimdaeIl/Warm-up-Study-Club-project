package inflearn.com.corporation.commute.controller;

import inflearn.com.corporation.commute.dto.request.CommuteByAllDayOfMonthRequest;
import inflearn.com.corporation.commute.dto.response.CommuteResponse;
import inflearn.com.corporation.commute.service.CommuteService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CommuteController {

    private final CommuteService commuteService;

    public CommuteController(CommuteService commuteService) {
        this.commuteService = commuteService;
    }

    @PostMapping("/api/v1/commute/start")
    public void startedCommute(@RequestParam(name = "memberId") Long memberId) {
        commuteService.startedCommute(memberId);
    }

    @PostMapping("/api/v1/commute/end")
    public void endedCommute(@RequestParam(name = "memberId") Long memberId) {
        commuteService.endedCommute(memberId);
    }

    @GetMapping("/api/v1/commute")
    public CommuteResponse memberCommuteByAllDayOfMonth(
            @RequestParam Long id,
            @RequestParam String date) {
        CommuteByAllDayOfMonthRequest request = new CommuteByAllDayOfMonthRequest(id, date);
        return commuteService.memberCommuteByAllDayOfMonth(request);
    }
}
