package com.meritconinc.shiplinx.action;

import java.io.InputStream;
import java.io.StringBufferInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.meritconinc.mmr.exception.CardProcessingException;
import com.meritconinc.mmr.exception.CustomerNameAlreadyTakenException;
import com.meritconinc.mmr.exception.UsernameAlreadyTakenException;
import com.meritconinc.mmr.model.admin.UserSearchCriteria;
import com.meritconinc.mmr.utilities.Common;
import com.meritconinc.mmr.utilities.MessageUtil;
import com.meritconinc.mmr.utilities.StringUtil;
import com.meritconinc.mmr.utilities.security.UserUtil;
import com.meritconinc.shiplinx.carrier.purolator.stub.ServiceAvailabilityWebServiceClient;
import com.meritconinc.shiplinx.exception.ShiplinxException;
import com.meritconinc.shiplinx.model.Address;
import com.meritconinc.shiplinx.model.Business;
import com.meritconinc.shiplinx.model.Carrier;
import com.meritconinc.shiplinx.model.CreditCard;
import com.meritconinc.shiplinx.model.Customer;
import com.meritconinc.shiplinx.model.CustomerCarrier;
import com.meritconinc.shiplinx.model.CustomerSalesUser;
import com.meritconinc.shiplinx.model.Province;
import com.meritconinc.shiplinx.service.CaptchaServiceSingleton;
import com.meritconinc.shiplinx.utils.ShiplinxConstants;
import com.meritconinc.shiplinx.utils.TimeZoneBean;
import com.meritconinc.shiplinx.utils.TimeZonesFactory;
import com.octo.captcha.service.CaptchaServiceException;
import com.opensymphony.xwork2.ActionContext;


/**
 * <code>Set welcome message.</code>
 */
public class CustomerManagerAction extends CustomerManagerBaseAction {
	private static final long serialVersionUID	= 25092007;
	private static final Logger log = LogManager.getLogger(CustomerManagerAction.class);
	private List<Customer> customerList;
//	private String businessName;
//	private HashMap hashMapAccounhtInfo;
	private List<CustomerCarrier> customerCarrierAccountList;
	private List<Carrier> carrierList;
	private List<Carrier> carrierListSelected;
	
	private List<Boolean> select;
	
	public HttpServletRequest request;
	private List<Province> provinces;
	
	private String signupJSP;
	
	private InputStream inputStream;
	public InputStream getInputStream() {
	return inputStream;
	}
	public void setInputStream(InputStream inputStream) {
	this.inputStream = inputStream;
	}
	
	public List<Boolean> getSelect() {
		return select;
	}


	public void setSelect(List<Boolean> select) {
		this.select = select;
	}


	public String execute() throws Exception {
		Customer customer=this.getCustomer();
		
		log.debug("CHECK BUSINESS NAME IN EXECUTE -------------"+customer.getName());
		return SUCCESS;
	}


	public List<Customer> getCustomerList(){
		return customerList;
	}
		
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	
	public String list() throws Exception {
		String strmethod = request.getParameter("method");
		initialize();
		Customer customer = getCustomer();
		
		if(UserUtil.getMmrUser().getUserRole().equals(ShiplinxConstants.ROLE_SALES))
		{
			customer.setSalesAgent(UserUtil.getMmrUser().getUsername());
		}
		
		//if the admin belongs to a branch, then show only the branch customers
		if(!StringUtil.isEmpty(UserUtil.getMmrUser().getBranch()))
		{
			customer.setBranch(UserUtil.getMmrUser().getBranch());
		}

		if(strmethod==null || strmethod.equalsIgnoreCase("edit")){
			customer.setBusinessId(UserUtil.getMmrUser().getBusinessId());
			customerList = getService().search(customer);
		}
		else{
			customerList = null;
			setCustomer(new Customer());
		}
		getSession().remove("edit");
		getSession().remove("ActiveCC");
		log.debug("ROLE OF THE USER IS::::::::::::::::::::::::::::::::::::::"+UserUtil.getMmrUser().getUserRole());
//		getSession().put("CUSTOMERLISTSIZE",customerList.size());
		//if (!(customerList.size() != 0))
		//	addActionError(getText("error.no.address.found"));
		return SUCCESS;
	}
	
	
	public String init() throws Exception {
		try {
			getSession().remove("customer");
			getSession().remove("edit");
			getSession().remove("ActiveCC");
			initialize();
//			getSession().remove("edit");
//			String country = "US";
			request.setAttribute("add", "true");
		} catch (Exception e) {
			addActionError(getText("error.timeZones"));
		}
		return SUCCESS;
	}

