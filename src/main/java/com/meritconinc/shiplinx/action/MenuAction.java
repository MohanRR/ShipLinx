package com.meritconinc.shiplinx.action;


import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.ServletRequestAware;

import com.meritconinc.mmr.utilities.security.UserUtil;
import com.opensymphony.xwork2.Preparable;



/**
 * <code>Set welcome message.</code>
 */
public class MenuAction extends BaseAction implements Preparable,ServletRequestAware{
	private static final long serialVersionUID	= 25092007;

	private static final Logger log = LogManager.getLogger(MenuAction.class);
	private HttpServletRequest request;
	
	private String url;

	public String getUrl() {
		return url;
	}

	public String execute() throws Exception {
		log.debug("--------execute----MenuAction-----"); 
		url= /*request.getContextPath() + */request.getParameter("value");
		com.meritconinc.mmr.model.security.User user = UserUtil.getMmrUser();
		String menu = request.getParameter("menu");
		getSession().remove("HighLightMenu");
		getSession().put("HighLightMenu", menu);
		
		return "success";
	}
	
	
	public void setUrl(String url) {
		this.url = url;
	}

	public void prepare() throws Exception {
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}
}
