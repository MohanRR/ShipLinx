package com.meritconinc.shiplinx.service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
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

import com.meritconinc.mmr.exception.CardProcessingException;
import com.meritconinc.mmr.exception.CreditOverrunException;
import com.meritconinc.mmr.model.admin.UserSearchCriteria;
import com.meritconinc.mmr.utilities.MessageUtil;
import com.meritconinc.mmr.utilities.MmrBeanLocator;
import com.meritconinc.mmr.utilities.StringUtil;
import com.meritconinc.mmr.utilities.mail.MailHelper;
import com.meritconinc.shiplinx.carrier.CarrierService;
import com.meritconinc.shiplinx.carrier.dhl.DHLAPI;
import com.meritconinc.shiplinx.dao.CarrierServiceDAO;
import com.meritconinc.shiplinx.exception.ShiplinxException;
import com.meritconinc.shiplinx.model.Business;
import com.meritconinc.shiplinx.model.CCTransaction;
import com.meritconinc.shiplinx.model.Carrier;
import com.meritconinc.shiplinx.model.CarrierChargeCode;
import com.meritconinc.shiplinx.model.Charge;
import com.meritconinc.shiplinx.model.ChargeGroup;
import com.meritconinc.shiplinx.model.CreditUsageReport;
import com.meritconinc.shiplinx.model.CustomerCarrier;
import com.meritconinc.shiplinx.model.CustomsInvoice;
import com.meritconinc.shiplinx.model.Markup;
import com.meritconinc.shiplinx.model.OrderStatus;
import com.meritconinc.shiplinx.model.Pickup;
import com.meritconinc.shiplinx.model.Province;
import com.meritconinc.shiplinx.model.Rating;
import com.meritconinc.shiplinx.model.Service;
import com.meritconinc.shiplinx.model.ShippingOrder;
import com.meritconinc.shiplinx.service.impl.InvoiceManagerImpl;
import com.meritconinc.shiplinx.utils.CarrierErrorMessage;
import com.meritconinc.shiplinx.utils.FormattingUtil;
import com.meritconinc.shiplinx.utils.PDFRenderer;
import com.meritconinc.shiplinx.utils.ShiplinxConstants;

public class CarrierServiceManagerImpl implements CarrierServiceManager, Runnable {
	private static final Logger log = LogManager.getLogger(CarrierServiceManagerImpl.class);
	private List<CarrierService> carrierServices; //configure the list in application context file
	private CarrierServiceDAO carrierServiceDAO;
	private List<CarrierErrorMessage> errorMessages;
	private CustomerManager customerService;
	private ShippingService shippingService;
	private AddressManager addressService;
	private BusinessManager businessService;
	protected MarkupManager markupManagerService;
	private CreditCardTransactionManager creditCardService;
	private PinBlockManager pinBlockManager;
	private PickupManager pickupService;
	
	public CarrierService carrierServiceThread;
	public CustomerCarrier customerCarrierThread;
	public ShippingOrder orderThread = new ShippingOrder();
	public List<Rating> rateListThread=new ArrayList<Rating>();
	public List<Service> carrierServicesList = new ArrayList<Service>();
	public List<Rating> ratesThread=new ArrayList<Rating>();
	
	private UserSearchCriteria criteria;
	
	private CarrierServiceManagerImpl parentThread;
	
	public CarrierServiceManagerImpl(CarrierService carrierService,List<Service> carrierServicesList,ShippingOrder shippingOrder,List<Rating> rateList,CustomerCarrier customerCarrier, CarrierServiceManagerImpl parentThread)
	{
		this.carrierServiceThread=carrierService;
		this.carrierServicesList =carrierServicesList; 
		this.orderThread=shippingOrder;
		this.rateListThread=rateList;
		this.customerCarrierThread=customerCarrier;
		this.parentThread = parentThread;
	}
	
	public CarrierServiceManagerImpl(){}
	
	public void setBusinessService(BusinessManager businessService) {
		this.businessService = businessService;
	}

	public void setMarkupManagerService(MarkupManager markupManagerService) {
		this.markupManagerService = markupManagerService;
	}

	public void setCreditCardService(CreditCardTransactionManager creditCardService) {
		this.creditCardService = creditCardService;
	}

	public void setPinBlockManager(PinBlockManager pinBlockManager) {
		this.pinBlockManager = pinBlockManager;
	}

	public void setPickupService(PickupManager pickupService) {
		this.pickupService = pickupService;
	}

	public boolean cancelOrder(Long orderId, boolean isAdmin) {
		List<CarrierErrorMessage> cancelErrorMessages = new ArrayList();

		boolean isOrderCanceled=false;
		try {
			ShippingOrder order = shippingService.getShippingOrder(orderId);
			order.setBusiness(businessService.getBusinessById(order.getCustomer().getBusinessId()));
			//get the appropriate account to be used to cancel the shipment
			//Check if the Carrier is null - it will be null for a warehouseOrder
			if(order.getCarrierId()!=null)
			{
				CustomerCarrier customerCarrier = getCarrierAccount(order.getCustomerId(), order.getBusinessId(), order.getCarrierId(), order.getFromAddress().getCountryCode(), order.getToAddress().getCountryCode());
				Carrier carrier = this.carrierServiceDAO.getCarrier(order.getCarrierId());
	
				CarrierService carrierService = getCarrierServiceBean(carrier.getImplementingClass());
				try{
					isOrderCanceled = carrierService.cancelOrder(order, customerCarrier);
				}
				catch (Exception e) {
					log.error("-------Exception----", e);
				}
			}
			else
				isOrderCanceled = true;

			
			//we need to refund the $$ back to the customer
			if(isOrderCanceled || isAdmin){
				order.setStatusId((long)ShiplinxConstants.STATUS_CANCELLED);
				if(order.getCcTransactions()!=null){
			
					for(CCTransaction transaction: order.getCcTransactions()){
						if(transaction.getStatus()==CCTransaction.PROCESSED_STATUS){
							boolean refunded = this.creditCardService.voidCharge(transaction, order.getCustomer());
							order.setPaidAmount(FormattingUtil.add(order.getPaidAmount().doubleValue(), transaction.getAmount()*-1).doubleValue());
						}
					}
				}
				shippingService.updateShippingOrder(order);
			}
		} catch (Exception e) {
			log.error("-------Exception----", e);
		}
		return isOrderCanceled;
	}

	public Object getShippingOrderStatus(ShippingOrder order) {
		// TODO Auto-generated method stub
		return null;
	}


