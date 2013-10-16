package com.meritconinc.shiplinx.service.impl;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.meritconinc.mmr.dao.UserDAO;
import com.meritconinc.mmr.model.security.User;
import com.meritconinc.mmr.utilities.MessageUtil;
import com.meritconinc.mmr.utilities.StringUtil;
import com.meritconinc.mmr.utilities.mail.MailHelper;
import com.meritconinc.mmr.utilities.security.UserUtil;
import com.meritconinc.shiplinx.dao.BusinessDAO;
import com.meritconinc.shiplinx.dao.CustomerDAO;
import com.meritconinc.shiplinx.dao.InvoiceDAO;
import com.meritconinc.shiplinx.dao.ShippingDAO;
import com.meritconinc.shiplinx.model.ARTransaction;
import com.meritconinc.shiplinx.model.Attachment;
import com.meritconinc.shiplinx.model.Business;
import com.meritconinc.shiplinx.model.CCTransaction;
import com.meritconinc.shiplinx.model.CarrierChargeCode;
import com.meritconinc.shiplinx.model.Charge;
import com.meritconinc.shiplinx.model.CreditCard;
import com.meritconinc.shiplinx.model.Customer;
import com.meritconinc.shiplinx.model.CustomerSalesUser;
import com.meritconinc.shiplinx.model.Invoice;
import com.meritconinc.shiplinx.model.InvoiceStatus;
import com.meritconinc.shiplinx.model.SalesRecord;
import com.meritconinc.shiplinx.model.ShippingOrder;
import com.meritconinc.shiplinx.service.CreditCardTransactionManager;
import com.meritconinc.shiplinx.service.InvoiceManager;
import com.meritconinc.shiplinx.service.PinBlockManager;
import com.meritconinc.shiplinx.utils.FormattingUtil;
import com.meritconinc.shiplinx.utils.PDFRenderer;
import com.meritconinc.shiplinx.utils.ShiplinxConstants;

public class InvoiceManagerImpl implements InvoiceManager {

	private static final Logger log = LogManager.getLogger(InvoiceManagerImpl.class);

	private CustomerDAO customerDAO;
	private ShippingDAO shippingDAO;
	private InvoiceDAO invoiceDAO;
	private BusinessDAO businessDAO;
	private CreditCardTransactionManager ccTransactionManager;
	private PinBlockManager pinBlockManager;
	private UserDAO userDAO;

	public BusinessDAO getBusinessDAO() {
		return businessDAO;
	}

	public void setBusinessDAO(BusinessDAO businessDAO) {
		this.businessDAO = businessDAO;
	}

	public void setCustomerDAO(CustomerDAO customerDAO) {
		this.customerDAO = customerDAO;
	}
	
	public void setShippingDAO(ShippingDAO shippingDAO) {
		this.shippingDAO = shippingDAO;
	}
	
		
	public void setInvoiceDAO(InvoiceDAO invoiceDAO) {
		this.invoiceDAO = invoiceDAO;
	}

	public UserDAO getUserDAO() {
		return userDAO;
	}

	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	public void setCcTransactionManager(
			CreditCardTransactionManager ccTransactionManager) {
		this.ccTransactionManager = ccTransactionManager;
	}

	public void setPinBlockManager(PinBlockManager pinBlockManager) {
		this.pinBlockManager = pinBlockManager;
	}

	public List<Invoice> autoGenInvoicesForBusiness(Long businessId, Invoice invoice, String branch){
		List<ShippingOrder> orders = shippingDAO.getUnbilledShipments(businessId, 0, branch);
		List<Long> orderIds = new ArrayList<Long>();
		for(ShippingOrder order: orders)
			orderIds.add(order.getId());
		return createInvoices(orderIds, invoice);
	}
	
	//The shipments to be invoiced can be for 1 or more customers, and there might be multiple currencies involved.
	//1 invoice to be created per customer and currency.
	public List<Invoice> createInvoices(List<Long> orderIds, Invoice invoice){

		List<Long> customerIds = shippingDAO.getCustomerIdsByOrderIds(orderIds);
		List<String> currencies = shippingDAO.getCurrencyByOrderIds(orderIds);
		List<Invoice> invoices = new ArrayList<Invoice>(); 
		
		for(Long cus: customerIds){
			for(String currency: currencies){
				//the way we are searching shipments here is NOT optimal. Can we extend the "getUnbilledShipments" query to search for shipments belonging to this customer/currency combination instead of injecting the orderIds?
				List<ShippingOrder> orders = shippingDAO.searchShipmentsByOrderIdsAndCustomerAndCurrency(orderIds, cus, currency);
				log.info("Found " + orders.size() + " orders to be billed for customer / currency : " + cus + " / " + currency);
				
				if(orders.size() > 0){
					Invoice i = createInvoice(orders, invoice, cus, currency);
					if(i!=null)
						invoices.add(i);
				}
			}
		}
		
		return invoices;
	}
	
