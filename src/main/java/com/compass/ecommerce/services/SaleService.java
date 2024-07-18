package com.compass.ecommerce.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.compass.ecommerce.dtos.MonthlyReportDto;
import com.compass.ecommerce.dtos.SaleDto;
import com.compass.ecommerce.dtos.SaleProductDto;
import com.compass.ecommerce.dtos.WeeklyReportDto;
import com.compass.ecommerce.models.ProductModel;
import com.compass.ecommerce.models.SaleModel;
import com.compass.ecommerce.models.SaleProductModel;
import com.compass.ecommerce.repositories.ProductRepository;
import com.compass.ecommerce.repositories.SaleRepository;

@Service
public class SaleService {

	@Autowired
	private SaleRepository saleRepository;

	@Autowired
	private ProductRepository productRepository;

	@Transactional
	public SaleModel createSale(SaleDto saleDto) {
		SaleModel saleModel = new SaleModel();
		saleModel.setSaleDate(saleDto.getSaleDate());
		saleModel.setDescription(saleDto.getDescription());

		double totalPrice = 0.0;
		for (SaleProductDto saleProductDto : saleDto.getProducts()) {
			ProductModel product = productRepository.findById(saleProductDto.getId())
					.orElseThrow(() -> new RuntimeException("Product not found"));

			// Verifica se há estoque suficiente
			if (product.getStockAmount() < saleProductDto.getAmount()) {
				throw new RuntimeException("Insufficient stock for product: " + product.getName());
			}

			// Atualiza o estoque do produto
			product.setStockAmount(product.getStockAmount() - saleProductDto.getAmount());
			productRepository.save(product);

			SaleProductModel saleProductModel = new SaleProductModel();
			saleProductModel.setSale(saleModel);
			saleProductModel.setProduct(product);
			saleProductModel.setAmount(saleProductDto.getAmount());
			totalPrice += product.getPrice() * saleProductDto.getAmount();
			saleModel.getSaleProducts().add(saleProductModel);
		}

		saleModel.setTotalPrice(totalPrice);
		return saleRepository.save(saleModel);
	}

	public List<SaleModel> getAllSales() {
		return saleRepository.findAll();
	}

	@Transactional
	public void deleteSaleById(UUID saleId) {
		Optional<SaleModel> saleOptional = saleRepository.findById(saleId);
		if (saleOptional.isPresent()) {
			SaleModel sale = saleOptional.get();
			saleRepository.delete(sale);
		} else {
			throw new RuntimeException("Sale not found with id: " + saleId);
		}
	}

	@Transactional
	public SaleModel updateSale(UUID saleId, SaleDto saleDto) {
		Optional<SaleModel> saleOptional = saleRepository.findById(saleId);
		if (saleOptional.isPresent()) {
			SaleModel saleModel = saleOptional.get();

			// Atualiza os dados da venda com base no SaleDto recebido
			saleModel.setSaleDate(saleDto.getSaleDate());
			saleModel.setDescription(saleDto.getDescription());

			// Limpa os produtos da venda para recriá-los com os novos dados
			saleModel.getSaleProducts().clear();

			double totalPrice = 0.0;
			for (SaleProductDto saleProductDto : saleDto.getProducts()) {
				ProductModel product = productRepository.findById(saleProductDto.getId())
						.orElseThrow(() -> new RuntimeException("Product not found"));
				SaleProductModel saleProductModel = new SaleProductModel();
				saleProductModel.setSale(saleModel);
				saleProductModel.setProduct(product);
				saleProductModel.setAmount(saleProductDto.getAmount());
				totalPrice += product.getPrice() * saleProductDto.getAmount();
				saleModel.getSaleProducts().add(saleProductModel);
			}

			saleModel.setTotalPrice(totalPrice);
			return saleRepository.save(saleModel);
		} else {
			throw new RuntimeException("Sale not found with id: " + saleId);
		}
	}

	public List<SaleModel> getSalesByDate(LocalDate startDate, LocalDate endDate) {
		LocalDateTime startDateTime = null;
		LocalDateTime endDateTime = null;

		if (startDate != null) {
			startDateTime = startDate.atStartOfDay();
		}

		if (endDate != null) {
			endDateTime = endDate.atTime(LocalTime.MAX);
		}

		if (startDateTime != null && endDateTime != null) {
			return saleRepository.findAllBySaleDateBetween(startDateTime, endDateTime);
		} else if (startDateTime != null) {
			return saleRepository.findAllBySaleDateBetween(startDateTime, startDateTime.plusDays(1).minusNanos(1));
		} else if (endDateTime != null) {
			return saleRepository.findAllBySaleDateBetween(endDateTime.toLocalDate().atStartOfDay(), endDateTime);
		} else {
			throw new RuntimeException("At least one date parameter must be provided.");
		}
	}