	public String signup() throws Exception {

		//first we need to determine the business 
		
//		String url = request.getRequestURL().toString();
//		Business b = null;
//		
//		//find a better way to query business table 
//		List<Business> businesses = businessService.search(b);		
//		for(Business bus: businesses){
//			if(bus.getSubdomain()!=null && url.contains(bus.getSubdomain())){
//				b = bus;
//				break;
//			}
//		}
		
		Business b = (Business)getSession().get(ShiplinxConstants.BUSINESS);
		Customer customer = new Customer();
		customer.setBusinessId(b.getId());
		customer.getAddress().setCountryCode(b.getAddress().getCountryCode());
		customer.getAddress().setProvinceCode(b.getAddress().getProvinceCode());
		customer.setTimeZone(b.getTimeZone());
//		customer.setPaymentType(ShiplinxConstants.PAYMENT_TYPE_CREDIT_CARD);
		customer.setPaymentType(ShiplinxConstants.PAYMENT_TYPE_ON_CREDIT);
		customer.setActive(true);
		
		this.setCustomer(customer);

		//for now enable all carriers associated with the business
		customer.setCustomerSelectedCarriers(carrierServiceManager.getCarriersForBusiness(customer.getBusinessId()));

		CreditCard cc = new CreditCard();
		setCreditCard(cc);
		
		//setting the signup jsp for this business
		signupJSP = b.getSignupJSP();
		getSession().put("CountryList", MessageUtil.getCountriesList());
		getSession().put("provinces", addressService.getProvinces(getCustomer().getAddress().getCountryCode()));
		List<TimeZoneBean> timeZoneList =(List<TimeZoneBean>) TimeZonesFactory.getSupportedTimeZones();
		getSession().put("timeZones", timeZoneList);
		
		return INPUT;
	}

	public String doSignup() throws Exception {
		Boolean isResponseCorrect = Boolean.FALSE;
		//remenber that we need an id to validate!
		String captchaId = request.getSession().getId();
		//retrieve the response
		String turing = request.getParameter("turing");
		
		boolean breturn = true;
		Business b = (Business)getSession().get(ShiplinxConstants.BUSINESS);
		signupJSP = b.getSignupJSP();
		// Call the Service method
		try {
			isResponseCorrect = CaptchaServiceSingleton.getInstance()
					.validateResponseForID(captchaId, turing);
		} catch (CaptchaServiceException e) {
			//should not happen, may be thrown if the id is not valid 
			e.printStackTrace();
		}
		
		if(!isResponseCorrect){
			addActionError(getText("error.captcha.noMatch"));
			return INPUT;
			
		}
		
		//When sign up occurs make sure we determine if there is a user code associated with this sign up
		String code = (String)ActionContext.getContext().getSession().get(ShiplinxConstants.USER_CODE);
		if(StringUtil.isEmpty(code)){
			code = Common.getCookie(ShiplinxConstants.USER_CODE);
			request.getSession().setAttribute(ShiplinxConstants.USER_CODE, code);
		}

		
		//now try and add the customer
		try {
			Customer customer = this.getCustomer();
			customer.setOnlineSignup(true);
			CreditCard cc = this.getCreditCard();
			if(StringUtil.isEmpty(cc.getCcNumber()))//will be null for noCC
			{
				addActionError(getText("credit.card.number.required"));
				breturn = false;
			}
			if(StringUtil.isEmpty(cc.getCvd()))//Will be null for noCC
			{
				addActionError(getText("credit.card.cvd.required"));
				breturn = false;
			}
			if(cc.getBillingAddress()!=null) //will not be null for both cases
			{
				//contact name
				if(StringUtil.isEmpty(cc.getBillingAddress().getContactName()))//Will be null for noCC
				{
					addActionError(getText("credit.card.billingaddress.contactname.required"));
					breturn = false;
				}
				//address1
				if(StringUtil.isEmpty(cc.getBillingAddress().getAddress1()))//Will be null for noCC
				{
					addActionError(getText("credit.card.billingaddress.address1.required"));
					breturn = false;
				}
				if(StringUtil.isEmpty(cc.getBillingAddress().getCity()))//Will be null for noCC
				{
					addActionError(getText("credit.card.billingaddress.city.required"));
					breturn = false;
				}
				if(StringUtil.isEmpty(cc.getBillingAddress().getPostalCode()))//Will be null for noCC
				{
					addActionError(getText("credit.card.billingaddress.postalcode.required"));
					breturn = false;
				}
			}
			if(!breturn)
				return INPUT;
			getService().add(customer, cc);
		} catch (UsernameAlreadyTakenException ue) {
			addActionError(getText("error.username.taken"));
			// addActionMessage(getText("error.username.taken"));
			return INPUT;
		} 
		catch (CustomerNameAlreadyTakenException ce) {
			addActionError(getText("error.customername.taken"));
			return INPUT;
		} catch (CardProcessingException cpe) {
			addActionError(MessageUtil.getMessage("error.creditcard.cannotAuth"));
			// addActionMessage(getText("error.customername.taken"));
			return INPUT;
		}
		catch (Exception e) {
			addActionError(getText("customer.account.notCreated"));
			return INPUT;
		}
		
		addActionMessage(getText("customer.registration.successful"));
		return SUCCESS;
		
	}

