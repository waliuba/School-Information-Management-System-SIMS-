package com.sims.backend.dtos.teachers;


import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeacherResponseDTO {

    private Long teacherId;
    private String teacherNo;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
}