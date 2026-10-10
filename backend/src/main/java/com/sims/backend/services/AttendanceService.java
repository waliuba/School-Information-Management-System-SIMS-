package com.sims.backend.services;

import com.sims.backend.dtos.attendance.AttendanceRequestDTO;
import com.sims.backend.dtos.attendance.AttendanceResponseDTO;
import com.sims.backend.mappers.AttendanceMapper;
import com.sims.backend.models.AttendanceModel;
import com.sims.backend.repositories.AttendanceRepository;
import com.sims.backend.repositories.StudentsRepository;
import com.sims.backend.repositories.UserRepository;
import com.sims.backend.models.StudentsModel;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Locale;
import java.util.Map;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final AttendanceMapper attendanceMapper;
    private final StudentsRepository studentsRepository;
    private final UserRepository userRepository;

    public List<AttendanceResponseDTO> getAttendanceForUsername(String username) {
        return userRepository.findByUsername(username)
                .flatMap(user -> studentsRepository.findByEmailIgnoreCase(user.getEmail()))
                .map(student -> getAttendanceByStudent(student.getStudentId()))
                .orElse(List.of());
    }

    public AttendanceResponseDTO createAttendance(
            AttendanceRequestDTO request
    ) {

        AttendanceModel attendance =
                attendanceMapper.toEntity(request);
        attendance.setStatus(normalizeStatus(request.getStatus()));

        AttendanceModel savedAttendance =
                attendanceRepository.save(attendance);

        return toResponseDTO(savedAttendance);
    }

    public List<AttendanceResponseDTO> getAllAttendance() {
        return toResponseDTOs(attendanceRepository.findAll());
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

        return toResponseDTOs(attendanceRepository.findByStudentId(studentId));
    }

    public List<AttendanceResponseDTO> getAttendanceByDate(
            LocalDate attendanceDate
    ) {

        return toResponseDTOs(attendanceRepository.findByAttendanceDate(attendanceDate));
    }

    public List<AttendanceResponseDTO> getAttendanceByStatus(
            String status
    ) {

        return toResponseDTOs(attendanceRepository.findByStatus(status));
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
                normalizeStatus(request.getStatus())
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

    private List<AttendanceResponseDTO> toResponseDTOs(List<AttendanceModel> attendanceRows) {
        if (attendanceRows.isEmpty()) return List.of();

        List<Long> studentIds = attendanceRows.stream()
                .map(AttendanceModel::getStudentId)
                .distinct()
                .toList();
        Map<Long, String> studentNames = studentsRepository.findAllById(studentIds).stream()
                .collect(Collectors.toMap(
                        StudentsModel::getStudentId,
                        this::formatStudentName,
                        (first, ignored) -> first
                ));

        return attendanceRows.stream()
                .map(row -> attendanceMapper.toResponseDTO(
                        row,
                        studentNames.getOrDefault(row.getStudentId(), "Unknown student")
                ))
                .toList();
    }

    private String formatStudentName(StudentsModel student) {
        return java.util.stream.Stream.of(
                        student.getFirstName(), student.getMiddleName(), student.getLastName())
                .filter(name -> name != null && !name.isBlank())
                .collect(java.util.stream.Collectors.joining(" "));
    }

    private String normalizeStatus(String status) {
        return status == null ? null : status.trim().toUpperCase(Locale.ROOT);
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
