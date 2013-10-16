package com.meritconinc.shiplinx.service;

import java.io.InputStream;
import java.util.List;

import com.meritconinc.mmr.exception.MarkupAlreadyExistsException;
import com.meritconinc.shiplinx.model.CarrierChargeCode;
import com.meritconinc.shiplinx.model.Charge;
import com.meritconinc.shiplinx.model.ChargeGroup;
import com.meritconinc.shiplinx.model.Markup;
import com.meritconinc.shiplinx.model.Service;
import com.meritconinc.shiplinx.model.ShippingOrder;

public interface MarkupManager {
	public List<Markup> getMarkupListForCustomer(Markup markup);
	public Double [] getFlatMarkups();
	public Integer [] getPercentageMarkups();
	public void deleteMarkup(Markup markup);
//	public void addMarkup(Markup markup) throws MarkupAlreadyExistsException;
	public Markup addMarkup(Markup markup);
	public void applyToAllCustomersMarkup(Markup markup);
	public void updateMarkup(Markup markup);
	public void calculatMarkup(ShippingOrder shipment, Charge charge);
	public Markup getUniqueMarkup(Markup markup);
	public Markup getMarkupObj(ShippingOrder shipment);
	public Double applyMarkup(ShippingOrder shipment, Charge charge, boolean setShipmentMarkup);
	public void copyCustomerMarkup(Long sourceCusId, Long targetCusId, long businessId);
	public void disableOrEnableAllServicesForCustomerAndCarrier(long customerId, long carrierId, boolean disable);
	public List<CarrierChargeCode> searchCharges(CarrierChargeCode carrierChargeCode);
	public void deleteCharges(long chargeId);
	public List<CarrierChargeCode> getCharges(CarrierChargeCode carrierChargeCode);
	
	public List<ChargeGroup> getChargeGroups();
	
	public void addOrUpdateCharge(CarrierChargeCode carrierChargeCode, boolean add);
	public Service uploadRateTemplateFile(Service service, Long customerId, Long busId, String uploadFileName, InputStream is, boolean b);
}
