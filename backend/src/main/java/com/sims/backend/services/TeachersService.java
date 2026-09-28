package com.sims.backend.services;

import com.sims.backend.dtos.teachers.*;

import com.sims.backend.mappers.TeachersMapper;
import com.sims.backend.models.TeachersModel;
import com.sims.backend.repositories.TeachersRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeachersService {

    private final TeachersRepository teachersRepository;
    private final TeachersMapper teachersMapper;

    public TeachersService(
            TeachersRepository teachersRepository,
            TeachersMapper teachersMapper
    ) {
        this.teachersRepository = teachersRepository;
        this.teachersMapper = teachersMapper;
    }

    // CREATE
    public TeacherResponseDTO createTeacher(TeacherRequestDTO request) {

        if (teachersRepository.existsByTeacherNo(request.getTeacherNo())) {
            throw new RuntimeException("Teacher number already exists");
        }

        if (request.getEmail() != null &&
                teachersRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        TeachersModel teacher = teachersMapper.toEntity(request);

        TeachersModel savedTeacher = teachersRepository.save(teacher);

        return teachersMapper.toResponseDTO(savedTeacher);
    }

    // GET ALL
    public List<TeacherResponseDTO> getAllTeachers() {

        return teachersRepository.findAll()
                .stream()
                .map(teachersMapper::toResponseDTO)
                .toList();
    }

    // GET BY ID
    public TeacherResponseDTO getTeacherById(Long teacherId) {

        TeachersModel teacher = teachersRepository.findById(teacherId)
                .orElseThrow(() ->
                        new RuntimeException("Teacher not found with ID: " + teacherId)
                );

        return teachersMapper.toResponseDTO(teacher);
    }

    // GET BY TEACHER NUMBER
    public TeacherResponseDTO getTeacherByTeacherNo(String teacherNo) {

        TeachersModel teacher = teachersRepository.findByTeacherNo(teacherNo)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Teacher not found with teacher number: " + teacherNo
                        )
                );

        return teachersMapper.toResponseDTO(teacher);
    }

    // UPDATE
    public TeacherResponseDTO updateTeacher(
            Long teacherId,
            TeacherRequestDTO request
    ) {

        TeachersModel teacher = teachersRepository.findById(teacherId)
                .orElseThrow(() ->
                        new RuntimeException("Teacher not found with ID: " + teacherId)
                );

        if (!teacher.getTeacherNo().equals(request.getTeacherNo())
                && teachersRepository.existsByTeacherNo(request.getTeacherNo())) {

            throw new RuntimeException("Teacher number already exists");
        }

        if (request.getEmail() != null
                && !request.getEmail().equals(teacher.getEmail())
                && teachersRepository.existsByEmail(request.getEmail())) {

            throw new RuntimeException("Email already exists");
        }

        teacher.setTeacherNo(request.getTeacherNo());
        teacher.setFirstName(request.getFirstName());
        teacher.setLastName(request.getLastName());
        teacher.setEmail(request.getEmail());
        teacher.setPhone(request.getPhone());

        TeachersModel updatedTeacher = teachersRepository.save(teacher);

        return teachersMapper.toResponseDTO(updatedTeacher);
    }

    // DELETE
    public void deleteTeacher(Long teacherId) {

        TeachersModel teacher = teachersRepository.findById(teacherId)
                .orElseThrow(() ->
                        new RuntimeException("Teacher not found with ID: " + teacherId)
                );

        teachersRepository.delete(teacher);
    }
}