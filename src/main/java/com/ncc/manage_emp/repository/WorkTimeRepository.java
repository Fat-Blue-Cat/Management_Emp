package com.ncc.manage_emp.repository;

import com.ncc.manage_emp.entity.WorkTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

//@Repository
public interface WorkTimeRepository extends JpaRepository<WorkTime,Long> {
    Boolean existsByUsersId(Long id);
    List<WorkTime> findAllByUsersId(Long userId);
    WorkTime findWorkTimeByUsersIdAndIsPrimaryWorkingTrue(Long userId);
    WorkTime findWorkTimeByUsersId(Long userId);
    @Query("SELECT wt FROM WorkTime wt WHERE wt.users.name LIKE %:name%")
    Page<WorkTime> findAll(Pageable pageable, String name);
    Page<WorkTime> findWorkTimeByUsersId(Long userId, Pageable pageable);
    @Modifying
    @Query("DELETE FROM WorkTime wt WHERE wt.id = :id")
    void deleteWorkTimeById(@Param("id") Long id);

}
