package com.ncc.manage_emp.entity_demo;

import jakarta.persistence.*;

@Entity
public class CourseRating {
    @EmbeddedId
    CourseRatingKey id;

    @ManyToOne
    @MapsId("studentId")
    @JoinColumn(name = "student_id")
    StudentCompositKey studentCompositKey;

    @ManyToOne
    @MapsId("courseId")
    @JoinColumn(name = "course_id")
    CourseCompositKey courseCompositKey;

    int rating;
}