	public String doSignupNoCC() throws Exception {
		Boolean isResponseCorrect = Boolean.FALSE;
		//remenber that we need an id to validate!
		String captchaId = request.getSession().getId();
		//retrieve the response
		String turing = request.getParameter("turing");
		
		boolean breturn = true;
		Business b = (Business)getSession().get(ShiplinxConstants.BUSINESS);
		signupJSP = b.getSignupJSP();
		// Call the Service method
		try {
			isResponseCorrect = CaptchaServiceSingleton.getInstance()
					.validateResponseForID(captchaId, turing);
		} catch (CaptchaServiceException e) {
			//should not happen, may be thrown if the id is not valid 
			e.printStackTrace();
		}
		
		if(!isResponseCorrect){
			addActionError(getText("error.captcha.noMatch"));
			return INPUT;
			
		}
		
		//now try and add the customer
		try {
			Customer customer = this.getCustomer();
			customer.setOnlineSignup(true);
			if(!breturn)
				return INPUT;
			getService().add(customer, null);
		} catch (UsernameAlreadyTakenException ue) {
			addActionError(getText("error.username.taken"));
			// addActionMessage(getText("error.username.taken"));
			return INPUT;
		} 
		catch (CustomerNameAlreadyTakenException ce) {
			addActionError(getText("error.customername.taken"));
			return INPUT;
		} catch (CardProcessingException cpe) {
			addActionError(MessageUtil.getMessage("error.creditcard.cannotAuth"));
			// addActionMessage(getText("error.customername.taken"));
			return INPUT;
		}
		catch (Exception e) {
			addActionError(getText("customer.account.notCreated"));
			return INPUT;
		}
		
		addActionMessage(getText("customer.registration.successful"));
		return SUCCESS;
		
	}

	public String save() throws Exception {
		try {
			Customer customer = this.getCustomer();
			carrierListSelected = new ArrayList<Carrier>(); // list to capture only selected carriers.
			carrierList = (List<Carrier>) getSession().get("CARRIERS");
			for ( int i = 0; i < select.size(); i++ ) 
			{
			    // If this checkbox was selected:
			    if ( select.get(i) != null && select.get(i) ) 
			    {
			    	carrierListSelected.add(carrierList.get(i)); // Add only the selected carriers by the customer.
			    }
			}
			//Set the selected Carriers to the Customer Object
			customer.setCustomerSelectedCarriers(carrierListSelected);
			
			//if the user belongs to a branch, then the customer being created goes into that branch
			if(!StringUtil.isEmpty(UserUtil.getMmrUser().getBranch()))
				customer.setBranch(UserUtil.getMmrUser().getBranch());
			if(StringUtil.isEmpty(customer.getDefaultCurrency()))
				customer.setDefaultCurrency("");
			getService().add(customer, null);
		} catch (UsernameAlreadyTakenException ue) {
			addActionError(getText("error.username.taken"));
			// addActionMessage(getText("error.username.taken"));
			return INPUT;
		} catch (CustomerNameAlreadyTakenException ce) {
			addActionError(getText("error.customername.taken"));
			// addActionMessage(getText("error.customername.taken"));
			return INPUT;
		}
		catch (Exception e) {
			addActionError(getText("customer.account.notCreated"));
			return INPUT;
		}
		addActionMessage(getText("customer.save.successfully"));
		return SUCCESS;
	}
	
