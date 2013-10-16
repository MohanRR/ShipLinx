package com.meritconinc.shiplinx.dao;

import java.util.List;

import com.meritconinc.shiplinx.model.Address;
import com.meritconinc.shiplinx.model.Country;
import com.meritconinc.shiplinx.model.Province;

public interface AddressDAO{
	
	List<Address> findaddressesByCustomer(Long custId);
	public Long add(Address addressbook);
	List<String> getCanadianProvinces();
	List<String> getUSStates();
	List<Country> getCountries();
	public void delete(String id);
	public void edit(Address addressbook);
	public Address findAddressById(String id);
	
	
	List<String> findDistributionListForCustomer(Long CustomerId);
	public void addDistributionList(Address addressbook);
	public void deleteDistributionList(String id, Long customerId);
	List<Province> getProvinces(String country);
	Province getProvince(String provinceCode);
	public Address findDefaultToAddressForCustomer(Long customerId);
	public Address findDefaultFromAddressForCustomer(Long customerId);

	Address findToAddressForOrder(Long customerId);
	public Address findFromAddressForOrder(Long customerId);
	
	List<Address> findAddresses(Address addressBook);
	
	List<Address> searchAddresses(Address addressBook);

}