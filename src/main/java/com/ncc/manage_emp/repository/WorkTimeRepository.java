package com.ncc.manage_emp.repository;

import com.ncc.manage_emp.entity.WorkTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface WorkTimeRepository extends JpaRepository<WorkTime,Long> {

    Boolean existsByUsersId(Long id);

    @Modifying
    @Query("DELETE FROM WorkTime w WHERE w.id = :id")
    void deleteById(Long id);



    @Query("SELECT w FROM WorkTime w WHERE w.users.id = :userid ORDER BY w.version DESC LIMIT 1")
    WorkTime findLastVersioning(@Param("userid") Long userid);

    @Query("SELECT w.users.id AS userId, MAX(w.version) AS maxVersion , w.id as id " +
            "FROM WorkTime w " +
            "WHERE NOT EXISTS (" +
            "    SELECT 1 " +
            "    FROM WorkTime w2 " +
            "    WHERE w2.users.id = w.users.id " +
            "    AND w2.version > w.version" +
            ") " +
            "GROUP BY w.users.id, w.id")
    List<Object[]> findMaxVersionForEachUser();




//    @Query("SELECT w " +
//            "FROM WorkTime w " +
//            "WHERE NOT EXISTS (" +
//            "    SELECT 1 " +
//            "    FROM WorkTime w2 " +
//            "    WHERE w2.users.id = w.users.id " +
//            "    AND w2.version > w.version" +
//            ") " +
//            "GROUP BY w.users.id")
//    List<Object[]> findMaxVersionForEachUser();





}