	public String listProvince() throws Exception {
		String country;
		country = request.getParameter("value");
		if(country == null || "".equals(country))
			country = "US";
		provinces=addressService.getProvinces(country);
		
		getSession().put("provinces", provinces);
		return SUCCESS;
	}
	
	
	public String saveCarrier(){
		Customer customer=this.getCustomer();
		
		log.debug("SAVE CARRIERS  CHECK BUSI-----------------"+customer.getName());		
		
		CustomerCarrier customercarrier=this.getCustomerCarrier();
		List<com.meritconinc.shiplinx.model.CustomerCarrier> carriers=customer.getCarriers();
		carriers.add(customercarrier);
		customer.setCarriers(carriers);
		addActionMessage(getText("customeraccount.info.save.successfully"));
		return SUCCESS;
	}
	
	public String info() {
		try {
			String id = request.getParameter("id");
			long customerId= 0L;
			if(id!=null)
			{
				customerId = Long.valueOf(id);
			}
			else
				// This is the logic implementation if the user clicks on 'My Account' where the id is not passed as param.
			{
				customerId = getLoginUser().getCustomerId();
			}
			this.setCustomer(getService().getCustomerInfoByCustomerId(customerId));
			initialize();

			getSession().put("oldBusinessName", this.getCustomer().getName());
			getSession().put("edit", "true");
			
			//Check if the customer has an active credit card?
			if(this.getCustomer().getCreditCardActive()!=null && this.getCustomer().getCreditCardActive().isActive() && request.getParameter("ajax")==null)
			{
				getSession().put("ActiveCC", "true");
				//setting the req to check if the customer has an active credit card 
				//then display the details of the active credit card.
				request.setAttribute("active_cc", "true");
			}
//			Address ad = new Address();
//			ad.setProvinceCode(getCustomer().getAddress().getProvinceCode());
//			setAddress(ad);

		} catch (Exception e) {
			addActionError(getText("error.timeZones"));
		}
		return SUCCESS;
	}
	
	public String getAccountInfo() {
		log.debug("-----getAccountInfo------");
		try {
			initialize();
			Customer customer = this.getCustomer();
			String id = request.getParameter("id");
			if (id != null) {
				long customerId = Long.valueOf(id);
				customer = getService().getCustomerInfoByCustomerId(customerId);
				this.setCustomer(customer);
			} else {
				String method = this.request.getParameter("method");
				if (method != null && method.equals("reset")) {
					this.getCustomer().setCustomerCarrier(new CustomerCarrier());
				}
			}
			getSession().remove("edit");
			customerCarrierAccountList =  getCarrierServiceManager().getAllCutomerCarrier(UserUtil.getMmrUser().getBusinessId(), customer.getId());
		}catch (Exception e) {
	    	e.printStackTrace();
		}
		return SUCCESS;
	}
	
