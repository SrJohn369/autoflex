package com.projeto.autoflex.repository;

import com.projeto.autoflex.entity.ProductMaterial;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductMaterialRepository extends JpaRepository<ProductMaterial, Long> {

    List<ProductMaterial> findByProductId(Long productId);

    void deleteByProductId(Long productId);
}
