package com.meritconinc.shiplinx.dao;

import java.util.List;

import com.meritconinc.shiplinx.model.Address;
import com.meritconinc.shiplinx.model.Pickup;

public interface PickupDAO{
	
	public Long add(Pickup pickup);	
	List<Pickup> findAddresses(Pickup pickup);
	public Pickup getPickupById(long pickupId);
	public void updatePickup(Pickup pickup);
	public Pickup getPickupByOrderId(long orderId);
	public List<Pickup> getPickups(Pickup pickup);

}