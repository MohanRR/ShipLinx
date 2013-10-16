package com.meritconinc.shiplinx.action;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.meritconinc.mmr.model.admin.UserSearchCriteria;
import com.meritconinc.mmr.model.security.User;
import com.meritconinc.mmr.service.UserService;
import com.meritconinc.mmr.utilities.StringUtil;
import com.meritconinc.mmr.utilities.security.UserUtil;
import com.meritconinc.shiplinx.model.ARTransaction;
import com.meritconinc.shiplinx.model.CreditCard;
import com.meritconinc.shiplinx.model.Customer;
import com.meritconinc.shiplinx.model.Invoice;
import com.meritconinc.shiplinx.model.InvoiceStatus;
import com.meritconinc.shiplinx.model.SalesRecord;
import com.meritconinc.shiplinx.model.ShippingOrder;
import com.meritconinc.shiplinx.service.CustomerManager;
import com.meritconinc.shiplinx.service.InvoiceManager;
import com.meritconinc.shiplinx.service.ShippingService;
import com.meritconinc.shiplinx.utils.FormattingUtil;
import com.meritconinc.shiplinx.utils.ShiplinxConstants;
import com.opensymphony.xwork2.Preparable;

public class InvoiceManagerAction extends BaseAction implements Preparable,
		ServletRequestAware, ServletResponseAware {
	private static final long serialVersionUID = 250927861;
	private List<ShippingOrder> orders;

	private static final Logger log = LogManager
			.getLogger(InvoiceManagerAction.class);
	private InvoiceManager invoiceManager;
	private ShippingService shippingService;
	public HttpServletRequest request;
	public HttpServletResponse response;
	private CustomerManager customerService;
	private List<Invoice> invoices;
	private List<SalesRecord> salesRecords;
	private List<InvoiceStatus> statusList;
	private Customer customer;
	private List<ShippingOrder> selectedOrders;
	private List<Invoice> selectedInvoices;
	private List<User> salesUsers;
	private UserService userService;

	private List<Boolean> select;
	private CreditCard creditCard;
	private List<ARTransaction> arTransactions;
	
	public static final List<String> MONTH_LIST = createMonthList();


	private Map<String, Long> invoiceSearchResult = new HashMap<String, Long>(); 
	
	private InputStream fileInputStream;

	@Override
	public void prepare() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void setServletRequest(HttpServletRequest request) {
		// TODO Auto-generated method stub
		this.request = request;
	}
	
	/**
	 * sets the HttpServletRequest
	 * 
	 * @param httpServletRequest
	 */
    public void setServletResponse(HttpServletResponse httpServletResponse) {
    	this.response = httpServletResponse;     
    }	
	
	public void setInvoiceManager(InvoiceManager invoiceManager) {
		this.invoiceManager = invoiceManager;
	}
	public void setCustomerService(CustomerManager customerService) {
		this.customerService = customerService;
	}	
	public void setShippingService(ShippingService shippingService) {
		this.shippingService = shippingService;
	}
	public InputStream getFileInputStream() {
		return fileInputStream;
	}

	public void setFileInputStream(InputStream fileInputStream) {
		this.fileInputStream = fileInputStream;
	}

	public UserService getUserService() {
		return userService;
	}
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public String execute() throws Exception {
		getSession().remove("invoice");
		log.debug("In execute of InvoiceAction");
		this.statusList = invoiceManager.getInvoiceStatusList();
		return SUCCESS;
	}

	public String searchInvoice()  {
		
		log.debug("In searchInvoice");
		String search=request.getParameter("search");
		log.debug("Search:-----"+search);
		Invoice i = this.getInvoice();
		this.statusList = invoiceManager.getInvoiceStatusList(); // This is to make sure the list statusList does'nt go as null back to the form.
		
		if(search!=null && search.equalsIgnoreCase("outstanding")){
			i = new Invoice();
			i.setPaymentStatusList(new int[]{Invoice.INVOICE_STATUS_UNPAID, Invoice.INVOICE_STATUS_PARTIAL_PAID});
		}
		
		i.setBusinessId(UserUtil.getMmrUser().getBusinessId());
		if(UserUtil.getMmrUser().getCustomerId()>0)
			i.setCustomerId(UserUtil.getMmrUser().getCustomerId());
		if(!StringUtil.isEmpty(UserUtil.getMmrUser().getBranch()))
			i.setBranch(UserUtil.getMmrUser().getBranch());
		

		invoices = invoiceManager.searchInvoices(i);
		log.debug("Found : " + invoices.size() + " invoices");
		
		if(search!=null && search.equalsIgnoreCase("outstanding")){
			request.setAttribute("postPayment", new Boolean(false));
			this.statusList = invoiceManager.getInvoiceStatusList();
			return "paylist";
		}
		return SUCCESS;
	}

	public void setInvoices(List<Invoice> invoices) {
		this.invoices = invoices;
	}

	public List<Invoice> getInvoices() {
		return invoices;
	}


	public String generateInvoice()  {
		
		log.debug("In generateInvoice");
		getSession().remove("invoice");
		Invoice i = this.getInvoice();
		i.setBusinessId(UserUtil.getMmrUser().getBusinessId());
		i.setCustomerId(new Long(0));
		
		orders = shippingService.getUnbilledShipments(i.getBusinessId(), i.getCustomerId().longValue(), UserUtil.getMmrUser().getBranch());
		log.debug("Found : " + orders.size() + " shipments that can be billed");		
		return SUCCESS;
	}
	
	public String searchInvoiceableShipments()  {
		
		Invoice i = this.getInvoice();
		i.setBusinessId(UserUtil.getMmrUser().getBusinessId());
		if(i.getCustomerId()==null)
			i.setCustomerId(new Long(0));
		orders = shippingService.getUnbilledShipments(i.getBusinessId(), i.getCustomerId().longValue(), UserUtil.getMmrUser().getBranch());
		
		log.debug("Found : " + orders.size() + " shipments that can be billed");		
		
		if(i.getCustomerId() > 0){
			try{
				customer = customerService.getCustomerInfoByCustomerId(i.getCustomerId());
			}
			catch(Exception e){				
			}
		}
		
		String type = request.getParameter("type");
		if(type!=null && type.equalsIgnoreCase("refresh"))
			return type;
		else
			return SUCCESS;
	}

	public String createInvoice()  {
		
		log.debug("In createInvoice");
		Invoice invoice = this.getInvoice();
		List<Long> orderIds = new ArrayList<Long>();
		for ( int i = 0; i < select.size(); i++ ) {
		    // If this checkbox was selected:
		    if ( select.get(i) != null && select.get(i) ) {
		    	// Get the matching test scenario:
		    	ShippingOrder order = this.getSelectedOrders().get(i);
		    	// ...and launch it:
		    	orderIds.add(order.getId());
		    } 		
		}
		
		if(orderIds.size() > 0)
			invoices = invoiceManager.createInvoices(orderIds, invoice);
		
		String args[] = new String[1];
		args[0] = String.valueOf(invoices.size());
		addActionMessage(getText("invoice.created", new String[]{args[0]}));
		this.statusList = invoiceManager.getInvoiceStatusList();
		
		return SUCCESS;
	}

	public String autoGenInvoices()  {
		
		log.debug("In autogen Invoices");
		Invoice invoice = this.getInvoice();
		
		try{
			invoices = invoiceManager.autoGenInvoicesForBusiness(UserUtil.getMmrUser().getBusinessId(), invoice, UserUtil.getMmrUser().getBranch());
		}
		catch(Exception e){
			getActionErrors().add(getText("invoice.autoGen.failed"));
		}
		
		this.statusList = invoiceManager.getInvoiceStatusList();
		String args[] = new String[1];
		args[0] = String.valueOf(invoices.size());
		this.addActionMessage(getText("invoice.created"));	
		
		return SUCCESS;
	}

	public String payInvoices()  {
		
		log.debug("In pay Invoices");
		List<Long> invoiceIds = new ArrayList<Long>();
		for ( int i = 0; i < select.size(); i++ ) {
		    // If this checkbox was selected:
		    if ( select.get(i) != null && select.get(i) ) {
		    	// Get the matching test scenario:
		    	Invoice invoice = this.getSelectedInvoices().get(i);
		    	// ...and launch it:
		    	invoiceIds.add(invoice.getInvoiceId());
		    } 		
		}
		
		invoices = invoiceManager.processPayment(invoiceIds, creditCard, true);		
		request.setAttribute("postPayment", new Boolean(true));		
		return SUCCESS;
	}

	public String updateAR()  {
		
		log.debug("In update A/R");
		String method=request.getParameter("method");
		log.debug("In update A/R-----method is:"+method);
		
		if(method!=null) 
			getSession().remove("invoice");
		Invoice i = this.getInvoice();
		
		i.setPaymentStatusList(new int[]{Invoice.INVOICE_STATUS_UNPAID, Invoice.INVOICE_STATUS_PARTIAL_PAID});
		i.setBusinessId(UserUtil.getMmrUser().getBusinessId());
		invoices = invoiceManager.searchInvoices(i);
		log.debug("Found : " + invoices.size() + " invoices");
		
		if(invoices.size()==0)
			this.addActionMessage(getText("AR.noInvoices"));
		
		return SUCCESS;
	}
	
	public String processAR()  {
		
		log.debug("In process AR");
		List<Invoice> invoicesToUpdate = new ArrayList<Invoice>();
		for ( int i = 0; i < select.size(); i++ ) {
		    // If this checkbox was selected:
		    if ( select.get(i) != null && select.get(i) ) {
		    	// Get the matching test scenario:
		    	Invoice invoice = this.getSelectedInvoices().get(i);
		    	// ...and launch it:
		    	invoicesToUpdate.add(invoice);
		    } 		
		}
		
		if(invoicesToUpdate.size()==0){
			this.addActionError(getText("AR.process.minInvoices"));
			return updateAR();
		}
		
		invoiceManager.processAR(invoicesToUpdate);		
		this.addActionMessage(getText("AR.processed"));
		return updateAR();
	}

	public String searchAR()  {
		
		log.debug("In search AR");
		String method=request.getParameter("method");
		if(method!=null) 
			getSession().remove("arTransaction");
		ARTransaction arTransaction = this.getARTransaction();
		
		arTransaction.setBusinessId(UserUtil.getMmrUser().getBusinessId());
		arTransactions = invoiceManager.searchARTransaction(arTransaction);
		
		return SUCCESS;
	}

	public Invoice getInvoice() {
		Invoice invoice = (Invoice)getSession().get("invoice");
		if (invoice == null) {
			invoice = new Invoice();
			setInvoice(invoice);
		}
		return invoice;
	}
	public void setInvoice(Invoice invoice) {

		getSession().put("invoice", invoice);
	}

	public SalesRecord getSalesRecord() {
		SalesRecord salesRecord = (SalesRecord)getSession().get("salesRecord");
		if (salesRecord == null) {
			salesRecord = new SalesRecord();
			setSalesRecord(salesRecord);
		}
		return salesRecord;
	}
	public void setSalesRecord(SalesRecord salesRecord) {

		getSession().put("salesRecord", salesRecord);
	}

	public ARTransaction getARTransaction() {
		ARTransaction arT = (ARTransaction)getSession().get("arTransaction");
		if (arT == null) {
			arT = new ARTransaction();
			setARTransaction(arT);
		}
		return arT;
	}
	public void setARTransaction(ARTransaction arT) {
		getSession().put("arTransaction", arT);
	}

	
	public String listInvoices(){
		String searchParameter = request.getParameter("searchStringInvoices");
		List<Invoice> invoices = null;
		if (searchParameter != null) {
			log.debug("Search string is : " + searchParameter);
			
			Invoice invoice = new Invoice();
			invoice.setBusinessId(UserUtil.getMmrUser().getBusinessId());	
			if(UserUtil.getMmrUser().getCustomerId() > 0)
				invoice.setCustomerId(UserUtil.getMmrUser().getCustomerId());
			if(!StringUtil.isEmpty(UserUtil.getMmrUser().getBranch()))
				invoice.setBranch(UserUtil.getMmrUser().getBranch());
			invoices = this.invoiceManager.searchInvoices(invoice);
			for(Invoice i: invoices){
				invoiceSearchResult.put(i.getInvoiceNum(), i.getInvoiceId());
			}
		} 

		return SUCCESS;
	}



	public List<InvoiceStatus> getStatusList() {
		return statusList;
	}

	public void setStatusList(List<InvoiceStatus> statusList) {
		this.statusList = statusList;
	}

	public List<ShippingOrder> getOrders() {
		return orders;
	}

	public void setOrders(List<ShippingOrder> orders) {
		this.orders = orders;
	}

	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public List<ShippingOrder> getSelectedOrders() {
		return selectedOrders;
	}
	public void setSelectedOrders(List<ShippingOrder> selectedOrders) {
		this.selectedOrders = selectedOrders;
	}

	public List<Boolean> getSelect() {
		return select;
	}
	public void setSelect(List<Boolean> select) {
		this.select = select;
	}

	public CreditCard getCreditCard() {
		return creditCard;
	}
	public void setCreditCard(CreditCard creditCard) {
		this.creditCard = creditCard;
	}

	public List<Invoice> getSelectedInvoices() {
		return selectedInvoices;
	}
	public void setSelectedInvoices(List<Invoice> selectedInvoices) {
		this.selectedInvoices = selectedInvoices;
	}

 	public List<ARTransaction> getArTransactions() {
		return arTransactions;
	}
	public void setArTransactions(List<ARTransaction> arTransactions) {
		this.arTransactions = arTransactions;
	}

	private static List<String> createMonthList() {
        List<String> result = new ArrayList<String>();
        result.add("01");
        result.add("02");
        result.add("03");
        result.add("04");
        result.add("05");
        result.add("06");
        result.add("07");
        result.add("08");
        result.add("09");
        result.add("10");
        result.add("11");
        result.add("12");
        return result;
    }

	   
	public Map<String, Long> getInvoiceSearchResult() {  
		return invoiceSearchResult;  
	}  
	   
	public void setInvoiceSearchResult(Map<String, Long> invoiceSearchResult) {  
		this.invoiceSearchResult = invoiceSearchResult;  
	}  
	
	public String getInvoicePdf() {
		try {
			String invoiceId = request.getParameter("invoiceId");
			if (invoiceId != null) {
				response.setHeader("Cache-Control", "no-cache"); 
//				response.setHeader("Content-Disposition","attachment; filename=label.pdf"); 
				response.setHeader("Expires", "0"); 
				response.setHeader("Cache-Control", 
				"must-revalidate, post-check=0, pre-check=0"); 
				response.setHeader("Pragma", "public"); 
				Long l = Long.parseLong(invoiceId);
				this.invoiceManager.getInvoicePdf(l, request.getContextPath(), response.getOutputStream(), false);
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		}

		return null;
	}	

	public String cancelInvoice() {
		String invoiceId = request.getParameter("invoiceId");
		boolean cancelled = false;
		if (invoiceId != null) {
			Long l = Long.parseLong(invoiceId);
			cancelled = invoiceManager.cancelInvoice(l);
		}
		
		if(cancelled)
			this.addActionMessage(getText("invoice.cancelled"));
		else
			this.addActionError(getText("invoice.notCancelled"));
		
		this.statusList = invoiceManager.getInvoiceStatusList();
		return this.searchInvoice();
	}	
	
	public String editInvoice()  {
		String invoiceId = request.getParameter("invoiceId");
		if (invoiceId != null) {
			long l = Long.parseLong(invoiceId);
			Invoice invoice = invoiceManager.getInvoiceById(l);
			this.setInvoice(invoice);
			return SUCCESS;
		}
		
		return ERROR;
	}	
	public String updateInvoice()  {
		try {
			Invoice invoice = this.getInvoice();
			if (invoice != null) {
				String [] ids = this.request.getParameterValues("actualChargeIds");
				String [] userCharges = this.request.getParameterValues("actualCharge");
				String [] userCosts = this.request.getParameterValues("actualChargeCost");
				String [] userNames = this.request.getParameterValues("actualChargeName");
				String [] trackNos = this.request.getParameterValues("trackingNumbers");	
				
				invoice = invoiceManager.updateInvoice(invoice, ids, userCharges, userCosts, 
											userNames, trackNos);
				this.setInvoice(invoice);
			}
	    } catch (Exception e) {
	    	e.printStackTrace();
	    	addActionError(getText("content.error.unexpected"));
		}
		
		return searchInvoice();
	}

	public String commReport() throws Exception {
		
		String method=request.getParameter("method");
		
		UserSearchCriteria criteria = new UserSearchCriteria();
		criteria.setBusinessId(UserUtil.getMmrUser().getBusinessId());
		criteria.setRoleCode(ShiplinxConstants.ROLE_SALES);
		criteria.setSortBy(UserSearchCriteria.SORT_BY_FIRSTNAME);
		if(!StringUtil.isEmpty(UserUtil.getMmrUser().getBranch()))
			criteria.setBranch(UserUtil.getMmrUser().getBranch());
		salesUsers = userService.findUsers(criteria, 0, 0);

		if(method!=null){
			getSession().remove("invoice");
			Invoice invoice = getInvoice();
			//default the date range to last month
			int month = Calendar.getInstance().get(Calendar.MONTH);
			int year = Calendar.getInstance().get(Calendar.YEAR);
			
			month--;
			
			if(month==-1){
				month = 11;
				year--;
			}
			
			Calendar calendar = new GregorianCalendar(year, month, Calendar.DAY_OF_MONTH);
			calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
			invoice.setToInvoiceDate_web(FormattingUtil.getFormattedDate(calendar.getTime(),FormattingUtil.DATE_FORMAT_WEB));
			calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
			invoice.setFromInvoiceDate_web(FormattingUtil.getFormattedDate(calendar.getTime(),FormattingUtil.DATE_FORMAT_WEB));
			
			return SUCCESS;
		}
		
		Invoice invoice = this.getInvoice();
		if(UserUtil.getMmrUser().getUserRole().equals(ShiplinxConstants.ROLE_SALES))
		{
			invoice.setSalesUsername(UserUtil.getMmrUser().getUsername());
		}
		invoice.setBusinessId(UserUtil.getMmrUser().getBusinessId());

		invoices = invoiceManager.generateCommReport(invoice);
		
		return SUCCESS;
	}

	public String salesReport() throws Exception {
		
		String method=request.getParameter("method");
		
		if(method!=null){
			getSession().remove("salesRecord");
			SalesRecord sales = getSalesRecord();			
			return SUCCESS;
		}
		
		SalesRecord sales = getSalesRecord();
		sales.setBusinessId(UserUtil.getMmrUser().getBusinessId());
		sales.setMonth(Integer.valueOf(sales.getMonthName()));
		if(!StringUtil.isEmpty(UserUtil.getMmrUser().getBranch()))
			sales.setBranch(UserUtil.getMmrUser().getBranch());

		salesRecords = invoiceManager.generateSalesReport(sales);		
		return SUCCESS;
	}

	public List<User> getSalesUsers() {
		return salesUsers;
	}

	/*Start: Method to send EMail Notification to the Customer of the requested Invoice*/
	public String sendEmailNotificationForInvoice()
	{
		log.debug("In Send Email Notification method.");
		//List<Long> invoiceIds = new ArrayList<Long>();
		boolean emailSent = false;
		
		
		try 
		{
			for ( int i = 0; i < select.size(); i++ ) 
			{
			    // If this checkbox was selected:
			    if ( select.get(i) != null && select.get(i) ) 
			    {		   
			    	Invoice invoice = this.getSelectedInvoices().get(i);	    	
			    	//invoiceIds.add(invoice.getInvoiceId());
			    				    	
			    	emailSent = invoiceManager.sendInvoiceEmailNotification(UserUtil.getMmrUser(), invoice);		    	
			    	
			    	
			    } 		
			}		
			
			if(emailSent)
	    		this.addActionMessage(getText("email.invoice.notification.sent"));
			else
				this.addActionError(getText("email.invoice.notification.not.sent"));
		}
		catch (Exception e) {
			// TODO: handle exception
		}
		
		
		
		return searchInvoice();
	}

	public List<SalesRecord> getSalesRecords() {
		return salesRecords;
	}
	public void setSalesRecords(List<SalesRecord> salesRecords) {
		this.salesRecords = salesRecords;
	}
	
	
	/*End: Method to send EMail Notification to the Customer of the requested Invoice*/
	
	public String downloadInvoiceCSV()
	{
		log.debug("In downloadInvoiceCSV method.");
		boolean CSVDownload = false;
		try 
		{
			String invoiceId = request.getParameter("invoiceId");
			String strFile = "Invoice_"+invoiceId+".csv";
			if (invoiceId != null) 
			{
				response.setContentType("application/octet-stream");
				response.setHeader("Content-Disposition","attachment; filename="+strFile); 
				Long l = Long.parseLong(invoiceId);
				CSVDownload = invoiceManager.downloadInvoiceCSV(l, response.getOutputStream());
				if(CSVDownload)
					this.addActionMessage(getText("download.invoice.csv.success"));
				else
					this.addActionError(getText("download.invoice.csv.failure"));
			}
		}
		catch (Exception e) 
		{
			// TODO: handle exception
		}
		
		return null;
	}
	
	
}
