package com.meritconinc.shiplinx.service;

import java.util.List;

import com.meritconinc.shiplinx.model.Business;
import com.meritconinc.shiplinx.model.Customer;
import com.meritconinc.shiplinx.model.CustomerCarrier;
import com.meritconinc.shiplinx.model.Pickup;

public interface PickupManager{
	
	public Long add(Pickup pickup);	
	List<Pickup> findAddresses(Pickup pickup);
	public Pickup getPickupById(long pickupId);
	public void updatePickup(Pickup pickup);
	public Pickup getPickupByOrderId(long orderId);
	public List<Pickup> getPickups(Pickup pickup);
}