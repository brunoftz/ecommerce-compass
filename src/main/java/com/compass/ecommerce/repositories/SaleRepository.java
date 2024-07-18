package com.compass.ecommerce.repositories;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.compass.ecommerce.models.SaleModel;

@Repository
public interface SaleRepository extends JpaRepository<SaleModel, UUID> {
	List<SaleModel> findAllBySaleDateBetween(LocalDateTime startDate, LocalDateTime endDate);
	List<SaleModel> findAllBySaleDate(LocalDate date);
	
}
