package com.sims.backend.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.sims.backend.models.SettingModel;

public interface SettingRepository extends JpaRepository<SettingModel, Long> {
    List<SettingModel> findByScopeAndOwnerId(String scope, Long ownerId);
    Optional<SettingModel> findByScopeAndOwnerIdAndKey(String scope, Long ownerId, String key);
}
