package com.projeto.autoflex.repository;

import com.projeto.autoflex.entity.RawMaterial;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RawMaterialRepository extends JpaRepository<RawMaterial, Long> {

    Optional<RawMaterial> findByCode(String code);

    boolean existsByCode(String code);

    boolean existsByCodeAndIdNot(String code, Long id);
}
