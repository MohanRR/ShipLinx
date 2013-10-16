package com.meritconinc.shiplinx.dao;


import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.meritconinc.mmr.constants.Constants;
import com.meritconinc.mmr.model.security.User;
import com.meritconinc.mmr.utilities.StringUtil;
import com.meritconinc.mmr.utilities.security.UserUtil;
import com.meritconinc.shiplinx.model.Customer;
import com.meritconinc.shiplinx.model.CustomerCarrier;
import com.meritconinc.shiplinx.model.CustomerSalesUser;
import com.meritconinc.shiplinx.model.CustomerTier;
import com.meritconinc.shiplinx.utils.ShiplinxConstants;



public class CustomerDAOImpl extends SqlMapClientDaoSupport implements CustomerDAO{
	private static final Logger log = LogManager.getLogger(CustomerDAOImpl.class);
	
	public long addCustomer(Customer customer) {
		long key=0;
		customer.setDateCreated(new Date());
		key = ((Long)getSqlMapClientTemplate().insert("addCustomer",customer)).longValue();
		return key;
		
	}
	public void addUser(Customer customer,long customerId){
		User user = new User();
		user.setCreatedAt(new Timestamp(new Date().getTime()));
		user.setEmail(customer.getAddress().getEmailAddress());
		
		Map<String, Object> paramObj = new HashMap<String, Object>();
		paramObj.put("username", customer.getUsername());
		paramObj.put("password", digest(customer.getPassword()));
		paramObj.put("password_changed", new Date());
		paramObj.put("first_name", customer.getAddress().getContactName());
		paramObj.put("last_name", null);
		paramObj.put("email", customer.getAddress().getEmailAddress());
		paramObj.put("user_comments", "");
		//paramObj.put("account_status",customer.getActive().equalsIgnoreCase("yes")?Constants.STATUS_ACTIVE:Constants.STATUS_INACTIVE);
		paramObj.put("created_by", customer.getUsername());
		paramObj.put("created", new Date());
		paramObj.put("company", customer.getName());
		paramObj.put("phone_number_ext", null);
		paramObj.put("occupation", "occupation");
		paramObj.put("address_line", null);
		paramObj.put("city", customer.getAddress().getCity());
		paramObj.put("state_province", null);
		paramObj.put("country_code", customer.getAddress().getCountryCode());
		paramObj.put("postal_zip", customer.getAddress().getPostalCode());
		paramObj.put("session_timeout",30);
		paramObj.put("locale", "en_CA");
		paramObj.put("expiration_date", null);
		paramObj.put("fax", "");
		paramObj.put("phone_number", customer.getAddress().getPhoneNo());
		paramObj.put("email", customer.getAddress().getEmailAddress());
		paramObj.put("subtype", "true");
		paramObj.put("passwordChangedAt", new Timestamp(new Date().getTime()));
				
		paramObj.put("enabled", 1);
		//paramObj.put("subtype", "true");
		paramObj.put("account_status",Constants.STATUS_ACTIVE);
		
		//setting default menu_id for the new customer
		paramObj.put("default_menuId",ShiplinxConstants.MENU_ID_NEW_SHIPMENT_PAGE);

//		if(customer.getActive().equals("1")){
//			paramObj.put("enabled", 1);
//			//paramObj.put("subtype", "true");
//			paramObj.put("account_status",Constants.STATUS_ACTIVE);
//			
//		}
//		else{
//			paramObj.put("enabled", 0);
//			//paramObj.put("subtype", "false");
//			paramObj.put("account_status",Constants.STATUS_INACTIVE);
//		}
		
		//setting default print options
		paramObj.put("printNoOfLabels", 1);
		paramObj.put("printNoOfCI", 3);
		paramObj.put("autoPrint", false);
		
		paramObj.put("accesstimes", 0);
		paramObj.put("loginfailedcount",0);
		paramObj.put("customerId", customerId);
		paramObj.put("role", ShiplinxConstants.ROLE_CUSTOMER_ADMIN);
		paramObj.put("businessId", customer.getBusinessId());
		getSqlMapClientTemplate().insert("createUser", paramObj);
		getSqlMapClientTemplate().insert("insertRole",paramObj);
				
	}
	public String digest(String original) {
		String digested = StringUtil.isEmpty(original) ? "" : DigestUtils
				.md5Hex(original);
		return digested;
	}
	
	public boolean isCustomerRegistered(String name, long businessId, long customerId) {
		Map<String, Object> paramObj = new HashMap<String, Object>(1);
		paramObj.put("customername", name);
		paramObj.put("businessId", businessId);
		paramObj.put("customerId", customerId);
		Integer count =  (Integer)getSqlMapClientTemplate().queryForObject("isCustomerRegistered", paramObj);
		return (count > 0);
	}
	
	
	public List<Customer> search(Customer customer){
		List<Customer> customers=null;
		try{
			return (List<Customer>)getSqlMapClientTemplate().queryForList("getCustomers", customer);

		}catch(Exception e){
			e.printStackTrace();
		}
		return customers;
	}	