	public Invoice createInvoice(List<ShippingOrder> orders, Invoice invoice, long customerId, String currency){
	
		Invoice i = new Invoice();
		i.setPaidAmount(0.0);
		Customer customer = customerDAO.getCustomerInfoByCustomerId(customerId, UserUtil.getMmrUser().getBusinessId());
		
		List<ShippingOrder> ordersInInvoice = new ArrayList<ShippingOrder>();
		try{
			List<Charge> allChargesForInvoice = new ArrayList<Charge>();
			for(ShippingOrder o: orders){	
				boolean added = false;
				double taxableAmount = 0;
				List<CarrierChargeCode> applicableTaxes = new ArrayList<CarrierChargeCode>();
				for(Charge c: o.getCharges()){
					if(c.getStatus()==null || c.getStatus()!=ShiplinxConstants.CHARGE_READY_TO_INVOICE)
						continue;
					//Need to include charges where the charge is 0 but cost is greater than 0 as it affects commissions which is based on invoice amounts
					if(c.getCharge()==0 && c.getCost()==0) //do not include charges that have no value in invoice.
						continue;
					allChargesForInvoice.add(c);
					if(!added){
						ordersInInvoice.add(o);
						added = true;
					}
					CarrierChargeCode ccc = shippingDAO.getChargeByCarrierAndCodes(o.getCarrierId(), c.getChargeCode(), c.getChargeCodeLevel2());
					
					log.info(c.getChargeCode() + " " + c.getChargeCodeLevel2());
					if(!ccc.isTax()){
						double cost = FormattingUtil.add(i.getInvoiceCost(), c.getCost()).doubleValue();
						i.setInvoiceCost(FormattingUtil.roundFigureRates(cost, 2));
						double amount = (FormattingUtil.add(i.getInvoiceAmount(), c.getCharge())).doubleValue();
						i.setInvoiceAmount(FormattingUtil.roundFigureRates(amount, 2));
						taxableAmount = (FormattingUtil.add(taxableAmount, c.getCharge().doubleValue())).doubleValue();
					}
					else{
						applicableTaxes.add(ccc);
						double cost = FormattingUtil.add(i.getInvoiceTaxCost(), c.getCost()).doubleValue();
						i.setInvoiceTaxCost(FormattingUtil.roundFigureRates(cost, 2));
					}				
				}
				
				//calculate taxes after all charges are taken into account
				for(Charge c: o.getCharges()){
					if(c.getStatus()==null || c.getStatus()!=ShiplinxConstants.CHARGE_READY_TO_INVOICE)
						continue;
					CarrierChargeCode ccc = shippingDAO.getChargeByCarrierAndCodes(o.getCarrierId(), c.getChargeCode(), c.getChargeCodeLevel2());
					if(ccc.isTax()){
						double tax = taxableAmount * ccc.getTaxRate()/100;
						c.setCharge(FormattingUtil.roundFigureRates(tax, 2));
						double totalTax = (FormattingUtil.add(i.getInvoiceTax(), c.getCharge())).doubleValue();
						i.setInvoiceTax(FormattingUtil.roundFigureRates(totalTax, 2));
					}
					
				}
			}
			
			if(allChargesForInvoice.size()==0) //did not find any charges that can be invoiced
				return null;
			
			i.setBusinessId(customer.getBusinessId());
			i.setCustomer(customer);
			i.setCustomerId(customerId);
			i.setPaymentStatus(Invoice.INVOICE_STATUS_UNPAID);
			i.setPayableDays(customer.getPayableDays());
			i.setDateCreated(new Timestamp(new Date().getTime()));
			i.setInvoiceDate(new Timestamp(new Date().getTime()));
			//setting the default currency of the customer to the invoice if it is set
			if(!StringUtil.isEmpty(customer.getDefaultCurrency()))
				i.setCurrency(customer.getDefaultCurrency());
			else
				i.setCurrency(currency);
			
			String[] pins = pinBlockManager.getNewPrefixedPinNumbers(ShiplinxConstants.PIN_TYPE_INVOICE_NUMBERS, 1, i.getBusinessId());
			i.setInvoiceNum(pins[0]);
			
			//determine the due date for the invoice
			int payableDays = customer.getPayableDays();
			Date dueDate = FormattingUtil.addDaysToDate(i.getInvoiceDate(), payableDays);
			i.setInvoiceDueDate(new Timestamp(dueDate.getTime())); 
		
			Long invoiceId = invoiceDAO.createInvoice(i);
			
			for(Charge charge:allChargesForInvoice){
				invoiceDAO.addShipmentAndChargeToInvoice(i.getInvoiceId(), charge.getOrderId(), charge.getId());
				//now add the mapping of invoices, shipments and charges
				charge.setStatus(ShiplinxConstants.CHARGE_INVOICED);
				shippingDAO.updateCharge(charge);
			}
			
			//For each order in the invoice, we need to determine how much was paid previously and apply it to the invoice
			for(ShippingOrder order: ordersInInvoice){
				double totalChargeForOrderOnInvoice = 0.0;
				for(Charge charge: allChargesForInvoice){
					if(charge.getOrderId() != order.getId())
						totalChargeForOrderOnInvoice = FormattingUtil.add(totalChargeForOrderOnInvoice, charge.getCharge().doubleValue()).doubleValue();
				}
				double unappliedAmount = order.getPaidAmount() - order.getAppliedAmount() - order.getRefundedAmount();
				if(unappliedAmount > 0){ //there is balance from what was paid that can be applied to the invoice
					if(totalChargeForOrderOnInvoice < unappliedAmount){ //unapplied is more than what is needed
						order.setAppliedAmount(FormattingUtil.add(order.getAppliedAmount().doubleValue(), totalChargeForOrderOnInvoice).doubleValue());
						i.setPaidAmount(FormattingUtil.add(i.getPaidAmount().doubleValue(), totalChargeForOrderOnInvoice).doubleValue());
					}
					else{ //unapplied is less that what is needed, only apply the unapplied amount as thats all we have available
						order.setAppliedAmount(FormattingUtil.add(order.getAppliedAmount().doubleValue(), unappliedAmount).doubleValue());
						i.setPaidAmount(FormattingUtil.add(i.getPaidAmount().doubleValue(), unappliedAmount).doubleValue());
					}
				}
				shippingDAO.updateShippingOrder(order);
			}
			//Determine the payment status of the invoice
			if(i.getBalanceDue() == 0)
				i.setPaymentStatus(Invoice.INVOICE_STATUS_PAID);
			else if(i.getBalanceDue() > 0 && i.getBalanceDue() < i.getTotalInvoiceCharge()){
				log.info("Invoice id: " + invoiceId + " . Total Charge / Balance Due : " + i.getTotalInvoiceCharge() + " / " + i.getBalanceDue());
				i.setPaymentStatus(Invoice.INVOICE_STATUS_PARTIAL_PAID);
			}
			
			invoiceDAO.updateInvoice(i);

			//If this is a credit card customer, we need to charge them for the invoice
			try{
				if(i.getBalanceDue() > 0 && customer.getPaymentType()==ShiplinxConstants.PAYMENT_TYPE_CREDIT_CARD){
					List<Long> invoiceIds = new ArrayList<Long>();
					invoiceIds.add(invoiceId);
					processPayment(invoiceIds, customer.getCreditCardActive(), false);
				}				
			}
			catch(Exception e){
				log.error("Unable to charge customer: " + customer.getName() + " credt card for invoice charges", e);
			}
		}
		catch(Exception e){
			log.error("Unable to create invoice for customer " + customer.getName());
			log.error(e.getMessage(), e);
			return null;
		}		
		return i;
	}
	
