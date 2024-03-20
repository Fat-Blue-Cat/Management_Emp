package com.ncc.manage_emp.repository;

import com.ncc.manage_emp.dto.UserDto;
import com.ncc.manage_emp.entity.TimeLogs;
import com.ncc.manage_emp.entity.Users;
import com.ncc.manage_emp.entity.WorkTime;
import org.apache.catalina.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users,Long> {
    Optional<Users> findByUserName(String username);


    Boolean existsByEmail(String email);

    Optional<Users> findByUserNameOrEmail(String username, String email);

    boolean existsByUserName(String username);

    @Query("SELECT u FROM Users u WHERE u.name LIKE %:name%")
    List<Users> findByName(@Param("name") String name,Sort sort);

//    @Query("SELECT  new com.ncc.manage_emp.dto.UserDto(u.userName, u.email, u.roleName) FROM Users u JOIN u.workTimeList w JOIN w.timeLogsList tl WHERE  w.id = tl.workTime.id AND tl.checkDate = :checkDate")
//    List<UserDto> getAllUserByTimeCheckin(@Param("checkDate") LocalDate localDate);

//    @Query(value = "SELECT * FROM users u " +
//            "JOIN work_time w ON u.id = w.user_id " +
//            "JOIN timelogs tl ON w.id = tl.work_time_id " +
//            "WHERE tl.check_date = :checkDate", nativeQuery = true)
//    List<Users> getAllUserByTimeCheckin(@Param("checkDate") LocalDate checkDate);

    @Query(value = "SELECT u.* " +
            "FROM users u " +
            "JOIN  work_time w ON u.id = w.user_id " +
            "JOIN timelogs tl ON w.id = tl.work_time_id " +
            " WHERE DATE(tl.check_date) = DATE(\"2024-03-20\")", nativeQuery = true)
    List<Users> getAllUserByTimeCheckin(@RequestParam("checkDate") LocalDate checkDate);

//    @Query("SELECT DISTINCT u FROM Users u " +
//            "JOIN FETCH u.workTimeList wt " +
//            "JOIN FETCH wt.timeLogsList tl " +
//            "WHERE tl.checkDate = DATE(\"2024-03-20\")")
//    List<Users> getAllUserByTimeCheckin();

//    @Query("SELECT u FROM Users  u join fetch u.workTimeList wt where  wt.id = (SELECT  wt.id FROM WorkTime wt JOIN FETCH wt.timeLogsList tl WHERE DATE(tl.checkDate) = :checkDate)")
//    List<Users> getAllUserByTimeCheckin(@Param("checkDate") LocalDate checkDate);

//    @Query("SELECT  wt FROM WorkTime wt JOIN wt.timeLogsList tl WHERE DATE(tl.checkDate) = :checkDate")
//    List<WorkTime> getAllUserByTimeCheckin(@Param("checkDate") LocalDate checkDate);

//    @Query("SELECT DISTINCT u FROM Users u " +
//            "JOIN FETCH u.workTimeList wt " +
//            "WHERE wt.timeLogsList.checkDate = '2024-03-20'")
//    List<Users> getUsersByCheckDate();

}
