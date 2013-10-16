package com.meritconinc.shiplinx.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.meritconinc.shiplinx.model.Address;
import com.meritconinc.shiplinx.model.CarrierChargeCode;
import com.meritconinc.shiplinx.model.ChargeGroup;
import com.meritconinc.shiplinx.model.LtlPoundRate;
import com.meritconinc.shiplinx.model.LtlSkidRate;
import com.meritconinc.shiplinx.model.Markup;
import com.meritconinc.shiplinx.model.Zone;

public class MarkupManagerDAOImpl extends SqlMapClientDaoSupport implements MarkupManagerDAO {

	public List<Markup> getMarkupListForCustomer(Markup markup) {
		// TODO Auto-generated method stub
		if (markup != null) {
//			Map<String, Object> paramObj = new HashMap<String, Object>(5);
//			paramObj.put("customerId", markup.getCustomerId());
//			paramObj.put("businessId", UserUtil.getMmrUser().getBusinessId());
//			if (markup.getFromCountryCode() != null)
//				paramObj.put("fromCountryCode", markup.getFromCountryCode());
//			if (markup.getToCountryCode() != null)
//				paramObj.put("toCountryCode", markup.getToCountryCode());
//			if (markup.getServiceId() != null)
//				paramObj.put("serviceId", markup.getServiceId());
			
			List<Markup> markupList =  (List<Markup>) getSqlMapClientTemplate().queryForList("findMarkupListForCustomer", markup);
			return markupList;
		}
		return null;
	}

	public Double[] getFlatMarkups() {
		// TODO Auto-generated method stub
		List<Markup> markupList =  (List) getSqlMapClientTemplate().queryForList("getFlatMarkups");
		if (markupList != null) {
			Double [] fMarkups = new Double[markupList.size()];
			int i=0;
			for(Markup m:markupList) {
				if (m.getMarkupFlat() != null)
					fMarkups[i++] = m.getMarkupFlat();
			}
			
			return fMarkups;
		}
		return null;		
	}

	public Integer[] getPercentageMarkups() {
		// TODO Auto-generated method stub
		List<Markup> markupList =  (List) getSqlMapClientTemplate().queryForList("getPercentageMarkups");
		if (markupList != null) {
			Integer [] pMarkups = new Integer[markupList.size()];
			int i=0;
			for(Markup m:markupList) {
				if (m.getMarkupPercentage() != null)
					pMarkups[i++] = m.getMarkupPercentage();
			}
			
			return pMarkups;
		}
		return null;
	}

	@Override
	public void deleteMarkup(Markup markup) {
		// TODO Auto-generated method stub
		if (markup != null) {
			Map<String, Object> deleteMarkupParamObj = new HashMap<String, Object>(5);
			deleteMarkupParamObj.put("customerId", markup.getCustomerId());
			deleteMarkupParamObj.put("serviceId", markup.getServiceId());
			deleteMarkupParamObj.put("fromCountryCode", markup.getFromCountryCode());
			deleteMarkupParamObj.put("toCountryCode", markup.getToCountryCode());
			deleteMarkupParamObj.put("businessId", markup.getBusinessId());
	
			getSqlMapClientTemplate().delete("deleteMarkup", deleteMarkupParamObj);		
		}
	}

	@Override
	public void addMarkup(Markup markup) {
		// TODO Auto-generated method stub
		if (markup != null) {
			if (markup.getFromWeight() == null)
				markup.setFromWeight(0.0);
			if (markup.getToWeight() == null)
				markup.setToWeight(0.0);
			getSqlMapClientTemplate().insert("addMarkup", markup);
		}
	}

