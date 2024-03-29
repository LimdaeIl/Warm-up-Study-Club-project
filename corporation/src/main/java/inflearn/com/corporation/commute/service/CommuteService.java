package inflearn.com.corporation.commute.service;

import inflearn.com.corporation.commute.api.ApiExplorer;
import inflearn.com.corporation.commute.dto.request.CommuteByAllDayOfMonthRequest;
import inflearn.com.corporation.commute.dto.response.CommuteOvertimeResponse;
import inflearn.com.corporation.commute.dto.response.CommuteResponse;
import inflearn.com.corporation.commute.dto.response.DetailResponse;
import inflearn.com.corporation.commute.entity.Commute;
import inflearn.com.corporation.commute.repository.CommuteRepository;
import inflearn.com.corporation.member.entity.Member;
import inflearn.com.corporation.member.repository.MemberRepository;
import inflearn.com.corporation.vacation.entity.Vacation;
import inflearn.com.corporation.vacation.repository.VacationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommuteService {

    private final CommuteRepository commuteRepository;
    private final MemberRepository memberRepository;
    private final VacationRepository vacationRepository;

    public CommuteService(CommuteRepository commuteRepository, MemberRepository memberRepository, VacationRepository vacationRepository) {
        this.commuteRepository = commuteRepository;
        this.memberRepository = memberRepository;
        this.vacationRepository = vacationRepository;
    }

    @Transactional
    public void startedCommute(Long memberId) {
        // 직원 조회하기
        Member member = getMemberById(memberId);

        // 당일 출근 기록이 없는 경우
        if (!commuteRepository.existsCommuteByMemberAndDate(member, LocalDate.now())) {
            Commute commute = new Commute(member);
            commuteRepository.save(commute);
        } else {
            throw new IllegalArgumentException(String.format("직원 (%s)은 이미 출근 했습니다. 직원 번호 : (%d)", member.getName(), member.getId()));
        }
    }

    @Transactional
    public void endedCommute(Long memberId) {
        // 직원 조회하기
        Member member = getMemberById(memberId);

        // 당일 출근 기록이 없는 경우
        Commute commute = commuteRepository.findCommuteByMemberAndDate(member, LocalDate.now())
                .orElseThrow(() -> new IllegalStateException("출근 기록이 없습니다."));

        // 퇴근 처리하기
        commute.endCommute(LocalTime.now().withNano(0));
        commuteRepository.save(commute);
    }

    @Transactional(readOnly = true)
    public CommuteResponse memberCommuteByAllDayOfMonth(CommuteByAllDayOfMonthRequest request) {

        // 직원 조회하기
        Member member = getMemberById(request.getId());

        // 입력 받은 날짜 치환
        String yearMonthString = request.getDate();
        LocalDate startWithOfMonth = YearMonth.parse(yearMonthString).atDay(1);
        LocalDate endWithOfMonth = YearMonth.parse(yearMonthString).atEndOfMonth();

        // 해당 월의 직원 근무 기록 가져오기
        List<Commute> commuteList = commuteRepository
                .findCommutesByMemberIdAndDateBetween(member.getId(), startWithOfMonth, endWithOfMonth);

        // 해당 월의 직원 휴가 기록 가져오기
        List<Vacation> vacationList = vacationRepository
                .findVacationsByMemberIdAndDateBetween(member.getId(), startWithOfMonth, endWithOfMonth);

        // detail 안의 날짜, 근무 시간(분)으로 변환하기
        List<DetailResponse> detailResponseList = new ArrayList<>();

        LocalDate currentDate = startWithOfMonth;

        while (!currentDate.isAfter(endWithOfMonth)) {
            LocalDate finalCurrentDate = currentDate; // 새로운 변수에 할당

            boolean usingDayOff = vacationList.stream()
                    .anyMatch(vacation -> vacation.getDate().equals(finalCurrentDate)); // 변경된 변수 사용

            if (usingDayOff) {
                // 휴가를 사용한 날짜일 경우 근무 시간을 0으로 설정
                detailResponseList.add(new DetailResponse(currentDate, 0L, true));
            } else {
                // 휴가를 사용하지 않은 날짜일 경우 해당 일자의 근무 시간 계산
                long workingMinutes = calculateWorkingMinutes(commuteList, currentDate);
                detailResponseList.add(new DetailResponse(currentDate, workingMinutes, false));
            }
            currentDate = currentDate.plusDays(1);
        }

        // 해당 달의 모든 근무 시간 합하기
        long sum = detailResponseList.stream()
                .mapToLong(DetailResponse::getWorkingMinutes)
                .sum();

        return new CommuteResponse(detailResponseList, sum);
    }

    public List<CommuteOvertimeResponse> memberCommuteOvertimeList(String date)  {

        // 입력 받은 날짜 치환
        LocalDate startWithOfMonth = YearMonth.parse(date).atDay(1);
        LocalDate endWithOfMonth = YearMonth.parse(date).atEndOfMonth();

        // 해당 월의 전체 일 수 계산
        int daysInMonth = endWithOfMonth.getDayOfMonth();

        // 공휴일 리스트 가져오기 - LocalDate 형식으로 List 안에 저장
        List<String> stringHolidayLists = null;
        try {
            stringHolidayLists = ApiExplorer.resultHoliday();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        List<LocalDate> localDates = changeTimeFormatLists(stringHolidayLists);

        // 년-월에 해당하는 날짜 개수 세기
        int count = countDatesStartingWith(localDates, date);

        // 토요일과 일요일 + 공휴일 갯수 개수 세기
        long weekends = countWeekends(startWithOfMonth, endWithOfMonth);
        count += (int) weekends;

        int legalTimeHour = (daysInMonth - count) * 8;
        int legalTimeMin = legalTimeHour * 60;

        // 근무 기록이 존재하는 직원 id 리스트 가져오기
        List<Long> distinctMemberId = commuteRepository.findDistinctMemberId();

        LocalDate currentDate = startWithOfMonth;

        List<CommuteOvertimeResponse> CommuteOvertimeResponseList = new ArrayList<>();

        for (Long id : distinctMemberId) {

            // 해당 월의 직원 근무 기록 가져오기
            List<Commute> commuteList = commuteRepository
                    .findCommutesByMemberIdAndDateBetween(id, startWithOfMonth, endWithOfMonth);

            long workingMinutes = 0;

            while (!currentDate.isAfter(endWithOfMonth)) {
                workingMinutes += calculateWorkingMinutes(commuteList, currentDate);
                currentDate = currentDate.plusDays(1);
            }

            long overtimes = 0;
            if (workingMinutes > legalTimeMin ) {
                overtimes = workingMinutes - legalTimeMin;
                System.out.println("overtimes = " + overtimes);
                System.out.println("legalTimeMin = " + legalTimeMin);
            }
            System.out.println("overtimes = " + overtimes);
            System.out.println("legalTimeMin = " + legalTimeMin);

            String name = memberRepository.findNameById(id);


            CommuteOvertimeResponseList.add(new CommuteOvertimeResponse(id, convertName(name), overtimes));
        }
        return CommuteOvertimeResponseList;
    }

    // 해당 날짜의 근무 시간 계산
    private long calculateWorkingMinutes(List<Commute> commuteList, LocalDate date) {
        return commuteList.stream()
                .filter(commute -> commute.getDate().equals(date))
                .mapToLong(commute -> Duration.between(commute.getStartedAt(), commute.getEndedAt()).toMinutes())
                .sum();
    }

    private Member getMemberById(Long memberId) {
        return memberRepository.findMemberById(memberId)
                .orElseThrow(() -> new IllegalArgumentException(String.format("직원 번호(%d)가 존재하지 않습니다.", memberId)));
    }

    private static long countWeekends(LocalDate startDate, LocalDate endDate) {
        long daysBetween = ChronoUnit.DAYS.between(startDate, endDate);
        long count = 0;

        for (int i = 0; i <= daysBetween; i++) {
            LocalDate currentDate = startDate.plusDays(i);
            if (currentDate.getDayOfWeek() == DayOfWeek.SATURDAY || currentDate.getDayOfWeek() == DayOfWeek.SUNDAY) {
                count++;
            }
        }

        return count;
    }


    private List<LocalDate> changeTimeFormatLists(List<String> stringHolidayLists) {
        List<LocalDate> dates = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        for (String dateString : stringHolidayLists) {
            LocalDate date = LocalDate.parse(dateString, formatter);
            dates.add(date);
        }

        return dates;
    }

    private static int countDatesStartingWith(List<LocalDate> dates, String prefix) {
        int count = 0;
        for (LocalDate date : dates) {
            if (date.toString().startsWith(prefix)) {
                count++;
            }
        }
        return count;
    }

    public static String convertName(String name) {
        int length = name.length();
        String convertedName;
        if (length <= 2) {
            convertedName = name.charAt(0) + "*";
        } else {
            convertedName = name.charAt(0) + "*".repeat(length - 2) + name.charAt(length - 1);
        }
        return convertedName;
    }

}
