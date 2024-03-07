package inflearn.com.corporation.commute.repository;

import inflearn.com.corporation.commute.entity.Commute;
import inflearn.com.corporation.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CommuteRepository extends JpaRepository<Commute, Long> {
    boolean existsCommuteByMemberAndDate(Member member, LocalDate date);
    Optional<Commute> findCommuteByMemberAndDate(Member member, LocalDate date);
    List<Commute> findCommutesByMemberIdAndDateBetween(Long id, LocalDate start, LocalDate end);
    List<Commute> findCommutesByDateBetween(LocalDate start, LocalDate end);
    @Query("SELECT DISTINCT c.member.id FROM Commute c")
    List<Long> findDistinctMemberId();

}
