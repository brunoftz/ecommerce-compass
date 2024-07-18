package com.compass.ecommerce.dtos;

public class WeeklyReportDto {
	private int week;
    private int month;
	private int year;
	private long totalProductsSold;
	private String bestSellingProduct;
	private double totalSalesValue;
	public int getWeek() {
		return week;
	}
	public void setWeek(int week) {
		this.week = week;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public long getTotalProductsSold() {
		return totalProductsSold;
	}
	public void setTotalProductsSold(long totalProductsSold) {
		this.totalProductsSold = totalProductsSold;
	}
	public String getBestSellingProduct() {
		return bestSellingProduct;
	}
	public void setBestSellingProduct(String bestSellingProduct) {
		this.bestSellingProduct = bestSellingProduct;
	}
	public double getTotalSalesValue() {
		return totalSalesValue;
	}
	public void setTotalSalesValue(double totalSalesValue) {
		this.totalSalesValue = totalSalesValue;
	}
	public int getMonth() {
		return month;
	}
	public void setMonth(int month) {
		this.month = month;
	}

}
