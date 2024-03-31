package com.ncc.manage_emp.repository;

import com.ncc.manage_emp.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule,Long> {
    Schedule findByJobId(String jobId);
}
