package inflearn.com.corporation.member.entity;

import inflearn.com.corporation.commute.entity.Commute;
import inflearn.com.corporation.member.entity.type.MemberRole;
import inflearn.com.corporation.team.entity.Team;
import inflearn.com.corporation.vacation.entity.Vacation;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberRole role;

    @Column(nullable = false)
    private LocalDate birthday;

    @Column(nullable = false)
    private LocalDate workStartDate;

    @OneToMany(mappedBy = "member")
    List<Commute> commutes = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    List<Vacation> vacations = new ArrayList<>();


    protected Member() {
    }

    public Member(String name, Team team, MemberRole role, LocalDate birthday, LocalDate workStartDate) {
        this.name = name;
        changeTeam(team);
        this.role = role;
        this.birthday = birthday;
        this.workStartDate = workStartDate;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Team getTeam() {
        return team;
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

    public List<Commute> getCommutes() {
        return commutes;
    }

    public List<Vacation> getVacations() {
        return vacations;
    }

    public void changeTeam(Team team) {
        if (this.team != null) { // 이전 팀이 설정되어 있으면
            this.team.getMembers().remove(this); // 이전 팀에서 해당 멤버 제거
        }
        this.team = team; // 새로운 팀으로 연관 관계 설정
        if (team != null) { // 새로운 팀이 null 이 아니라면
            team.getMembers().add(this); // 새로운 팀에 해당 멤버 추가
        }
    }

    public boolean hasUsedVacationOnDate(LocalDate date) {
        return vacations.stream()
                .anyMatch(vacation -> vacation.getDate().equals(date));
    }
}