package com.projeto.autoflex.repository;

import com.projeto.autoflex.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByCode(String code);

    boolean existsByCode(String code);

    boolean existsByCodeAndIdNot(String code, Long id);

    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.materials m LEFT JOIN FETCH m.rawMaterial ORDER BY p.value DESC")
    List<Product> findAllWithMaterialsOrderByValueDesc();
}
