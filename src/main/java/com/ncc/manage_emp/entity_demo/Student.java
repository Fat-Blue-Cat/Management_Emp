package com.ncc.manage_emp.entity_demo;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Entity
@Data
public class Student {

    @Id
    Long id;

    @Column(name = "student_name")
    String name;

    @ManyToMany
    @JoinTable(
            name = "course_like",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id"))
    Set<Course> likedCourses;

}