	@Override
	public void updateMarkup(Markup markup) {
		// TODO Auto-generated method stub
		if (markup != null) {
			Map<String, Object> updateMarkupParamObj = new HashMap<String, Object>(10);
			updateMarkupParamObj.put("customerId", markup.getCustomerId());
			updateMarkupParamObj.put("serviceId", markup.getServiceId());
			updateMarkupParamObj.put("fromCountryCode", markup.getFromCountryCode());
			updateMarkupParamObj.put("toCountryCode", markup.getToCountryCode());
			updateMarkupParamObj.put("businessId", markup.getBusinessId());
			updateMarkupParamObj.put("muPercent", markup.getMarkupPercentage());
			updateMarkupParamObj.put("muFlat", markup.getMarkupFlat());
			updateMarkupParamObj.put("disabled", markup.getDisabled());
			updateMarkupParamObj.put("fromWeight", markup.getFromWeight());
			updateMarkupParamObj.put("toWeight", markup.getToWeight());
	
			getSqlMapClientTemplate().insert("updateMarkup", updateMarkupParamObj);				
		}		
	}

	@Override
	public List<Markup> getMarkupListForUniqueMarkup(Markup markup) {
		// TODO Auto-generated method stub
		if (markup != null) {
//			Map<String, Object> paramObj = new HashMap<String, Object>(5);
//			paramObj.put("customerId", markup.getCustomerId());
//			paramObj.put("businessId", UserUtil.getMmrUser().getBusinessId());
//			if (markup.getFromCountryCode() != null)
//				paramObj.put("fromCountryCode", markup.getFromCountryCode());
//			if (markup.getToCountryCode() != null)
//				paramObj.put("toCountryCode", markup.getToCountryCode());
//			if (markup.getServiceId() != null)
//				paramObj.put("serviceId", markup.getServiceId());
			
			return getSqlMapClientTemplate().queryForList("findMarkupListForUniqueMarkup", markup);
		}
		return null;
	}

	@Override
	public void deleteCustomerMarkup(Long customerId, long businessId) {
		// TODO Auto-generated method stub
		if (customerId != null) {
			Map<String, Object> deleteMarkupParamObj = new HashMap<String, Object>(1);
			deleteMarkupParamObj.put("customerId", customerId);
			deleteMarkupParamObj.put("businessId", businessId);
			getSqlMapClientTemplate().delete("deleteCustomerMarkup", deleteMarkupParamObj);		
		}
	}
	
	public void disableOrEnableServicesForCustomer(long customerId, long[] serviceIds, boolean disable){
		Map<String, Object> paramObj = new HashMap<String, Object>(1);
		paramObj.put("customerId", customerId);
		paramObj.put("serviceIds", serviceIds);
		if(disable)
			paramObj.put("disable", new Boolean(true));
		else
			paramObj.put("disable", new Boolean(false));
		getSqlMapClientTemplate().delete("disableServicesForCustomer", paramObj);		
		
	}
	
	public List<CarrierChargeCode> searchCharges(CarrierChargeCode carrierChargeCode)
	{
		List<CarrierChargeCode> ccclst = (List)getSqlMapClientTemplate().queryForList("getChargesByCarrierId", carrierChargeCode);
		return ccclst;
	}
	
	public void deleteCharges(long chargeId)
	{
		Map<String, Object> paramObj = new HashMap<String, Object>(1);
		paramObj.put("chargeId", chargeId);
		getSqlMapClientTemplate().delete("deleteChargeById", paramObj);
	}
	
	public List<CarrierChargeCode> getCharges(CarrierChargeCode carrierChargeCode)
	{
		List<CarrierChargeCode> ccclst = (List)getSqlMapClientTemplate().queryForList("getChargesByChargeId", carrierChargeCode);
		return ccclst;
	}
	
	public List<ChargeGroup> getChargeGroups()
	{
		List<ChargeGroup> cglst = (List)getSqlMapClientTemplate().queryForList("getChargeGroups");
		return cglst;
	}
	
	public void addOrUpdateCharge(CarrierChargeCode carrierChargeCode, boolean add)
	{
		if(add)
			getSqlMapClientTemplate().delete("CreateCharge", carrierChargeCode);
		else
			getSqlMapClientTemplate().delete("EditCharge", carrierChargeCode);
	}

	@Override
	public void addPoundRateRecord(LtlPoundRate poundRate) {
		try {
			getSqlMapClientTemplate().insert("addLtlPoundRate", poundRate);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		}
	}

