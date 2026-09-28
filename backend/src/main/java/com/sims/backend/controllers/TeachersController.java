package com.sims.backend.controllers;

import com.sims.backend.dtos.teachers.*;

import com.sims.backend.services.TeachersService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teachers")
public class TeachersController {

    private final TeachersService teachersService;

    public TeachersController(TeachersService teachersService) {
        this.teachersService = teachersService;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<TeacherResponseDTO> createTeacher(
            @Valid @RequestBody TeacherRequestDTO request
    ) {

        TeacherResponseDTO response =
                teachersService.createTeacher(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // GET ALL
    @GetMapping
    public ResponseEntity<List<TeacherResponseDTO>> getAllTeachers() {

        return ResponseEntity.ok(
                teachersService.getAllTeachers()
        );
    }

    // GET BY ID
    @GetMapping("/{teacherId}")
    public ResponseEntity<TeacherResponseDTO> getTeacherById(
            @PathVariable Long teacherId
    ) {

        return ResponseEntity.ok(
                teachersService.getTeacherById(teacherId)
        );
    }

    // GET BY TEACHER NUMBER
    @GetMapping("/teacher-no/{teacherNo}")
    public ResponseEntity<TeacherResponseDTO> getTeacherByTeacherNo(
            @PathVariable String teacherNo
    ) {

        return ResponseEntity.ok(
                teachersService.getTeacherByTeacherNo(teacherNo)
        );
    }

    // UPDATE
    @PutMapping("/{teacherId}")
    public ResponseEntity<TeacherResponseDTO> updateTeacher(
            @PathVariable Long teacherId,
            @Valid @RequestBody TeacherRequestDTO request
    ) {

        return ResponseEntity.ok(
                teachersService.updateTeacher(
                        teacherId,
                        request
                )
        );
    }

    // DELETE
    @DeleteMapping("/{teacherId}")
    public ResponseEntity<Void> deleteTeacher(
            @PathVariable Long teacherId
    ) {

        teachersService.deleteTeacher(teacherId);

        return ResponseEntity.noContent().build();
    }
}
