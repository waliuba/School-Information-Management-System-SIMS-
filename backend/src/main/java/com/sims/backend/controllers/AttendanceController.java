package com.sims.backend.controllers;

import com.sims.backend.dtos.attendance.AttendanceRequestDTO;
import com.sims.backend.dtos.attendance.AttendanceResponseDTO;
import com.sims.backend.services.AttendanceService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;

    @PostMapping
    public ResponseEntity<AttendanceResponseDTO> createAttendance(
            @Valid @RequestBody AttendanceRequestDTO request
    ) {

        AttendanceResponseDTO response =
                attendanceService.createAttendance(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<AttendanceResponseDTO>> getAllAttendance() {

        return ResponseEntity.ok(
                attendanceService.getAllAttendance()
        );
    }

    @GetMapping("/{attendanceId}")
    public ResponseEntity<AttendanceResponseDTO> getAttendanceById(
            @PathVariable Long attendanceId
    ) {

        return ResponseEntity.ok(
                attendanceService.getAttendanceById(attendanceId)
        );
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<AttendanceResponseDTO>> getAttendanceByStudent(
            @PathVariable Long studentId
    ) {

        return ResponseEntity.ok(
                attendanceService.getAttendanceByStudent(studentId)
        );
    }

    @GetMapping("/date/{attendanceDate}")
    public ResponseEntity<List<AttendanceResponseDTO>> getAttendanceByDate(
            @PathVariable LocalDate attendanceDate
    ) {

        return ResponseEntity.ok(
                attendanceService.getAttendanceByDate(attendanceDate)
        );
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<AttendanceResponseDTO>> getAttendanceByStatus(
            @PathVariable String status
    ) {

        return ResponseEntity.ok(
                attendanceService.getAttendanceByStatus(status)
        );
    }

    @PutMapping("/{attendanceId}")
    public ResponseEntity<AttendanceResponseDTO> updateAttendance(
            @PathVariable Long attendanceId,
            @Valid @RequestBody AttendanceRequestDTO request
    ) {

        return ResponseEntity.ok(
                attendanceService.updateAttendance(
                        attendanceId,
                        request
                )
        );
    }

    @DeleteMapping("/{attendanceId}")
    public ResponseEntity<Void> deleteAttendance(
            @PathVariable Long attendanceId
    ) {

        attendanceService.deleteAttendance(attendanceId);

        return ResponseEntity.noContent().build();
    }
}