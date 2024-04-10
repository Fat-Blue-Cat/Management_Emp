    package com.ncc.manage_emp.entity;

    import com.fasterxml.jackson.annotation.JsonIgnore;
    import jakarta.persistence.*;
    import lombok.*;


    import java.io.Serializable;
    import java.time.LocalDate;
    import java.time.LocalDateTime;
    import java.time.LocalTime;
    import java.util.ArrayList;
    import java.util.Date;
    import java.util.List;



    @NoArgsConstructor
    @AllArgsConstructor
    @Entity
    @Builder
    @Data
    @Table(name = "work_time")
    public class WorkTime implements Serializable {
            @Id
            @GeneratedValue(strategy = GenerationType.IDENTITY)
            @Column(name = "id")
            private Long id;
            @Column(name = "checkin_work")
            private LocalTime checkinWork;
            @Column(name = "checkout_work")
            private LocalTime checkoutWork;
            @Column(name = "is_primary_working")
            private Boolean isPrimaryWorking;
            @Column(name = "create_at")
            private Date createAt;

            @ToString.Exclude
            @EqualsAndHashCode.Exclude
            @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
                    CascadeType.DETACH, CascadeType.REFRESH})
            @JoinColumn(name = "user_id")
            @JsonIgnore
            private Users users;
            

    }
