package inflearn.com.corporation.commute.dto.response;

import java.time.LocalDate;

public class DetailResponse {

    private LocalDate date;

    private Long workingMinutes;

    private boolean usingDayOff;

    protected DetailResponse() {}

    public DetailResponse(LocalDate date, Long workingMinutes, boolean usingDayOff) {
        this.date = date;
        this.workingMinutes = workingMinutes;
        this.usingDayOff = usingDayOff;
    }

    public LocalDate getDate() {
        return date;
    }

    public Long getWorkingMinutes() {
        return workingMinutes;
    }

    public boolean isUsingDayOff() {
        return usingDayOff;
    }
}
