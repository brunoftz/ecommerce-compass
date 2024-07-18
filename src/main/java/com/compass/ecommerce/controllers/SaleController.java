package com.compass.ecommerce.controllers;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.compass.ecommerce.dtos.MonthlyReportDto;
import com.compass.ecommerce.dtos.SaleDto;
import com.compass.ecommerce.dtos.WeeklyReportDto;
import com.compass.ecommerce.models.SaleModel;
import com.compass.ecommerce.services.SaleService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/sales")
public class SaleController {

    @Autowired
    private SaleService saleService;

    
    @CacheEvict(value = {"sale", "weeklyReport"}, allEntries = true)
    @PostMapping
    public ResponseEntity<SaleModel> createSale(@RequestBody @Valid SaleDto saleDto) {
        SaleModel createdSale = saleService.createSale(saleDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSale);
    }
    

    @Cacheable(value = "sale")
    @GetMapping
    public ResponseEntity<List<SaleModel>> getAllSales() {
        return ResponseEntity.status(HttpStatus.OK).body(saleService.getAllSales());
    }
    
    @CacheEvict(value = {"sale", "weeklyReport"}, allEntries = true)
    @DeleteMapping("/{saleId}")
    public ResponseEntity<Void> deleteSaleById(@PathVariable UUID saleId) {
        try {
            saleService.deleteSaleById(saleId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    

    @CachePut(value = {"sale", "weeklyRep"})
    @PutMapping("/{saleId}")
    public ResponseEntity<SaleModel> updateSaleById(@PathVariable UUID saleId, @RequestBody SaleDto saleDto) {
        try {
            SaleModel updatedSale = saleService.updateSale(saleId, saleDto);
            return ResponseEntity.ok(updatedSale);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @Cacheable(value = "sale")
    @GetMapping("/filter-by-date")
    public List<SaleModel> getSalesByDate(
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return saleService.getSalesByDate(startDate, endDate);
    }
    
    @Cacheable(value = "monthlyRep")
    @GetMapping("/report/monthly")
    public MonthlyReportDto getMonthlyReport(
            @RequestParam(value = "month", required = true) int month,
            @RequestParam(value = "year", required = true) int year) {
        return saleService.getMonthlyReport(month, year);
    }

    @Cacheable(value = "weeklyRep")
    @GetMapping("/report/weekly")
    public WeeklyReportDto getWeeklyReport(
            @RequestParam(value = "weekOfMonth", required = true) int weekOfMonth,
            @RequestParam(value = "month", required = true) int month,
            @RequestParam(value = "year", required = true) int year) {
        return saleService.getWeeklyReport(weekOfMonth, month, year);
    }


}
