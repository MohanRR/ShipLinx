package com.meritconinc.mmr.service.impl;

import java.util.List;

import org.apache.log4j.Logger;

import com.meritconinc.mmr.dao.PropertyDAO;
import com.meritconinc.mmr.model.common.PropertyVO;
import com.meritconinc.mmr.service.PropertyService;

public class PropertyServiceImpl implements PropertyService {

	private static final Logger log = Logger.getLogger(PropertyServiceImpl.class);

	private PropertyDAO propertyDAO;

	public void setPropertyDAO(PropertyDAO propertyDAO) {
		this.propertyDAO = propertyDAO;
	}
	
	public String readProperty(String scope, String name){
		return propertyDAO.readProperty(scope, name);
	}

	public List<PropertyVO> readProperties(){
		return propertyDAO.readProperties();
	}
	
	public void updateProperty(String scope, String name, String value){
		propertyDAO.updateProperty(scope, name, value);
	}

	public String readTextProperty(String scope, String name){
		return propertyDAO.readTextProperty(scope, name);
	}
	
	public List<PropertyVO> readTextProperties(){
		return propertyDAO.readTextProperties();
	}
	
	public void updateTextProperty(String scope, String name, String value){
		propertyDAO.updateTextProperty(scope, name, value);
	}

}