package inflearn.com.corporation.vacation.dto.resquest;

import java.time.LocalDate;

public class VacationRegisterRequest {

    private Long id;

    private LocalDate date;

    protected VacationRegisterRequest() {}

    public VacationRegisterRequest(Long id, LocalDate date) {
        this.id = id;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }
}