	@Override
	public void addSkidRateRecord(LtlSkidRate skidRate) {
		try {
			getSqlMapClientTemplate().insert("addLtlSkidRate", skidRate);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		}	
	}

	@Override
	public void deletePoundRateRecord(LtlPoundRate poundRate) {
		getSqlMapClientTemplate().delete("deleteLtlPoundRate", poundRate);
	}

	@Override
	public void deleteSkidRateRecord(LtlSkidRate skidRate) {
		getSqlMapClientTemplate().delete("deleteLtlSkidRate", skidRate);
	}
	
	@SuppressWarnings("all")
	@Override
	public LtlPoundRate getPoundRate(LtlPoundRate poundRateTobeSearched) {
		List<LtlPoundRate> lprList = (List) getSqlMapClientTemplate().queryForList("getLtlPoundRate", poundRateTobeSearched);
		if (lprList != null && lprList.size() > 0)
			return lprList.get(0);
		return null;
	}

	@SuppressWarnings("all")
	@Override
	public LtlPoundRate getPoundRate(LtlPoundRate poundRateTobeSearched, Double totalWeight) {
		if (totalWeight != null)
			poundRateTobeSearched.setRangeFrom(totalWeight.intValue()); // Setting it in RangeFrom property to compare in ranges
		List<LtlPoundRate> lprList = (List) getSqlMapClientTemplate().queryForList("getLtlPoundRateByRange", poundRateTobeSearched);
		if (lprList != null && lprList.size() > 0)
			return lprList.get(0);
		return null;
	}
	@SuppressWarnings("all")
	@Override
	public LtlSkidRate getSkidRate(LtlSkidRate skidRateTobeSearched) {
		List<LtlSkidRate> lsrList = (List) getSqlMapClientTemplate().queryForList("getLtlSkidRate", skidRateTobeSearched);
		if (lsrList != null && lsrList.size() > 0)
			return lsrList.get(0);
		return null;
	}

	@SuppressWarnings("all")
	@Override
	public Zone findZone(Long zoneStructureId, Address address) {
		if (zoneStructureId != null && address != null) {
			Map<String, Object> paramObj = new HashMap<String, Object>(5);
			paramObj.put("zoneStructureId", zoneStructureId);
			paramObj.put("postalCode", address.getPostalCode());
			paramObj.put("provinceCode", address.getProvinceCode());
			paramObj.put("countryCode", address.getCountryCode());
			paramObj.put("cityName", address.getCity());
					
//			List<Zone> listZone = (List) getSqlMapClientTemplate().queryForList("findZoneByPostalCode", paramObj);
//			if (listZone == null || listZone.size() == 0) {
			List<Zone> listZone = (List) getSqlMapClientTemplate().queryForList("findZoneByCity", paramObj);
			if (listZone != null && listZone.size() > 0)
				return listZone.get(0);
//			} else {
//				return listZone.get(0);
//			}
		}
		return null;
	}
	
	public Zone findZoneByPostalCode(Long zoneStructureId, Address address) {
		if (zoneStructureId != null && address != null) {
			Map<String, Object> paramObj = new HashMap<String, Object>(5);
			paramObj.put("zoneStructureId", zoneStructureId);
			paramObj.put("countryCode", address.getCountryCode());
			paramObj.put("postalCode", address.getPostalCode());
//			List<Zone> listZone = (List) getSqlMapClientTemplate().queryForList("findZoneByPostalCode", paramObj);
//			if (listZone == null || listZone.size() == 0) {
			List<Zone> listZone = (List) getSqlMapClientTemplate().queryForList("findZoneByPostalCode", paramObj);
			if (listZone != null && listZone.size() > 0)
				return listZone.get(0);
//			} else {
//				return listZone.get(0);
//			}
		}
		return null;
	}


	@Override
	public Zone addZone(Zone zone) {
		if (zone != null) {
			Long id = (Long) getSqlMapClientTemplate().insert("addZone", zone);
			zone.setZoneId(id);
		}
		
		return zone;
	}

}