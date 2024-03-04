package inflearn.com.corporation.member.repository;

import inflearn.com.corporation.member.entity.Member;
import inflearn.com.corporation.member.entity.type.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

}
