package com.sims.backend.services;

import java.util.List;
import java.util.TreeMap;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sims.backend.dtos.attendance.AttendanceResponseDTO;
import com.sims.backend.models.AttendanceModel;
import com.sims.backend.repositories.AttendanceRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;

    @Transactional(readOnly = true)
    public List<AttendanceResponseDTO> getAllAttendance() {
        var latestAttendanceByStudent = new TreeMap<Long, AttendanceModel>();
        for (AttendanceModel attendance : attendanceRepository.findAll()) {
            Long studentId = attendance.getStudentsModel().getStudentId();
            latestAttendanceByStudent.merge(studentId, attendance, this::latestAttendance);
        }

        return latestAttendanceByStudent.values().stream()
                .map(this::toResponse)
                .toList();
    }

    private AttendanceModel latestAttendance(AttendanceModel existing, AttendanceModel candidate) {
        int dateComparison = candidate.getAttendanceDate().compareTo(existing.getAttendanceDate());
        if (dateComparison > 0
                || (dateComparison == 0
                        && candidate.getAttendanceId().compareTo(existing.getAttendanceId()) > 0)) {
            return candidate;
        }
        return existing;
    }

    private AttendanceResponseDTO toResponse(AttendanceModel attendance) {
        var student = attendance.getStudentsModel();
        return new AttendanceResponseDTO(
                attendance.getAttendanceId(),
                student.getStudentId(),
                student.getAdmissionNo(),
                student.getFirstName() + " " + student.getLastName(),
                attendance.getAttendanceDate(),
                attendance.getStatus(),
                attendance.getRemarks());
    }
}