	public void calculateInvoiceCharges(Invoice i){
		
		i.setInvoiceAmount(new Double(0));
		i.setInvoiceCost(new Double(0));
		i.setInvoiceTax(new Double(0));
		i.setInvoiceTaxCost(new Double(0));
		
		for(ShippingOrder o: i.getOrders()){			
			double taxableAmount = 0;
			List<CarrierChargeCode> applicableTaxes = new ArrayList<CarrierChargeCode>();
			for(Charge c: o.getChargesForInvoice()){

				CarrierChargeCode ccc = shippingDAO.getChargeByCarrierAndCodes(o.getCarrierId(), c.getChargeCode(), c.getChargeCodeLevel2());
				
				log.info(c.getChargeCode() + " " + c.getChargeCodeLevel2());
				if(!ccc.isTax()){
					double cost = FormattingUtil.add(i.getInvoiceCost(), c.getCost()).doubleValue();
					i.setInvoiceCost(FormattingUtil.roundFigureRates(cost, 2));
					double amount = (FormattingUtil.add(i.getInvoiceAmount(), c.getCharge())).doubleValue();
					i.setInvoiceAmount(FormattingUtil.roundFigureRates(amount, 2));
					taxableAmount = (FormattingUtil.add(taxableAmount, c.getCharge().doubleValue())).doubleValue();
				}
				else{
					applicableTaxes.add(ccc);
					double cost = FormattingUtil.add(i.getInvoiceTaxCost(), c.getCost()).doubleValue();
					i.setInvoiceTaxCost(FormattingUtil.roundFigureRates(cost, 2));
				}				
			}
			
			for(Charge c: o.getChargesForInvoice()){
				CarrierChargeCode ccc = shippingDAO.getChargeByCarrierAndCodes(o.getCarrierId(), c.getChargeCode(), c.getChargeCodeLevel2());
				if(ccc.isTax()){
					double tax = taxableAmount * ccc.getTaxRate()/100;
					c.setCharge(FormattingUtil.roundFigureRates(tax, 2));
					double totalTax = (FormattingUtil.add(i.getInvoiceTax(), c.getCharge())).doubleValue();
					i.setInvoiceTax(FormattingUtil.roundFigureRates(totalTax, 2));
				}
				
			}
		}
		if(i.getBalanceDue() == 0)
			i.setPaymentStatus(Invoice.INVOICE_STATUS_PAID);
		else if(i.getBalanceDue() > 0 && i.getBalanceDue() < i.getTotalInvoiceCharge()){
			log.info("Invoice id: " + i.getInvoiceId() + " . Total Charge / Balance Due : " + i.getTotalInvoiceCharge() + " / " + i.getBalanceDue());
			i.setPaymentStatus(Invoice.INVOICE_STATUS_PARTIAL_PAID);
		}
		
		invoiceDAO.updateInvoice(i);

		
	}

	
	
	public boolean cancelInvoice(long invoiceId){
		Invoice invoice = invoiceDAO.getInvoiceById(invoiceId);
		if(invoice==null){
			log.error("Could not find invoice with id " + invoiceId);
		}
		try{
			if(invoice.getPaymentStatus() != Invoice.INVOICE_STATUS_UNPAID){
				log.error("Invoice " + invoice.getInvoiceNum() + " cannot be cancelled!");
				return false;
			}
		
			for(ShippingOrder order: invoice.getOrders()){
				List<Charge> charges = (shippingDAO.getShippingOrderChargesByInvoice(order.getId(), invoice.getInvoiceId()));
				
				for(Charge charge: charges){
					charge.setStatus(ShiplinxConstants.CHARGE_READY_TO_INVOICE);
					shippingDAO.updateCharge(charge);
				}
				shippingDAO.updateShippingOrderBillingStatus(order);
			}
			
			invoice.setPaymentStatus(Invoice.INVOICE_STATUS_CANCELLED);
			
			//delete all mappings of the charges to invoice in the invoice_charges table
			invoiceDAO.deleteShipmentAndChargeFromInvoice(invoice.getInvoiceId(), 0, 0);
			
			invoiceDAO.updateInvoice(invoice);
			
		}
		catch(Exception e){
			log.error("Unable to cancel invoice" + invoice.getInvoiceNum(), e);
			return false;
		}
		
		return true;

	}
	
	private double determinePreviouslyPaidAmount(long orderId, long invoiceId){
		//if the order was already billed previously on another invoice, then we do not consider the amounts that were paid on credit card at shipment time
		//as those charges would already have been taken into account when creating the previous invoice. In other words, the new charges coming in are adjustments
		
		double totalPaid = 0.0;
		List<Long> invoices = invoiceDAO.findPreviousInvoiceId(orderId, invoiceId);
		if(invoices==null || invoices.size()==0){
			log.info("Order " + orderId + " is being invoiced for the first time");
			
			ShippingOrder order = this.shippingDAO.getShippingOrder(orderId);
			
			for(CCTransaction transaction: order.getCcTransactions()){
				if(transaction.getStatus() == CCTransaction.PROCESSED_STATUS){
					totalPaid = FormattingUtil.add(totalPaid, transaction.getAmount()).doubleValue();
				}
			}

		}
		//shipment has been invoiced before
		return totalPaid;
	}
	
	public List<Invoice> searchInvoices(Invoice invoice){
		
        if(invoice.getFromInvoiceDate_web()!=null && invoice.getFromInvoiceDate_web().length()>0){
			Date from = FormattingUtil.getDate(invoice.getFromInvoiceDate_web(), FormattingUtil.DATE_FORMAT_WEB);
			invoice.setFromInvoiceDate(from);
		}
		if(invoice.getToInvoiceDate_web()!=null && invoice.getToInvoiceDate_web().length()>0){
			Date to = FormattingUtil.getDateEndOfDay(invoice.getToInvoiceDate_web(), FormattingUtil.DATE_FORMAT_WEB_ENDOFDAY);
			invoice.setToInvoiceDate(to);
		}

		boolean statusSelected=false;
		for(int i: invoice.getPaymentStatusList()){
			if(i > 0)
				statusSelected = true;
				break;
		}
		if(!statusSelected)
			invoice.setPaymentStatusList(new int[0]);
		
		return invoiceDAO.searchInvoices(invoice);
	}
	
	public Invoice getInvoiceById(long invoiceId){
		Invoice invoice = invoiceDAO.getInvoiceById(invoiceId);
	
		//Not all charges of a shipment will necessarily belong to a given invoice. The charges of a shipment may be spread across several invoices.
		//Here we are attaching the charges to the shipments based on a specific invoice.
		for(ShippingOrder order: invoice.getOrders()){
			order.setChargesForInvoice(shippingDAO.getShippingOrderChargesByInvoice(order.getId(), invoice.getInvoiceId()));
		}
		
		return invoice;
	}
	
	public List<ShippingOrder> getShippingOrders(long invoiceId){
		return null;
	}

	
	
