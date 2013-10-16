package com.meritconinc.shiplinx.dao;


import java.util.List;

import com.meritconinc.shiplinx.model.BillingStatus;
import com.meritconinc.shiplinx.model.CarrierChargeCode;
import com.meritconinc.shiplinx.model.Charge;
import com.meritconinc.shiplinx.model.ChargeGroup;
import com.meritconinc.shiplinx.model.CustomsInvoice;
import com.meritconinc.shiplinx.model.CustomsInvoiceProduct;
import com.meritconinc.shiplinx.model.DangerousGoods;
import com.meritconinc.shiplinx.model.OrderProduct;
import com.meritconinc.shiplinx.model.OrderStatus;
import com.meritconinc.shiplinx.model.Package;
import com.meritconinc.shiplinx.model.PackageType;
import com.meritconinc.shiplinx.model.Products;
import com.meritconinc.shiplinx.model.Service;
import com.meritconinc.shiplinx.model.ShippingLabel;
import com.meritconinc.shiplinx.model.ShippingOrder;

public interface ShippingDAO{

	public List<String> findPackageTypeByName(String name);

	public PackageType findPackageType(String name);

	public List<PackageType> getPackages();

	public List getServices();

	void save(ShippingOrder shippingOrder) throws Exception;

	/**
	 * Add Packages
	 * @param packageList
	 * @param orderId 
	 */
	public void addPackage(List<Package> packageList, Long orderId) throws Exception;

	public Long saveCharges(Charge shippingCharge) throws Exception;

	public Long getServiceIdByName(String serviceName);

	public List<PackageType> getAllPackages();
	
	public List<OrderStatus> getShippingOrdersAllStatus();
	
	public List<OrderStatus> getShippingOrdersStatusOptions(long currentStatus);
	
	public List<String> getSearchOrderResult(ShippingOrder order);

	public void saveLabel(ShippingLabel label);

	public List<ShippingLabel> getLabelsByOrderId(long longValue);

	public List<String> getTodaysOrderResult(long customerId);
	
	public PackageType findOrderPackageType(long orderId);
	public ShippingOrder getShippingOrder(long orderId);	
	
	public List<Package> getShippingPackages(long orderId);
	
//	public void updateShippingOrder(long orderId);	
	public void updateShippingOrder(ShippingOrder order);
	
	public void deleteShippingOrder(long orderId);	

	public Service getServiceById(Long serviceId);

	public List<Charge> getShippingOrderCharges(long orderId);
	public List<Charge> getShippingOrderChargesByInvoice(long orderId, long invoiceId);

	public List<ShippingOrder> search(long carrierId);
	
	public List<CarrierChargeCode> getChargeListByCarrierAndCodes(long carrierId, String chargeCode, String chargeCodeLevel2);	
	public CarrierChargeCode getChargeByCarrierAndCodes(long carrierId, String chargeCode, String chargeCodeLevel2);	
	public ChargeGroup getChargeGroup(int groupId);

	public List<Long> getAllCustomersWithPendingCharges(long businessId);
	public List<Long> getAllShipmentsWithPendingChargesForCustomer(long customerId);

	public List<ShippingOrder> getOrdersByTrackingNumber(Long carrierId, String trackingNumber);
	public List<ShippingOrder> getOrdersByMasterTrackingNumber(Long carrierId, String trackingNumber);
	public void updateCharge(Charge shippingCharge) throws Exception;

	public void updatePackage(Package pkg);
	public List<ShippingOrder> getShipments(ShippingOrder so);
	
	public List<ShippingOrder> getLiveUnpaidShipments(long customerId);
	public List<ShippingOrder> getUnbilledShipments(long businessId, long customerId, String branch);
	public List<ShippingOrder> searchShipmentsByOrderIdsAndCustomerAndCurrency(List<Long> orderIds, long customerId, String currency);
	public List<Long> getCustomerIdsByOrderIds(List<Long> orderIds);
	public List<String> getCurrencyByOrderIds(List<Long> orderIds);



	public void releaseCharges(String ediInvoiceNumber);

	public void updateShippingOrderCustomerBilling(ShippingOrder shipment);

	public void updateShippingOrderBillingInfo(ShippingOrder shipment);
	public void updateShippingOrderBillingStatus(ShippingOrder shipment);

	public void deleteCharge(Long id);
	
	public boolean addCustomsInvoiceInformation(CustomsInvoice ci);
	public boolean addCustomsInvoiceProdutInformation(CustomsInvoiceProduct cip);
	public CustomsInvoice getCustomsInvoiceByOrderId(long orderId);
	public boolean deleteCustomsInvoice(long ciId);

	public ShippingOrder findByBatchId(String batchId);
	public void updateShippingOrderExtended(ShippingOrder order);
	
	public void saveProductDetails(Products product, long orderId);
	public Service getServiceByCarrierAndTransitCode(Long carrierId, String transitCode);
	public void addPackage(Package pack);
	
	public void updateOrderStatus(long OrderId, long statusId);
	
	public void updateOrderFulfilled(Products prods, long orderId);
	
	public List<OrderProduct> getOrderProducts(long orderId);
	
	public List<ShippingOrder> searchShipmentsByStatusIdsAndCarrier(List<Long> statusIds, long carrierId);
	
	public List<ShippingOrder> searchReferenceShipments(ShippingOrder order);
	
	public List<BillingStatus> getShippingBillingAllStatus();
	
	public List<DangerousGoods> getDangerousGoodsAll();

}