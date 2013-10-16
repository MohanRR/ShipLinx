package com.meritconinc.shiplinx.model;

import java.util.Date;

import com.meritconinc.shiplinx.utils.ShiplinxConstants;


public class Business {
	
	private long id;
	private String name;
	private String shortCode;
	private String systemName;
	private String logoFileName;
	private String logoHiResFileName;
	private String logoURL;
	private long addressId;
	private boolean active;
	private Date dateCreated;
	private String subdomain;
	private String logoutURL;
	private String supportURL;
	
	private Address address;
	
	private String headerKey;
	
	private String smtpHost;
	private String smtpUsername;
	private String smtpPassword;
	private int smtpPort;

	private String termsURL;
	private String timeZone;
	private Integer defaultNetTerms;
	private double defaultCreditLimit;
	
	private String emailHeaderImage;
	private String taxInfo;
	
	private String financeEmail;
	
	private String warehouseEmail;
	
	private String invoiceNotificationBody;
	private String customerNotificationBody;
	private String customerNotificationSubject;
	private String ratesNotificationBody;
	
	private String orderNotificationBody;
	private String shipOrderNotificationBody;
	private String shipOrderNotificationSubject;
	
	private String invoicingTemplate;
	
	private long businessCarrierId;
	
	private int defaultPaymentTypeLevel;
	
	//Credit Card Information storage
	private boolean storeCC;
	
	//Signup Page
	private String signupJSP;
	
