package com.sims.backend.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "ATTENDANCE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttendanceModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ATTENDANCE_ID", nullable = false)
    private Long attendanceId;

    @Column(name = "STUDENT_ID", nullable = false)
    private Long studentId;

    @Column(name = "ATTENDANCE_DATE", nullable = false)
    private LocalDate attendanceDate;

    @Column(name = "STATUS", nullable = false, length = 20)
    private String status;

    @Column(name = "REMARKS", length = 255)
    private String remarks;
}