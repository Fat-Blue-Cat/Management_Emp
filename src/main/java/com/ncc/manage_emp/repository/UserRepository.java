package com.ncc.manage_emp.repository;

import com.ncc.manage_emp.entity.Users;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users,Long> {
    Optional<Users> findByUserName(String username);


    Boolean existsByEmail(String email);

    Optional<Users> findByUserNameOrEmail(String username, String email);

    boolean existsByUserName(String username);

    @Query("SELECT distinct u FROM Users  u where u.id = :id")
    Users getUserById(Long id);

    @Query("SELECT u FROM Users u WHERE u.name LIKE %:name%")
    List<Users> findByName(@Param("name") String name,Sort sort);

    // Sử dụng FetchType.EAGER với JOIN FETCH
    @Query("SELECT DISTINCT u FROM Users u LEFT JOIN FETCH u.workTimeList")
    List<Users> findAllEager();



//    @Query("SELECT  new com.ncc.manage_emp.dto.UserDto(u.userName, u.email, u.roleName) FROM Users u JOIN u.workTimeList w JOIN w.timeLogsList tl WHERE  w.id = tl.workTime.id AND tl.checkDate = :checkDate")
//    List<UserDto> getAllUserByTimeCheckin(@Param("checkDate") LocalDate localDate);

//    @Query(value = "SELECT * FROM users u " +
//            "JOIN work_time w ON u.id = w.user_id " +
//            "JOIN timelogs tl ON w.id = tl.work_time_id " +
//            "WHERE tl.check_date = :checkDate", nativeQuery = true)
//    List<Users> getAllUserByTimeCheckin(@Param("checkDate") LocalDate checkDate);

//    @Query(value = "SELECT c.* " +
////            "FROM users u " +
////            "LEFT JOIN  work_time w ON u.id = w.user_id " +
////            "LEFT JOIN timelogs tl ON w.id = tl.work_time_id " +
////            " WHERE tl.check_date = :checkDate", nativeQuery = true)
////    public List<Users> getAllUserByTimeCheckin(LocalDate checkDate);

//    @Query(value = "SELECT w " +
//            "FROM TimeLogs tl " +
//            "JOIN FETCH tl.workTime w JOIN FETCH w.users u WHERE tl.checkDate = :checkDate")
//    public List<Object[]> getAllUserByTimeCheckin(LocalDate checkDate);


//    public List<Object[]> getAllUserByTimeCheckin( LocalDate checkDate);

//    List<Object> findByWorkTimeListTimeLogsListCheckDate(LocalDate checkDate);

    @Query("SELECT u FROM Users u " +
            "JOIN u.timeLogsList tl "+
            "WHERE tl.checkDate BETWEEN :startDate AND :endDate")
    List<Users> getAllUserByTimeCheckin(LocalDate startDate, LocalDate endDate);


    @Query("SELECT u FROM Users u LEFT JOIN FETCH u.timeLogsList tl " +
            "WHERE tl.checkinType = false " +
            "AND month(tl.checkDate) = month(:checkDate)")
    List<Users> getAllTimeLogFailAllUser(LocalDate checkDate);


    @Query("SELECT u FROM Users u LEFT JOIN FETCH u.timeLogsList tl " +
            "WHERE tl.checkinTime IS NULL " +
            "AND tl.checkDate = :checkDate")
    List<Users> getAllUserForgetCheckIn(LocalDate checkDate);

    @Query("SELECT u FROM Users u " +
            "LEFT JOIN FETCH u.timeLogsList tl " +
//            "WHERE tl.checkinTime IS NOT NULL " +
            "WHERE tl.checkDate = :checkDate " +
            "AND tl.checkoutTime IS NULL")
    List<Users> getAllUserForgetCheckout(LocalDate checkDate);


//

}
