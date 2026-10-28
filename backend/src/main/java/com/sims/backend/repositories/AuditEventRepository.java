package com.sims.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.sims.backend.models.AuditEventModel;

public interface AuditEventRepository extends JpaRepository<AuditEventModel, Long> {}
