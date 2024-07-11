package com.compass.ecommerce.repositories;

import com.compass.ecommerce.models.SaleModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface SaleRepository extends JpaRepository<SaleModel, Long> {
}
