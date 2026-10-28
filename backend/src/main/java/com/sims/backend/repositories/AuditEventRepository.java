package com.sims.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.sims.backend.models.AuditEventModel;
import java.util.List;

public interface AuditEventRepository extends JpaRepository<AuditEventModel, Long> {
    List<AuditEventModel> findTop100ByOrderByOccurredAtDesc();
}
