package inflearn.com.corporation.vacation.repository;

import inflearn.com.corporation.vacation.entity.VacationRegistrationRule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VacationRegistrationRuleRepository extends JpaRepository<VacationRegistrationRule, Long> {
    Optional<VacationRegistrationRule> findVacationRegistrationRuleByTeamName(String name);
}
