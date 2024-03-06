package inflearn.com.corporation.team.entity;

import inflearn.com.corporation.member.entity.Member;
import inflearn.com.corporation.member.entity.type.MemberRole;
import inflearn.com.corporation.vacation.entity.VacationRegistrationRule;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_id")
    private Long id;
    
    @Column(nullable = false)
    private String name;

    private String manager;

    @OneToMany(mappedBy = "team")
    List<Member> members = new ArrayList<>();

    private Long memberCount;

    @OneToOne(mappedBy = "team", fetch = FetchType.LAZY)
    private VacationRegistrationRule vacationRegistrationRule;

    protected Team() {}

    public Team(String name) {
        this.name = name;
        this.memberCount = 0L;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getManager() {
        return manager;
    }

    public List<Member> getMembers() {
        return members;
    }

    public Long getMemberCount() {
        return memberCount;
    }

    public VacationRegistrationRule getVacationRegistrationRule() {
        return vacationRegistrationRule;
    }

    public boolean hasManager() {
        return members.stream().anyMatch(member -> member.getRole() == MemberRole.MANAGER);
    }

    public void setManagerName(String manager) {
        this.manager = manager;
    }

    public void addMember(Member member) {
        members.add(member);
        memberCount++;
    }

    public void updateRegistrationRule(int date) {
        if (this.vacationRegistrationRule != null) {
            vacationRegistrationRule.changeRegistrationRule(date);
        } else {
            vacationRegistrationRule = new VacationRegistrationRule(this, date);
        }
    }

}
