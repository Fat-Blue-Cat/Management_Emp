package com.ncc.manage_emp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "timelogs")
public class TimeLogs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "checkin_time")
    private LocalTime checkinTime;

    @Column(name = "checkin_type")
    private boolean checkinType;

    @Column(name = "checkout_time")
    private LocalTime checkoutTime;

    @Column(name = "check_date")
    private LocalDate checkDate;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "work_time_id")
    private WorkTime workTime;




}
