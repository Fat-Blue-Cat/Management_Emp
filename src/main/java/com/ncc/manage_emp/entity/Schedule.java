package com.ncc.manage_emp.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "schedules")
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name ="job_id")
    private String jobId;
    @Column(name = "cron_Expression")
    private String cronExpression;

}
