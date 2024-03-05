package inflearn.com.corporation.commute.dto.request;

public class CommuteByAllDayOfMonthRequest {

    private Long id;

    private String date;

    protected CommuteByAllDayOfMonthRequest() {}

    public CommuteByAllDayOfMonthRequest(Long id) {
        this.id = id;
    }

    public CommuteByAllDayOfMonthRequest(Long id, String date) {
        this.id = id;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public String getDate() {
        return date;
    }
}