	public String saveCarrierAccount() {
		log.debug("-----saveCarrierAccount------");
//		long customerId =0;
//		String username=request.getParameter("name");
		CustomerCarrier customerCarrier;
		
		try {
			
			getSession().remove("edit");
			
			Customer customer = this.getCustomer();
			String country  =customer.getCustomerCarrier().getCountry();
			//TODO get carrier id from database according to name
			//Long carrierId = customer.getCustomerCarrier().getCarrierName().equalsIgnoreCase("Federal Express")? 1L : 2L;
			
			//if the customer account is selected as default 
			if(customer.getCustomerCarrier().isDefaultAccount()){
				//set other account of that customer for selected country and carrier as default = false
				//and the selected account will be now as default
				
				getService().setOtherCarrierAccountAsFalse(customer.getId(),country,
						customer.getCustomerCarrier().getCarrierId());
			}
			
			customerCarrier = customer.getCustomerCarrier();
			customerCarrier.setCustomerId(customer.getId());
			customerCarrier.setCountry(country);
			customerCarrier.setBusinessId(UserUtil.getMmrUser().getBusinessId());
			
			String strCustomerCarrierId = request.getParameter("customerCarrierId");
			
			if(strCustomerCarrierId!=null && !"".equals(strCustomerCarrierId)){
				Long customerCarrierId = new Long(strCustomerCarrierId);
				getService().editCustomerCarrier(customerCarrier,customerCarrierId);
				//return "SUCCESS_EDIT";
			}
			else{
				getService().saveCustomerCarrier(customerCarrier);
			}
			
		}

		catch (ShiplinxException e) {
	    	addActionError(getText("customerCarrier.accountRegistered", new String[]{e.getMessage()}));
	    	return INPUT;
		}
		catch (Exception e) {
	    	e.printStackTrace();
	    	addActionError(getText("error.customerAcount.save"));
		}
		    
	    addActionMessage(getText("success.customerAcount.save"));
		return getAccountInfo();
	}
	
	public String editCarrierAccount() {
		log.debug("-----editCarrierAccount------");
//		long customerId =0;
		CustomerCarrier customerCarrier;
		
		try {
			//carrierList = carrierServiceManager.getCarriers();
//			customerId = getLoginUser().getCustomerId();
			Customer customer = this.getCustomer();
			
			Long customerCarrierId = new Long(request.getParameter("id"));
			customerCarrier = getService().getCustomerCarrierInfoById(customer.getId(),customerCarrierId);
			
			customer.setCustomerCarrier(customerCarrier);
			
			request.setAttribute("customerCarrierId", customerCarrierId);
			
			customerCarrierAccountList =  getCarrierServiceManager().getAllCutomerCarrier(UserUtil.getMmrUser().getBusinessId(), customer.getId());

			getSession().put("edit", "true");
			
	    }catch (Exception e) {
	    	addActionError(getText("error.customerAcount.edit"));
		}
		return SUCCESS;
	}
	
	public String deleteCarrierAccount() {
		
		String id=request.getParameter("id");
		CustomerCarrier customerCarrier;
		
		try {
			//carrierList = carrierServiceManager.getCarriers();
			getService().deleteCustomerCarrier(id);
			
	    }catch (Exception e) {
	    	addActionError(getText("error.customerAcount.delete"));
		}
		return SUCCESS;
	}
	
	public String edit() throws Exception {
		boolean error = false;
		try {
			
			Customer customer = getCustomer();
//			Address ad = getAddress();
//			customer.getAddress().setProvinceCode(ad.getProvinceCode());
			getService().edit(customer, (String) getSession().get("oldBusinessName"));
			getSession().remove("oldBusinessName");
			getSession().remove("edit");
			this.setCustomer(getService().getCustomerInfoByCustomerId(customer.getId()));
		} catch (CustomerNameAlreadyTakenException ce) {
			addActionError(getText("error.customername.taken"));
			// addActionMessage(getText("error.customername.taken"));
			error=true;
		}
		catch (CardProcessingException ce) {
			addActionError(MessageUtil.getMessage("error.creditcard.cannotAuth"));
			// addActionMessage(getText("error.customername.taken"));
			error=true;
		}
		catch (Exception e)
		{
			error=true;
		}
		if(error)
		{
			//to determine if en error occured during updation of the customer with credit card info entered and it must show up in the redirecting page.
			if(this.getCustomer().getNewCreditCard()!=null && this.getCustomer().getNewCreditCard().getCcNumber().trim().length()>0)
				request.setAttribute("new_cc", "true");
			return INPUT;
		}
		
		if(UserUtil.getMmrUser().getCustomerId()>0){ //this is a customer user, do not take user to search results page
			addActionMessage(MessageUtil.getMessage("customer.edit.successful"));
			return "success2";
		}
		
		addActionMessage(getText("customer.save.successfully"));
		return list();
	}
		
