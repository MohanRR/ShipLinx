/**
 * 
 */
package com.meritconinc.mmr.service.acegi;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

/**
 * 
 * Listen for events from ACEGI
 * These events could be fired at different times during authentication/authorization process
 * Listener is registered for events internally, just bean with id "securityListener" 
 * in ACEGI configuration should to be specified as:
 * <bean id="securityListener" class="com.meritconinc.mmr.service.acegi.ApplicationSecurityListener"/>
 * By now it only cares about AuthenticationFailureBadCredentialsEvent event
 * Each event encapsulates Authentication object, which carries all information about user
 */
public class ApplicationSecurityListener implements ApplicationListener{
	private ApplicationContext ctx;
	/* (non-Javadoc)
	 * @see org.springframework.context.ApplicationListener#onApplicationEvent(org.springframework.context.ApplicationEvent)
	 *  event types: AuthorizedEvent, AuthenticationFailureBadCredentialsEvent, AuthenticationSuccessEvent, ContextRefreshedEvent
	 *  not currently used
	 */
	
	public void onApplicationEvent(ApplicationEvent event) {
		// put processing events here
	}
}