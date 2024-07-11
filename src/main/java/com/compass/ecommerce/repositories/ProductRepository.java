package com.compass.ecommerce.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.compass.ecommerce.models.ProductModel;

@Repository
public interface ProductRepository extends JpaRepository<ProductModel, UUID> {
}
