package com.sims.backend.controllers;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.sims.backend.dtos.ApiResponse;
import com.sims.backend.dtos.results.*;
import com.sims.backend.services.ResultsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/results")
@RequiredArgsConstructor
public class ResultsController {
    private final ResultsService service;
    @GetMapping public ResponseEntity<ApiResponse<List<ResultResponseDTO>>> list(){return ResponseEntity.ok(ApiResponse.of("Results retrieved successfully",service.list()));}
    @GetMapping("/{id}") public ResponseEntity<ApiResponse<ResultResponseDTO>> get(@PathVariable Long id){return ResponseEntity.ok(ApiResponse.of("Result retrieved successfully",service.get(id)));}
    @PostMapping public ResponseEntity<ApiResponse<ResultResponseDTO>> create(@Valid @RequestBody ResultRequestDTO dto){return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.of("Result created successfully",service.create(dto)));}
    @PutMapping("/{id}") public ResponseEntity<ApiResponse<ResultResponseDTO>> update(@PathVariable Long id,@Valid @RequestBody ResultRequestDTO dto){return ResponseEntity.ok(ApiResponse.of("Result updated successfully",service.update(id,dto)));}
    @DeleteMapping("/{id}") public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id){service.delete(id);return ResponseEntity.ok(ApiResponse.of("Result deleted successfully",null));}
    @PostMapping(value="/upload",consumes="multipart/form-data") public ResponseEntity<ApiResponse<List<ResultResponseDTO>>> upload(@RequestPart("file") MultipartFile file){return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.of("Results uploaded successfully",service.upload(file)));}
}
