package com.ncc.manage_emp.repository;

import com.ncc.manage_emp.entity.TimeLogs;
import com.ncc.manage_emp.entity.WorkTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Repository
public interface TimeLogRepository extends JpaRepository<TimeLogs,Long> {

    @Query("SELECT w FROM WorkTime w JOIN FETCH w.timeLogsList l WHERE w.users.id = :userId AND WEEK(l.checkDate) = WEEK(:filterDate)")
    List<WorkTime> findAllTimeLogByUserId(@Param("userId") Long userId, @Param("filterDate") LocalDateTime filterDate);

    @Query("SELECT t FROM TimeLogs t JOIN FETCH t.workTime w WHERE w.users.id = :userId AND t.checkDate = :localDate")
    TimeLogs findTimeLogsByCheckDate(@Param("userId") Long userId, @Param("localDate") LocalDate localDate);

    @Query("SELECT wt FROM WorkTime wt " +
            "JOIN FETCH wt.timeLogsList tl " +
            "WHERE tl.checkinType = false " +
            "AND month(tl.checkDate) = month(:checkDate) " +
            "AND wt.users.id = :userId")
    List<WorkTime> getTimeLogFail(@Param("userId") Long userId, @Param("checkDate") LocalDate checkDate);


    boolean existsAllByCheckDate(LocalDate localDate);


    @Query("SELECT w.id FROM WorkTime w " +
            "WHERE NOT EXISTS(SELECT 1 FROM WorkTime w2 WHERE w2.users.id= w.users.id AND w2.version >  w.version) " +
            "AND w.id NOT IN (SELECT tl.workTime.id FROM TimeLogs tl WHERE tl.checkDate = :localDate)")
    List<Long> getAllWorkTimeNotExistToDay(LocalDate localDate);


//    List<WorkTime> find

}
