package com.meritconinc.shiplinx.model;

import java.io.Serializable;
import java.util.Comparator;

import com.meritconinc.shiplinx.utils.ShiplinxConstants;

public class Charge implements Serializable {
	private Long chargeId;
	private Double cost;
	private Double charge;
	private Double tariffRate = 0.0;
	private Long orderId;
	private String name;
	private Long id;
	private String chargeCode;
	private String chargeCodeLevel2;
	private String currency;
	private Double discountAmount = 0.0;
	private String ediInvoiceNumber;
	private Integer status;
	private Long invoiceId;
	private String invoiceNum;
	private int type;
	
	//for web display
	private int displayOrder;
	private double staticAmount; //this is the amount that should not be marked up/down

	public Long getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(Long invoiceId) {
		this.invoiceId = invoiceId;
	}

	public String getInvoiceNum() {
		return invoiceNum;
	}

	public void setInvoiceNum(String invoiceNum) {
		this.invoiceNum = invoiceNum;
	}

	public Charge(String surchargeName, double totalCost,Long id) {
		this.name = surchargeName;
		this.cost = totalCost;
		this.chargeId = id;
	}
	
	public Charge() {
	}
	
	public Charge(String surchargeName, double totalCost) {
		this.name = surchargeName;
		this.cost = totalCost;
	}

	public Long getChargeId() {
		return chargeId;
	}
	public void setChargeId(Long chargeId) {
		this.chargeId = chargeId;
	}
	public Double getCost() {
		return cost;
	}
	public void setCost(Double amount) {
		this.cost = amount;
	}
	public Long getOrderId() {
		return orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public String getChargeCode() {
		return chargeCode;
	}
	public void setChargeCode(String chargeCode) {
		this.chargeCode = chargeCode;
	}
	public Double getCharge() {
		return charge;
	}
	public void setCharge(Double charge) {
		this.charge = charge;
	}

	public String getChargeCodeLevel2() {
		return chargeCodeLevel2;
	}

	public void setChargeCodeLevel2(String chargeCodeLevel2) {
		this.chargeCodeLevel2 = chargeCodeLevel2;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}
	

	public Double getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(Double discountAmount) {
		this.discountAmount = discountAmount;
	}

	public String getEdiInvoiceNumber() {
		return ediInvoiceNumber;
	}

	public void setEdiInvoiceNumber(String ediInvoiceNumber) {
		this.ediInvoiceNumber = ediInvoiceNumber;
	}

	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}

	public Double getTariffRate() {
		return tariffRate;
	}
	public void setTariffRate(Double tariffRate) {
		this.tariffRate = tariffRate;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}	
	
	public String getStatusText() {
		// CHARGE_STATUS_TEXT = {"Unknown", "Pending Release", "Ready to Invoice", "Invoiced", "Cancelled"};
		if (status == ShiplinxConstants.CHARGE_PENDING_RELEASE)
			return ShiplinxConstants.CHARGE_STATUS_TEXT[1];  
		
		if (status == ShiplinxConstants.CHARGE_READY_TO_INVOICE)
			return ShiplinxConstants.CHARGE_STATUS_TEXT[2]; 
		
		if (status == ShiplinxConstants.CHARGE_INVOICED)
			return ShiplinxConstants.CHARGE_STATUS_TEXT[3]; 
		
		if (status == ShiplinxConstants.CHARGE_CANCELLED)
			return ShiplinxConstants.CHARGE_STATUS_TEXT[4]; 		
		
		return ShiplinxConstants.CHARGE_STATUS_TEXT[0];
	}
	
	public void setStatusText(String text) {
		if (text != null && !text.isEmpty()) {
			if (text.equalsIgnoreCase(ShiplinxConstants.CHARGE_STATUS_TEXT[1]))
				status = ShiplinxConstants.CHARGE_PENDING_RELEASE;
			else if (text.equalsIgnoreCase(ShiplinxConstants.CHARGE_STATUS_TEXT[2]))
				status = ShiplinxConstants.CHARGE_READY_TO_INVOICE;
			else if (text.equalsIgnoreCase(ShiplinxConstants.CHARGE_STATUS_TEXT[3]))
				status = ShiplinxConstants.CHARGE_INVOICED;
			else if (text.equalsIgnoreCase(ShiplinxConstants.CHARGE_STATUS_TEXT[4]))
				status = ShiplinxConstants.CHARGE_CANCELLED;
			else 
				status = ShiplinxConstants.CHARGE_QUOTED;
		} else
			status = ShiplinxConstants.CHARGE_QUOTED;
	}

	public int getDisplayOrder() {
		return displayOrder;
	}
	public void setDisplayOrder(int displayOrder) {
		this.displayOrder = displayOrder;
	}
	
	public double getStaticAmount() {
		return staticAmount;
	}
	public void setStaticAmount(double staticAmount) {
		this.staticAmount = staticAmount;
	}

	public static Comparator ChargeComparator = new Comparator() {
		public int compare(Object arg0, Object arg1) {
			long cus1 = ((Charge) arg0).getDisplayOrder();
			long cus2 = ((Charge) arg1).getDisplayOrder();
			
			if(cus1>cus2)
				return 1;
			else if(cus1<cus2)
				return -1;
			else return 0;
		
		}
	};
	

	public static void copyCharge(Charge srcCharge, Charge targetCharge){
		targetCharge.setCost(srcCharge.getCost());
		targetCharge.setCharge(srcCharge.getCharge());
		targetCharge.setTariffRate(srcCharge.getTariffRate());
		targetCharge.setName(srcCharge.getName());
		targetCharge.setChargeCode(srcCharge.getChargeCode());
		targetCharge.setChargeCodeLevel2(srcCharge.getChargeCodeLevel2());
		targetCharge.setCurrency(srcCharge.getCurrency());
		targetCharge.setDiscountAmount(srcCharge.getDiscountAmount());
		targetCharge.setType(srcCharge.getType());
	}




}