	public void prepare() throws Exception {
		//get the sales users for this business
		
		if(UserUtil.getMmrUser()==null)
			return;
		UserSearchCriteria criteria = new UserSearchCriteria();
		criteria.setBusinessId(UserUtil.getMmrUser().getBusinessId());
		criteria.setRoleCode(ShiplinxConstants.ROLE_SALES);
		salesUsers = userService.findUsers(criteria, 0, 0);
	}

//	public HashMap getHashMapAccounhtInfo() {
//		return hashMapAccounhtInfo;
//	}
//
//	public void setHashMapAccounhtInfo(HashMap hashMapAccounhtInfo) {
//		this.hashMapAccounhtInfo = hashMapAccounhtInfo;
//	}

	public List<CustomerCarrier> getCustomerCarrierAccountList() {
		return customerCarrierAccountList;
	}

	public void setCustomerCarrierAccountList(
			List<CustomerCarrier> customerCarrierAccountList) {
		this.customerCarrierAccountList = customerCarrierAccountList;
	}

//	public List<Carrier> getCarrierList() {
//		return carrierList;
//	}
//
//	public void setCarrierList(List<Carrier> carrierList) {
//		this.carrierList = carrierList;
//	}
	
	
	private Map<String, Long> customerSearchResult = new HashMap<String, Long>();
	
	private Map<String, Double> customerSalesSearchResult = new HashMap<String, Double>();
	   
	public Map<String, Long> getCustomerSearchResult() {  
		return customerSearchResult;  
	}  
	   
	public void setCustomerSearchResult(Map<String, Long> customerSearchResult) {  
		this.customerSearchResult = customerSearchResult;  
	}  
	
	public String listCustomers(){
		String searchParameter = request.getParameter("searchString");
		log.debug("Search string is : " + searchParameter);
		
		Customer c = new Customer();
		c.setName(searchParameter);
		c.setBusinessId(UserUtil.getMmrUser().getBusinessId());		
		if(!StringUtil.isEmpty(UserUtil.getMmrUser().getBranch()))
			c.setBranch(UserUtil.getMmrUser().getBranch());
		
		List<Customer> customers = getService().search(c);
		
		for(Customer cust: customers){
			customerSearchResult.put(cust.getName(), cust.getId());
		}
		
		return SUCCESS;
	}
	
