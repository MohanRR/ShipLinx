/**
 * 
 */
package com.meritconinc.mmr.utilities;

import javax.servlet.ServletContext;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.meritconinc.mmr.utilities.context.ServletContextHolderListener;

/**
 * @author brinzf2
 *
 */
public class MmrBeanLocator{
	private static MmrBeanLocator beanLocator_ = new MmrBeanLocator();
	
	/**
	 * private constructor
	 *
	 */
	private MmrBeanLocator(){
	}
	
	/**
	 * Returns an instance of the MmrBeanLocator
	 * 
	 * @return
	 */
	public static MmrBeanLocator getInstance(){
		return beanLocator_;
	}
	
	/**
	 * Returns an instance of a bean
	 * 
	 * @param beanId - the id of the bean to be located
	 * @return
	 */
	public Object findBean(String beanId){
		//Initially we used org.apache.struts2.portlet.context.ServletContextHolderListener class
		// get the spring applicaiton context
		ServletContext servletContext = ServletContextHolderListener.getServletContext();
		if (servletContext == null) return null;
		WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		// look up the defined bean we're looking for
		return context.getBean(beanId);
	}

}