package com.meritconinc.shiplinx.model;

public class CustomerSalesUser {
	
	private int id;
	private long customerId;
	private String salesAgent;
	private double commissionPercentage=0; //this is for small packages
	private double commissionPercentagePerPalletService=0;
	private double commissionPercentagePerSkidService=0;
	
	public long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}
	public String getSalesAgent() {
		return salesAgent;
	}
	public void setSalesAgent(String salesAgent) {
		this.salesAgent = salesAgent;
	}
	public double getCommissionPercentage() {
		return commissionPercentage;
	}
	public void setCommissionPercentage(double commissionPercentage) {
		this.commissionPercentage = commissionPercentage;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public double getCommissionPercentagePerPalletService() {
		return commissionPercentagePerPalletService;
	}
	public void setCommissionPercentagePerPalletService(
			double commissionPercentagePerPalletService) {
		this.commissionPercentagePerPalletService = commissionPercentagePerPalletService;
	}
	public double getCommissionPercentagePerSkidService() {
		return commissionPercentagePerSkidService;
	}
	public void setCommissionPercentagePerSkidService(
			double commissionPercentagePerSkidService) {
		this.commissionPercentagePerSkidService = commissionPercentagePerSkidService;
	}
	
	
	
}
