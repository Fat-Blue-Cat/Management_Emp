package com.ncc.manage_emp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;



@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "users")
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,name = "user_name")
    private String userName;

    @Column(nullable = false, name = "name")
    private String name;

    @Column(name = "password")
    private String password;

    @Column(nullable = false,name = "email")
    private String email;

    @Column(name = "google_id")
    private String googleId;

    @Column(name = "create_at")
    private Date createAt;

    @OneToMany(mappedBy = "users",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<WorkTime> workTimeList;

    @Column(name = "role_name")
    private String roleName;

}
