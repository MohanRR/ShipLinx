package com.meritconinc.shiplinx.dao;

import java.util.List;

import com.meritconinc.shiplinx.model.Carrier;
import com.meritconinc.shiplinx.model.CarrierChargeCode;
import com.meritconinc.shiplinx.model.CustomerCarrier;
import com.meritconinc.shiplinx.model.Service;

public interface CarrierServiceDAO{

	public List<CustomerCarrier> getCutomerCarrier(Long customerId);

	public List<Service> getServicesForCarrier(Long carrierId);
	
	public Service getService(Long serviceId);
	public Service getServiceByCarrierIdAndTransitCode(Long carrierId, String transitCode);
	public Service getServiceByCarrierIdAndCode(Long carrierId, String code);

	public Carrier getCarrier(Long carrierId);
	public Carrier getCarrierByBusiness(Long carrierId, Long businessId);

	public CustomerCarrier getCutomerCarrierDefaultAccount(long carrierId,long customerId);
	
	public CustomerCarrier getOrderCutomerCarrier(Long orderId);

	public List<CustomerCarrier> getAllCutomerCarrier(long businessId, Long customerId);
	
	public List<CustomerCarrier> getAllCustomersCarrier(long businessId, long carrierId);
	public List<Carrier> getCarriers();

	public CustomerCarrier getDefAccountByCountry(long carrierId,long customerId,String country);

	public List<CustomerCarrier> getCarrierAccounts(Long customerId, String countryName);
	public CustomerCarrier getCarrierAccount(Long customerId, Long businessId, Long carrierId, String country, String toCountry);

	public List<Carrier> getCarriersForBusiness(long businessId);
	
	public Long getMaxServiceIdForCarrier(long carrierId);
	
	public void addService(Service service)  throws Exception;
	
	
}