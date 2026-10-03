package com.sims.backend.dtos.results;

import java.math.BigDecimal;
public class ResultResponseDTO {
    private Long performanceId, studentId, courseId;
    private String studentName, admissionNo, courseName, grade, performance;
    private BigDecimal score;
    public Long getPerformanceId(){return performanceId;} public void setPerformanceId(Long v){performanceId=v;}
    public Long getStudentId(){return studentId;} public void setStudentId(Long v){studentId=v;}
    public Long getCourseId(){return courseId;} public void setCourseId(Long v){courseId=v;}
    public String getStudentName(){return studentName;} public void setStudentName(String v){studentName=v;}
    public String getAdmissionNo(){return admissionNo;} public void setAdmissionNo(String v){admissionNo=v;}
    public String getCourseName(){return courseName;} public void setCourseName(String v){courseName=v;}
    public String getGrade(){return grade;} public void setGrade(String v){grade=v;}
    public String getPerformance(){return performance;} public void setPerformance(String v){performance=v;}
    public BigDecimal getScore(){return score;} public void setScore(BigDecimal v){score=v;}
}
