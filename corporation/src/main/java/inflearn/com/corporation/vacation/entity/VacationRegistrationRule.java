package inflearn.com.corporation.vacation.entity;

import inflearn.com.corporation.team.entity.Team;
import jakarta.persistence.*;

@Entity
public class VacationRegistrationRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @Column(name = "registration_date")
    private int registrationDate; // 등록 가능 일자, 예: A팀의 경우 1일 전, B팀의 경우 7일 전


    protected VacationRegistrationRule() {
    }

    public VacationRegistrationRule(Team team, int registrationDate) {
        this.team = team;
        this.registrationDate = registrationDate;
    }

    public Long getId() {
        return id;
    }

    public Team getTeam() {
        return team;
    }
    public int getRegistrationDate() {
        return registrationDate;
    }
    public void changeRegistrationRule(int date) {
        this.registrationDate = date;
    }
}
