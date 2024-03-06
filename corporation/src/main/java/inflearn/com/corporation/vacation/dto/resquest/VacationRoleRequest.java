package inflearn.com.corporation.vacation.dto.resquest;

public class VacationRoleRequest {

    private String name;
    private int minDay;

    protected VacationRoleRequest() {}

    public VacationRoleRequest(String name, int minDay) {
        this.name = name;
        this.minDay = minDay;
    }

    public String getName() {
        return name;
    }

    public int getMinDay() {
        return minDay;
    }
}
