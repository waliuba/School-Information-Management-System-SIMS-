package com.sims.backend.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(
    name = "ATTENDANCE",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "UQ_ATTENDANCE_STUDENT_DATE",
            columnNames = {"STUDENT_ID", "ATTENDANCE_DATE"}
        )
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttendanceModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ATTENDANCE_ID")
    private Long attendanceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "STUDENT_ID",
        nullable = false,
        foreignKey = @ForeignKey(name = "FK_ATTENDANCE_STUDENT")
    )
    private StudentModel student;

    @Column(name = "ATTENDANCE_DATE", nullable = false)
    private LocalDate attendanceDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false, length = 20)
    private AttendanceStatus status;

    @Column(name = "REMARKS", length = 255)
    private String remarks;
}