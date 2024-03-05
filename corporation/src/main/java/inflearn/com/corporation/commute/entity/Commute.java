package inflearn.com.corporation.commute.entity;

import inflearn.com.corporation.member.entity.Member;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
public class Commute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "commute_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
    private LocalDate date;
    private LocalTime startedAt;
    private LocalTime endedAt;

    protected Commute() {
    }

    public Commute(Member member) {
        this.member = member;
        this.date = LocalDate.now();
        this.startedAt = LocalTime.now().withNano(0);
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

    public LocalTime getStartedAt() {
        return startedAt;
    }

    public LocalTime getEndedAt() {
        return endedAt;
    }

    // 퇴근 처리
    public void endCommute(LocalTime endedAt) {
        if (this.endedAt != null) {
            throw new IllegalStateException("이미 퇴근 처리되었습니다.");
        }
        this.endedAt = endedAt;
    }

}
