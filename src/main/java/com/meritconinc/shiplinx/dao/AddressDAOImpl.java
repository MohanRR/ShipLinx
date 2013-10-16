package com.meritconinc.shiplinx.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.meritconinc.shiplinx.model.Address;
import com.meritconinc.shiplinx.model.Country;
import com.meritconinc.shiplinx.model.Province;

public class AddressDAOImpl extends SqlMapClientDaoSupport implements AddressDAO{
	private static final Logger log = LogManager.getLogger(AddressDAOImpl.class);

	public List<Address> findaddressesByCustomer(Long custId){
		List<Address> address = new ArrayList<Address>();
		try{
			Map<String, Object> paramObj = new HashMap<String, Object>(1);
			paramObj.put("customerId",custId);
			address =  (List<Address>)getSqlMapClientTemplate().queryForList("searchAddress", paramObj);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return address;
	}

	public Long add(Address address) {
		Long id = (Long) getSqlMapClientTemplate().insert("addAddresses",address);

		return id;
	}

	public List<String> getCanadianProvinces(){

		List<String> provinces =  (List<String>)getSqlMapClientTemplate().queryForList("getCanadianProvinces");
		return provinces;
	}

	public List<String> getUSStates(){

		List<String> states =  (List<String>)getSqlMapClientTemplate().queryForList("getUSStates");
		return states;
	}

	public List<Country> getCountries(){
		return getSqlMapClientTemplate().queryForList("getAllCountries");
	}

	public void delete(String id){
		Map<String, Object> paramObj = new HashMap<String, Object>(1);
		paramObj.put("addressid", id);
		getSqlMapClientTemplate().delete("deleteaddress", paramObj);

	}

	public void edit(Address address){
		try{
			getSqlMapClientTemplate().update("updateaddress", address);
			
			if(address.getDefaultFromAddress()==true && address.getCustomerId()>0)
				getSqlMapClientTemplate().update("updateFromAddressNotDefault", address);
			if(address.getDefaultToAddress()==true && address.getCustomerId()>0)
				getSqlMapClientTemplate().update("updateToAddressNotDefault", address);
			
		}catch(Exception e){
			e.printStackTrace();
		}

	}

	public Address findAddressById(String id) {
		Address address =  (Address)getSqlMapClientTemplate().queryForObject("findAddressById", Long.valueOf(id));
		return address;
	}
	
	public void addDistributionList(Address address) {
		getSqlMapClientTemplate().insert("uploadDistribution_Address",address);
		log.debug("@@@@@@@@------@@@@@@@@@------Address entered for ----------------------------:"+address.getAbbreviationName());
	}

	public void deleteDistributionList(String distributionListId, Long customerId){
		Map<String, Object> paramObj = new HashMap<String, Object>(1);
		paramObj.put("distributionListId", distributionListId);
		paramObj.put("customerId", customerId);
		getSqlMapClientTemplate().delete("deleteDistributionAddress", paramObj);
	//	getSqlMapClientTemplate().delete("deleteDistributionList", paramObj);

	}

	public List<String> findDistributionListForCustomer(Long customerId) {
		log.debug("ADDRESS BOOK CHECK INSIDE DAO  -- findDistributionListForCustomer -- "+customerId);
		Map<String, Object> paramObj = new HashMap<String, Object>(1);
		paramObj.put("customerId", customerId);
		List<String> distributionList =  (List<String>)getSqlMapClientTemplate().queryForList("searchDistributionList", paramObj);
		log.debug("distributionList  ---- "+ distributionList);
		return distributionList;
	}

	public List<Province> getProvinces(String country) {
		Map<String, Object> paramObj = new HashMap<String, Object>(1);
		paramObj.put("countryCode", country);
		return getSqlMapClientTemplate().queryForList("getProvinces",paramObj);
	}
	public Province getProvince(String provinceCode) {
		Map<String, Object> paramObj = new HashMap<String, Object>(1);
		if(provinceCode.equalsIgnoreCase("QC"))
			provinceCode = new String("PQ");
		paramObj.put("provinceCode", provinceCode);
		return (Province)getSqlMapClientTemplate().queryForObject("getProvince",paramObj);
	}


	public Address findDefaultToAddressForCustomer(Long customerId) {
		Address address = null;
		try{
		log.debug("ADDRESS BOOK CHECK INSIDE DAO  findDefaultToAddressForCustomer---- ");
		
		Map<String, Object> paramObj = new HashMap<String, Object>(1);
		logger.debug(" customerId 111 "+customerId);
		paramObj.put("customerId", customerId);
		address = (Address)getSqlMapClientTemplate().queryForObject("findDefaultToAddressForCustomer", paramObj);
		logger.debug(" RUCHITA CHECK RETRIEVED address IN DAO  "+ address);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return address;
		
	}

	public Address findDefaultFromAddressForCustomer(Long customerId) {
		log.debug("ADDRESS BOOK CHECK INSIDE DAO  findDefaultFromAddressForCustomer---- ");
		Address address = null;
		try{
		Map<String, Object> paramObj = new HashMap<String, Object>(1);
		logger.debug(" customerId 2222 "+customerId);
		paramObj.put("customerId", customerId);
		address =  (Address)getSqlMapClientTemplate().queryForObject("findDefaultFromAddressForCustomer", paramObj);
		logger.debug("findDefaultFromAddressForCustomer "+address);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return address;
		
	}

	public Address findToAddressForOrder(Long orderId) {
		Address address = null;
		try{
		log.debug("ADDRESS BOOK CHECK INSIDE DAO  findToAddressForOrder---- ");
		
		Map<String, Object> paramObj = new HashMap<String, Object>(1);
		logger.debug(" orderId 111 "+orderId);
		paramObj.put("orderId", orderId);
		address =  (Address)getSqlMapClientTemplate().queryForObject("getToAddressForOrder", paramObj);
		logger.debug(" RUCHITA CHECK RETRIEVED address IN DAO  "+address);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return address;
		
	}

	public Address findFromAddressForOrder(Long orderId) {
		log.debug("ADDRESS BOOK CHECK INSIDE DAO  findFromAddressForOrder---- ");
		Address address = null;
		try{
		Map<String, Object> paramObj = new HashMap<String, Object>(1);
		logger.debug(" orderId 2222 "+orderId);
		paramObj.put("orderId", orderId);
		address =  (Address)getSqlMapClientTemplate().queryForObject("getFromAddressForOrder", paramObj);
		logger.debug("getFromAddressForOrder "+address);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return address;
		
	}
	
	public List<Address> findAddresses(Address addressBook) {
		List<Address> address = new ArrayList();
		try{
			address =  (List)getSqlMapClientTemplate().queryForList("findAddressesByCustomer", addressBook);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return address;
	}
	
	/* Start : Sumanth Kulkarni S
	 * Date: 14 Oct 2011
	 */
	public List<Address> searchAddresses(Address add){
		
		List<Address> address = new ArrayList();
		try{
			address =  (List)getSqlMapClientTemplate().queryForList("findAddressesByCustomerSearch", add);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return address;	
	
	}
	/* End : Sumanth Kulkarni S
	 * Date: 14 Oct 2011
	 */

}