package com.ncc.manage_emp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.*;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Builder
@Table(name = "users")
public class Users implements Serializable {


    @Id // primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @Column(nullable = false, name = "user_name")
    private String userName;

    @Column(nullable = false, name = "name")
    private String name;

    @JsonIgnore
    @Column(name = "password")
    private String password;

    @Column(nullable = false, name = "email")
    private String email;

    @JsonIgnore
    @Column(name = "google_id")
    private String googleId;

    @Column(name = "checkin_code")
    private String checkinCode;

    @Column(name = "create_at")
    private Date createAt;

    @Column(name = "role_name")
    private String roleName;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<WorkTime> workTimeList = new ArrayList<>();

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "users", fetch = FetchType.EAGER,
            cascade = CascadeType.ALL
//            cascade = {CascadeType.PERSIST, CascadeType.MERGE,
//                    CascadeType.DETACH, CascadeType.REFRESH}
    )
    private List<TimeLogs> timeLogsList = new ArrayList<>();





}
