package com.meritconinc.shiplinx.dao;


import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.meritconinc.mmr.model.security.User;
import com.meritconinc.mmr.utilities.StringUtil;
import com.meritconinc.mmr.utilities.security.UserUtil;
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
import com.meritconinc.shiplinx.utils.ShiplinxConstants;



public class ShippingDAOImpl extends SqlMapClientDaoSupport implements ShippingDAO{
	private static final Logger log = LogManager.getLogger(ShippingDAOImpl.class);
	
	
	public List<String> findPackageTypeByName(String name) {
		
		List<String> packageType=null;
		try{
			packageType =  (List<String>)getSqlMapClientTemplate().queryForList("getPackage",name);
		}catch(Exception e){
			e.printStackTrace();
		}
		return packageType;
	}


	public List getPackages() {
		List packageType=null;
		try{
			packageType =  (List)getSqlMapClientTemplate().queryForList("getAllPackages");
		}catch(Exception e){
			e.printStackTrace();
		}
		return packageType;
	}
	
	public PackageType findPackageType(String type){
		Map<String, Object> paramObj = new HashMap<String, Object>(1);
		paramObj.put("type", type);
		PackageType packageType=null;
		try{
			packageType =  (PackageType) getSqlMapClientTemplate().queryForObject("getPackageType",paramObj);
//			log.debug("-----"+packageType);
		}catch(Exception e){
			e.printStackTrace();
		}
		return packageType;
	}


	public List getServices() {

		List services=null;
		try{
			services =  (List) getSqlMapClientTemplate().queryForList("getServices");
		}catch(Exception e){
			e.printStackTrace();
		}
		return services;
	}


	public void save(ShippingOrder shippingOrder)  throws Exception{
		Map<String, Object> paramObj = new HashMap<String, Object>(1);
		shippingOrder.setDateCreated(new Timestamp(Calendar.getInstance().getTimeInMillis()));
		try{
			getSqlMapClientTemplate().insert("addShippingOrder", shippingOrder);
		}catch(Exception e){
//			log.debug("-----Exception-----"+e);
			e.printStackTrace();
			throw e;
		}
		
	}


	/**
	 * Add Packages
	 * @param packageList
	 * @param orderId 
	 */
	public void addPackage(List<Package> packageList, Long orderId)  throws Exception{
		int i=0;
		try{
		for (Package pack : packageList) {
			pack.setOrderId(orderId);
			getSqlMapClientTemplate().insert("addPackage", pack);
		}}catch (Exception e) {
			logger.debug("----addPackage----Exception----"+e);
			throw e;
		}
	}


	public Long saveCharges(Charge shippingCharge)  throws Exception{
//		log.debug("------saveCharges----"+shippingCharge);
		try{
			return (Long) getSqlMapClientTemplate().insert("addCharge", shippingCharge);
		}catch (Exception e) {
//			log.debug("------Exception----"+e);
			throw e;
		}
	}


	public Long getServiceIdByName(String serviceName) {
		Long seriveId =0L;
		try{
			seriveId =  ((Service)getSqlMapClientTemplate().queryForObject("getServiceByName",serviceName)).getId();
		}catch(Exception e){
			e.printStackTrace();
		}
		return seriveId;
	}


