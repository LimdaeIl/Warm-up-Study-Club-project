package inflearn.com.corporation.team.repository;

import inflearn.com.corporation.team.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, Long> {
    boolean existsByName(String name);

    Optional<Team> findByName(String name);
}
