package com.meritconinc.mmr.utilities.mail;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * @author Florin
 *
 * The class Authenticator represents an object that knows how to obtain 
 * authentication for a network connection. Usually, it will do this by prompting the user 
 * for information. 
 */

public class SmtpAuthenticator extends Authenticator {
	private String username;
	private String password;

	/**
	 * SmtpAuthenticator constructor 
	 * 
	 * @param username
	 * @param password
	 */
	public SmtpAuthenticator(String username, String password) {
		this.username=username;
		this.password=password;
	}
  
	/**
	 * Called when password authentication is needed. 
	 * 
	 * @return The PasswordAuthentication collected from the user, or null if none is provided. 
	 */
  	public PasswordAuthentication getPasswordAuthentication() {

		return new PasswordAuthentication(username, password);
  	}
}