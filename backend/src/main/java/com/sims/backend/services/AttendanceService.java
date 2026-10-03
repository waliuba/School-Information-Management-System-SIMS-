package com.sims.backend.services;

import com.sims.backend.dtos.attendance.AttendanceRequestDTO;
import com.sims.backend.dtos.attendance.AttendanceResponseDTO;
import com.sims.backend.mappers.AttendanceMapper;
import com.sims.backend.models.AttendanceModel;
import com.sims.backend.repositories.AttendanceRepository;
import com.sims.backend.repositories.StudentsRepository;
import com.sims.backend.models.StudentsModel;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final AttendanceMapper attendanceMapper;
    private final StudentsRepository studentsRepository;

    public AttendanceResponseDTO createAttendance(
            AttendanceRequestDTO request
    ) {

        AttendanceModel attendance =
                attendanceMapper.toEntity(request);

        AttendanceModel savedAttendance =
                attendanceRepository.save(attendance);

        return toResponseDTO(savedAttendance);
    }

    public List<AttendanceResponseDTO> getAllAttendance() {

        return attendanceRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public AttendanceResponseDTO getAttendanceById(
            Long attendanceId
    ) {

        AttendanceModel attendance =
                attendanceRepository.findById(attendanceId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Attendance record not found with ID: "
                                                + attendanceId
                                )
                        );

        return toResponseDTO(attendance);
    }

    public List<AttendanceResponseDTO> getAttendanceByStudent(
            Long studentId
    ) {

        return attendanceRepository
                .findByStudentId(studentId)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public List<AttendanceResponseDTO> getAttendanceByDate(
            LocalDate attendanceDate
    ) {

        return attendanceRepository
                .findByAttendanceDate(attendanceDate)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public List<AttendanceResponseDTO> getAttendanceByStatus(
            String status
    ) {

        return attendanceRepository
                .findByStatus(status)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public AttendanceResponseDTO updateAttendance(
            Long attendanceId,
            AttendanceRequestDTO request
    ) {

        AttendanceModel existingAttendance =
                attendanceRepository.findById(attendanceId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Attendance record not found with ID: "
                                                + attendanceId
                                )
                        );

        existingAttendance.setStudentId(request.getStudentId());

        existingAttendance.setAttendanceDate(
                request.getAttendanceDate()
        );

        existingAttendance.setStatus(
                request.getStatus()
        );

        existingAttendance.setRemarks(
                request.getRemarks()
        );

        AttendanceModel updatedAttendance =
                attendanceRepository.save(existingAttendance);

        return toResponseDTO(updatedAttendance);
    }

    private AttendanceResponseDTO toResponseDTO(AttendanceModel attendance) {
        String studentName = studentsRepository.findById(attendance.getStudentId())
                .map(student -> formatStudentName(student))
                .orElse("Unknown student");
        return attendanceMapper.toResponseDTO(attendance, studentName);
    }

    private String formatStudentName(StudentsModel student) {
        return java.util.stream.Stream.of(
                        student.getFirstName(), student.getMiddleName(), student.getLastName())
                .filter(name -> name != null && !name.isBlank())
                .collect(java.util.stream.Collectors.joining(" "));
    }

    public void deleteAttendance(Long attendanceId) {

        if (!attendanceRepository.existsById(attendanceId)) {
            throw new RuntimeException(
                    "Attendance record not found with ID: "
                            + attendanceId
            );
        }

        attendanceRepository.deleteById(attendanceId);
    }
}
