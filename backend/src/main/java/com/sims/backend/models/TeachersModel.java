package com.sims.backend.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "TEACHERS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeachersModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TEACHER_ID")
    private Long teacherId;

    @Column(name = "TEACHER_NO", nullable = false, length = 20, unique = true)
    private String teacherNo;

    @Column(name = "FIRST_NAME", nullable = false, length = 50)
    private String firstName;

    @Column(name = "LAST_NAME", nullable = false, length = 50)
    private String lastName;

    @Column(name = "EMAIL", length = 100)
    private String email;

    @Column(name = "PHONE", length = 20)
    private String phone;
}