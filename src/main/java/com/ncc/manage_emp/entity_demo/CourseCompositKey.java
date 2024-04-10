package com.ncc.manage_emp.entity_demo;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;

import java.util.Set;

@Entity
public class CourseCompositKey {
    @Id
    Long id;

    @OneToMany(mappedBy = "courseCompositKey")
    Set<CourseRating> ratings;


}