	public MonthlyReportDto getMonthlyReport(int month, int year) {
		LocalDateTime startDate = LocalDateTime.of(year, month, 1, 0, 0);
		LocalDateTime endDate = startDate.toLocalDate().with(TemporalAdjusters.lastDayOfMonth()).atTime(23, 59, 59);
		List<SaleModel> sales = saleRepository.findAllBySaleDateBetween(startDate, endDate);

		return generateMonthlyReport(sales, month, year);
	}

	public WeeklyReportDto getWeeklyReport(int weekOfMonth, int month, int year) {
		LocalDate startOfMonth = LocalDate.of(year, month, 1);
		LocalDate startOfWeek = startOfMonth.with(WeekFields.of(Locale.getDefault()).dayOfWeek(), 1)
				.plusWeeks(weekOfMonth - 1);
		LocalDate endOfWeek = startOfWeek.plusDays(6);

		LocalDateTime startDate = startOfWeek.atStartOfDay();
		LocalDateTime endDate = endOfWeek.atTime(23, 59, 59);

		List<SaleModel> sales = saleRepository.findAllBySaleDateBetween(startDate, endDate);

		return generateWeeklyReport(sales, weekOfMonth, month, year);
	}

	private MonthlyReportDto generateMonthlyReport(List<SaleModel> sales, int month, int year) {
	    long totalProductsSold = sales.stream()
	            .flatMap(sale -> sale.getSaleProducts().stream())
	            .filter(SaleProductModel::getStatus)  // Only active products
	            .mapToLong(SaleProductModel::getAmount)
	            .sum();

	    Map<String, Long> productSales = sales.stream()
	            .flatMap(sale -> sale.getSaleProducts().stream())
	            .filter(SaleProductModel::getStatus)  // Only active products
	            .collect(Collectors.groupingBy(
	                    saleProduct -> saleProduct.getProduct().getName(),
	                    Collectors.summingLong(SaleProductModel::getAmount)
	            ));

	    String bestSellingProduct = productSales.entrySet().stream()
	            .max(Map.Entry.comparingByValue())
	            .map(Map.Entry::getKey)
	            .orElse("No products sold");

	    double totalSalesValue = sales.stream()
	            .mapToDouble(sale -> sale.getSaleProducts().stream()
	                    .filter(SaleProductModel::getStatus)  // Only active products
	                    .mapToDouble(sp -> sp.getProduct().getPrice() * sp.getAmount())
	                    .sum())
	            .sum();

	    MonthlyReportDto report = new MonthlyReportDto();
	    report.setMonth(month);
	    report.setYear(year);
	    report.setTotalProductsSold(totalProductsSold);
	    report.setBestSellingProduct(bestSellingProduct);
	    report.setTotalSalesValue(totalSalesValue);
	    return report;
	}


	private WeeklyReportDto generateWeeklyReport(List<SaleModel> sales, int weekOfMonth, int month, int year) {
	    long totalProductsSold = sales.stream()
	            .flatMap(sale -> sale.getSaleProducts().stream())
	            .filter(SaleProductModel::getStatus)  // Only active products
	            .mapToLong(SaleProductModel::getAmount)
	            .sum();

	    Map<String, Long> productSales = sales.stream()
	            .flatMap(sale -> sale.getSaleProducts().stream())
	            .filter(SaleProductModel::getStatus)  // Only active products
	            .collect(Collectors.groupingBy(
	                    saleProduct -> saleProduct.getProduct().getName(),
	                    Collectors.summingLong(SaleProductModel::getAmount)
	            ));

	    String bestSellingProduct = productSales.entrySet().stream()
	            .max(Map.Entry.comparingByValue())
	            .map(Map.Entry::getKey)
	            .orElse("No products sold");

	    double totalSalesValue = sales.stream()
	            .mapToDouble(sale -> sale.getSaleProducts().stream()
	                    .filter(SaleProductModel::getStatus)  // Only active products
	                    .mapToDouble(sp -> sp.getProduct().getPrice() * sp.getAmount())
	                    .sum())
	            .sum();

	    WeeklyReportDto report = new WeeklyReportDto();
	    report.setWeek(weekOfMonth);
	    report.setMonth(month);
	    report.setYear(year);
	    report.setTotalProductsSold(totalProductsSold);
	    report.setBestSellingProduct(bestSellingProduct);
	    report.setTotalSalesValue(totalSalesValue);
	    return report;
	}


}
