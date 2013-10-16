package com.meritconinc.shiplinx.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.meritconinc.shiplinx.dao.PickupDAO;
import com.meritconinc.shiplinx.model.Pickup;
import com.meritconinc.shiplinx.utils.ShiplinxConstants;

public class PickupDAOImpl extends SqlMapClientDaoSupport implements PickupDAO{
	private static final Logger log = LogManager.getLogger(PickupDAOImpl.class);

	public Long add(Pickup pickup) {
		Long id = (Long) getSqlMapClientTemplate().insert("addPickup",pickup);
		return id;
	}

	public List<Pickup> findAddresses(Pickup pickup) {
		List<Pickup> pickups = new ArrayList();
		return pickups;
	}
	
	public Pickup getPickupById(long pickupId){
		return (Pickup)getSqlMapClientTemplate().queryForObject("getPickupById", pickupId);		
	}

	public Pickup getPickupByOrderId(long orderId){
		Map<String, Object> paramObj = new HashMap<String, Object>();
		paramObj.put("orderId", orderId);
		paramObj.put("status", ShiplinxConstants.STATUS_PICKUP_ACTIVE);
		return (Pickup)getSqlMapClientTemplate().queryForObject("findPickupByOrderId", orderId);		
	}
	
	public List<Pickup> getPickups(Pickup pickup){
		List<Pickup> pickupList = new ArrayList<Pickup>();
		pickupList = (List<Pickup>)getSqlMapClientTemplate().queryForList("findPickups", pickup);	
		return pickupList;
		
	}
	
	

	public void updatePickup(Pickup pickup){
		try{
			getSqlMapClientTemplate().update("updatePickup", pickup);			
		}catch(Exception e){
			e.printStackTrace();
		}

	}


}