	public List<PackageType> getAllPackages() {
		// TODO Auto-generated method stub
		try {
			return getSqlMapClientTemplate().queryForList("getAllPackages");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<OrderStatus> getShippingOrdersStatusOptions(long currentStatus) {

		List<OrderStatus> orderStatus=null;
		Map<String, Object> paramObj = new HashMap<String, Object>(1);
		paramObj.put("currStatus", currentStatus);
		try{
			orderStatus =  (List) getSqlMapClientTemplate().queryForList("getShippingOrdersStatusOptions", paramObj);
		}catch(Exception e){
			e.printStackTrace();
		}
		return orderStatus;
	}
	
	public List<OrderStatus> getShippingOrdersAllStatus() {

		List<OrderStatus> orderStatus=null;
		try{
			orderStatus =  (List) getSqlMapClientTemplate().queryForList("getShippingOrdersAllStatus");
		}catch(Exception e){
			e.printStackTrace();
		}
		return orderStatus;
	}
	
	public List<String> getSearchOrderResult(ShippingOrder order) {

		List<String> searchResult=null;
		try{
			Map<String, Object> paramObj = new HashMap<String, Object>();
			paramObj.put("carrierId", order.getCarrierId());
			paramObj.put("serviceId", order.getServiceId());
			paramObj.put("statusId", order.getStatusId());
			paramObj.put("masterTrackingNum", order.getMasterTrackingNum());	

			paramObj.put("toDate", (("".equals(order.getToDate()) || order.getToDate() ==null) ? Calendar.getInstance().getTime() : order.getToDate()));

			if(!"".equals(order.getFromDate()))
				paramObj.put("fromDate", order.getFromDate());	
			
			searchResult =  (List<String>) getSqlMapClientTemplate().queryForList("getSearchOrderResult",paramObj);
		}catch(Exception e){
			e.printStackTrace();
		}
		return searchResult;
	}
	
	
	public void saveLabel(ShippingLabel label) {
//		log.debug("------saveLabel----"+label);
		try{
			getSqlMapClientTemplate().insert("saveLabel", label);
		}catch (Exception e) {
//			log.debug("------"+e);
			e.printStackTrace();
		}
	}

	public List<ShippingLabel> getLabelsByOrderId(long orderId) {
		List<ShippingLabel> shippingLabel  = null;
		try{
			Map<String, Object> paramObj = new HashMap<String, Object>();
			paramObj.put("orderId", orderId);
			shippingLabel =  (List<ShippingLabel>)getSqlMapClientTemplate().queryForList("getShippingLabelByOrderId",paramObj);
		}catch(Exception e){
//			log.debug("--------"+e);
			e.printStackTrace();
		}
		return shippingLabel;
	}
	
	public List<String> getTodaysOrderResult(long customerId) {

		List<String> orderResult=null;
		try{
			Map<String, Object> paramObj = new HashMap<String, Object>();
			paramObj.put("customerId", customerId);
					
			
			orderResult =  (List<String>) getSqlMapClientTemplate().queryForList("getTodaysOrderResult",paramObj);
		}catch(Exception e){
			e.printStackTrace();
		}
		return orderResult;
	}
	
	public PackageType findOrderPackageType(long orderId){
		
		Map<String, Object> paramObj = new HashMap<String, Object>(1);
		paramObj.put("orderId", orderId);
		PackageType packageType=null;
		try{
			packageType =  (PackageType) getSqlMapClientTemplate().queryForObject("findOrderPackageType",paramObj);
//			log.debug("-----"+packageType);
		}catch(Exception e){
			e.printStackTrace();
		}
		return packageType;
	}
	
	public ShippingOrder getShippingOrder(long orderId){
		
		Map<String, Object> paramObj = new HashMap<String, Object>(1);
		paramObj.put("orderId", orderId);
		ShippingOrder shipingOrder=null;
		try{
			shipingOrder =  (ShippingOrder) getSqlMapClientTemplate().queryForObject("getShippingOrderById",paramObj);
//			log.debug("-----"+shipingOrder);
//			log.debug("-----"+shipingOrder.getId());
		}catch(Exception e){
			e.printStackTrace();
		}
		return shipingOrder;
	}
	
	public List<Package> getShippingPackages(long orderId){
		Map<String, Object> paramObj = new HashMap<String, Object>(1);
		paramObj.put("orderId", orderId);
		List<Package> listPackage=null;
		try{
			listPackage =  (List<Package>) getSqlMapClientTemplate().queryForList("getShippingPackages",paramObj);
//			log.debug("-----"+listPackage);
		}catch(Exception e){
			e.printStackTrace();
		}
		return listPackage;
	
	}
	
//	public void updateShippingOrder(long orderId){
//		Map<String, Object> paramObj = new HashMap<String, Object>(1);
//		paramObj.put("orderId", orderId);
//		try{
//			getSqlMapClientTemplate().update("updateShippingOrder", paramObj);
//		}catch(Exception e){
//			e.printStackTrace();
//		}		
//	}
//
	public void updateShippingOrder(ShippingOrder order){
		getSqlMapClientTemplate().update("updateShippingOrder",order);
		updateShippingOrderBillingStatus(order);
	}

	public void deleteShippingOrder(long orderId){
//		log.debug("----deleteShippingOrder-----");
		Map<String, Object> paramObj = new HashMap<String, Object>(1);
		paramObj.put("orderId", orderId);
		try{
			getSqlMapClientTemplate().update("deleteShippingOrder", paramObj);
		}catch(Exception e){
			e.printStackTrace();
		}

		
	}


	public Service getServiceById(Long serviceId) {
		Map<String, Object> paramObj = new HashMap<String, Object>(1);
		paramObj.put("serviceId", serviceId);
		Service service = null;
		try{
			service =  (Service) getSqlMapClientTemplate().queryForObject("getService",paramObj);
//			log.debug("-----"+service);
		}catch(Exception e){
			e.printStackTrace();
		}
		return service;
	}

	public List<Charge> getShippingOrderCharges(long orderId){
		Map<String, Object> paramObj = new HashMap<String, Object>(1);
		paramObj.put("orderId", orderId);
		List<Charge> listCharge=null;
		try{
			listCharge =  (List<Charge>) getSqlMapClientTemplate().queryForList("getShippingCharges",paramObj);
//			log.debug("-----"+listCharge);
		}catch(Exception e){
			e.printStackTrace();
		}
		return listCharge;
	}

	public List<Charge> getShippingOrderChargesByInvoice(long orderId, long invoiceId){
		Map<String, Object> paramObj = new HashMap<String, Object>();
		paramObj.put("orderId", orderId);
		paramObj.put("invoiceId", invoiceId);
		List<Charge> listCharge=null;
		try{
			listCharge =  (List<Charge>) getSqlMapClientTemplate().queryForList("getShippingChargesByInvoice",paramObj);
//			log.debug("-----"+listCharge);
		}catch(Exception e){
			e.printStackTrace();
		}
		return listCharge;
	}

	public List<ShippingOrder> search(long carrierId) {
//		log.debug("--search---"+carrierId);
		
		List<ShippingOrder> searchResult=null;
		try{
			Map<String, Object> paramObj = new HashMap<String, Object>();
			paramObj.put("carrierId", carrierId);
			//paramObj.put("customerId", customerId);
			searchResult =  (List<ShippingOrder>) getSqlMapClientTemplate().queryForList("searchOrderByCarrierId",paramObj);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return searchResult;
	}

	public List<CarrierChargeCode> getChargeListByCarrierAndCodes(long carrierId, String chargeCode, String chargeCodeLevel2){
		
		//Currently we have 2 carriers for UPS, hard-coding here so that the charge codes are shared. Other option is to duplicate the charge code entries
		//in the carrier_charge_code table for carrier id 5
		if(carrierId==ShiplinxConstants.CARRIER_UPS_USA)
			carrierId = ShiplinxConstants.CARRIER_UPS;
		
		List<CarrierChargeCode> searchResult=null;
		try{
			Map<String, Object> paramObj = new HashMap<String, Object>();
			paramObj.put("carrierId", carrierId);
			paramObj.put("chargeCode", chargeCode);
			paramObj.put("chargeCodeLevel2", chargeCodeLevel2);
			searchResult =  (List<CarrierChargeCode>) getSqlMapClientTemplate().queryForList("getChargeByCarrierAndCodes",paramObj);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return searchResult;
	}

	public ChargeGroup getChargeGroup(int groupId){
		
		try{
			Map<String, Object> paramObj = new HashMap<String, Object>();
			paramObj.put("groupId", groupId);
			return (ChargeGroup) getSqlMapClientTemplate().queryForObject("getChargeGroup",paramObj);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	public CarrierChargeCode getChargeByCarrierAndCodes(long carrierId, String chargeCode, String chargeCodeLevel2){
		
		//Currently we have 2 carriers for UPS, hard-coding here so that the charge codes are shared. Other option is to duplicate the charge code entries
		//in the carrier_charge_code table for carrier id 5
		if(carrierId==ShiplinxConstants.CARRIER_UPS_USA)
			carrierId = ShiplinxConstants.CARRIER_UPS;
		
		List<CarrierChargeCode> searchResult = this.getChargeListByCarrierAndCodes(carrierId, chargeCode, chargeCodeLevel2) ;
		if(searchResult == null || searchResult.size()==0)
			return null;
		return searchResult.get(0);
	}

	public List<Long> getAllCustomersWithPendingCharges(long businessId){
		List<Long> searchResult=null;
		try{
			Map<String, Object> paramObj = new HashMap<String, Object>();
			paramObj.put("businessId", businessId);
			searchResult =  (List<Long>) getSqlMapClientTemplate().queryForList("getAllCustomersWithPendingCharges",paramObj);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return searchResult;
		
	}

	public List<Long> getAllShipmentsWithPendingChargesForCustomer(long customerId){
		List<Long> searchResult=null;
		try{
			Map<String, Object> paramObj = new HashMap<String, Object>();
			paramObj.put("customerId", customerId);
			searchResult =  (List<Long>) getSqlMapClientTemplate().queryForList("getAllShipmentsWithPendingChargesForCustomer",paramObj);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return searchResult;

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ShippingOrder> getOrdersByTrackingNumber(Long carrierId, String trackingNumber) {
		// TODO Auto-generated method stub
		try {
			Map<String, Object> paramObj = new HashMap<String, Object>();
			paramObj.put("carrierId", carrierId);
			paramObj.put("masterTrackingNum", trackingNumber);	

			return getSqlMapClientTemplate().queryForList("searchOrderByTrackingNumber",paramObj);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	@Override 
	public List<ShippingOrder> getOrdersByMasterTrackingNumber(Long carrierId, String trackingNumber) {
		try {
			Map<String, Object> paramObj = new HashMap<String, Object>();
			paramObj.put("carrierId", carrierId);
			paramObj.put("masterTrackingNum", trackingNumber);	

			return getSqlMapClientTemplate().queryForList("searchOrderByMasterTrackingNumber",paramObj);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}	


	@Override
	public void updateCharge(Charge charge) throws Exception {
		// TODO Auto-generated method stub
		try {
			Map<String, Object> paramObj = new HashMap<String, Object>();
			paramObj.put("name", charge.getName());
			paramObj.put("cost", charge.getCost());
			paramObj.put("status", charge.getStatus());
			paramObj.put("charge", charge.getCharge());
			paramObj.put("discountAmount", charge.getDiscountAmount());
			paramObj.put("tariffRate", charge.getTariffRate());
			paramObj.put("id", charge.getId());

			getSqlMapClientTemplate().update("updateCharge", paramObj);
		} catch(Exception e) {
			e.printStackTrace();
		}		
	}


	@Override
	public void updatePackage(Package pkg) {
		// TODO Auto-generated method stub
		try {
//			Map<String, Object> paramObj = new HashMap<String, Object>();
//			paramObj.put("reference1", pkg.getReference1());
//			paramObj.put("reference2", pkg.getReference2());
//			paramObj.put("reference3", pkg.getReference3());
//			paramObj.put("weight", pkg.getWeight());
//			paramObj.put("billedWeight", pkg.getBilledWeight());
//			paramObj.put("type", pkg.getType());			
//			paramObj.put("packageId", pkg.getPackageId());

			getSqlMapClientTemplate().update("updatePackage", pkg);
		} catch(Exception e) {
			e.printStackTrace();
		}		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ShippingOrder> getUnbilledShipments(long businessId, long customerId, String branch) {
		// TODO Auto-generated method stub
		try {
			Map<String, Object> paramObj = new HashMap<String, Object>();
			paramObj.put("businessId", businessId);
			paramObj.put("customerId", customerId);	
			paramObj.put("branch", branch);	
			paramObj.put("statusId", ShiplinxConstants.CHARGE_READY_TO_INVOICE);	

			return getSqlMapClientTemplate().queryForList("searchUnbilledShipments2",paramObj);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	public List<ShippingOrder> getLiveUnpaidShipments(long customerId) {
		// TODO Auto-generated method stub
		try {
			Map<String, Object> paramObj = new HashMap<String, Object>();
			paramObj.put("customerId", customerId);	
			paramObj.put("statusId", ShiplinxConstants.STATUS_CANCELLED);	

			return getSqlMapClientTemplate().queryForList("searchLiveUnpaidShipments",paramObj);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	public List<ShippingOrder> searchShipmentsByOrderIdsAndCustomerAndCurrency(List<Long> orderIds, long customerId, String currency){
		// TODO Auto-generated method stub
		try {
			Map<String, Object> paramObj = new HashMap<String, Object>();
			paramObj.put("orderIds", orderIds);
			paramObj.put("customerId", customerId);	
			paramObj.put("currency", currency);	
			return getSqlMapClientTemplate().queryForList("searchShipmentsByOrderIdsAndCustomerAndCurrency",paramObj);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public List<ShippingOrder> searchShipmentsByStatusIdsAndCarrier(List<Long> statusIds, long carrierId){
		// TODO Auto-generated method stub
		try {
			Map<String, Object> paramObj = new HashMap<String, Object>();
			paramObj.put("statusIds", statusIds);
			paramObj.put("carrierId", carrierId);	
			return getSqlMapClientTemplate().queryForList("searchShipmentsByStatusIdsAndCarrier",paramObj);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	

	public List<Long> getCustomerIdsByOrderIds(List<Long> orderIds) {
		// TODO Auto-generated method stub
		try {
			return getSqlMapClientTemplate().queryForList("searchCustomerIdsByOrderIds",orderIds);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	public List<String> getCurrencyByOrderIds(List<Long> orderIds) {
		// TODO Auto-generated method stub
		try {
			return getSqlMapClientTemplate().queryForList("searchCurrencyByOrderIds",orderIds);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<ShippingOrder> getShipments(ShippingOrder so) {
		// TODO Auto-generated method stub
		try {
			if (so.getCustomerId() != null && so.getCustomerId().longValue() <= 0)
				so.setCustomerId(null);
			if (so.getCarrierId() != null && so.getCarrierId().longValue() <= 0)
				so.setCarrierId(null);
			if (so.getServiceId() != null && so.getServiceId().longValue() <= 0)
				so.setServiceId(null);
			if (so.getFromDate() != null && so.getFromDate().trim().isEmpty())
				so.setFromDate(null);
			if (so.getMasterTrackingNum() != null && so.getMasterTrackingNum().trim().isEmpty())
				so.setMasterTrackingNum(null);
			if (so.getEdiInvoiceNumber() != null && so.getEdiInvoiceNumber().trim().isEmpty())
				so.setEdiInvoiceNumber(null);	
			if(so.getShowCancelledShipments()!=null && so.getShowCancelledShipments())
				so.setCancelledShipments("Y");		//checked - show cancelled shipments
			else
				so.setCancelledShipments("N");		//not checked - dont show cancelled shipments
			if (so.getStatusId() != null && so.getStatusId().longValue() <=0)
				so.setStatusId(null);
			
			//If search is conducted by order id or order num or tracking number, or batch id or reference, then excluded date range search
			if((!StringUtil.isEmpty(so.getMasterTrackingNum())) || (so.getId()!=null && so.getId()>0) 
					|| (!StringUtil.isEmpty(so.getOrderNum())) || (!StringUtil.isEmpty(so.getEdiInvoiceNumber()))
					|| (!StringUtil.isEmpty(so.getBatchId())) || (!StringUtil.isEmpty(so.getReferenceCode()))){
				so.setFromDate(null);
				so.setToDate(null);
			}
			
			if(StringUtil.isEmpty(so.getOrderBy())){
				so.setOrderBy("o.order_id");
			}
			else{//set the field
				if(so.getOrderBy().equalsIgnoreCase("Order #"))
					so.setOrderBy("o.order_id");
				else if(so.getOrderBy().equalsIgnoreCase("Shipment Date"))
					so.setOrderBy("o.scheduled_ship_date");
				else 
					so.setOrderBy("o.order_id");
			}
			
			if(StringUtil.isEmpty(so.getOrder())){
				so.setOrder("asc");
			}
			
			User user = UserUtil.getMmrUser();
			String username = null;
			if(user!=null)
				username = user.getUsername();
			
			log.info("User " + username + " performing search from / to: " + so.getFromDate()  + " / " + so.getToDate());
			
			List<ShippingOrder> list = getSqlMapClientTemplate().queryForList("findShipments",so);
			return list;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}


	@Override
	public void releaseCharges(String ediInvoiceNumber) {
		// TODO Auto-generated method stub
		try {
			Map<String, Object> paramObj = new HashMap<String, Object>();
			paramObj.put("ediInvoiceNumber", ediInvoiceNumber);
			paramObj.put("statusPendingRelease",ShiplinxConstants.CHARGE_PENDING_RELEASE);
			paramObj.put("statusReadyToInvoice", ShiplinxConstants.CHARGE_READY_TO_INVOICE);
			paramObj.put("billingStatusAwaitingConfirmation", ShiplinxConstants.BILLING_STATUS_AWAITING_CONFIRMATION);

			getSqlMapClientTemplate().update("releaseCharges", paramObj);
		} catch(Exception e) {
			e.printStackTrace();
		}		
	}


	@Override
	public void updateShippingOrderCustomerBilling(ShippingOrder shipment) {
		// TODO Auto-generated method stub
		Map<String, Object> paramObj = new HashMap<String, Object>(5);
		try {
			if (shipment != null) {
				for (Charge charge:shipment.getCharges()) {
					this.updateCharge(charge);
				}
				paramObj.put("orderId", shipment.getId());
				paramObj.put("customerId", shipment.getCustomerId());
				paramObj.put("markPercent", shipment.getMarkPercent());
				paramObj.put("markType", shipment.getMarkType());
				paramObj.put("billingStatus", shipment.getBillingStatus());
				
				getSqlMapClientTemplate().update("updateShippingOrderCustomerBilling", paramObj);
			}
		}catch(Exception e){
			e.printStackTrace();
		}		
	}


	@Override
	public void updateShippingOrderBillingInfo(ShippingOrder shipment) {
		// TODO Auto-generated method stub
		Map<String, Object> paramObj = new HashMap<String, Object>(5);
		try {
			if (shipment != null) {
//				for (Charge charge:shipment.getCharges()) {
//					this.updateCharge(charge);
//				}
				paramObj.put("orderId", shipment.getId());
				paramObj.put("customerId", shipment.getCustomerId());
				paramObj.put("markPercent", shipment.getMarkPercent());
				paramObj.put("markType", shipment.getMarkType());
				paramObj.put("billingStatus", shipment.getBillingStatus());
				
				getSqlMapClientTemplate().update("updateShippingOrderCustomerBilling", paramObj);
			}
		}catch(Exception e){
			e.printStackTrace();
		}		
	}

	public void updateShippingOrderBillingStatus(ShippingOrder shipment) {
		// TODO Auto-generated method stub
		int billingStatus = determineShipmentBillingStatus(shipment);
		log.debug("Billing Status for order " + shipment.getId() + " is " + billingStatus + " // previously " + shipment.getBillingStatus());
		if(shipment.getBillingStatus()!=null && shipment.getBillingStatus() == billingStatus)
			return;

		Map<String, Object> paramObj = new HashMap<String, Object>(5);
		try {
			if (shipment != null) {
				paramObj.put("orderId", shipment.getId());
				paramObj.put("billingStatus", billingStatus);
				
				getSqlMapClientTemplate().update("updateShippingOrderBillingStatus", paramObj);
			}
		}catch(Exception e){
			e.printStackTrace();
		}		
	}


	@Override
	public void deleteCharge(Long id) {
		// TODO Auto-generated method stub
		if (id != null) {
			Map<String, Object> paramObj = new HashMap<String, Object>(1);
			paramObj.put("id",id);
			getSqlMapClientTemplate().delete("deleteCharge", paramObj);		
		}		
	}
	
	public boolean deleteCustomsInvoice(long ciId){
		if (ciId > 0) {
			Map<String, Object> paramObj = new HashMap<String, Object>(1);
			paramObj.put("id",ciId);
			getSqlMapClientTemplate().delete("deleteCustomsInvoiceProduct", paramObj);
			getSqlMapClientTemplate().delete("deleteCustomsInvoice", paramObj);
		}		
		return true;
		
	}

	public boolean addCustomsInvoiceInformation(CustomsInvoice ci)
	{
		boolean retval = true;
		try 
		{
			long id = (Long)getSqlMapClientTemplate().insert("addCI", ci);
			ci.setId(id);
		}
		catch (Exception e) 
		{
			retval=false;
			log.error("Could not add customs invoice!", e);
		}
		return retval;
	}
	
	public boolean addCustomsInvoiceProdutInformation(CustomsInvoiceProduct cip)
	{	
		boolean retval=true;
		try 
		{
			getSqlMapClientTemplate().insert("addCIProduct", cip);
		}
		catch (Exception e) 
		{
			retval=false;
			log.error("Could not add customs invoice product!", e);
		}
		
		return retval;
	
	}

	public CustomsInvoice getCustomsInvoiceByOrderId(long orderId){
		Map<String, Object> paramObj = new HashMap<String, Object>();
		paramObj.put("orderId", orderId);
		return (CustomsInvoice)getSqlMapClientTemplate().queryForObject("getCustomsInvoiceByOrderId", orderId);		
	}


	@Override
	public ShippingOrder findByBatchId(String batchId) {
		// TODO Auto-generated method stub
		Map<String, Object> paramObj = new HashMap<String, Object>();
		paramObj.put("batchId", batchId);
		return (ShippingOrder)getSqlMapClientTemplate().queryForObject("findShipmentByBatchId", paramObj);	
	}


	@Override
	public void updateShippingOrderExtended(ShippingOrder order) {
		// TODO Auto-generated method stub
		getSqlMapClientTemplate().update("updateShippingOrderExtended",order);
	}
	
	public void saveProductDetails(Products product, long orderId)
	{
		Map<String, Object> paramObj = new HashMap<String, Object>();
		paramObj.put("productId", product.getProductId());
		paramObj.put("orderId", orderId);
		paramObj.put("orderedQuantity", product.getOrderedQuantity());
		getSqlMapClientTemplate().insert("addProductsToShipment", paramObj);
	}
	
	public void updateOrderFulfilled(Products product, long orderId)
	{
		//update the fulfilled value in order_product table.
		Map<String, Object> paramObj = new HashMap<String, Object>();
		paramObj.put("productId", product.getProductId());
		paramObj.put("orderId", orderId);
		paramObj.put("fulfilled", product.getOrderedQuantity());
		getSqlMapClientTemplate().update("updatefulfilledProductOrder", paramObj);
	}
	
	@Override
	public Service getServiceByCarrierAndTransitCode(Long carrierId, String transitCode) {
		// TODO Auto-generated method stub
		try{
			Map<String, Object> paramObj = new HashMap<String, Object>(2);
			paramObj.put("carrierId", carrierId);
			paramObj.put("transitCode", transitCode);
			return (Service)getSqlMapClientTemplate().queryForObject("getServiceByCarrierAndTransitCode", paramObj);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public void addPackage(Package pack) {
		try {
			getSqlMapClientTemplate().insert("addPackage", pack);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		}		
	}
	
	public void updateOrderStatus(long OrderId, long statusId)
	{
		Map<String, Object> paramObj = new HashMap<String, Object>(5);
		try 
		{
			paramObj.put("orderId", OrderId);
			paramObj.put("statusId", statusId);
			getSqlMapClientTemplate().update("updateShippingOrderStatus", paramObj);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			logger.error(e);
		}
	}
	
	public List<OrderProduct> getOrderProducts(long orderId)
	{
		Map<String, Object> paramObj = new HashMap<String, Object>(1);
		List<OrderProduct> oplist = new ArrayList<OrderProduct>();
		try 
		{
			paramObj.put("orderId", orderId);
			oplist = (List) getSqlMapClientTemplate().queryForList("getOrderProducts", paramObj);
			
		} catch (Exception e) 
		{
			log.error("Error Occured while getting the Order Product List"+e);
			e.printStackTrace();
		}
		
		return oplist;
	}

	public List<ShippingOrder> searchReferenceShipments(ShippingOrder order)
	{
		User user = UserUtil.getMmrUser();
		String username = null;
		if(user!=null)
			username = user.getUsername();
		
		log.info("User " + username + " performing search from / to: " + order.getFromDate()  + " / " + order.getToDate() + " / " + order.getReferenceValue());
		List<ShippingOrder> soList = new ArrayList<ShippingOrder>();
		try 
		{
			soList = (List) getSqlMapClientTemplate().queryForList("searchReferenceShipments", order);
		}
		catch (Exception e) 
		{
			log.error("Error Occured while getting the Shipments by Reference",e);
		}
		return soList;
	}
	
	public List<BillingStatus> getShippingBillingAllStatus()
	{
		List<BillingStatus> billingStatus=null;
		try{
			billingStatus =  (List) getSqlMapClientTemplate().queryForList("getShippingBillingAllStatus");
		}catch(Exception e){
			e.printStackTrace();
		}
		return billingStatus;
	}
 
	private int determineShipmentBillingStatus(ShippingOrder order){
		
		//if the order is an orphan or if we are waiting for admin to confirm the linked customer, then leave status as is
		if(order.getBillingStatus()!=null && (order.getBillingStatus().intValue()==ShiplinxConstants.BILLING_STATUS_AWAITING_CONFIRMATION || 
				order.getBillingStatus().intValue()==ShiplinxConstants.BILLING_STATUS_ORPHAN))
			return order.getBillingStatus();
		
		//if customer is not known, this should be marked as an orphan
		if(order.getCustomerId()!=null && order.getCustomerId()==0)
			return ShiplinxConstants.BILLING_STATUS_ORPHAN;
		
		//if shipment is TP or Collect, then mark accordingly
		if(order.getBillToType()!=null){
			if(order.getBillToType().equalsIgnoreCase(ShiplinxConstants.BILL_TO_THIRD_PARTY)){
				return ShiplinxConstants.BILLING_STATUS_THIRD_PARTY;
			}
			if(order.getBillToType().equalsIgnoreCase(ShiplinxConstants.BILL_TO_COLLECT)){
				return ShiplinxConstants.BILLING_STATUS_COLLECT;
			}
		}
		
		//if there are no actual charges for the shipment, then it means that the shipment was never invoiced
		if(order.getActualCharges()==null || order.getActualCharges().size()==0)
			return ShiplinxConstants.BILLING_STATUS_NOT_INVOICED;
		
		//if there are any charges that are pending release or ready to invoice, then we set the shipment as ready to invoice
		for(Charge c: order.getActualCharges()){
			if(c.getStatus() == ShiplinxConstants.CHARGE_PENDING_RELEASE || c.getStatus() == ShiplinxConstants.CHARGE_READY_TO_INVOICE){
				return ShiplinxConstants.BILLING_STATUS_READY_TO_INVOICE;
			}
		}
		
		boolean allInvoiced = true;
		for(Charge c: order.getActualCharges()){
			if(c.getStatus() != ShiplinxConstants.CHARGE_INVOICED){
				allInvoiced = false;
			}
		}
		
		if(allInvoiced)
			return ShiplinxConstants.BILLING_STATUS_INVOICED;
			
		log.debug("Could not determine billing status of order " + order.getId());
		
		
		return order.getBillingStatus();
		
	}
	
	public List<DangerousGoods> getDangerousGoodsAll()
	{
		List<DangerousGoods> dgList = new ArrayList<DangerousGoods>();
		try {
			dgList = (List) getSqlMapClientTemplate().queryForList("getDangerousGoodsAll");
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return dgList;
		
	}
	

}