	//css style
	private String businessCSS;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getShortCode() {
		return shortCode;
	}
	public void setShortCode(String shortCode) {
		this.shortCode = shortCode;
	}
	public String getLogoFileName() {
		return logoFileName;
	}
	public void setLogoFileName(String logoFileName) {
		this.logoFileName = logoFileName;
	}
	public String getLogoHiResFileName() {
		return logoHiResFileName;
	}
	public void setLogoHiResFileName(String logoHiResFileName) {
		this.logoHiResFileName = logoHiResFileName;
	}
	public long getAddressId() {
		return addressId;
	}
	public void setAddressId(long addressId) {
		this.addressId = addressId;
	}
	public Address getAddress() {
		return address;
	}
	public void setAddress(Address address) {
		this.address = address;
	}
	/**
	 * @return the active
	 */
	public boolean isActive() {
		return active;
	}
	/**
	 * @param active the active to set
	 */
	public void setActive(boolean active) {
		this.active = active;
	}
	/**
	 * @return the dateCreated
	 */
	public Date getDateCreated() {
		return dateCreated;
	}
	/**
	 * @param dateCreated the dateCreated to set
	 */
	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}
	
	/**
	 * 
	 * @return the HeaderKey
	 */
	public String getHeaderKey() {
		return headerKey;
	}
	
	/**
	 * 
	 * @param headerKey the headerkey to be set
	 */
	public void setHeaderKey(String headerKey) {
		this.headerKey = headerKey;
	}
	public String getLogoutURL() {
		return logoutURL;
	}
	public void setLogoutURL(String logoutURL) {
		this.logoutURL = logoutURL;
	}
	public String getSupportURL() {
		return supportURL;
	}
	public void setSupportURL(String supportURL) {
		this.supportURL = supportURL;
	}
	public String getSmtpHost() {
		return smtpHost;
	}
	public void setSmtpHost(String smtpHost) {
		this.smtpHost = smtpHost;
	}
	public String getSmtpUsername() {
		return smtpUsername;
	}
	public void setSmtpUsername(String smtpUsername) {
		this.smtpUsername = smtpUsername;
	}
	public String getSmtpPassword() {
		return smtpPassword;
	}
	public void setSmtpPassword(String smtpPassword) {
		this.smtpPassword = smtpPassword;
	}
	public int getSmtpPort() {
		return smtpPort;
	}
	public void setSmtpPort(int smtpPort) {
		this.smtpPort = smtpPort;
	}
	
	public String getSubdomain() {
		return subdomain;
	}
	public void setSubdomain(String subdomain) {
		this.subdomain = subdomain;
	}
	public String getTermsURL() {
		return termsURL;
	}
	public void setTermsURL(String termsURL) {
		this.termsURL = termsURL;
	}
	public String getTimeZone() {
		return timeZone;
	}
	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}
	public Integer getDefaultNetTerms() {
		return defaultNetTerms;
	}
	public void setDefaultNetTerms(Integer defaultNetTerms) {
		this.defaultNetTerms = defaultNetTerms;
	}
	public double getDefaultCreditLimit() {
		return defaultCreditLimit;
	}
	public void setDefaultCreditLimit(double defaultCreditLimit) {
		this.defaultCreditLimit = defaultCreditLimit;
	}
	public String getEmailHeaderImage() {
		return emailHeaderImage;
	}
	public void setEmailHeaderImage(String emailHeaderImage) {
		this.emailHeaderImage = emailHeaderImage;
	}
	public String getTaxInfo() {
		return taxInfo;
	}
	public void setTaxInfo(String taxInfo) {
		this.taxInfo = taxInfo;
	}
	public String getSystemName() {
		return systemName;
	}
	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}
	public String getFinanceEmail() {
		return financeEmail;
	}
	public void setFinanceEmail(String financeEmail) {
		this.financeEmail = financeEmail;
	}
	public String getInvoiceNotificationBody() {
		return invoiceNotificationBody;
	}
	public void setInvoiceNotificationBody(String invoiceNotificationBody) {
		this.invoiceNotificationBody = invoiceNotificationBody;
	}
	public String getCustomerNotificationBody() {
		return customerNotificationBody;
	}
	public void setCustomerNotificationBody(String customerNotificationBody) {
		this.customerNotificationBody = customerNotificationBody;
	}
	public String getRatesNotificationBody() {
		return ratesNotificationBody;
	}
	public void setRatesNotificationBody(String ratesNotificationBody) {
		this.ratesNotificationBody = ratesNotificationBody;
	}
	public String getOrderNotificationBody() {
		return orderNotificationBody;
	}
	public void setOrderNotificationBody(String orderNotificationBody) {
		this.orderNotificationBody = orderNotificationBody;
	}
	public String getWarehouseEmail() {
		return warehouseEmail;
	}
	public void setWarehouseEmail(String warehouseEmail) {
		this.warehouseEmail = warehouseEmail;
	}
	public String getInvoicingTemplate() {
		return invoicingTemplate;
	}
	public void setInvoicingTemplate(String invoicingTemplate) {
		this.invoicingTemplate = invoicingTemplate;
	}
	public long getBusinessCarrierId() {
		return businessCarrierId;
	}
	public void setBusinessCarrierId(long businessCarrierId) {
		this.businessCarrierId = businessCarrierId;
	}
	
	public String getDefaultCurreny(){
		if(address.getCountryCode().equalsIgnoreCase(ShiplinxConstants.US))
			return "USD";
		return "CAD";

	}
	public String getCustomerNotificationSubject() {
		return customerNotificationSubject;
	}
	public void setCustomerNotificationSubject(String customerNotificationSubject) {
		this.customerNotificationSubject = customerNotificationSubject;
	}
	
	public int getDefaultPaymentTypeLevel() {
		return defaultPaymentTypeLevel;
	}
	public void setDefaultPaymentTypeLevel(int defaultPaymentTypeLevel) {
		this.defaultPaymentTypeLevel = defaultPaymentTypeLevel;
	}
	public boolean isStoreCC() {
		return storeCC;
	}
	public void setStoreCC(boolean storeCC) {
		this.storeCC = storeCC;
	}
	public String getShipOrderNotificationBody() {
		return shipOrderNotificationBody;
	}
	public void setShipOrderNotificationBody(String shipOrderNotificationBody) {
		this.shipOrderNotificationBody = shipOrderNotificationBody;
	}
	public String getShipOrderNotificationSubject() {
		return shipOrderNotificationSubject;
	}
	public void setShipOrderNotificationSubject(String shipOrderNotificationSubject) {
		this.shipOrderNotificationSubject = shipOrderNotificationSubject;
	}
	public String getSignupJSP() {
		return signupJSP;
	}
	public void setSignupJSP(String signupJSP) {
		this.signupJSP = signupJSP;
	}
	public String getBusinessCSS() {
		return businessCSS;
	}
	public void setBusinessCSS(String businessCSS) {
		this.businessCSS = businessCSS;
	}
	public String getLogoURL() {
		return logoURL;
	}
	public void setLogoURL(String logoURL) {
		this.logoURL = logoURL;
	}
	
	
}