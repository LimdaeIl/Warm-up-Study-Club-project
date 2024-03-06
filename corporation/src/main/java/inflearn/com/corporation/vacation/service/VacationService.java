package inflearn.com.corporation.vacation.service;

import inflearn.com.corporation.member.entity.Member;
import inflearn.com.corporation.member.repository.MemberRepository;
import inflearn.com.corporation.vacation.dto.resquest.VacationRegisterRequest;
import inflearn.com.corporation.vacation.entity.Vacation;
import inflearn.com.corporation.vacation.entity.tpye.VacationType;
import inflearn.com.corporation.vacation.repository.VacationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
public class VacationService {

    private static final long BEGINNER_VACATION_COUNT = 11;
    private static final long VACATION_COUNT = 15;

    private final VacationRepository vacationRepository;
    private final MemberRepository memberRepository;

    public VacationService(VacationRepository vacationRepository, MemberRepository memberRepository) {
        this.vacationRepository = vacationRepository;
        this.memberRepository = memberRepository;
    }

    @Transactional
    public void registeredVacation(VacationRegisterRequest request) {
        Member member = getMemberById(request.getId());

        // 휴가 정책 맞는지 확인하기
        int registrationDate = getRegistrationDate(member);
        long daysBetween = calculateDaysBetween(LocalDate.now(), request.getDate());

        // 남은 휴가일 갯수 확인하기
        long countedVacation = countRemainingVacation(member);

        if (isBeginner(member) && isVacationFullyUsed(BEGINNER_VACATION_COUNT, countedVacation)) {
            throwVacationFullyUsedException(BEGINNER_VACATION_COUNT);
        } else if (!isBeginner(member) && isVacationFullyUsed(VACATION_COUNT, countedVacation)) {
            throwVacationFullyUsedException(VACATION_COUNT);
        }

        // 휴가 등록하기
        if (canRegisterVacation(request.getDate(), registrationDate, daysBetween) && !isVacationExists(member, request.getDate())) {
            saveVacation(member, request.getDate());
        }
    }

    @Transactional
    public int countVacation(Long id) {
        Member member = getMemberById(id);

        // 남은 휴가일 갯수 확인하기
        long countedVacation = countRemainingVacation(member);

        if (isBeginner(member)) {
            return (int) (BEGINNER_VACATION_COUNT - countedVacation);
        } else {
            return (int) (VACATION_COUNT - countedVacation);
        }
    }

    private Member getMemberById(Long memberId) {
        return memberRepository.findMemberById(memberId)
                .orElseThrow(() -> new IllegalArgumentException(String.format("직원 번호(%d)가 존재하지 않습니다.", memberId)));
    }

    private int getRegistrationDate(Member member) {
        return member.getTeam().getVacationRegistrationRule().getRegistrationDate();
    }

    private long calculateDaysBetween(LocalDate startDate, LocalDate endDate) {
        return ChronoUnit.DAYS.between(startDate, endDate);
    }

    private long countRemainingVacation(Member member) {
        return vacationRepository.countVacationByMember(member);
    }

    private boolean isBeginner(Member member) {
        return member.getWorkStartDate().getYear() == LocalDate.now().getYear();
    }

    private boolean isVacationFullyUsed(long totalVacationCount, long countedVacation) {
        return totalVacationCount <= countedVacation;
    }

    private void throwVacationFullyUsedException(long totalVacationCount) {
        throw new IllegalArgumentException(String.format("이미 휴가(%d 회)를 모두 사용했습니다. ", totalVacationCount));
    }

    private boolean canRegisterVacation(LocalDate requestDate, int registrationDate, long daysBetween) {
        return daysBetween - registrationDate >= 0;
    }

    private boolean isVacationExists(Member member, LocalDate date) {
        return vacationRepository.existsVacationByMemberAndDate(member, date);
    }

    private void saveVacation(Member member, LocalDate date) {
        Vacation vacation = new Vacation(member, date, VacationType.ANNUAL_LEAVE);
        vacationRepository.save(vacation);
    }
}