	public List<Rating> rateShipment(ShippingOrder order) {
		List<Rating> ratingList = new ArrayList<Rating>();
		ArrayList threadList=new ArrayList();
		ShippingOrder shippingorderThread=new ShippingOrder();
		cleanupInput(order);
		
		if(order.getToAddress().getProvinceCode()==null)
			order.getToAddress().setProvinceCode("");
		
		ratingList = new ArrayList<Rating>();
		errorMessages = new ArrayList<CarrierErrorMessage>();
		try{
			//get the carrier available to customer
			
			if (order.getCustomer() == null)
				order.setCustomer(customerService.getCustomerInfoByCustomerId(order.getCustomerId()));
			order.setBusinessId(order.getCustomer().getBusinessId());
			order.setBusiness(businessService.getBusinessById(order.getBusinessId()));
			
	        if(order.getScheduledShipDate_web()!=null && order.getScheduledShipDate_web().length()>0){
				Date date = FormattingUtil.getDate(order.getScheduledShipDate_web(), FormattingUtil.DATE_FORMAT_WEB);
				order.setScheduledShipDate(new Timestamp(date.getTime()));
			}
	        else
	        	order.setScheduledShipDate(new Timestamp(Calendar.getInstance().getTimeInMillis()));
	        
			order.setDateCreated(new Timestamp(Calendar.getInstance().getTimeInMillis()));
			
//			On Tue, Apr 24, 2012 at 7:25 PM, Rizwan Merchant <rizwan.merchant@meritconinc.com> wrote:
//				1. During Shipping web service request, we need to perform rating using the service id sent in the ship request "Service" object
//				- When rating is performed in the CarrierServiceImpl, if the service id is present in the order, then invoke only the carrier associated with that service
//				- Loop throuh all the rates and return only the one where the service id mathes
			List<Carrier> carriersForBusiness = new ArrayList<Carrier>();
			long shipServiceId = -1;
			if (order.getService() != null && order.getService().getCarrier() != null) {
				shipServiceId = order.getService().getId();
				carriersForBusiness = new ArrayList<Carrier>();
				carriersForBusiness.add(order.getService().getCarrier());
			} else {
				if(order.getCarrierId_web()!=null && order.getCarrierId_web()> 0)//if a carrier is selected for Quick shipment
					carriersForBusiness.add(this.carrierServiceDAO.getCarrier(order.getCarrierId_web()));
				else
					carriersForBusiness = this.getCarriersForBusiness(order.getBusinessId());
			}
			
			for(Carrier carrier:carriersForBusiness){
				CarrierService carrierService = getCarrierServiceBean(carrier.getImplementingClass());
				
				//get the appropriate account to be used to generate/rate the shipment
				CustomerCarrier customerCarrier = null;
				
				try{
					customerCarrier = getCarrierAccount(order.getCustomerId(), order.getBusinessId(), carrier.getId(), order.getFromAddress().getCountryCode(), order.getToAddress().getCountryCode());
				}
				catch(Exception e){
					log.error("Could not determine the account to use for shipment!", e);
					customerCarrier = null;
				}
				
				if(customerCarrier==null){
					log.warn("No account foound for customer " + order.getCustomerId() + " to use for rating of carrier " + carrier.getName());
					continue;
				}
				
				//if the discount from the carrier is flat across all services, this information is stored in business_carrier table
				//example DHL, Loomis for business 1
				customerCarrier.setBusinessCarrierDiscount(carrier.getBusinessCarrierDiscount());
				//get the rates for all services
				List<Service> carrierServicesList = getServicesForCarrier(carrier.getId());
//				order.setCustomerCarrier(customerCarrier);
				List<Rating> tempRatingList = new ArrayList<Rating>();
				try{
					//tempRatingList = carrierService.rateShipment(order, carrierServicesList, carrier.getId(), customerCarrier);
					//if(tempRatingList != null)
					//	ratingList.addAll(tempRatingList);
					shippingorderThread = order;
					CarrierServiceManagerImpl carrierServiceManagerImpl = new CarrierServiceManagerImpl(carrierService,carrierServicesList,shippingorderThread,ratingList,customerCarrier, this);
					Thread t=new Thread(carrierServiceManagerImpl);
					threadList.add(t);
					t.start();
				}
				catch(ShiplinxException e) {
					for(String s :e.getErrorMessages()){
						CarrierErrorMessage message = new CarrierErrorMessage(carrier.getId(),customerCarrier.getCarrierName() + " : " + s);					
						this.getParentThread().getErrorMessages().add(message);	
					}
					continue;
				}
			}
			
			for(int i=0;i<threadList.size();i++)
			{
				Thread t1=(Thread)threadList.get(i);
				try {
					t1.join();
				} catch (InterruptedException e) {
					log.debug("Thread is Interrupted"+e.getMessage());
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
			setRateListThread(rateListThread);
			setOrderThread(orderThread);
			setErrorMessages(errorMessages);
			//process the rates before presenting
			if(order.getServiceId_web()!=null && order.getServiceId_web()>0)
				shipServiceId = order.getServiceId_web();
			processRates(ratingList, order, shipServiceId);
			Collections.sort(ratingList, Rating.PriceComparator);//Sorting the Total of Rate
			
			//call the Quick Ship Filter is Quick Ship is requested
			if(ratingList.size()>0 && (order.isCheapestMethod() || order.isFastestMethod()))//filter only if one of the options: cheapest or fastest is selected.
					ratingList = quickShipFilter(ratingList, order);
		}		
		
		catch (Exception e) {
			log.debug("-------Exception----"+e);
			e.printStackTrace();
		}
		return ratingList;
	}
	
	private List<Rating> quickShipFilter(List<Rating> ratingList, ShippingOrder order)
	{
		log.debug("Inside quickShipFilter method---------------------------");
		List<Rating> returnRatingList = new ArrayList<Rating>();
			
		if(order.isFastestMethod() && order.isCheapestMethod())
		{
			//find the fastest transit time
			int fastestTime = 999999;
			for(Rating r:ratingList){
				if(r.getTransitDays()>0 && r.getTransitDays()<fastestTime)
					fastestTime = r.getTransitDays();
			}
			for(Rating r: ratingList)
			{
				//now get all those that are the "fastest"
				if(r.getTransitDays() == fastestTime)
					returnRatingList.add(r);
			}
			//now get the cheapest of all rates for the selected carrier.
			return findMinimumRating(returnRatingList);
		}
		else if(!order.isFastestMethod() && order.isCheapestMethod())
		{
			return findMinimumRating(ratingList);
		}
		else if(order.isFastestMethod() && !order.isCheapestMethod())
		{
			//find the fastest transit time
			int fastestTime = 999999;
			for(Rating r:ratingList){
				if(r.getTransitDays()>0 && r.getTransitDays()<fastestTime)
					fastestTime = r.getTransitDays();
			}
			for(Rating r: ratingList)
			{
				//now get all those that are the "fastest"
				if(r.getTransitDays() == fastestTime)
					returnRatingList.add(r);
			}
			//According to Jay, if fastest is chosen, then return the fastest cheapest - Jan 22 2013
			return findMinimumRating(returnRatingList);
		}
		
		return returnRatingList;
	}
	
	private List<Rating> findMinimumRating(List<Rating> RatingList)
	{
		List<Rating> minRatingList = new ArrayList<Rating>();
		double temp = 0.0;
		if(RatingList.size() > 0)
		{
			temp = RatingList.get(0).getTotal();
			
			for(Rating r: RatingList)
			{
				if(r.getTotal() <= temp)
					minRatingList.add(r);
				else
					break;
			}
		}
		return minRatingList;
	}
	
	private List<CustomerCarrier> getCarrierAccounts(Long customerId,
			String countryName) {
		return getCarrierServiceDAO().getCarrierAccounts(customerId, countryName);
	}


	
	public long createPickup(Pickup pickup) throws Exception{

		long pickupId;
		try {
			errorMessages = new ArrayList<CarrierErrorMessage>();
			CustomerCarrier customerCarrier = getCarrierAccount(pickup.getCustomerId(), pickup.getBusinessId(), pickup.getCarrierId(), pickup.getAddress().getCountryCode(), pickup.getDestinationCountryCode());
			Carrier carrier = this.carrierServiceDAO.getCarrier(pickup.getCarrierId());
			Service service = carrierServiceDAO.getService(pickup.getServiceId());
			
			pickup.setServiceCode(service.getCode());

			CarrierService carrierService = getCarrierServiceBean(carrier.getImplementingClass());
			
			pickup.setCarrierAccount(customerCarrier);
			
			carrierService.requestPickup(pickup);
			
			//do not associate this address with customer, otherwise it shows up on address book
			pickup.getAddress().setCustomerId(0);
			pickup.getAddress().setDefaultFromAddress(false);
			pickup.getAddress().setDefaultToAddress(false);
			
			//save the pick up information
			long addressId = addressService.add(pickup.getAddress());
			pickup.setAddressId(addressId);
			pickup.setStatus(ShiplinxConstants.STATUS_PICKUP_ACTIVE);
			pickupId = pickupService.add(pickup);
			if(!StringUtil.isEmpty(pickup.getConfirmationNum()))
				sendPickupNotificationMail(pickup);
		}catch(ShiplinxException e) {
			log.error("Error in pick up request!", e);
			for(String s :e.getErrorMessages()){
				CarrierErrorMessage message = new CarrierErrorMessage(pickup.getCarrierId()," : " + s);					
				errorMessages.add(message);	
			}
			throw e;
		}catch(Exception e) {
			log.error("Pick up creation error",e);
			throw e;
		}		
		return pickupId;
	}
	
	private boolean sendPickupNotificationMail(Pickup pickup)
	{
		boolean retval=false;
		String toAddress = null;
		Business business = businessService.getBusinessById(pickup.getBusinessId());
				
		toAddress = pickup.getAddress().getEmailAddress();
		if(toAddress==null || toAddress.length()==0){
			log.error("User's email address is not sent, cannot send an email quote!");
			return false;
		}
		try{
		// GROUP_EMAIL_ADDRESS_en_CA

		String subject = MessageUtil.getMessage("label.subject.pickup.notification");
		
		String body = MessageUtil.getMessage("mail.pickup.notification.body");
	
		if(body== null || body.length()==0){
			log.error("Cannot find template to send pickup notification");
			return false;
		}
		body = new String(body.replaceAll("%CONF", pickup.getConfirmationNum()));
		body = new String(body.replaceAll("%ATTENTION", pickup.getAddress().getContactName()));
		body = new String(body.replaceAll("%ShipDate", FormattingUtil.getFormattedDate(pickup.getPickupDate(),FormattingUtil.DATE_FORMAT_WEB)+""));
		body = new String(body.replaceAll("%SFROMCOMPANY", pickup.getAddress().getAbbreviationName()));
		body = new String(body.replaceAll("%SFROMADDRESS1", pickup.getAddress().getAddress1()));
		body = new String(body.replaceAll("%SFROMADDRESS2%", pickup.getAddress().getAddress2()));
		body = new String(body.replaceAll("%SFROMZIP", pickup.getAddress().getPostalCode()));
		body = new String(body.replaceAll("%SFROMPROVINCE",pickup.getAddress().getProvinceCode()));
		body = new String(body.replaceAll("%SFROMCOUNTRY", pickup.getAddress().getCountryCode()));
		body = new String(body.replaceAll("%SFROMCITY", pickup.getAddress().getCity()));
		
		body = new String(body.replaceAll("%READYHR", pickup.getReadyHour()));
		body = new String(body.replaceAll("%READYMIN", pickup.getReadyMin()));
		body = new String(body.replaceAll("%CLOSEHR", pickup.getCloseHour()));
		body = new String(body.replaceAll("%CLOSEMIN", pickup.getCloseMin()));
		body = new String(body.replaceAll("%LOCATION", pickup.getPickupLocation()));
		body = new String(body.replaceAll("%REFERENCE", pickup.getPickupReference()));
		body = new String(body.replaceAll("%INSTRUCTIONS", pickup.getInstructions()));

		body = new String(body.replaceAll("%CARRIERSERVICE", carrierServiceDAO.getCarrier(pickup.getCarrierId()).getName()+" - "+shippingService.getServiceById(pickup.getServiceId()).getName()));
		body = new String(body.replaceAll("%QUANTITY", pickup.getQuantity()+" Pcs"));
		body = new String(body.replaceAll("%TOTALWEIGHT", pickup.getTotalWeight()+""));
		body = new String(body.replaceAll("%WEIGHTUNITS", pickup.getWeightUnit())); 
		
		
		retval= MailHelper.sendEmailNow2(business.getSmtpHost(), business.getSmtpUsername(), business.getSmtpPassword(), business.getName(), business.getSmtpPort(), business.getAddress().getEmailAddress(), toAddress, null, subject, body, null, true);
	} 
	catch (MessagingException e) {
		log.error("Error sending email - Messaging Exception: ", e);
		retval = false;
	}
	catch(Exception e)
	{
		log.error("Error sending email - Exception: " ,e);
		retval = false;
	}	
	
		return retval;
	}
	
	public boolean cancelPickup(Pickup pickup) throws Exception{

		try {
			CustomerCarrier customerCarrier = getCarrierAccount(pickup.getCustomerId(), pickup.getBusinessId(), pickup.getCarrierId(), pickup.getAddress().getCountryCode(), pickup.getDestinationCountryCode());
			Carrier carrier = this.carrierServiceDAO.getCarrier(pickup.getCarrierId());

			CarrierService carrierService = getCarrierServiceBean(carrier.getImplementingClass());
			
			pickup.setCarrierAccount(customerCarrier);
			pickup.setStatus(ShiplinxConstants.STATUS_PICKUP_CANCELLED);
			pickupService.updatePickup(pickup); //We will set the pick up status to cancelled irrespective of what the carrier returns
			
			boolean cancelled = carrierService.cancelPickup(pickup);

			return cancelled;
			
		}
		catch(Exception e) {
			log.error("Error in cancel pick up with id" + pickup.getPickupId(), e);
			throw e;
			
		}		
	}


	private CustomerCarrier getCarrierAccount(long customerId, long businessId, long carrierId, String fromCountryCode, String toCountryCode) {
		
		CustomerCarrier carrierAccount = null;
		
		//First look for customer specific
		carrierAccount = carrierServiceDAO.getCarrierAccount(customerId, businessId , carrierId, fromCountryCode, toCountryCode);
		if(carrierAccount!=null)
			return carrierAccount;

		carrierAccount = carrierServiceDAO.getCarrierAccount(customerId, businessId , carrierId, fromCountryCode, null);
		if(carrierAccount!=null)
			return carrierAccount;

		carrierAccount = carrierServiceDAO.getCarrierAccount(customerId, businessId, carrierId, toCountryCode, null);
		if(carrierAccount!=null)
			return carrierAccount;

		carrierAccount = carrierServiceDAO.getCarrierAccount(customerId, businessId, carrierId, null, null);
		if(carrierAccount!=null)
			return carrierAccount;

		//Check the defaults now
		carrierAccount = carrierServiceDAO.getCarrierAccount(new Long(0), businessId , carrierId, fromCountryCode, toCountryCode);
		if(carrierAccount!=null)
			return carrierAccount;

		carrierAccount = carrierServiceDAO.getCarrierAccount(new Long(0), businessId, carrierId, fromCountryCode, null);
		if(carrierAccount!=null)
			return carrierAccount;

		carrierAccount = carrierServiceDAO.getCarrierAccount(new Long(0), businessId, carrierId, toCountryCode, null);
		if(carrierAccount!=null)
			return carrierAccount;

		carrierAccount = carrierServiceDAO.getCarrierAccount(new Long(0), businessId, carrierId, null, null);
		if(carrierAccount!=null)
			return carrierAccount;

		return carrierAccount;
	}

	public CarrierService shipOrder(ShippingOrder order, Rating rate)  throws Exception{
		log.debug("-------shipOrder----");
		CarrierService cs = null;
		if (errorMessages != null) {
			errorMessages.clear();
			errorMessages = null;
		}
		errorMessages = new ArrayList<CarrierErrorMessage>();

		if (rate == null) {
//			Most likely this call is from web service
//			On Tue, Apr 24, 2012 at 7:25 PM, Rizwan Merchant <rizwan.merchant@meritconinc.com> wrote:
//			1. During Shipping web service request, we need to perform rating using the service id sent in the ship request "Service" object
//			- When rating is performed in the CarrierServiceImpl, if the service id is present in the order, then invoke only the carrier associated with that service
//			- Loop throuh all the rates and return only the one where the service id mathes
			if (order != null && order.getService() != null && order.getService().getId() != null) {
				//Run address validation to ensure we have the right information
				addressService.runAddressValidation(order.getFromAddress());
				addressService.runAddressValidation(order.getToAddress());
				
				// Service code is service id, therefore retrieve service based on service id
				Service service = this.shippingService.getServiceById(order.getService().getId());
				if (service != null && service.getCarrier() != null) {
					order.setCarrierId(service.getCarrierId());
					order.setCarrierName(service.getCarrier().getName());
					order.setService(service);
					List<Rating> ratings = this.rateShipment(order);
					if (ratings != null && ratings.size() > 0) {
						rate = ratings.get(0);
//						order.setCustomerCarrier(rate.getCustomerCarrier());
					}
				}
			}
		}
		// Make sure rates are retrieved
		if (rate == null) {
			if (order.getCarrierId() != null)
				errorMessages.add(new CarrierErrorMessage(order.getCarrierId(),MessageUtil.getMessage("rate.not.found.for.specified.service", MessageUtil.getLocale())));
			else
				errorMessages.add(new CarrierErrorMessage(order.getCarrierId(),MessageUtil.getMessage("rate.not.found.for.specified.service", MessageUtil.getLocale())));
			throw new ShiplinxException();
		}
		// ----------------------------------------------------------------
		
		cleanupInput(order);

		if (order.getCustomer() == null)
			order.setCustomer(customerService.getCustomerInfoByCustomerId(order.getCustomerId()));

		//If the credit card information has not been set on the payment page, it means that either customer is a credit customer or has an active cc profile on file, try to retrieve the active cc profile 
		if(order.getCreditCard()==null && order.getCustomer().getCreditCardActive()!=null){
			order.setPaymentRequired(true);
			order.setCreditCard(order.getCustomer().getCreditCardActive());
		}
		
		//check if customer has crossed credit limit, if credit limit is set to 0 means no limit
		if(order.getCustomer().getCreditLimit() > 0){
			CreditUsageReport cur = this.customerService.getCreditUsageReport(order.getCustomerId(), 
																order.getCustomer().getBusinessId());
			if(cur.getTotalCreditUsed() > order.getCustomer().getCreditLimit()){
				errorMessages.add(new CarrierErrorMessage(order.getCarrierId(),MessageUtil.getMessage("error.credit.overrun", MessageUtil.getLocale())));
				throw new CreditOverrunException(cur);
			}
		}
		
		Carrier carrier = this.carrierServiceDAO.getCarrier(rate.getCarrierId());
		CarrierService carrierService = getCarrierServiceBean(carrier.getImplementingClass());
		if(!StringUtil.isEmpty(rate.getCurrency()))
			order.setCurrency(rate.getCurrency());
		else
			order.setCurrency(ShiplinxConstants.CURRENCY_CA_STRING); //hard-coding for now, should be based on business default
		
		//get customer Carreir for cust id and carrier id 
		//CustomerCarrier carrierData = getCarrierServiceDAO().getCutomerCarrierDefaultAccount(order.getCarrierId(),order.getCustomerId());
		
		
	//if (rate.getInstanceAPIName().indexOf(apiName) > 0) {
		//ship the order
		cs = carrierService;
		order.setCarrierId(rate.getCarrierId());
		order.setServiceName(rate.getServiceName());
		order.setServiceId(rate.getServiceId());
		order.setOriginalServiceId(rate.getOriginalServiceId());
		order.setService(getService(order.getServiceId()));
		order.setStatusId(new Long(ShiplinxConstants.STATUS_DISPATCHED));
		//order.getCharges().addAll(rate.getCharges());
		order.setCharges(rate.getCharges());
		order.setFuelPercent((float)rate.getFuel_perc());
						
		order.setEdiVerified(false);
		
//		order.setCustomerCarrier(rate.getCustomerCarrier());
		String[] pins = this.pinBlockManager.getNewPrefixedPinNumbers(ShiplinxConstants.PIN_TYPE_ORDER_NUMBERS, 1, order.getBusinessId());
		order.setOrderNum(pins[0]);
		
		//if customer account is inactive, do not allow shipment
		if(order.getCustomer().getActive() == false){
			errorMessages.add(new CarrierErrorMessage(order.getCarrierId(),MessageUtil.getMessage("error.account.notactive", MessageUtil.getLocale())));
			throw new ShiplinxException();
		}
		
		try{
			if(order.isPaymentRequired()){
				CCTransaction transaction = this.creditCardService.authorizeCard(1.0, order.getCreditCard(), order.getCustomer());
				if(transaction == null)
					throw new CardProcessingException("");
				if(transaction.getStatus() == CCTransaction.DECLINED_STATUS)
					throw new CardProcessingException(transaction.getProcMessage());
			}
			//authorization went through, card is good
			
			long pickupId = 0;
			//schedule pick up if requested
			if(order.getPickup() != null && order.getPickup().isPickupRequired()){
				copyFromOrderToPickup(order, rate);
				pickupId = createPickup(order.getPickup());
			}
			
			carrierService.shipOrder(order, rate, rate.getCustomerCarrier());
			//save the markup information
			order.setMarkPercent(rate.getMarkupPercentage());
			order.setMarkType(rate.getMarkupType());
			order.setQuotedWeight((float)rate.getBillWeight());
			order.setQuotedWeightUOM(rate.getBillWeightUOM());
			order.setTrackingURL(cs.getTrackingURL(order));
			
			// *FKhan* - 4 Jan. 2012 - LTL Per Pound Shipment
			if (rate.getRatedAsWeight() > 0)
				order.setRatedAsWeight(rate.getRatedAsWeight());
			//---------------------------------------------------
			
			if (order.getId() != null && order.getId().longValue() > 0) {
				// Most likely this order is from Batch Shipment and it was already saved
				// therefore order needs to be updated
				shippingService.update(order);
			} else {
				shippingService.save(order);
			}
			
			//if no trackin # is assigned, then use the order Id
			if(StringUtil.isEmpty(order.getMasterTrackingNum())){
				order.setMasterTrackingNum(order.getId().toString());
			}
			else{
				order.setTrackingURL(carrierService.getTrackingURL(order));
			}
			shippingService.updateShippingOrder(order);
			
			order.setPaymentRequired(shippingService.isPaymentRequired(order));
			//if pickup was scheduled, then save order id in the pick up record
			if(pickupId > 0){
				order.getPickup().setOrderId(order.getId());
				order.getPickup().setStatus(ShiplinxConstants.STATUS_PICKUP_ACTIVE);
				pickupService.updatePickup(order.getPickup());
			}
			
			//shipment went through, charge card if needed				
			if(order.isPaymentRequired()){
				CCTransaction transaction = shippingService.processPayment(order, null);
				order.getCcTransactions().add(transaction);
			}
			//check if template is set in business, then only send mail if it is set.
			Business business = businessService.getBusinessById(order.getBusinessId());
			if(!StringUtil.isEmpty(business.getShipOrderNotificationBody()))
			{
				sendOrderShippedEmailNotification(order);
			}
		}catch(ShiplinxException e) {
			log.error("Error in ship order!", e);
			if(order.getPickup().getPickupId() > 0) //pick up was scheduled, need to cancel it
				try{
					cancelPickup(order.getPickup());
				}
			catch(Exception ex){}
			for(String s :e.getErrorMessages()){
				CarrierErrorMessage message = new CarrierErrorMessage(carrier.getId()," : " + s);					
				errorMessages.add(message);	
			}
			throw e;
		}catch(Exception e) {
			log.debug("------------------Exception----------------",e);
			if(order.getPickup().getPickupId() > 0) //pick up was scheduled, need to cancel it
				try{
					cancelPickup(order.getPickup());
				}
			catch(Exception ex){}
			throw e;
		}
	

		return cs;
	}
	
	private boolean sendOrderShippedEmailNotification(ShippingOrder so)
	{
		boolean retval=true;
		String toAddress = so.getToAddress().getEmailAddress();
		
		if(toAddress==null || toAddress.length()==0){
			log.error("User's email address is not set, cannot send an shipment notification!");
			return false;
		}
		try{
		// GROUP_EMAIL_ADDRESS_en_CA
		//String locale = user.getLocale();
		
		String subject = MessageUtil.getMessage(
				so.getBusiness().getShipOrderNotificationSubject());
		subject = new String(subject.replaceAll("%COMPANYNAME", so.getFromAddress().getAbbreviationName()));
		
		String body = MessageUtil.getMessage(
				so.getBusiness().getShipOrderNotificationBody());
	
		if(body== null || body.length()==0){
			log.error("Cannot find template to send order creation notification for customer " + so.getCustomer().getName());
			return false;
		}
		
		body = new String(body.replaceAll("%TOADDRESSNAME", so.getToAddress().getContactName()));
		body = new String(body.replaceAll("%FROMCOMPANYNAME", so.getFromAddress().getAbbreviationName()));
		body = new String(body.replaceAll("%CARRIERNAME", so.getCarrierName()));
		if(!StringUtil.isEmpty(so.getReferenceCode()))
			body = new String(body.replaceAll("%SHIPMENTNO", "#" + so.getReferenceCode()));
		else
			body = new String(body.replaceAll("%SHIPMENTNO", "#" + String.valueOf(so.getId())));
		body = new String(body.replaceAll("%ORDERNO", String.valueOf(so.getId())));
		body = new String(body.replaceAll("%TOADD_ABBREVIATION", so.getToAddress().getAbbreviationName()));
		body = new String(body.replaceAll("%SERVICENAME", so.getService().getName()));
		body = new String(body.replaceAll("%TOADD_ADDRESS1",so.getToAddress().getAddress1()));
		body = new String(body.replaceAll("%TOADD_ADDRESS2",so.getToAddress().getAddress2()));
		body = new String(body.replaceAll("%TOADD_CITY",so.getToAddress().getCity()));
		body = new String(body.replaceAll("%TOADD_PROVINCECODE",so.getToAddress().getProvinceCode()));
		body = new String(body.replaceAll("%TOADD_POSTALCODE",so.getToAddress().getPostalCode()));
		body = new String(body.replaceAll("%TOADD_COUNTRYCODE",so.getToAddress().getCountryCode()));
		body = new String(body.replaceAll("%TOADD_CONTACTNAME",so.getToAddress().getContactName()));
		body = new String(body.replaceAll("%TOADD_PHONENO",so.getToAddress().getPhoneNo()));
		body = new String(body.replaceAll("%CARRIER",so.getCarrierName()));
		body = new String(body.replaceAll("%SERVICE",so.getServiceName()));
		body = new String(body.replaceAll("%TRACKINGNUMBER",so.getMasterTrackingNum()));
		body = new String(body.replaceAll("%TRACKINGURL","\""+so.getTrackingURL()+"\""));
		body = new String(body.replaceAll("%BUSINESSLOGO","\""+so.getBusiness().getLogoURL()+"\""));
		
		
		List<String> bccAddresses = new ArrayList<String>();
		//bccAddresses.add(user.getBusiness().getAddress().getEmailAddress());
		
		retval= MailHelper.sendEmailNow2(so.getBusiness().getSmtpHost(), so.getBusiness().getSmtpUsername(), so.getBusiness().getSmtpPassword(), so.getBusiness().getName(), so.getBusiness().getSmtpPort(), so.getBusiness().getAddress().getEmailAddress(), toAddress, bccAddresses, subject, body, null, true);
	} 
	catch (MessagingException e) {
		log.error("Error sending email - Messaging Exception: ", e);
		retval = false;
	}
	catch(Exception e)
	{
		log.error("Error sending email - Exception: " ,e);
		retval = false;
	}		
		return retval;
	}

	public List<CarrierService> getCarrierServices() {
		return carrierServices;
	}
	public void setCarrierServices(List<CarrierService> carrierServices) {
		this.carrierServices = carrierServices;
	}

	public List<CustomerCarrier> getCutomerCarrier(Long customerId){
		return getCarrierServiceDAO().getCutomerCarrier(customerId);
	}
	
	public List<CustomerCarrier> getAllCutomerCarrier(long businessId, long customerId){
		return getCarrierServiceDAO().getAllCutomerCarrier(businessId, customerId);
	}
	
	public List<CustomerCarrier> getAllCustomersCarrier(long businessId, long carrierId)
	{
		return getCarrierServiceDAO().getAllCustomersCarrier(businessId, carrierId);
	}
	
	public List<Carrier> getCarriersForBusiness(long businessId){
		return getCarrierServiceDAO().getCarriersForBusiness(businessId);
	}


	public CustomerCarrier getOrderCutomerCarrier(Long orderId){
		return getCarrierServiceDAO().getOrderCutomerCarrier(orderId);
	}
	
	public CarrierServiceDAO getCarrierServiceDAO() {
		return carrierServiceDAO;
	}

	public void setCarrierServiceDAO(CarrierServiceDAO carrierServiceDAO) {
		this.carrierServiceDAO = carrierServiceDAO;
	}

	public List<Service> getServicesForCarrier(Long carrierId) {
		return getCarrierServiceDAO().getServicesForCarrier(carrierId);
	}
	
	public Service getService(Long serviceId){
		return getCarrierServiceDAO().getService(serviceId);
	}

	public List<CarrierErrorMessage> getErrorMessages() {
		return errorMessages;
	}
	
	public void setErrorMessages(List<CarrierErrorMessage> errorMessages) {
		this.errorMessages = errorMessages;
	}

	/**
	 * Shipping Label
	 */
	public void getShippingLabel(ShippingOrder shippingOrder,OutputStream outputStream) {

		try{
			//get the carrier available to customer
			//List<CustomerCarrier> customerCarrierList = getCutomerCarrier(shippingOrder.getCustomerId());
			PDFRenderer pdfRenderer = new PDFRenderer();			
			ArrayList<String> srcList = new ArrayList<String>();
			String shippingLabelFileName = pdfRenderer.getUniqueTempPDFFileName("shippingLabel" + shippingOrder.getId());
			File fLabelPDF = new File(shippingLabelFileName);
			fLabelPDF.deleteOnExit();
	    	BufferedOutputStream labelBOS = new BufferedOutputStream(new FileOutputStream(fLabelPDF));

	    	errorMessages = new ArrayList<CarrierErrorMessage>();
			Carrier carrier = carrierServiceDAO.getCarrier(shippingOrder.getCarrierId());
			CustomerCarrier customerCarrier = getCarrierAccount(shippingOrder.getCustomerId(), shippingOrder.getBusinessId(), carrier.getId(), shippingOrder.getFromAddress().getCountryCode(), shippingOrder.getToAddress().getCountryCode());
			//for(CustomerCarrier customerCarrier : customerCarrierList){
			CarrierService carrierService = getCarrierServiceBean(carrier.getImplementingClass());
			carrierService.generateShippingLabel(labelBOS,shippingOrder.getId(), customerCarrier);
			srcList.add(shippingLabelFileName);	
			
			String customsInvoicePDF = pdfRenderer.getUniqueTempPDFFileName("customsInvoice" + shippingOrder.getId());
			if(getCustomsInvoice(shippingOrder, customsInvoicePDF)){
				srcList.add(customsInvoicePDF);						
			}
			pdfRenderer.concatPDF(srcList, outputStream);
				//carrierService.generateShippingLabel(outputStream,54);
		}
		catch(ShiplinxException e) {
			log.error("-------LabelGenerationException----------",e);
		}

		catch (Exception e) {
			log.debug("-------Exception----"+e);
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Shipping Label
	 */
	public void getShippingLabels(List<String> lstOrders,OutputStream outputStream,int scopies, int ccopies ) {
		
		try{
						
			ArrayList<String> srcList = new ArrayList<String>();
			
			for ( int i = 0; i < lstOrders.size(); i++ ) 
			{
				
				if(lstOrders.get(i).length() > 0)
				{
					
					ShippingOrder shippingOrder = shippingService.getShippingOrder(new Long(lstOrders.get(i)));
					
					if(shippingOrder.isPaymentRequired()){ //do not return label if payment not captured
						log.warn("Payment required for order with id: " + shippingOrder.getId() + ". Not generating label!");
						continue;
					}
					
					//get the carrier available to customer
					//List<CustomerCarrier> customerCarrierList = getCutomerCarrier(shippingOrder.getCustomerId());
					PDFRenderer pdfRenderer = new PDFRenderer();
					String shippingLabelFileName = pdfRenderer.getUniqueTempPDFFileName("shippingLabel" + shippingOrder.getId());
					File fLabelPDF = new File(shippingLabelFileName);
					fLabelPDF.deleteOnExit();
			    	BufferedOutputStream labelBOS = new BufferedOutputStream(new FileOutputStream(fLabelPDF));
	
			    	errorMessages = new ArrayList<CarrierErrorMessage>();
					Carrier carrier = carrierServiceDAO.getCarrier(shippingOrder.getCarrierId());
					
					CustomerCarrier customerCarrier = getCarrierAccount(shippingOrder.getCustomerId(), shippingOrder.getBusinessId(), carrier.getId(), shippingOrder.getFromAddress().getCountryCode(), shippingOrder.getToAddress().getCountryCode());
					//for(CustomerCarrier customerCarrier : customerCarrierList){
					CarrierService carrierService = getCarrierServiceBean(carrier.getImplementingClass());
					carrierService.generateShippingLabel(labelBOS,Long.valueOf(lstOrders.get(i)), customerCarrier);
					for(int s=0; s<scopies; s++) // generate Shipping Label for the no of copies selected.
					{
						srcList.add(shippingLabelFileName);
					}
					
					String customsInvoicePDF = pdfRenderer.getUniqueTempPDFFileName("customsInvoice" + shippingOrder.getId());
					
					for(int c=0; c<ccopies; c++)
					{
						if(getCustomsInvoice(shippingOrder, customsInvoicePDF))
						{
							srcList.add(customsInvoicePDF);
						}
					}
					pdfRenderer.concatPDF(srcList, outputStream);
					
					//carrierService.generateShippingLabel(outputStream,54);
				}//End of If
			}//End of for
		}
		catch(ShiplinxException e) {
			log.error("-------LabelGenerationException----------",e);
		}

		catch (Exception e) {
			log.debug("-------Exception----"+e);
			e.printStackTrace();
		}
		
	}

	/**
	 * Shipping Label
	 */
	private boolean getCustomsInvoice(ShippingOrder shippingOrder,String fileName) {

		
		CustomsInvoice ci = shippingService.getCustomsInvoiceByOrderId(shippingOrder.getId());
		if(ci==null)
			return false;
		shippingOrder.setCustomsInvoice(ci);
		Business business = businessService.getBusinessById(shippingOrder.getBusinessId());			
		try {
			InputStream stream = this.getClass().getClassLoader().getResourceAsStream(
								"./jasperreports/CustomsInvoice.jasper");
			JasperReport jasperReport = (JasperReport) JRLoader.loadObject(stream);

			Map parameters = new HashMap();
			parameters.put("CustomsInvoice", ci);
			parameters.put("ShippingOrder", shippingOrder);
			String logoPath = "/images/" + business.getLogoFileName();
	        String logo2Path = "/images/" + business.getLogoHiResFileName();
			URL logo = (InvoiceManagerImpl.class.getResource(logoPath));        	
			URL logo2 = (InvoiceManagerImpl.class.getResource(logo2Path));  
			
	        parameters.put("logo", logo);
	        parameters.put("logo2", logo2);

			JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(shippingOrder.getCustomsInvoice().getProducts());

			JasperPrint jasperPrint = JasperFillManager.fillReport(
							jasperReport, parameters, ds);

			JasperExportManager.exportReportToPdfFile(jasperPrint, fileName);
			return true;

		} catch (Exception e) {
			log.error("Could not generate Shiplinx Invoice Main !!", e);
			return false;
		}

	}

	protected void cleanupInput(ShippingOrder order ){
//		make sure that for each of the packages, the length is set as the dimension of the longest side
		List<com.meritconinc.shiplinx.model.Package> packages = (List<com.meritconinc.shiplinx.model.Package>)order.getPackages();

		order.setBillingStatus(ShiplinxConstants.BILLING_STATUS_READY_TO_INVOICE); //if the shipment is created on the system, then this value is pre-set so when edi processing occurs this field does not need to be set
		order.setInsuranceValue(0f);
		order.setCODValue(0d);
		if(!(order.getFromAddress().getCountryCode().equalsIgnoreCase(ShiplinxConstants.CANADA) || order.getFromAddress().getCountryCode().equalsIgnoreCase(ShiplinxConstants.US))){
			order.setDimType(ShippingOrder.DIM_TYPE_METRIC);
		}

	//	if(order.getPackageTypeId().getPackageTypeId() == ShiplinxConstants.PACKAGE_TYPE_PALLET)
		//	return;
		
		for(com.meritconinc.shiplinx.model.Package p:packages){			
			
			if(p.getCodAmount() == null)
				p.setCodAmount(new Float(0));
			if(p.getInsuranceAmount() == null)
				p.setInsuranceAmount(new Float(0));

			order.setInsuranceValue(order.getInsuranceValue() + p.getInsuranceAmount().floatValue());
			order.setCODValue(order.getCODValue() + p.getCodAmount().floatValue());
			
			p.setLength(p.getLength().setScale(2, BigDecimal.ROUND_UP));
			p.setHeight(p.getHeight().setScale(2, BigDecimal.ROUND_UP));
			p.setWidth(p.getWidth().setScale(2, BigDecimal.ROUND_UP));
//			p.setCodAmount(p.getCodAmount().setScale(1, BigDecimal.ROUND_UNNECESSARY));
//			p.setInsuranceAmount(p.getInsuranceAmount().setScale(1, BigDecimal.ROUND_UNNECESSARY));
			
			//do not arrange the dims if type is pallet.
			if(order.getPackageTypeId().getPackageTypeId() != ShiplinxConstants.PACKAGE_TYPE_PALLET){
				double length = p.getLength().doubleValue();
				double height = p.getHeight().doubleValue();
				double width = p.getWidth().doubleValue();
				if(length < height){
					double temp = length;
					p.setLength(new BigDecimal(height).setScale(2, BigDecimal.ROUND_UP));
					p.setHeight(new BigDecimal(temp).setScale(2, BigDecimal.ROUND_UP));
					length = p.getLength().doubleValue();
				}
				if(length < width){
					double temp = length;
					p.setLength(new BigDecimal(width).setScale(2, BigDecimal.ROUND_UP));
					p.setWidth(new BigDecimal(temp).setScale(2, BigDecimal.ROUND_UP));
				}
			}	
			p.setBilledWeight(p.getWeight());
		}
		
		if (order.getQuantity() == null || order.getQuantity().intValue() <= 0)
			order.setQuantity(packages.size());
		
		if(order.getFromAddress().getPostalCode()!=null)
			order.getFromAddress().setPostalCode((order.getFromAddress().getPostalCode().replaceAll(" ", "")));
		if(order.getToAddress().getPostalCode()!=null)
			order.getToAddress().setPostalCode((order.getToAddress().getPostalCode().replaceAll(" ", "")));
		
		if(order.getFromAddress().getPhoneNo()!=null)
			order.getFromAddress().setPhoneNo(order.getFromAddress().getPhoneNo().replaceAll( "[^\\d]", "" ));
		if(order.getToAddress().getPhoneNo()!=null)
			order.getToAddress().setPhoneNo(order.getToAddress().getPhoneNo().replaceAll( "[^\\d]", "" ));

	}

	private void processRates(List<Rating> ratingList, ShippingOrder order, long shipServiceId){
		
//		List<Integer> disabledServices = new ArrayList<Integer>();
		int index = 0;
		List<Rating> ratesToRemove = new ArrayList<Rating>();
		//List<Rating> rateTotal = new ArrayList<Rating>();
		for(Rating rate: ratingList){

			//if this is a new service then add it first
			if(rate.getNewService()!=null){
				long serviceId = addNewService(rate.getNewService());
				rate.setServiceId(serviceId);
			}
			
			if (shipServiceId != -1 && rate.getServiceId()!=shipServiceId) {
//				- Loop throuh all the rates and return only the one where the service id matches
				log.debug("Rate with index " + index + " and service " + rate.getServiceName() + " not being requested for customer " + order.getCustomer().getName() + " / " + order.getCustomerId());
				ratesToRemove.add(rate);
				continue;
			}

			//if the shipment is a "LTL PER SKID" service, then markup should be based on # of skids. 
			//Using the same weight fields to store skid range
			Service service = carrierServiceDAO.getService(rate.getServiceId());
			if(service!=null && service.getServiceType() == ShiplinxConstants.SERVICE_TYPE_LTL_SKID){
				log.debug("Applying markup based on # of skids: " + order.getPackages().size());
				order.setWeightToMarkup((double)order.getPackages().size());				
			}
			else
				order.setWeightToMarkup(rate.getBillWeight());
			//mark all disabled services
			Markup markup = markupManagerService.getMarkupObj(order);
			markup.setServiceId(rate.getServiceId());
			markup = markupManagerService.getUniqueMarkup(markup);
			
			if(markup==null || markup.isDisabledFlag()){
				log.debug("Rate with index " + index + " and service " + rate.getServiceName() + " disabled or mark up record not found for customer " + order.getCustomer().getName() + " / " + order.getCustomerId());
//				disabledServices.add(index);
				//As per JQ Mar 29, 2013. If shipping TP or Collect, then disabled should not matter
				if(StringUtil.isEmpty(order.getBillToAccountNum()))
						ratesToRemove.add(rate);
				else
					rate.setCharges(new ArrayList<Charge>()); //remove all the charges if this is TP or Collect
				continue;
			}
			
			shippingService.applyAdditionalHandling(order, rate, ShiplinxConstants.CHARGE_TYPE_QUOTED);
			
			rate.setMarkupPercentage(markup.getMarkupPercentage());
			rate.setMarkupTypeText(markup.getTypeText());
			rate.setMarkupType(markup.getType());
				
			rate.setTotal(0);
			rate.setTotalCost(0);
			if(StringUtil.isEmpty(rate.getCarrierName()))
				rate.setCarrierName(this.carrierServiceDAO.getCarrierByBusiness(rate.getCarrierId(), order.getBusinessId()).getName());
			
			if(StringUtil.isEmpty(rate.getServiceName()))
				rate.setServiceName(shippingService.getServiceById(rate.getServiceId()).getName());
			order.setServiceId(rate.getServiceId());
			for(Charge c: rate.getCharges()){				
				c.setStatus(ShiplinxConstants.CHARGE_QUOTED);
//				log.info("Looking up charge for carrier/code/code2: " + rate.getCarrierId() + " / " + c.getChargeCode() + " / " + c.getChargeCodeLevel2());
				CarrierChargeCode chargeGroupCode = this.shippingService.getChargeByCarrierAndCodes(rate.getCarrierId(), c.getChargeCode(), c.getChargeCodeLevel2());
				//this should really return only 1 row
				if (chargeGroupCode != null && 
						(chargeGroupCode.getGroupCode().equalsIgnoreCase(ShiplinxConstants.GROUP_FUEL_CHARGE) ||
								chargeGroupCode.getGroupCode().equalsIgnoreCase(ShiplinxConstants.GROUP_FREIGHT_CHARGE)	)) {
					c.setCharge( markupManagerService.applyMarkup(order, c, false) );
				} else {
					if(c.getTariffRate()!=null)
						c.setCharge(c.getTariffRate());
					else
						c.setCharge(c.getCost());
				}	
				if (chargeGroupCode != null)
					c.setDisplayOrder(chargeGroupCode.getDisplayOrder());
				else{
					log.error("Could not set display order for this charge!");
					c.setDisplayOrder(Integer.MAX_VALUE); //this shouldn't happen, but if it does then set the value to high number
				}
				c.setCharge(FormattingUtil.roundFigureRates(c.getCharge(), 2));
				c.setCost(FormattingUtil.roundFigureRates(c.getCost(), 2));
				if(c.getTariffRate()!=null)
					c.setTariffRate(FormattingUtil.roundFigureRates(c.getTariffRate(), 2));
				rate.setTotal(FormattingUtil.add(rate.getTotal(), c.getCharge().doubleValue()).doubleValue());
				rate.setTotalCost(FormattingUtil.add(rate.getTotalCost(), c.getCost().doubleValue()).doubleValue());
				if(chargeGroupCode!=null){
					c.setChargeId(chargeGroupCode.getId());
					if(c.getName()==null || c.getName().length()==0)
						c.setName(chargeGroupCode.getChargeName());
				}
			}
			Collections.sort(rate.getCharges(), Charge.ChargeComparator);

			//calculate taxes (for Canadian Shipments)
			if(order.getFromAddress().getCountryCode().equalsIgnoreCase(ShiplinxConstants.CANADA) && order.getToAddress().getCountryCode().equalsIgnoreCase(ShiplinxConstants.CANADA))
				applyTaxes(rate, order);

			rate.setBillWeightUOM(ShiplinxConstants.WEIGHT_UNITS_LBS); //always calculating and quoting in LBS for now
			rate.setTotal(FormattingUtil.roundFigureRates(rate.getTotal(), 2));
			rate.setTotalCost(FormattingUtil.roundFigureRates(rate.getTotalCost(), 2));
			index++;
			//rateTotal.add(rate);
		}
		//Collections.sort(rateTotal, Rating.PriceComparator);//Sorting the Total of Rate
		//remove the rates that we could not find markup records for, and those that are disabled
		ratingList.removeAll(ratesToRemove);
		

		//DHL Deferred - set transit days to same as UPS Standard, this is hard-code for IC
		Rating dhlDeferred = null;
		Rating upsStandard = null;
		for(Rating rating: ratingList){
			if(rating.getCarrierId()==ShiplinxConstants.CARRIER_UPS && (rating.getServiceId()==204 || rating.getServiceId()==208)){
				upsStandard = rating;
			}
			if(rating.getCarrierId()==ShiplinxConstants.CARRIER_DHL && rating.getServiceId()==DHLAPI.SERVICE_INTL_GROUND){
				dhlDeferred = rating;
			}
		}
		if(dhlDeferred!=null && upsStandard!=null)
			dhlDeferred.setTransitDays(upsStandard.getTransitDays());
		
	}
	
	public List<Carrier> getCarriers(){
		return getCarrierServiceDAO().getCarriers();
	}
	
	public CustomerManager getCustomerService() {
		return customerService;
	}

	public void setCustomerService(CustomerManager customerManager) {
		this.customerService = customerManager;
	}

	public ShippingService getShippingService() {
		return shippingService;
	}

	public void setShippingService(ShippingService shippingService) {
		this.shippingService = shippingService;
	}

	public CustomerCarrier getDefAccountByCountry(long carrierId,long customerId,String country){
		return getCarrierServiceDAO().getDefAccountByCountry(carrierId, customerId, country);
	}

	public AddressManager getAddressService() {
		return addressService;
	}

	public void setAddressService(AddressManager addressService) {
		this.addressService = addressService;
	}
	
	private void copyFromOrderToPickup(ShippingOrder order, Rating rate){
		order.getPickup().setDestinationCountryCode(order.getToAddress().getCountryCode());
		order.getPickup().setServiceCode(order.getService().getCode());
		order.getPickup().setQuantity(order.getPackages().size());
		order.getPickup().setAddress(order.getFromAddress());
		order.getPickup().setCarrierId(order.getCarrierId());
		order.getPickup().setCustomerId(order.getCustomerId());
		order.getPickup().setBusinessId(order.getBusinessId());
		order.getPickup().setPickupDate(order.getScheduledShipDate());
		order.getPickup().setPackageTypeId(order.getPackageTypeId().getPackageTypeId());
		order.getPickup().setTotalWeight(order.getTotalWeight());
		order.getPickup().setServiceId(order.getServiceId());
	}
	private CarrierService getCarrierServiceBean(String implementingClass) {
		// TODO Auto-generated method stub
		// when it invoked from a thread, it does not work.
//		WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext((ServletContext)ActionContext.getContext().get(ServletActionContext.SERVLET_CONTEXT));
//		CarrierService carrierService = (CarrierService)context.getBean(carrier.getImplementingClass());
		CarrierService carrierService = (CarrierService) MmrBeanLocator.getInstance().findBean(implementingClass);

		return carrierService;
	}	
	
	public void run(){
		try {
			log.debug("Inside--the--run--method--");
			
			ratesThread = carrierServiceThread.rateShipment(orderThread,carrierServicesList,carrierServiceThread.getCarrierId(),customerCarrierThread);
			//logger.debug("rates"+ratesThread+" for carrier ="+carrierServiceThread.getCarrierId());
			if(ratesThread != null) {
				log.debug("inside if rate!=null");
				for(int x=0;x<ratesThread.size();x++) {
					rateListThread.add(ratesThread.get(x));						
				}
			}
			
		} catch (ShiplinxException e) {
			for(String s :e.getErrorMessages()){
				CarrierErrorMessage message = new CarrierErrorMessage(carrierServiceThread.getCarrierId(),customerCarrierThread.getCarrierName() + " : " + s);					
				getParentThread().getErrorMessages().add(message);
			}
		}
	
	}

	public ShippingOrder getOrderThread() {
		return orderThread;
	}

	public void setOrderThread(ShippingOrder orderThread) {
		this.orderThread = orderThread;
	}

	public List<Rating> getRateListThread() {
		return rateListThread;
	}

	public void setRateListThread(List<Rating> rateListThread) {
		this.rateListThread = rateListThread;
	}
	
	private void applyTaxes(Rating rate, ShippingOrder order){
		
		Province destinationProvince = addressService.getProvince(order.getToAddress().getProvinceCode());
		//tax applies based on province destination
		ChargeGroup taxChargeGroup = shippingService.getChargeGroup(destinationProvince.getTaxChargeGroup());
		
		Charge c = new Charge();
		c.setStatus(ShiplinxConstants.CHARGE_QUOTED);
		c.setName(taxChargeGroup.getGroupCode());
		double tax_cost = rate.getTotalCost() * taxChargeGroup.getTaxRate()/100;
		double tax_charge = rate.getTotal() * taxChargeGroup.getTaxRate()/100;
		double tax_tariff = rate.getTariffRate() * taxChargeGroup.getTaxRate()/100;
		c.setCharge(tax_charge);
		c.setCost(tax_cost);
		c.setTariffRate(tax_tariff);
		
		c.setCharge(FormattingUtil.roundFigureRates(c.getCharge(), 2));
		c.setCost(FormattingUtil.roundFigureRates(c.getCost(), 2));
		if(c.getTariffRate()!=null)
			c.setTariffRate(FormattingUtil.roundFigureRates(c.getTariffRate(), 2));
		rate.getCharges().add(c);
		rate.setTotal(FormattingUtil.add(rate.getTotal(), c.getCharge().doubleValue()).doubleValue());
		rate.setTotalCost(FormattingUtil.add(rate.getTotalCost(), c.getCost().doubleValue()).doubleValue());
		rate.setTariffRate(FormattingUtil.add(rate.getTariffRate(), c.getTariffRate().doubleValue()).doubleValue());

		//special case, for shipments going from Quebec to Quebec, we need to add QST as well
		if(order.getFromAddress().getProvinceCode().equalsIgnoreCase("PQ") && order.getToAddress().getProvinceCode().equalsIgnoreCase("PQ")){
			taxChargeGroup = shippingService.getChargeGroup(12); //QST
			c = new Charge();
			c.setStatus(ShiplinxConstants.CHARGE_QUOTED);
			c.setName(taxChargeGroup.getGroupCode());
			tax_cost = rate.getTotalCost() * taxChargeGroup.getTaxRate()/100;
			tax_charge = rate.getTotal() * taxChargeGroup.getTaxRate()/100;
			tax_tariff = rate.getTariffRate() * taxChargeGroup.getTaxRate()/100;
			c.setCharge(tax_charge);
			c.setCost(tax_cost);
			c.setTariffRate(tax_tariff);
			
			c.setCharge(FormattingUtil.roundFigureRates(c.getCharge(), 2));
			c.setCost(FormattingUtil.roundFigureRates(c.getCost(), 2));
			if(c.getTariffRate()!=null)
				c.setTariffRate(FormattingUtil.roundFigureRates(c.getTariffRate(), 2));
			rate.getCharges().add(c);
			rate.setTotal(FormattingUtil.add(rate.getTotal(), c.getCharge().doubleValue()).doubleValue());
			rate.setTotalCost(FormattingUtil.add(rate.getTotalCost(), c.getCost().doubleValue()).doubleValue());
			rate.setTariffRate(FormattingUtil.add(rate.getTariffRate(), c.getTariffRate().doubleValue()).doubleValue());
		
		}
}
	
	public Service getServiceByCarrierIdAndCode(Long carrierId, String code){
		return this.carrierServiceDAO.getServiceByCarrierIdAndCode(carrierId, code);
	}

	public long addNewService(Service service){
		//first determine the service id to be assigned to this service
		Long maxServiceIdforCarrier = carrierServiceDAO.getMaxServiceIdForCarrier(service.getCarrierId());
		if(maxServiceIdforCarrier==null || maxServiceIdforCarrier.longValue()==0)
			service.setId(service.getCarrierId() * 100);
		else
			service.setId(maxServiceIdforCarrier + 1);
		
		try{
			carrierServiceDAO.addService(service);
			log.info("Added service " + service.getName() + " for carrier " + service.getCarrierId() );
		}
		catch(Exception e){
			log.error("Unable to add service with name " + service.getName() + " and carrier " + service.getCarrierId());
		}
		
		//add a new default markup record for this service
		Markup markup = new Markup();
		markup.setCustomerId(new Long(0));
		markup.setBusinessId(service.getBusinessId());
		markup.setFromCountryCode("ANY");
		markup.setToCountryCode("ANY");
		markup.setMarkupPercentage(0);
		markup.setMarkupFlat(0.0);
		markup.setDisabled(1);
		markup.setType(ShiplinxConstants.TYPE_MARKUP);
		markup.setFromWeight(0.0);
		markup.setToWeight(0.0);
		markup.setServiceId(service.getId());
		this.markupManagerService.addMarkup(markup);

		log.info("Added markup for service " + service.getName());
		
		return service.getId();
	}

	public UserSearchCriteria getCriteria() {
		return criteria;
	}

	public void setCriteria(UserSearchCriteria criteria) {
		this.criteria = criteria;
	}

	public CarrierServiceManagerImpl getParentThread(){
		return parentThread;
	}

	@Override
	public void uploadRates(long serviceId, long customerId, long busId, boolean isOverwrite) throws Exception {
		Service service = this.getService(serviceId);
//		Customer customer = this.customerService.getCustomerInfoByCustomerId(customerId);
		if (service != null && service.getCarrier() != null) {
			CarrierService carrierService = getCarrierServiceBean(service.getCarrier().getImplementingClass());
			if (carrierService != null) {
				carrierService.uploadRates(service, customerId, busId, isOverwrite);
			}
		}
	}
	
	public void checkForShipmentStatusUpdates() {
		
		long start = System.currentTimeMillis();
		log.info("Starting Status Update Job...");

		//shippingService.runBillingUpdate();

//		
//		ShippingOrder order = new ShippingOrder();
//		Integer[] statusIds = new Integer[3];
//		statusIds[0] = ShiplinxConstants.STATUS_DISPATCHED;
//		statusIds[1] = ShiplinxConstants.STATUS_INTRANSIT;
//		statusIds[2] = ShiplinxConstants.STATUS_EXCEPTION;
//		order.setStatusIds(statusIds);
//
//		//only in the last month
//		int month = Calendar.getInstance().get(Calendar.MONTH);
//		int year = Calendar.getInstance().get(Calendar.YEAR);
//		int today = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);	
//
//		Calendar calendar = new GregorianCalendar(year, month, today);			
//		order.setToDate(FormattingUtil.getFormattedDate(calendar.getTime(),FormattingUtil.DATE_FORMAT_WEB));
//		
//		calendar.add(Calendar.DATE, -31);
//		order.setFromDate(FormattingUtil.getFormattedDate(calendar.getTime(),FormattingUtil.DATE_FORMAT_WEB));	
//
//		List<ShippingOrder> orders = shippingService.getShipments(order);
//
//		for(ShippingOrder so: orders){
//			log.debug("Attempting to update status of order #" + so.getId());
//			CarrierService carrierService = getCarrierServiceBean(so.getCarrier().getImplementingClass());
//			//get the appropriate account to be used to generate/rate the shipment
//			CustomerCarrier customerCarrier = getCarrierAccount(so.getCustomerId(), so.getBusinessId(), so.getCarrier().getId(), so.getFromAddress().getCountryCode(), so.getToAddress().getCountryCode());
//			
//			OrderStatus status = carrierService.checkForShipmentStatusUpdates(so, customerCarrier);
//			
//			if(status==null)
//				continue;
//			
//			//update the shipment and the order status
//			//shipment contains POD information
//			shippingService.updateOrderStatus(so.getId(), status.getId(), status.getComment(), false, so.getPodReceiver(), so.getPodDateTime());
//			
//		}
//		
		long end = System.currentTimeMillis();
		log.info("Update status process took " + (end-start)/1000 + " seconds!");
		
	}
	
	

}
