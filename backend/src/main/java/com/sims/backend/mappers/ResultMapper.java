package com.sims.backend.mappers;

import com.sims.backend.dtos.results.ResultResponseDTO;
import com.sims.backend.models.StudentPerformanceModel;

public final class ResultMapper {
    private ResultMapper() {}
    public static ResultResponseDTO toDTO(StudentPerformanceModel model) {
        ResultResponseDTO dto = new ResultResponseDTO();
        dto.setPerformanceId(model.getPerformanceId());
        dto.setStudentId(model.getStudentModel().getStudentId());
        dto.setStudentName(model.getStudentModel().getFirstName()+" "+model.getStudentModel().getLastName());
        dto.setAdmissionNo(model.getStudentModel().getAdmissionNo());
        dto.setCourseId(model.getCourseModel().getCourseId());
        dto.setCourseName(model.getCourseModel().getCourseName());
        dto.setScore(model.getScore()); dto.setGrade(model.getGrade()); dto.setPerformance(model.getPerformance());
        return dto;
    }
}