	public Customer getCustomerInfoByCustomerId(long customerId, long businessId){
		try{
			Map<String, Object> paramObj = new HashMap<String, Object>(1);
			paramObj.put("id", customerId);
			paramObj.put("businessId", businessId);
			Customer customer=(Customer)getSqlMapClientTemplate().queryForObject("getCustomerInfoByCustomerId",paramObj);
			return customer;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	
	public void edit(Customer customer){

		getSqlMapClientTemplate().update("editCustomer",customer);
		
	}

	
	public long getCustomerIdByUserName(String username){
		long customerId;
		Map<String, Object> paramObj = new HashMap<String, Object>(2);
		paramObj.put("username", username);
		paramObj.put("businessId", UserUtil.getMmrUser().getBusinessId());

		customerId=(Long)getSqlMapClientTemplate().queryForObject("getCustomerIdByUserName",paramObj);
		
		return customerId;
	}
	
	public Long getCustomerIdByName(String name, Long busId){
		Long customerId;
		Map<String, Object> paramObj = new HashMap<String, Object>(2);
		paramObj.put("name", name);
		paramObj.put("businessId", busId);

		customerId=(Long) getSqlMapClientTemplate().queryForObject("getCustomerIdByName",paramObj);
		
		return customerId;
	}	
	
	public CustomerCarrier getCustomerCarrier(long customerId) {
		CustomerCarrier  customerCarrier;
		Map<String, Object> paramObj = new HashMap<String, Object>(1);
		paramObj.put("customerId", customerId);
		paramObj.put("businessId", UserUtil.getMmrUser().getBusinessId());

		customerCarrier=(CustomerCarrier)getSqlMapClientTemplate().queryForObject("getCustomerCarrier",paramObj);
		
		return customerCarrier;
	}
	
	public boolean isCarrierAccountRegistered(String accountNum, long carrierId) {
		Map<String, Object> paramObj = new HashMap<String, Object>(1);
		paramObj.put("accountNum", accountNum);
		paramObj.put("carrierId", carrierId);

		Integer count =  (Integer)getSqlMapClientTemplate().queryForObject("isCarrierAccountRegistered", paramObj);
		return (count>0);

	}
	
	public void saveCustomerCarrier(CustomerCarrier customerCarrier) {
		getSqlMapClientTemplate().insert("saveCustomerCarrier", customerCarrier);
	}
	
	public CustomerCarrier getCustomerCarrierInfoById(long customerId,Long carrierAccountId) {
		CustomerCarrier  customerCarrier;
		Map<String, Object> paramObj = new HashMap<String, Object>(1);
		paramObj.put("customerId", customerId);
		paramObj.put("carrierAccountId", carrierAccountId);

		customerCarrier=(CustomerCarrier)getSqlMapClientTemplate().queryForObject("getCustomerCarrierById",paramObj);
		
		return customerCarrier;
	}
	
	public void deleteCustomerCarrier(String carrierAccountId) {
		Map<String, Object> paramObj = new HashMap<String, Object>(1);
		paramObj.put("carrierAccountId", carrierAccountId);

		getSqlMapClientTemplate().delete("deleteCustomerCarrier",paramObj);
	}
	public void editCustomerCarrier(CustomerCarrier customerCarrier,
			Long customerCarrierId) {
		customerCarrier.setCarrierAccountId(customerCarrierId);
		getSqlMapClientTemplate().update("editCustomerCarrier", customerCarrier);
	}
	
	public void setDefaultCarrierAccount(long customerId, String country, long carrierId) {
		Map<String, Object> paramObj = new HashMap<String, Object>(1);
		paramObj.put("customerId", customerId);
		paramObj.put("country", country);
		paramObj.put("default", false);
		paramObj.put("carrierId", carrierId);
		try{
		getSqlMapClientTemplate().update("setOtherCarrierAccountAsFalse", paramObj);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	public List<CustomerCarrier> getCustomerCarrierByAccountNumber(String accountNumber) {
		// TODO Auto-generated method stub
		try{
			Map<String, Object> paramObj = new HashMap<String, Object>(1);
			paramObj.put("accountNumber", accountNumber);
			return getSqlMapClientTemplate().queryForList("getCustomerCarrierByAccountNumber", paramObj);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;

	}
	@Override
	public Customer getCustomerByApiInfo(String apiUserName, String apiUserPassword) {
		// TODO Auto-generated method stub
		try {
			Map<String, Object> paramObj = new HashMap<String, Object>(2);
			paramObj.put("apiUserName", apiUserName);
			paramObj.put("apiUserPassword", apiUserPassword);
			Customer customer = (Customer) getSqlMapClientTemplate().queryForObject("getCustomerByApiInfo", paramObj);
			return customer;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public List<CustomerSalesUser> getCustomerSalesUser(CustomerSalesUser csu){
		return getSqlMapClientTemplate().queryForList("getCustomerSalesUser", csu);
	}
	
	public long addCustomerSales(CustomerSalesUser customerSalesUser) {
		long key=0;
		key = ((Long)getSqlMapClientTemplate().insert("addCustomerSales",customerSalesUser)).longValue();
		return key;
		
	}
	
	public void deleteCustomerSalesUser(CustomerSalesUser customerSalesUser)
	{
		getSqlMapClientTemplate().delete("deleteCustomerSalesUser",customerSalesUser);	
	}
	
	public void updateCustomerSalesUser(CustomerSalesUser customerSalesUser)
	{
		getSqlMapClientTemplate().delete("updateCustomerSalesUser",customerSalesUser);	
	}
	
	public List<CustomerTier> getCustomerByTier(Customer customer){
		return getSqlMapClientTemplate().queryForList("getCustomerTierInfo", customer);
	}
	
	public boolean isWarehouseCustomer(long customerId)
	{
		boolean retval = false;
		Map map = new HashMap();
		map.put("customerId", customerId);
		try 
		{
			retval = (Boolean)getSqlMapClient().queryForObject("isWarehouseCustomer", map);	
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			log.error("Error occured when check if customer is a warehouse customer"+e);
		}
		
		return retval;
	}
	

}