package com.ncc.manage_emp.entity_demo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class CourseRatingKey implements Serializable {
    @Column(name = "student_id")
    Long studentId;

    @Column(name = "course_id")
    Long courseId;
}
