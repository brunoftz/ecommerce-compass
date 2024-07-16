package com.compass.ecommerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.compass.ecommerce.models.SaleModel;
import java.util.UUID;

@Repository
public interface SaleRepository extends JpaRepository<SaleModel, UUID> {
}
