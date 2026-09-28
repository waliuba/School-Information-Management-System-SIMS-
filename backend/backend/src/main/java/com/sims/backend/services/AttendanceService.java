package com.sims.backend.services;

import com.sims.backend.dtos.attendance.AttendanceRequestDTO;
import com.sims.backend.dtos.attendance.AttendanceResponseDTO;
import com.sims.backend.mappers.AttendanceMapper;
import com.sims.backend.models.AttendanceModel;
import com.sims.backend.models.StudentModel;
import com.sims.backend.repositories.AttendanceRepository;
import com.sims.backend.repositories.StudentRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final StudentRepository studentRepository;
    private final AttendanceMapper attendanceMapper;

    @Transactional
    public AttendanceResponseDTO createAttendance(
            AttendanceRequestDTO dto) {

        if (attendanceRepository
                .existsByStudentStudentIdAndAttendanceDate(
                        dto.studentId(),
                        dto.attendanceDate())) {

            throw new RuntimeException(
                    "Attendance already exists for this student on this date"
            );
        }

        StudentModel student = studentRepository
                .findById(dto.studentId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Student not found with ID: "
                                        + dto.studentId()
                        ));

        AttendanceModel attendance =
                attendanceMapper.toEntity(dto);

        attendance.setStudent(student);

        AttendanceModel savedAttendance =
                attendanceRepository.save(attendance);

        return attendanceMapper.toResponseDTO(savedAttendance);
    }

    @Transactional(readOnly = true)
    public AttendanceResponseDTO getAttendanceById(
            Long attendanceId) {

        AttendanceModel attendance =
                attendanceRepository.findById(attendanceId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Attendance not found with ID: "
                                                + attendanceId
                                ));

        return attendanceMapper.toResponseDTO(attendance);
    }

    @Transactional(readOnly = true)
    public List<AttendanceResponseDTO> getAllAttendance() {

        return attendanceRepository.findAll()
                .stream()
                .map(attendanceMapper::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<AttendanceResponseDTO> getAttendanceByStudent(
            Long studentId) {

        if (!studentRepository.existsById(studentId)) {
            throw new RuntimeException(
                    "Student not found with ID: " + studentId
            );
        }

        return attendanceRepository
                .findByStudentStudentId(studentId)
                .stream()
                .map(attendanceMapper::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<AttendanceResponseDTO> getAttendanceByDate(
            LocalDate date) {

        return attendanceRepository
                .findByAttendanceDate(date)
                .stream()
                .map(attendanceMapper::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<AttendanceResponseDTO> getAttendanceByStatus(
            String status) {

        return attendanceRepository
                .findByStatus(
                        com.sims.backend.models.AttendanceStatus
                                .valueOf(status.toUpperCase())
                )
                .stream()
                .map(attendanceMapper::toResponseDTO)
                .toList();
    }

    @Transactional
    public AttendanceResponseDTO updateAttendance(
            Long attendanceId,
            AttendanceRequestDTO dto) {

        AttendanceModel attendance =
                attendanceRepository.findById(attendanceId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Attendance not found with ID: "
                                                + attendanceId
                                ));

        StudentModel student =
                studentRepository.findById(dto.studentId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Student not found with ID: "
                                                + dto.studentId()
                                ));

        attendance.setStudent(student);
        attendance.setAttendanceDate(dto.attendanceDate());
        attendance.setStatus(dto.status());
        attendance.setRemarks(dto.remarks());

        AttendanceModel updated =
                attendanceRepository.save(attendance);

        return attendanceMapper.toResponseDTO(updated);
    }

    @Transactional
    public void deleteAttendance(Long attendanceId) {

        if (!attendanceRepository.existsById(attendanceId)) {
            throw new RuntimeException(
                    "Attendance not found with ID: " + attendanceId
            );
        }

        attendanceRepository.deleteById(attendanceId);
    }
}
