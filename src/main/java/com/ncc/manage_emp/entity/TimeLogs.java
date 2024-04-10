package com.ncc.manage_emp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;


@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
@Builder
@Table(name = "timelogs")
public class TimeLogs implements Serializable {
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


    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonIgnore
    @ManyToOne()
//    cascade = {CascadeType.PERSIST, CascadeType.MERGE,
//            CascadeType.DETACH, CascadeType.REFRESH}
    @JoinColumn(name = "user_id")
    private Users users;


}
