package com.meritconinc.mmr.action.admin;

import javax.servlet.ServletContext;

import org.apache.struts2.util.ServletContextAware;

import com.meritconinc.mmr.action.BaseAction;
import com.meritconinc.mmr.service.SystemService;

public class SystemResourceAction extends BaseAction implements ServletContextAware {
	private ServletContext servletContext;
	private SystemService systemService;
		
	public SystemService getSystemService() {
		return systemService;
	}

	public void setSystemService(SystemService systemService) {
		this.systemService = systemService;
		if ( this.servletContext != null ) {
			this.systemService.setServletContext(this.servletContext);
		}
	}
	
	public String view() {
		return SUCCESS;
	}

	public String clearCache() {		
		systemService.clearCache();
		addActionMessage("Cache has been refreshed on server " + systemService.getServerName());
		return SUCCESS;
	}

	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
		if ( systemService != null ) {
			this.systemService.setServletContext(servletContext);			
		}
	}
	
		 
}
