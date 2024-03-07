package inflearn.com.corporation.member.repository;

import inflearn.com.corporation.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findMemberById(Long memberId);

    @Query("SELECT m.name FROM Member m WHERE m.id = :id")
    String findNameById(Long id);
}