	public List<Invoice> processPayment(List<Long> invoiceIds, CreditCard creditCard, boolean sendAdminNotification){
		List<Invoice> invoices = new ArrayList<Invoice>();
		
		Invoice i = invoiceDAO.getInvoiceById(invoiceIds.get(0));
		Customer c = i.getCustomer();
		StringBuilder stb = new StringBuilder();
		stb.append("Dear Admin,\n\nThe following payments were processed for invoices belonging to customer: " + c.getName() + "\n\n");
		Business b = businessDAO.getBusiessById(i.getBusinessId());
		
		for(long id: invoiceIds){
			Invoice invoice = this.invoiceDAO.getInvoiceById(id);
			if(b==null)
				b = businessDAO.getBusiessById(invoice.getBusinessId());
			
			double balanceDue = invoice.getBalanceDue();
			CCTransaction transaction = new CCTransaction();
			transaction.setEntityId(invoice.getInvoiceId());
			transaction.setEntityType(CCTransaction.ENTITY_TYPE_INVOICE);
			transaction.setBusinessId(invoice.getBusinessId());
			transaction.setCustomerId(invoice.getCustomerId());
			
			stb.append("Invoice #" + invoice.getInvoiceNum() + " Amount: " + invoice.getCurrency() + " " + FormattingUtil.roundFigureRates(balanceDue, 2) + " Result: ");
			
			try{
				transaction = ccTransactionManager.chargeCard(transaction, balanceDue, creditCard, invoice.getCurrency(), invoice.getCustomer());
			}
			catch(Exception e){
				log.error("Unable to process payment for invoice " + id, e);
			}
			
			if(transaction.getStatus() == CCTransaction.PROCESSED_STATUS){	
				stb.append("Payment Processed.\n");
				ARTransaction aRTransaction = new ARTransaction();
				aRTransaction.setModeOfPayment(ShiplinxConstants.PAYMENT_TYPE_CC);
				aRTransaction.setPaymentRefNum(transaction.getAuthNum());
				aRTransaction.setPaymentDate(new Timestamp(transaction.getDateCreated().getTime()));			
				aRTransaction.setInvoiceId(invoice.getInvoiceId());
				aRTransaction.setAmount(invoice.getBalanceDue()); //assumption is that customer pays full amount due online, no option of partial paying
				addARTransaction(aRTransaction);				
			}
			else{
				stb.append("Payment Not Processed!\n");
			}
				
			invoice.setTransaction(transaction); //for display purposes only
			invoices.add(invoice);
			
		}
		stb.append("\nSystem Generated Email.");
		
		if(sendAdminNotification){
			try{
				MailHelper.sendEmailNow2(b.getSmtpHost(), b.getSmtpUsername(), b.getSmtpPassword(), b.getName(), b.getSmtpPort(), 
					b.getFinanceEmail(), b.getFinanceEmail(), null, "Credit Card Payment Notification", stb.toString(), null, false);
			} catch (MessagingException e) {
				log.error("Error sending email - Messaging Exception: ", e);
			}
		}
		
		return invoices;
	}

	public void processAR(List<Invoice> invoicesToUpdate){
		for(Invoice i: invoicesToUpdate){
			ARTransaction transaction = i.getArTransaction();
			transaction.setInvoiceId(i.getInvoiceId());
			if(transaction.getPaymentDate_web()==null || transaction.getPaymentDate_web().length()==0){
				java.util.Date today = new java.util.Date();
				transaction.setPaymentDate(new Timestamp(today.getTime()));
			}
			else{
				Date paymentDate = FormattingUtil.getDate(transaction.getPaymentDate_web(), FormattingUtil.DATE_FORMAT_WEB);
				transaction.setPaymentDate(new Timestamp(paymentDate.getTime()));
			}
			addARTransaction(transaction);
			
		}
	}


	public void addARTransaction(ARTransaction transaction){
		Invoice invoice = invoiceDAO.getInvoiceById(transaction.getInvoiceId());
		transaction.setBusinessId(invoice.getBusinessId());
		transaction.setCustomerId(invoice.getCustomerId());
		invoice.setPaidAmount(FormattingUtil.add(invoice.getPaidAmount().doubleValue(),transaction.getAmount().doubleValue()).doubleValue());
		if(invoice.getBalanceDue() == 0)
			invoice.setPaymentStatus(Invoice.INVOICE_STATUS_PAID);
		else if(invoice.getBalanceDue() > 0 && invoice.getBalanceDue() < invoice.getTotalInvoiceCharge()){
			log.info("Invoice id: " + invoice.getInvoiceId() + " . Total Charge / Balance Due : " + invoice.getTotalInvoiceCharge() + " / " + invoice.getBalanceDue());
			invoice.setPaymentStatus(Invoice.INVOICE_STATUS_PARTIAL_PAID);
		}
		
		invoiceDAO.updateInvoice(invoice);
		
		try{
			invoiceDAO.addARTransaction(transaction);
		}
		catch(Exception e){
			log.error("Could not update receivable for invoice " + invoice.getInvoiceId());
		}

	}
	
	public List<ARTransaction> searchARTransaction(ARTransaction arTransaction){
		if(arTransaction.getFromTransactionDate_web()!=null && arTransaction.getFromTransactionDate_web().length()>0){
			Date from = FormattingUtil.getDate(arTransaction.getFromTransactionDate_web(), FormattingUtil.DATE_FORMAT_MMDDYYYY);
			arTransaction.setFromTransactionDate(from);
		}
		if(arTransaction.getToTransactionDate_web()!=null && arTransaction.getToTransactionDate_web().length()>0){
			Date to = FormattingUtil.getDateEndOfDay(arTransaction.getToTransactionDate_web(), FormattingUtil.DATE_FORMAT_MMDDYYYY_HHMMSS);
			arTransaction.setToTransactionDate(to);
		}

		return invoiceDAO.searchARTransaction(arTransaction);
	}
		
	public List<InvoiceStatus> getInvoiceStatusList(){
		return invoiceDAO.getInvoiceStatusList();
	}
	
