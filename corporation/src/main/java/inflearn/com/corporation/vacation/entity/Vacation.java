package inflearn.com.corporation.vacation.entity;

import inflearn.com.corporation.member.entity.Member;
import inflearn.com.corporation.vacation.entity.tpye.VacationType;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Vacation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
    private LocalDate date;
    @Enumerated(EnumType.STRING)
    private VacationType vacationType;

    protected Vacation() {}

    public Vacation(Member member, LocalDate date, VacationType vacationType) {
        this.member = member;
        this.date = date;
        this.vacationType = vacationType;
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public LocalDate getDate() {
        return date;
    }

    public VacationType getVacationType() {
        return vacationType;
    }
}
