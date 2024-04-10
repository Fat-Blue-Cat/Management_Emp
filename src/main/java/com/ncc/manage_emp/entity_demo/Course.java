package com.ncc.manage_emp.entity_demo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;

import java.util.Set;

@Entity
public class Course {
    @Id
    Long id;

    @Column(name = "course_name")
    String name;

    @ManyToMany
    Set<Student> likes;


}