	public void getInvoicePdf(Long id, String url, OutputStream outStream, boolean detailed) throws Exception {
		try {
			Invoice invoice = getInvoiceById(id);

			PDFRenderer pdfRenderer = new PDFRenderer();
			
			String mainPDFFileName = pdfRenderer.getUniqueTempPDFFileName("invoice" + invoice.getInvoiceNum());
			generateInvoiceMainPage(url, invoice, mainPDFFileName);
			ArrayList<String> srcList = new ArrayList<String>();
			srcList.add(mainPDFFileName);

			if(detailed){
//				String ediPDFFileName = pdfRenderer.getUniqueTempPDFFileName("ediInvoice");
//				if ( generateInvoiceEdiBackupPages(invoice, ediPDFFileName) )
//					srcList.add(ediPDFFileName);
//				
//				String webPDFFileName = pdfRenderer.getUniqueTempPDFFileName("webInvoice");
//				if ( generateInvoiceWOBackupPages(invoice, webPDFFileName) )
//					srcList.add(webPDFFileName);
			}

			pdfRenderer.concatPDF(srcList, outStream);
			
			// delete temp files
			pdfRenderer.deleteFiles(srcList);

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	@SuppressWarnings("unchecked")
	private boolean generateInvoiceMainPage(String url, Invoice invoice, String fileName) throws Exception {
		//String fileName = getUniqueTempPDFFileName("mainInvoice");

		long start = System.currentTimeMillis();
		if (invoice != null && invoice.getCustomer() != null && 
			invoice.getBusinessId() != null && invoice.getOrders() != null) {
			Customer customer = invoice.getCustomer();
			Business business = this.businessDAO.getBusiessById(invoice.getBusinessId());			
			if (business != null) {
				try {
					InputStream stream = null;
					
					if(StringUtil.isEmpty(business.getInvoicingTemplate()))
						stream = this.getClass().getClassLoader().getResourceAsStream(
							"./jasperreports/InvoiceMain.jasper");
					else
						stream = this.getClass().getClassLoader().getResourceAsStream(
						"./jasperreports/" + business.getInvoicingTemplate() + ".jasper");
					
					JasperReport jasperReport = (JasperReport) JRLoader.loadObject(stream);

					Map parameters = new HashMap();
					parameters.put("Business", business);
					parameters.put("Invoice", invoice);
					parameters.put("Customer", customer);
					String logoPath = "/images/" + business.getLogoFileName();
			        String logo2Path = "/images/" + business.getLogoHiResFileName();
					URL logo = (InvoiceManagerImpl.class.getResource(logoPath));        	
					URL logo2 = (InvoiceManagerImpl.class.getResource(logo2Path));  
					
			        parameters.put("logo", logo);
			        parameters.put("logo2", logo2);
			        
					JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(invoice.getOrders());

					JasperPrint jasperPrint = JasperFillManager.fillReport(
									jasperReport, parameters, ds);

					JasperExportManager.exportReportToPdfFile(jasperPrint, fileName);
					return true;

				} catch (Exception e) {
					log.error("Could not generate Shiplinx Invoice Main !!");
					e.printStackTrace();
					throw e;
				}
			}
		}
		long end = System.currentTimeMillis();
		log.debug("Time to generate Shipment Invoice Main Page : " + (end - start) + " ms");
		return false;
	}
	
	//Comm report generation
	//Total charge minus total cost gives the commissionable amount. Taxes not included
	public List<Invoice> generateCommReport(Invoice invoice){
       if(invoice.getFromInvoiceDate_web()!=null && invoice.getFromInvoiceDate_web().length()>0){
			Date from = FormattingUtil.getDate(invoice.getFromInvoiceDate_web(), FormattingUtil.DATE_FORMAT_WEB);
			invoice.setFromInvoiceDate(from);
		}
		if(invoice.getToInvoiceDate_web()!=null && invoice.getToInvoiceDate_web().length()>0){
			Date to = FormattingUtil.getDateEndOfDay(invoice.getToInvoiceDate_web(), FormattingUtil.DATE_FORMAT_WEB_ENDOFDAY);
			invoice.setToInvoiceDate(to);
		}

		invoice.setPaymentStatusList(new int[]{Invoice.INVOICE_STATUS_UNPAID, Invoice.INVOICE_STATUS_PARTIAL_PAID, Invoice.INVOICE_STATUS_PAID});
		
		List<Invoice> invoices = invoiceDAO.searchInvoices(invoice);
			
		//sort the list of invoices by customer
		Collections.sort(invoices, Invoice.CustomerComparator);
		
		CustomerSalesUser csu = new CustomerSalesUser();
		csu.setSalesAgent(invoice.getSalesUsername());
		
		for(Invoice i: invoices){
			
			csu.setCustomerId(i.getCustomerId());
			csu = customerDAO.getCustomerSalesUser(csu).get(0); //there should be exactly 1 record

			Invoice in = this.getInvoiceById(i.getInvoiceId()); //in order to set the charges for the shipments in the invoice, we need to 
			//retrieve the invoice again by id
			for(ShippingOrder orderInInvoice: in.getOrders()){

				List<Charge> chargesForOrder = orderInInvoice.getChargesForInvoice();
				double totalCostForOrder = 0, totalChargeForOrder = 0;
				for(Charge c: chargesForOrder){
					
					//ignore taxes when calculating commissionable amount
					CarrierChargeCode ccc = shippingDAO.getChargeByCarrierAndCodes(orderInInvoice.getCarrierId(), c.getChargeCode(), c.getChargeCodeLevel2());
					if(ccc==null || ccc.isTax())
						continue;
					totalCostForOrder = FormattingUtil.add(c.getCost().doubleValue(), totalCostForOrder).doubleValue();
					totalChargeForOrder = FormattingUtil.add(c.getCharge().doubleValue(), totalChargeForOrder).doubleValue();
				}
				double commissionPerc = 0; //to be determined based on the service type
				if(orderInInvoice.getService().getServiceType() == ShiplinxConstants.SERVICE_TYPE_LTL_POUND)
					commissionPerc = csu.getCommissionPercentagePerPalletService();
				else if(orderInInvoice.getService().getServiceType() == ShiplinxConstants.SERVICE_TYPE_LTL_SKID)
					commissionPerc = csu.getCommissionPercentagePerSkidService();
				else
					commissionPerc = csu.getCommissionPercentage(); //default to small package commission percentage
				
				double commissionAmount = (totalChargeForOrder - totalCostForOrder) * commissionPerc / 100;
				i.setCommissionAmount(FormattingUtil.add(commissionAmount, i.getCommissionAmount()).doubleValue());
			}
			
		}
		
		return invoices;

	}

	//Sales report generation
	//taxes not included
	public List<SalesRecord> generateSalesReport(SalesRecord sales){

		sales.setPaymentStatusList(new int[]{Invoice.INVOICE_STATUS_UNPAID, Invoice.INVOICE_STATUS_PARTIAL_PAID, Invoice.INVOICE_STATUS_PAID});
		List<SalesRecord> salesRecords = invoiceDAO.getSalesReport(sales);		
		return salesRecords;

	}

	@Override
	public Invoice updateInvoice(Invoice invoice, String[] ids,	String[] userCharges, 
						String[] userCosts, String[] userNames, String[] trackNos) throws Exception {
		// TODO Auto-generated method stub
		if (invoice != null && ids != null && userCharges != null && userCosts != null && 
				userNames != null && trackNos != null) {
			for (int i=0; i<ids.length; i++) {
				Long id = Long.parseLong(ids[i]);
				ShippingOrder order = getOrder(invoice, trackNos[i]);
				if (order != null) {
					Charge soCharge = getCharge(order, id);
					if (soCharge != null) {
						Double ch = StringUtil.getDouble(userCharges[i]); // Double.parseDouble(userCharges[i]);
						Double cost = StringUtil.getDouble(userCosts[i]); // Double.parseDouble(userCosts[i]);
						String name = userNames[i];
						if (ch.doubleValue() == 0 && cost.doubleValue() == 0) {
							this.shippingDAO.deleteCharge(soCharge.getId());
							this.invoiceDAO.deleteShipmentAndChargeFromInvoice(invoice.getInvoiceId().longValue(), 
									order.getId().longValue(), soCharge.getId().longValue());
						} else if (isChargeDirty(soCharge, ch, cost, name)) {
							this.shippingDAO.updateCharge(soCharge);
						}
					}
				}
			}
			
			long invId = invoice.getInvoiceId().longValue();
			invoice = getInvoiceById(invId);
			calculateInvoiceCharges(invoice);
			invoice = getInvoiceById(invId);			
		}
		
		return invoice;
	}
	private ShippingOrder getOrder(Invoice invoice, String trackNo) {
		// TODO Auto-generated method stub
		if (invoice != null)
			for (ShippingOrder o:invoice.getOrders())
				if (o.getMasterTrackingNum().equals(trackNo))
					return o;
		
		return null;
	}
	private Charge getCharge(ShippingOrder so, Long id) {
		// TODO Auto-generated method stub
		for (Charge c:so.getCharges()) {
			if (c.getId().longValue() == id.longValue())
				return c;
		}
		return null;
	}
	
	private boolean isChargeDirty(Charge soCharge, Double charge, Double cost, String name) {
		// TODO Auto-generated method stub
		boolean isDirty = false;
		if (!soCharge.getName().equalsIgnoreCase(name)) {
			isDirty = true;
			soCharge.setName(name);
		}
		if ((soCharge.getCharge() == null && charge != null) ||
			(charge != null && soCharge.getCharge().doubleValue() != charge.doubleValue())	) {
			isDirty = true;
			soCharge.setCharge(charge);			
		}
		if ((soCharge.getCost() == null && cost != null) || 
			(cost != null && soCharge.getCost().doubleValue() != cost.doubleValue()) ) {
			isDirty = true;
			soCharge.setCost(cost);
		}		
		return isDirty;
	}
	
	public boolean sendInvoiceEmailNotification(User user, Invoice invoice) 
	{
		boolean boolret = true;
		List<String> bccAddresses = new ArrayList<String>();
		String toAddress = "";
		
		try
		{		
			//String strEmailto = null;
			//strEmailto = invoice.getCustomer().getAddress().getEmailAddress().toString().trim();
			invoice = this.getInvoiceById(invoice.getInvoiceId());		
			PDFRenderer pdfRenderer = new PDFRenderer();
			String mainPDFFileName = pdfRenderer.getUniqueTempPDFFileName("invoice" + invoice.getInvoiceNum());
			File fInvoicePDF = new File(mainPDFFileName);
	    	fInvoicePDF.deleteOnExit();
	    	BufferedOutputStream invBOS = new BufferedOutputStream(new FileOutputStream(fInvoicePDF));
	    	
	    	Long l = invoice.getInvoiceId();
	    	getInvoicePdf(l, null, invBOS, false); 
	    	invBOS.close();
	    	
			List<Attachment> lstFileAttachment = new ArrayList<Attachment>();
			Attachment attachment = new Attachment();
			attachment.setFile(fInvoicePDF);
			attachment.setContentType("pdf");
			lstFileAttachment.add(attachment);
			// GROUP_EMAIL_ADDRESS_en_CA
			String locale = user.getLocale();
	
			String subject = MessageUtil.getMessage(
					"message.send.invoice.notification.subject", locale);
			subject = new String(subject.replaceAll("%BUSINESSNAME", user.getBusiness().getName()));
			
			String body = MessageUtil.getMessage(
					user.getBusiness().getInvoiceNotificationBody(), locale);
		
			if(body== null || body.length()==0){
				log.error("Cannot find template to send invoice notification for business " + user.getBusiness().getName());
				return false;
			}
			
			body = new String(body.replaceAll("%BUSINESSNAME", user.getBusiness().getName()));
			body = new String(body.replaceAll("%CONTACT", user.getBusiness().getAddress().getContactName()));
			body = new String(body.replaceAll("%PHONENO",user.getBusiness().getAddress().getPhoneNo()));
			body = new String(body.replaceAll("%EMAIL", user.getBusiness().getAddress().getEmailAddress()));

			bccAddresses.add(user.getBusiness().getAddress().getEmailAddress());
			/*If the Invoicing Email is null, then the mail is sent to the customer email address*/
			if(invoice.getCustomer().getInvoicingEmail()!= null && invoice.getCustomer().getInvoicingEmail().length()>0)
				toAddress = invoice.getCustomer().getInvoicingEmail();
			else
				toAddress = invoice.getCustomer().getAddress().getEmailAddress();
			
			MailHelper.sendEmailNow2(user.getBusiness().getSmtpHost(), user.getBusiness().getSmtpUsername(), user.getBusiness().getSmtpPassword(), user.getBusiness().getName(), user.getBusiness().getSmtpPort(), user.getBusiness().getFinanceEmail(), toAddress, bccAddresses, subject, body, lstFileAttachment, true);
		} catch (MessagingException e) {
			log.error("Error sending email - Messaging Exception: ", e);
			boolret = false;
		}
		catch (IOException ioe)
		{
			log.error("Error sending email - IOException : ", ioe);
			boolret = false;
		}
		catch(Exception e)
		{
			log.error("Error sending email - Exception: " + e.getMessage());
			boolret = false;
		}
		
		return boolret;
	}
	
	public boolean downloadInvoiceCSV(long invoiceId, OutputStream ostream)
	{
		Invoice invoice = getInvoiceById(invoiceId);
		Business business = getBusinessDAO().getBusiessById(invoice.getBusinessId());
		Customer customer = invoice.getCustomer();
		int j=0;
		
		for(ShippingOrder so: invoice.getOrders())
		{
			int i=0;
			int chargecnt = 0;
			String[] strheader = new String[100];
			String[] strvalues = new String[100];
			
			StringBuffer stbheader = new StringBuffer();
			StringBuffer stbheadervalues = new StringBuffer();
			
			strheader[i]="Invoice Number,";
			if(invoice.getInvoiceNum()!=null)
				strvalues[i]=invoice.getInvoiceNum()+",";
			else
				strvalues[i]=",";
			i++;
			
			strheader[i]="Invoice Date,";
			if(invoice.getInvoiceDate()!=null)
				strvalues[i]=invoice.getInvoiceDate()+",";
			else
				strvalues[i]=",";
			i++;
			
			strheader[i]="Invoice due date,";
			if(invoice.getInvoiceDueDate()!=null)
				strvalues[i]=invoice.getInvoiceDueDate()+",";
			else
				strvalues[i]=",";
			i++;
			
			strheader[i]="Order No,";
			if(so.getOrderNum()!=null)
				strvalues[i]=so.getOrderNum()+",";
			else
				strvalues[i]=",";
			i++;
			
			strheader[i]="Carrier,";
			if(so.getCarrierName()!=null)
				strvalues[i]=so.getCarrierName()+",";
			else
				strvalues[i]=",";
			i++;
			
			strheader[i]="Inv Total,";
			strvalues[i]=invoice.getTotalInvoiceCharge()+",";
			i++;
			
			strheader[i]="Trans Cnt,";
			if(invoice.getOrders()!=null)
				strvalues[i]=invoice.getOrders().size()+",";
			else
				strvalues[i]=",";
			i++;
			
			strheader[i]="Bill-to Account,";
			if(invoice.getCustomer()!=null)
			{
				if(invoice.getCustomer().getAccountNumber()!=null)
					strvalues[i]=invoice.getCustomer().getAccountNumber()+","; // Bill to Account
				else
					strvalues[i]=",";
			}
			else
				strvalues[i]=",";
			i++;
			
			strheader[i]="Tracking Number,";
			if(so.getMasterTrackingNum()!=null)
				strvalues[i]=so.getMasterTrackingNum()+",";
			else
				strvalues[i]=",";
			i++;
			
			
			strheader[i]="Ship Date,";
			if(so.getScheduledShipDate()!=null)
				strvalues[i]=so.getScheduledShipDate()+",";
			else
				strvalues[i]=",";
			i++;
			
			strheader[i]="Service Name,";
			if(so.getService()!=null)
				strvalues[i]=so.getService().getName()+",";
			else
				strvalues[i]=",";
			i++;
			
			strheader[i]="Service Code,";
			if(so.getServiceId()!=null)
				strvalues[i]=so.getServiceId()+",";
			else
				strvalues[i]=",";
			i++;
			
			strheader[i]="Package Type,";
			if(so.getPackageTypeId()!=null)
			{
				if(so.getPackageTypeId().getType()!=null)
					strvalues[i]=so.getPackageTypeId().getType()+",";
				else
					strvalues[i]=",";
			}
			else
				strvalues[i]=",";
			i++;
			
			strheader[i]="Package Code,";
			if(so.getPackageTypeId()!=null)
			{
				if(so.getPackageTypeId().getPackageTypeId()!=null)
					strvalues[i]=so.getPackageTypeId().getPackageTypeId()+",";
				else
					strvalues[i]=",";
			}
			else
				strvalues[i]=",";
			i++;
			
			strheader[i]="Ref 1,";
			if(so.getReferenceOne()!=null)
				strvalues[i]=so.getReferenceOne()+",";
			else
				strvalues[i]=",";
			i++;
			
			strheader[i]="Ref 2,";
			if(so.getReferenceTwo()!=null)
				strvalues[i]=so.getReferenceTwo()+",";
			else
				strvalues[i]=",";
			i++;
			/*
			strheader[i]="Ref 3,";
			strvalues[i]=so.getPack().getReference3()+",";
			i++;
			*/
			double netCharge=0;
			for(Charge c: so.getChargesForInvoice())	// This would repeat for the no of Charges present with an increment by 1 in chargecnt
			{
				netCharge = (FormattingUtil.add(netCharge, c.getCharge().doubleValue())).doubleValue();
			}
			strheader[i]="Net Chrg,";
			strvalues[i]=netCharge+" ,";
			i++;
			
			strheader[i]="Curr,";
			if(invoice.getCurrency()!=null)
				strvalues[i]=invoice.getCurrency()+",";
			else
				strvalues[i]=",";
			i++;
			
			for(Charge c: so.getChargesForInvoice())	// This would repeat for the no of Charges present with an increment by 1 in chargecnt
			{
				chargecnt++;
				strheader[i]="Charge "+chargecnt+" Code,";
				if(c.getChargeCode()!=null)
					strvalues[i]=c.getChargeCode()+",";
				else
					strvalues[i]=",";
				i++;
				
				strheader[i]="Charge "+chargecnt+" Name,";
				if(c.getName()!=null)
					strvalues[i]=c.getName()+",";
				else
					strvalues[i]=",";
				i++;
				
				strheader[i]="Charge "+chargecnt+" Amount,";
				if(c.getCharge()!=null)
					strvalues[i]=c.getCharge()+",";
				else
					strvalues[i]=",";
				i++;
			}
			chargecnt++;
			for(int a=chargecnt;a<=10;a++) 	// Limit to 10 Charge Code / Name / Amount values.
			{
				strheader[i]="Charge "+a+" Amount,";
				strvalues[i]=",";
				i++;
				
				strheader[i]="Charge "+a+" Name,";
				strvalues[i]=",";
				i++;
				
				strheader[i]="Charge "+a+" Amount,";
				strvalues[i]=",";
				i++;
				
			}
			
			strheader[i]="Pcs,";
			if(so.getQuantity()!=null)
				strvalues[i]=so.getQuantity()+",";
			else
				strvalues[i]=",";
			i++;
			
			strheader[i]="Bill Weight,";
			if(so.getBilledWeight()!=null)
				strvalues[i]=so.getBilledWeight()+",";
			else
				strvalues[i]=",";
			i++;

			strheader[i]="Bill Weight Unit,";
			if(so.getBilledWeightUOM()!=null)
				strvalues[i]=so.getBilledWeightUOM()+",";
			else
				strvalues[i]=",";
			i++;

			strheader[i]="Quoted Weight,";
			if(so.getQuotedWeight()!=null)
				strvalues[i]=so.getQuotedWeight()+",";
			else
				strvalues[i]=",";
			i++;
			
			strheader[i]="Quoted Weight UOM,";
			if(so.getQuotedWeightUOM()!=null)
				strvalues[i]=so.getQuotedWeightUOM()+",";
			else
				strvalues[i]=",";
			i++;
			
			//From Address Information
			strheader[i]="Shipper Name,";
			if(so.getFromAddress()!=null)
			{
				if(so.getFromAddress().getContactName()!=null)
					strvalues[i]=so.getFromAddress().getContactName()+",";
				else
					strvalues[i]=",";
			}
			else
				strvalues[i]=",";
			i++;
			
			strheader[i]="Shipper Company,";
			if(so.getFromAddress()!=null)
			{
				if(so.getFromAddress().getAbbreviationName()!=null)
					strvalues[i]=so.getFromAddress().getAbbreviationName()+",";
				else
					strvalues[i]=",";
			}
			else
				strvalues[i]=",";
			i++;
			
			strheader[i]="Shipper Address 1,";
			if(so.getFromAddress()!=null)
			{
				if(so.getFromAddress().getAddress1()!=null && so.getFromAddress().getAddress1().contains(","))
					strvalues[i]=StringUtil.replace(so.getFromAddress().getAddress1(), ",", " ", true)+",";
				else if(so.getFromAddress().getAddress1()==null)
					strvalues[i]=",";
				else
					strvalues[i]= so.getFromAddress().getAddress1()+",";
			}
			else
				strvalues[i]=",";
			i++;
			
			strheader[i]="Shipper Address 2,";
			if(so.getFromAddress()!=null)
			{
				if(so.getFromAddress().getAddress2()!=null && so.getFromAddress().getAddress2().contains(","))
					strvalues[i]=StringUtil.replace(so.getFromAddress().getAddress2(), ",", " ", true)+",";
				else if(so.getFromAddress().getAddress2()==null)
					strvalues[i]=",";
				else
					strvalues[i]= so.getFromAddress().getAddress2()+",";
			}
			else
				strvalues[i]=",";
			i++;
			
			strheader[i]="Shipper City,";
			if(so.getFromAddress()!=null)
			{
				if(so.getFromAddress().getCity()!=null)
					strvalues[i]=so.getFromAddress().getCity()+",";
				else
					strvalues[i]=",";
			}
			else
				strvalues[i]=",";
			i++;
			
			strheader[i]="ST,";
			if(so.getFromAddress()!=null)
			{
				if(so.getFromAddress().getProvinceCode()!=null)
					strvalues[i]=so.getFromAddress().getProvinceCode()+",";
				
				else
					strvalues[i]=",";
			}
			else
				strvalues[i]=",";
			i++;
			
			strheader[i]="Postal,";
			if(so.getFromAddress()!=null)
			{
				if(so.getFromAddress().getPostalCode()!=null)
					strvalues[i]=so.getFromAddress().getPostalCode()+",";
				else
					strvalues[i]=",";
			}
			else
				strvalues[i]=",";
			i++;
								
			strheader[i]="Cntry1,";
			if(so.getFromAddress()!=null)
			{
				if(so.getFromAddress().getCountryName()!=null)
					strvalues[i]=so.getFromAddress().getCountryName()+",";
				else
					strvalues[i]=",";
			}
			else
				strvalues[i]=",";
			i++;
						
			//To Address Information
			strheader[i]="Recipient Name,";
			if(so.getToAddress()!=null)
			{
				if(so.getToAddress().getContactName()!=null)
					strvalues[i]=so.getToAddress().getContactName()+",";
				else
					strvalues[i]=",";
			}
			else
				strvalues[i]=",";
			i++;
			
			strheader[i]="Recipient Company,";
			if(so.getToAddress()!=null)
			{
				if(so.getToAddress().getAbbreviationName()!=null)
					strvalues[i]=so.getToAddress().getAbbreviationName()+",";
				else
					strvalues[i]=",";
			}
			else
				strvalues[i]=",";
			i++;
			
			strheader[i]="Recipient Address 1,";
			if(so.getToAddress()!=null)
			{
				if(so.getToAddress().getAddress1()!=null && so.getToAddress().getAddress1().contains(","))
					strvalues[i]=StringUtil.replace(so.getToAddress().getAddress1(), ",", " ", true);
				else if(so.getToAddress().getAddress1()==null)
					strvalues[i]=",";
				else
					strvalues[i]=so.getToAddress().getAddress1()+",";
			}
			else
				strvalues[i]=",";
			i++;
			
			strheader[i]="Recipient Address 2,";
			if(so.getToAddress()!=null)
			{
				if(so.getToAddress().getAddress2()!=null && so.getToAddress().getAddress2().contains(","))
					strvalues[i]=StringUtil.replace(so.getToAddress().getAddress2(), ",", " ", true);
				else if(so.getToAddress().getAddress2()==null)
					strvalues[i]=",";
				else
					strvalues[i]=so.getToAddress().getAddress2()+",";
			}
			else
				strvalues[i]=",";
			i++;
			
			strheader[i]="Recipient City,";
			if(so.getToAddress()!=null)
			{
				if(so.getToAddress().getCity()!=null)
					strvalues[i]=so.getToAddress().getCity()+",";
				else
					strvalues[i]=",";
			}
			else
				strvalues[i]=",";
			i++;
			
			strheader[i]="ST2,";
			if(so.getToAddress()!=null)
			{
				if(so.getToAddress().getProvinceCode()!=null)
					strvalues[i]=so.getToAddress().getProvinceCode()+",";
				else
					strvalues[i]=",";
			}
			else
				strvalues[i]=",";
			i++;
			
			strheader[i]="Postal2,";
			if(so.getToAddress()!=null)
			{
				if(so.getToAddress().getPostalCode()!=null)
					strvalues[i]=so.getToAddress().getPostalCode()+",";
				else
					strvalues[i]=",";
			}
			else
				strvalues[i]=",";
			i++;
			
			strheader[i]="Cntry2,";
			if(so.getToAddress()!=null)
			{
				if(so.getToAddress().getCountryName()!=null)
					strvalues[i]=so.getToAddress().getCountryName()+",";
				else
					strvalues[i]=",";
			}
			else
				strvalues[i]=",";
			i++;
			/*
			strheader[i]="Dlvry Date,";
			if(so.getDateOfDelivery()!=null)
				strvalues[i]=so.getDateOfDelivery()+",";
			else
				strvalues[i]=",";
			i++;
			
			strheader[i]="Time,";
			if(so.getScheduledAt()!=null)
				strvalues[i]=so.getScheduledAt()+",";
			else
				strvalues[i]=",";
			i++;
			
			strheader[i]="Signature,";
			if(so.getSignatureRequired()!=null)
				strvalues[i]=so.getSignatureRequired()+",";
			else
				strvalues[i]=",";
			i++;
			*/
			strheader[i]="\n";
			strvalues[i]="\n";
			i++;
			j++;
			
			//Copy data from String arrays to String Buffer only once
			if(j==1)
			{
				for(int h=0;h<i;h++)
				{
					stbheader.append(strheader[h]);
				}
			}// end of condition check for j == 1
			for(int hv=0;hv<i;hv++)
			{
				stbheadervalues.append(strvalues[hv]);
			}
			try 
			{
				if(j==1)
					ostream.write(stbheader.toString().getBytes()); // Write the Header columns only once.
					ostream.write(stbheadervalues.toString().getBytes());
					log.debug("Wrote Line for Order Id: "+so.getId());
					
			}
			catch (Exception e) 
			{
				log.error("Could not write header for the Invoice :"+invoiceId);
				continue;
			}
		}
		try 
		{
			ostream.flush();
		} 
		catch (Exception e) 
		{
			log.error("OutputStream Exception :"+e);
			e.printStackTrace();
		}
		return true;
	}
	
	
}