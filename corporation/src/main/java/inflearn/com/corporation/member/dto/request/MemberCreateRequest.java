package inflearn.com.corporation.member.dto.request;

import inflearn.com.corporation.member.entity.type.MemberRole;

import java.time.LocalDate;

public class MemberCreateRequest {

    private String name;
    private String teamName;
    private MemberRole role;
    private LocalDate birthday;
    private LocalDate workStartDate;

    protected MemberCreateRequest() {}

    public MemberCreateRequest(String name, MemberRole role, LocalDate birthday, LocalDate workStartDate) {
        this.name = name;
        this.role = role;
        this.birthday = birthday;
        this.workStartDate = workStartDate;
    }

    public MemberCreateRequest(String name, String teamName, MemberRole role, LocalDate birthday, LocalDate workStartDate) {
        this.name = name;
        this.teamName = teamName;
        this.role = role;
        this.birthday = birthday;
        this.workStartDate = workStartDate;
    }

    public String getName() {
        return name;
    }

    public String getTeamName() {
        return teamName;
    }

    public MemberRole getRole() {
        return role;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public LocalDate getWorkStartDate() {
        return workStartDate;
    }
}
