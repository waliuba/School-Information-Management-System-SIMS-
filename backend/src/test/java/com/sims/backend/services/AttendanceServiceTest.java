package com.sims.backend.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sims.backend.enums.AttendanceStatus;
import com.sims.backend.models.AttendanceModel;
import com.sims.backend.models.StudentsModel;
import com.sims.backend.repositories.AttendanceRepository;

@ExtendWith(MockitoExtension.class)
class AttendanceServiceTest {

    @Mock
    private AttendanceRepository attendanceRepository;

    @InjectMocks
    private AttendanceService attendanceService;

    @Test
    void returnsOneLatestAttendanceRecordForEachOf150Students() {
        List<AttendanceModel> attendanceRecords = new ArrayList<>();
        for (long studentId = 1; studentId <= 150; studentId++) {
            attendanceRecords.add(attendanceRecord(
                    studentId, studentId, LocalDate.of(2026, 2, 2)));
            attendanceRecords.add(attendanceRecord(
                    studentId + 150, studentId, LocalDate.of(2026, 2, 3)));
        }
        when(attendanceRepository.findAll()).thenReturn(attendanceRecords);

        var result = attendanceService.getAllAttendance();

        assertThat(result).hasSize(150);
        assertThat(result)
                .extracting(record -> record.studentId())
                .doesNotHaveDuplicates()
                .containsExactlyElementsOf(
                        java.util.stream.LongStream.rangeClosed(1, 150)
                                .boxed()
                                .toList());
        assertThat(result)
                .allSatisfy(record -> assertThat(record.attendanceDate())
                        .isEqualTo(LocalDate.of(2026, 2, 3)));
    }

    private AttendanceModel attendanceRecord(long attendanceId, long studentId, LocalDate date) {
        StudentsModel student = new StudentsModel();
        student.setStudentId(studentId);
        student.setAdmissionNo("SIMS" + studentId);
        student.setFirstName("Student");
        student.setLastName(Long.toString(studentId));

        AttendanceModel attendance = new AttendanceModel();
        attendance.setAttendanceId(attendanceId);
        attendance.setStudentsModel(student);
        attendance.setAttendanceDate(date);
        attendance.setStatus(AttendanceStatus.PRESENT);
        attendance.setRemarks("Present");
        return attendance;
    }
}
