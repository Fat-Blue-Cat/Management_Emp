package com.ncc.manage_emp.repository;

import com.ncc.manage_emp.entity.TimeLogs;
import com.ncc.manage_emp.entity.Users;
import com.ncc.manage_emp.model_custom_results.closed_projections.UserView;
import com.ncc.manage_emp.model_custom_results.open_projections.UserViewOpen;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    Users getUserById(Long id);

    @Query("SELECT u FROM Users u WHERE u.name LIKE %:name%")
    Page<Users> findByName(@Param("name") String name, Pageable pageable);

    @Query("SELECT tl FROM TimeLogs tl " +
            "WHERE (tl.checkDate BETWEEN :startDate AND :endDate) AND  tl.users.name LIKE %:name%")
    Page<TimeLogs> getAllUserByTimeCheckin(LocalDate startDate, LocalDate endDate, Pageable pageable, String name);

    @Query("SELECT tl FROM TimeLogs tl " +
            "WHERE ((:typeCheck = 'ALL') OR " +
            "      (:typeCheck = 'NON-CHECK' AND tl.checkinTime IS NULL AND tl.checkinType = false) OR " +
            "      (:typeCheck = 'LATE' AND tl.checkinType = false AND tl.checkinTime IS NOT NULL)) " +
            "      AND month(tl.checkDate) = month(:checkDate) " +
            "      AND tl.users.name LIKE %:name%")
    List<TimeLogs> getAllTimeLogFailAllUser(LocalDate checkDate, String typeCheck, String name);

    @Query("SELECT u FROM Users u LEFT JOIN FETCH u.timeLogsList tl " +
            "WHERE tl.checkinTime IS NULL " +
            "AND tl.checkDate = :checkDate")
    List<Users> getAllUserForgetCheckIn(LocalDate checkDate);

    @Query("SELECT u FROM Users u " +
            "LEFT JOIN FETCH u.timeLogsList tl " +
            "WHERE tl.checkDate = :checkDate " +
            "AND tl.checkoutTime IS NULL")
    List<Users> getAllUserForgetCheckout(LocalDate checkDate);

    /* ==============EXAMPLE CLOSED PROJECTIONS =====================*/
    UserView findUsersById(Long userId);

    /* ============== EXAMPLE OPEN PROJECTIONS ================*/
    UserViewOpen findUsersByName(String name);

    <T> T findUsersByName(String name, Class<T> type);


}
