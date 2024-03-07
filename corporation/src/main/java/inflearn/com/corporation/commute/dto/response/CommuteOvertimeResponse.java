package inflearn.com.corporation.commute.dto.response;


public class CommuteOvertimeResponse {

    private Long id;

    private String name;

    private Long overtimeMinutes;

    protected CommuteOvertimeResponse() {}

    public CommuteOvertimeResponse(Long id, String name, Long overtimeMinutes) {
        this.id = id;
        this.name = name;
        this.overtimeMinutes = overtimeMinutes;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getOvertimeMinutes() {
        return overtimeMinutes;
    }
}
