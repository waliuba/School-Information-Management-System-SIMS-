package com.sims.backend.services;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import com.sims.backend.dtos.results.ResultRequestDTO;
import com.sims.backend.dtos.results.ResultResponseDTO;
import com.sims.backend.exceptions.BusinessRuleException;
import com.sims.backend.exceptions.ResourceNotFoundException;
import com.sims.backend.mappers.ResultMapper;
import com.sims.backend.models.*;
import com.sims.backend.repositories.*;

@Service
public class ResultsService {
    private final StudentPerformanceRepository results;
    private final StudentsRepository students;
    private final CoursesRepository courses;
    public ResultsService(StudentPerformanceRepository results, StudentsRepository students, CoursesRepository courses) {
        this.results=results; this.students=students; this.courses=courses;
    }
    public List<ResultResponseDTO> list(){ return results.findAllByOrderByScoreDesc().stream().map(ResultMapper::toDTO).toList(); }
    public ResultResponseDTO get(Long id){ return results.findById(id).map(ResultMapper::toDTO).orElseThrow(()->new ResourceNotFoundException("Result not found")); }
    public ResultResponseDTO create(ResultRequestDTO dto){
        if(results.findByStudentModel_StudentId(dto.getStudentId()).isPresent()) throw new BusinessRuleException("A result already exists for this student");
        StudentPerformanceModel model=new StudentPerformanceModel(); return save(dto,model);
    }
    public ResultResponseDTO update(Long id, ResultRequestDTO dto){
        StudentPerformanceModel model=results.findById(id).orElseThrow(()->new ResourceNotFoundException("Result not found"));
        results.findByStudentModel_StudentId(dto.getStudentId()).filter(existing->!existing.getPerformanceId().equals(id)).ifPresent(existing->{throw new BusinessRuleException("A result already exists for this student");});
        return save(dto,model);
    }
    private ResultResponseDTO save(ResultRequestDTO dto, StudentPerformanceModel model){
        StudentsModel student=students.findById(dto.getStudentId()).orElseThrow(()->new ResourceNotFoundException("Student not found"));
        Courses course=courses.findById(dto.getCourseId()).orElseThrow(()->new ResourceNotFoundException("Course not found"));
        BigDecimal score=dto.getScore(); model.setStudentModel(student); model.setCourseModel(course); model.setScore(score);
        model.setGrade(score.compareTo(BigDecimal.valueOf(80))>=0?"A":score.compareTo(BigDecimal.valueOf(70))>=0?"B":score.compareTo(BigDecimal.valueOf(60))>=0?"C":score.compareTo(BigDecimal.valueOf(50))>=0?"D":"E");
        model.setPerformance(score.compareTo(BigDecimal.valueOf(80))>=0?"Excellent":score.compareTo(BigDecimal.valueOf(70))>=0?"Good":score.compareTo(BigDecimal.valueOf(60))>=0?"Average":score.compareTo(BigDecimal.valueOf(50))>=0?"Needs Improvement":"At Risk");
        return ResultMapper.toDTO(results.save(model));
    }
    public void delete(Long id){ if(!results.existsById(id)) throw new ResourceNotFoundException("Result not found"); results.deleteById(id); }
    @Transactional
    public List<ResultResponseDTO> upload(MultipartFile file) {
        if(file==null || file.isEmpty()) throw new IllegalArgumentException("CSV file is required");
        try(var reader=new java.io.BufferedReader(new java.io.InputStreamReader(file.getInputStream()))) {
            List<ResultResponseDTO> saved=new java.util.ArrayList<>(); String line; int row=0;
            while((line=reader.readLine())!=null){ row++; if(line.isBlank()) continue; if(row==1 && line.toLowerCase().contains("student")) continue;
                String[] cells=line.split(",",-1); if(cells.length<3) throw new IllegalArgumentException("CSV row "+row+" must contain studentId, courseId, score");
                ResultRequestDTO dto=new ResultRequestDTO(); dto.setStudentId(Long.valueOf(cells[0].trim())); dto.setCourseId(Long.valueOf(cells[1].trim())); dto.setScore(new BigDecimal(cells[2].trim()));
                if(dto.getScore().compareTo(BigDecimal.ZERO)<0 || dto.getScore().compareTo(BigDecimal.valueOf(100))>0) throw new IllegalArgumentException("CSV row "+row+" score must be between 0 and 100");
                saved.add(create(dto));
            } return saved;
        } catch (IllegalArgumentException ex){ throw ex; } catch(Exception ex){ throw new IllegalArgumentException("Could not read results CSV: "+ex.getMessage()); }
    }
}
