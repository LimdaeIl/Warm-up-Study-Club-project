package inflearn.com.corporation.vacation.repository;

import inflearn.com.corporation.member.entity.Member;
import inflearn.com.corporation.vacation.entity.Vacation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface VacationRepository extends JpaRepository<Vacation, Long> {
    boolean existsVacationByMemberAndDate(Member member, LocalDate date);
    long countVacationByMember(Member member);
    List<Vacation> findVacationsByMemberIdAndDateBetween(Long memberId, LocalDate startDate, LocalDate endDate);


}
