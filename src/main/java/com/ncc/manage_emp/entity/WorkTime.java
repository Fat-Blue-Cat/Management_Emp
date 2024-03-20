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
    @Table(name = "work_time")
    public class WorkTime {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id")
        private Long id;

        @Column(name = "checkin_code")
        private String checkinCode;

        @Column(name = "checkin_work")
        private LocalTime checkinWork;

        @Column(name = "checkout_work")
        private LocalTime checkoutWork;

        @Column(name = "version")
        private Long version;


        @ManyToOne
        @JoinColumn(name = "user_id")
        @JsonIgnore
        private Users users;

        @OneToMany(mappedBy = "workTime", cascade = CascadeType.ALL)
        private List<TimeLogs> timeLogsList;

    }
