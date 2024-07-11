package com.compass.ecommerce.repositories;

import com.compass.ecommerce.models.SaleProductModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SaleProductRepository extends JpaRepository<SaleProductModel, Long> {
   
}
