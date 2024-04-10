package com.ncc.manage_emp.entity_demo;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.util.Set;

@Entity
public class StudentCompositKey {
    @Id
    Long id;

    @OneToMany(mappedBy = "studentCompositKey")
    Set<CourseRating> ratings;
}