	public String listCustomersWithOrphan(){
		String searchParameter = request.getParameter("searchString");
		log.debug("Search string is : " + searchParameter);
		
		Customer c = new Customer();
		c.setName(searchParameter);
		c.setBusinessId(UserUtil.getMmrUser().getBusinessId());		
		List<Customer> customers = getService().search(c);
		
		// First record is empty 
		customerSearchResult.put("ORPHAN", 0L);		
		
		for(Customer cust: customers){
			customerSearchResult.put(cust.getName(), cust.getId());
		}
		
		return SUCCESS;
	}	
	
	
	public String sendCustomerEmailNotification() 
	{
		log.debug("-----sendCustomerEmailNotification------");
		boolean emailSent = false;
		try {
			Customer customer = this.getCustomer();
			String id = request.getParameter("id");
			if (id != null) 
			{
				long customerId = Long.valueOf(id);
				emailSent = getService().sendCustomerMailNotification(customerId, UserUtil.getMmrUser());
				list();
			}
			
			if(emailSent)
				this.addActionMessage(getText("email.customer.notification.sent"));
			else
				this.addActionError(getText("email.customer.notification.not.sent"));
		
		}catch (Exception e) {
	    	e.printStackTrace();
		}
		return SUCCESS;
	}
	
	
	public String manageSalesUsers()
	{
		log.debug("---Inside--manageSalesUsers---------");
		Customer customer = this.getCustomer();
		List <CustomerSalesUser> customerSalesList = getService().getCustomerSalesInfoByCustId(customer.getId());
		List comm_pcList = new ArrayList();
		List comm_psList = new ArrayList();
		List comm_ppList = new ArrayList();
		for(com.meritconinc.mmr.model.security.User user : salesUsers)
		{
			comm_pcList.add(user.getCommissionPercentage());
			comm_ppList.add(user.getCommissionPercentagePP());
			comm_psList.add(user.getCommissionPercentagePS());
		}
		customer.setCustomerSalesUser(customerSalesList);
		request.setAttribute("ppList", comm_ppList);
		request.setAttribute("pcList", comm_pcList);
		request.setAttribute("psList", comm_psList);
		return SUCCESS;
	}
	
	
	public String addnewCustomerSales()
	{
		log.debug("---Inside--addnewCustomerSales---------");
		
		Customer customer = this.getCustomer();
		CustomerSalesUser customerSalesUser = new CustomerSalesUser();
		boolean retval = false;
		
		String strSalesAgent= request.getParameter("SalesAgent");
		Double dCommissionPercentage= Double.parseDouble(request.getParameter("Commission_Percentage").toString());
		Double dCommissionPercentage_pp = Double.parseDouble(request.getParameter("Commission_Percentage_PP").toString());
		Double dCommissionPercentage_ps = Double.parseDouble(request.getParameter("Commission_Percentage_PS").toString());
		
		customerSalesUser.setCommissionPercentage(dCommissionPercentage);
		customerSalesUser.setCommissionPercentagePerPalletService(dCommissionPercentage_pp);
		customerSalesUser.setCommissionPercentagePerSkidService(dCommissionPercentage_ps);
		customerSalesUser.setSalesAgent(strSalesAgent);
		customerSalesUser.setCustomerId(customer.getId());
				
		retval = getService().addCustomerSales(customerSalesUser);
		
		if(retval)
			this.addActionMessage(getText("customer.sales.added.successfully"));
		else
			this.addActionError(getText("customer.sales.added.failed"));
			
		return SUCCESS;
	}
	
	
	public String deleteCustomerSales()
	{
		log.debug("---Inside--deleteCustomerSales---------");
		
		CustomerSalesUser customerSalesUser = new CustomerSalesUser();
		boolean retval = false;
		
		String csId= request.getParameter("cs_id");
				
		customerSalesUser.setId(Integer.parseInt(csId));
				
		retval = getService().deleteCustomerSales(customerSalesUser);
		
		if(retval)
			this.addActionMessage(getText("customer.sales.deleted.successfully"));
		else
			this.addActionError(getText("customer.sales.delete.failed"));
		
		return SUCCESS;
	}
	
	
	public String updateCustomerSales()
	{
		log.debug("---Inside--updateCustomerSales---------");
		
		CustomerSalesUser customerSalesUser = new CustomerSalesUser();
		Double dcommissionpercentage=0.0, dcommissionpercentage_pp=0.0, dcommissionpercentage_ps=0.0;
		boolean retval = false;
		
		String csId= request.getParameter("cs_id");
		dcommissionpercentage = Double.parseDouble(request.getParameter("cp"));
		dcommissionpercentage_pp = Double.parseDouble(request.getParameter("cp_pp"));
		dcommissionpercentage_ps = Double.parseDouble(request.getParameter("cp_ps"));
				
		customerSalesUser.setId(Integer.parseInt(csId));
		customerSalesUser.setCommissionPercentage(dcommissionpercentage);
		customerSalesUser.setCommissionPercentagePerPalletService(dcommissionpercentage_pp);
		customerSalesUser.setCommissionPercentagePerSkidService(dcommissionpercentage_ps);
		
		retval = getService().updateCustomerSales(customerSalesUser);
		
		if(retval)
			this.addActionMessage(getText("customer.sales.updated.successfully"));
		else
			this.addActionError(getText("customer.sales.update.failed"));
		
		return SUCCESS;
	}
	
	public String getAddressSuggest() throws Exception {

		String postalCode = (String)request.getParameter("postalCode");
		String country = (String)request.getParameter("countryCode");
		
		Address address = new Address();
		address.setPostalCode(postalCode);
		address.setCountryCode(country);
		
		ServiceAvailabilityWebServiceClient zipCodeValidator = new ServiceAvailabilityWebServiceClient();
		address = zipCodeValidator.getSuggestedAddress(address);
		
		if(address!=null){
			inputStream = new StringBufferInputStream(address.getCity());	
			this.getCustomer().getAddress().setProvinceCode(address.getProvinceCode());
			this.setCustomer(this.getCustomer());	
		}
		return SUCCESS;
	}
	public String getSignupJSP() {
		return signupJSP;
	}
	public void setSignupJSP(String signupJSP) {
		this.signupJSP = signupJSP;
	}
	
	
	
	
}
