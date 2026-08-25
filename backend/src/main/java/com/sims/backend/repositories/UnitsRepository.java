package com.sims.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import com.sims.backend.models.UnitsModel;

public interface UnitsRepository extends JpaRepository<UnitsModel, Long> {

    Optional<UnitsModel> findByUnitCode(String unitCode);

    List<UnitsModel> findByUnitName(String unitName);

    List<UnitsModel> findByUnitNameOrStatus(String unitName, String status);

    List<UnitsModel> findByStatus(String status);

}
