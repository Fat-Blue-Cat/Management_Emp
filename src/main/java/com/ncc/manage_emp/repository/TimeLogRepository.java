package com.ncc.manage_emp.repository;

import com.ncc.manage_emp.dto.TimeLogDto;
import com.ncc.manage_emp.entity.TimeLogs;
import com.ncc.manage_emp.entity.Users;
import com.ncc.manage_emp.entity.WorkTime;
import com.ncc.manage_emp.model_custom_results.CountErrorByMonth;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Objects;

//@Repository
public interface TimeLogRepository extends JpaRepository<TimeLogs,Long> {
    @Query("SELECT u FROM Users u JOIN u.timeLogsList tl  WHERE tl.users.id = :userId AND tl.checkDate BETWEEN :startDate AND :endDate")
    Users findAllTimeLogByUserId(@Param("userId") Long userId, @Param("startDate") LocalDate startDate,@Param("endDate") LocalDate endDate);

    @Query(value = "SELECT * FROM timelogs tl WHERE tl.user_id = :userId AND tl.check_date = :localDate", nativeQuery = true)
    TimeLogs findTimeLogsByCheckDate(@Param("userId") Long userId, @Param("localDate") LocalDate localDate);

    @Query("SELECT tl FROM TimeLogs tl " +
            "WHERE tl.checkinType = false " +
            "AND month(tl.checkDate) = month(:checkDate) " +
            "AND tl.users.id = :userId")
    Page<TimeLogs> getTimeLogFail(@Param("userId") Long userId, @Param("checkDate") LocalDate checkDate, Pageable pageable);

    boolean existsAllByCheckDate(LocalDate localDate);

    @Query("SELECT u.id FROM Users u " +
            "WHERE u.checkinCode IS NOT NULL " +
            "AND u.id NOT IN (SELECT tl.users.id FROM TimeLogs tl WHERE tl.checkDate = :localDate)")
    List<Long> getAllWorkTimeNotExistToDay(LocalDate localDate);


      /*  ============= CUSTOM CONSTRUCTION ====================*/
    @Query("SELECT NEW com.ncc.manage_emp.model_custom_results.CountErrorByMonth(count(t.checkinType), FUNCTION('MONTH', t.checkDate)) " +
            "FROM TimeLogs t WHERE t.checkinType = false GROUP BY FUNCTION('MONTH', t.checkDate)")
    List<CountErrorByMonth> countTimeLogsErrorByMonth();

    TimeLogs findTimeLogsByUsersIdAndCheckDate(Long id, LocalDate localDate);

}
