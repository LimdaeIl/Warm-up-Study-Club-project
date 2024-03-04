package inflearn.com.corporation.team.dto.request;

public class TeamCreateRequest {

    private String name;

    private String manager;

    protected TeamCreateRequest() {}

    public TeamCreateRequest(String name) {
        this.name = name;
    }

    public TeamCreateRequest(String name, String manager) {
        this.name = name;
        this.manager = manager;
    }

    public String getName() {
        return name;
    }

    public String getManager() {
        return manager;
    }
}
