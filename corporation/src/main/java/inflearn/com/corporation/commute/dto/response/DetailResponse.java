package inflearn.com.corporation.commute.dto.response;

import java.time.LocalDate;

public class DetailResponse {

    private LocalDate date;

    private Long workingMinutes;

    protected DetailResponse() {}

    public DetailResponse(LocalDate date, Long workingMinutes) {
        this.date = date;
        this.workingMinutes = workingMinutes;
    }

    public LocalDate getDate() {
        return date;
    }

    public Long getWorkingMinutes() {
        return workingMinutes;
    }
}
