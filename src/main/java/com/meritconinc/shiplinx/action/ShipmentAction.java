/**
 * 
 */
package com.meritconinc.shiplinx.action;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringBufferInputStream;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.Response;

import net.sf.jasperreports.engine.util.FormatUtils;

import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.meritconinc.mmr.constants.NavConsts;
import com.meritconinc.mmr.dao.MenusDAO;
import com.meritconinc.mmr.exception.CardProcessingException;
import com.meritconinc.mmr.model.security.User;
import com.meritconinc.mmr.service.UserService;
import com.meritconinc.mmr.utilities.MessageUtil;
import com.meritconinc.mmr.utilities.MmrBeanLocator;
import com.meritconinc.mmr.utilities.StringUtil;
import com.meritconinc.mmr.utilities.security.UserUtil;
import com.meritconinc.shiplinx.carrier.CarrierService;
import com.meritconinc.shiplinx.carrier.purolator.stub.ServiceAvailabilityWebServiceClient;
import com.meritconinc.shiplinx.dao.CustomerDAO;
import com.meritconinc.shiplinx.exception.ShiplinxException;
import com.meritconinc.shiplinx.model.Address;
import com.meritconinc.shiplinx.model.BatchShipmentInfo;
import com.meritconinc.shiplinx.model.BillingStatus;
import com.meritconinc.shiplinx.model.CCTransaction;
import com.meritconinc.shiplinx.model.Carrier;
import com.meritconinc.shiplinx.model.CarrierChargeCode;
import com.meritconinc.shiplinx.model.Charge;
import com.meritconinc.shiplinx.model.Country;
import com.meritconinc.shiplinx.model.CreditCard;
import com.meritconinc.shiplinx.model.Customer;
import com.meritconinc.shiplinx.model.CustomsInvoice;
import com.meritconinc.shiplinx.model.CustomsInvoiceProduct;
import com.meritconinc.shiplinx.model.DangerousGoods;
import com.meritconinc.shiplinx.model.Invoice;
import com.meritconinc.shiplinx.model.LoggedEvent;
import com.meritconinc.shiplinx.model.OrderProduct;
import com.meritconinc.shiplinx.model.OrderStatus;
import com.meritconinc.shiplinx.model.Package;
import com.meritconinc.shiplinx.model.PackageType;
import com.meritconinc.shiplinx.model.PackageTypes;
import com.meritconinc.shiplinx.model.Pickup;
import com.meritconinc.shiplinx.model.Products;
import com.meritconinc.shiplinx.model.Province;
import com.meritconinc.shiplinx.model.Rating;
import com.meritconinc.shiplinx.model.Service;
import com.meritconinc.shiplinx.model.ShippingOrder;
import com.meritconinc.shiplinx.service.AddressManager;
import com.meritconinc.shiplinx.service.CarrierServiceManager;
import com.meritconinc.shiplinx.service.CustomerManager;
import com.meritconinc.shiplinx.service.InvoiceManager;
import com.meritconinc.shiplinx.service.LoggedEventService;
import com.meritconinc.shiplinx.service.PickupManager;
import com.meritconinc.shiplinx.service.ProductManager;
import com.meritconinc.shiplinx.service.ShippingService;
import com.meritconinc.shiplinx.utils.CarrierErrorMessage;
import com.meritconinc.shiplinx.utils.FormattingUtil;
import com.meritconinc.shiplinx.utils.ShiplinxConstants;

/**
 * @author Rahul
 *
 */
public class ShipmentAction extends BaseAction implements ServletRequestAware, ServletResponseAware{
	private static final Logger log = LogManager.getLogger(ShipmentAction.class);
	private static final long serialVersionUID	= 18052007;
	private HttpServletRequest request;
//	private ShippingOrder shippingOrder;
	private AddressManager addressService;
	private ShippingService shippingService;
	
	private ProductManager productManagerService;
	private UserService userService;
	private List<Country> countries;

	private String contact;
	private String consigneeName;
	private String id;
	private String city;
	private String postalCode;
	private String state;
	
	private List packageTypeList;
	private List<Province> provinces;
	private CarrierServiceManager carrierServiceManager;
	//private List<Rating> ratingList;
	private List<String> orderList;
	private HttpServletResponse response;
	private CustomerManager customerService;
	private Address addressBook;
	private File upload;
	private String uploadFileName;
//	private List<OrderStatus> orderStatusList;
	
	private String nextAction;
	
	private List<Products> warehouseProductsList;
	List<Products> warehouseAllProdList = new ArrayList<Products>();
	
	private List<Address> addresses = null;
	
//	private List<Carrier> carriers;
//	private List<Service> services;
	
//	private List<ShippingOrder> shipments;
	private List<Boolean> select;
	private List<ShippingOrder> selectedShipments;
//	private Charge newCharge;
	private Map<String, Long> carrierChargesSearchResult = new HashMap<String, Long>();
	private static List<CarrierChargeCode> carrierChargesList = null;
	
	private Map<String, Long> customerSearchResult = new HashMap<String, Long>();
	// City, Zipcode_Start List
	private List<String> citySuggestList;
	// Zipcode_Start, City List
	private List<String> zipSuggestList;
	
	private List<LoggedEvent> loggedList = new ArrayList<LoggedEvent>();
	
	private LoggedEventService loggedEventService;
	private PickupManager pickupService;
	public List<Pickup> listPickups; 
	
	public InvoiceManager invoiceManagerService;
	
	private InputStream inputStream;
	public InputStream getInputStream() {
	return inputStream;
	}
	public void setInputStream(InputStream inputStream) {
	this.inputStream = inputStream;
	}

	
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
//	public String executebackup() throws Exception {
//		try{
//			log.debug("-----execute------");
////			getSession().remove("shippingOrder");
//			getSession().remove("PackageType");
//			String method=request.getParameter("method");
//			if(method==null || !(method.equals("update"))){
//				getSession().remove("EDIT_ORDER_ID");
//			}
//			String orderId=(String)getSession().get("EDIT_ORDER_ID");
//			
//			log.debug("-----orderId------"+orderId);			
//			Long customerId = getLoginUser().getCustomerId();
//			log.debug("-----customerId------"+customerId);
//			String fromCountry = "";
//			String toCountry = "";
//			
//			List<Province> toProvinces;
//			List<Province> fromProvinces;
//			Address addressbookFrom;
//			Address addressbookTo;
//			if(orderId==null){
//				addressbookFrom = addressService.findDefaultFromAddressForCustomer(customerId);
//				addressbookTo = addressService.findDefaultToAddressForCustomer(customerId);
//			}
//			else{				
//				ShippingOrder order = shippingService.getShippingOrder(Long.valueOf(orderId));
//				addressbookFrom = order.getFromAddress();
//				addressbookTo = order.getToAddress();
//			}
//			ShippingOrder shippingOrder = getShippingOrder();
//			shippingOrder.setToAddress(addressbookTo);
//			shippingOrder.setFromAddress(addressbookFrom);
//			shippingOrder.setCustomerId(getLoginUser().getCustomerId());
//			
//			if(orderId!=null){
//				shippingOrder.setPackageTypeId(shippingService.findOrderPackageType(Long.parseLong(orderId)));
//				ShippingOrder shippingOrder1=shippingService.getShippingOrder(Long.parseLong(orderId));
//
//				getSession().put("PackageType",shippingOrder.getPackageTypeId().getType());
//				shippingOrder.setReferenceCode(shippingOrder1.getReferenceCode());
//				shippingOrder.setSatDelivery(shippingOrder1.getSatDelivery());
//				shippingOrder.setHoldForPickupRequired(shippingOrder1.getHoldForPickupRequired());
//				shippingOrder.setInsideDelivery(shippingOrder1.getInsideDelivery());
//				shippingOrder.setPickupTime(shippingOrder1.getPickupTime());
//				shippingOrder.setSpecialEquipment(shippingOrder1.getSpecialEquipment());
//				shippingOrder.setCODPayment(shippingOrder1.getCODPayment());
//				shippingOrder.setCODValue(shippingOrder1.getCODValue());
//				shippingOrder.setCODCurrency(shippingOrder1.getCODCurrency());
//				shippingOrder.setCODPayableTO(shippingOrder1.getCODPayableTO());
//				shippingOrder.setDangerousGoods(shippingOrder1.getDangerousGoods());
//				shippingOrder.setReqDeliveryDate(shippingOrder1.getReqDeliveryDate());
//				shippingOrder.setScheduledShipDate(shippingOrder1.getScheduledShipDate());
//				shippingOrder.setPickUpNum(shippingOrder1.getPickUpNum());
//				shippingOrder.setReferenceOne(shippingOrder1.getReferenceOne());
//				shippingOrder.setReferenceTwo(shippingOrder1.getReferenceTwo());
//				shippingOrder.setMasterTrackingNum(shippingOrder1.getMasterTrackingNum());
//				shippingOrder.setRes(shippingOrder1.getRes());
//				shippingOrder.setFromTailgate(shippingOrder1.getFromTailgate());
//				shippingOrder.setToTailgate(shippingOrder1.getToTailgate());
//				shippingOrder.setFromAttention(shippingOrder1.getFromAttention());
//				shippingOrder.setToAttention(shippingOrder1.getToAttention());
//				shippingOrder.setNotifyRecipient(shippingOrder1.getNotifyRecipient());
//				shippingOrder.setConfirmDelivery(shippingOrder1.getConfirmDelivery());
//			}else{
//				shippingOrder.setPackageTypeId(shippingService.findPackageType(ShiplinxConstants.PACKAGE_TYPE_ENVELOPE_STRING));
//			}
//			//set the default package
//
//			
//			if(addressbookFrom != null)
//				fromCountry = addressbookFrom.getCountryCode();
//			if(addressbookTo != null)
//				toCountry = addressbookTo.getCountryCode();
//			
//			//if not set set as default
//			if(fromCountry==null ||"".equals(fromCountry))
//				fromCountry = ShiplinxConstants.CANADA;
//			if(toCountry==null ||"".equals(toCountry))
//				toCountry = ShiplinxConstants.CANADA;
//			
//			toProvinces=addressService.getProvinces(toCountry);
//			fromProvinces=addressService.getProvinces(fromCountry);
//			
//			getSession().put("Fromprovinces", fromProvinces);
//			getSession().put("Toprovinces", toProvinces);
//			getSession().put("ToCountry", toCountry);
//			getSession().put("FromCountry", fromCountry);
//		
//			countries=addressService.getCountries();
//			getSession().put("CountryList", countries);
////			getSession().put("shippingOrder",shippingOrder);
//			
//			packageTypeList = shippingService.getPackages();
//			List<PackageType> listPackages = shippingService.getPackages();
//			
//			List<HashMap<String, String>> packageOption = new ArrayList<HashMap<String, String>>();
//			
//			// To generate packages radio buttons on jsp
//			//short packeCounter=0;
//			for(PackageType pType :listPackages){
//				HashMap<String, String> packages = new HashMap<String, String>();
//				packages.put(pType.getType(),pType.getName());
//				packageOption.add(packages);
//			}
//			
//			getSession().put("packageOptions", packageOption);
//			
//			
//			}catch (Exception e) {
//				log.debug("-----------------Exception----------------"+e);
//				e.printStackTrace();
//			}
//			
//			//set customer info for shipment
////			ShiplinxConstants.setCustomer(customerService.getCustomerInfoByCustomerId(getLoginUser().getCustomerId()));
//			if(getSession().get("WINDOW_STATUS")==null){
//				getSession().put("WINDOW_STATUS","show");
//			}
////			getSession().put("shippingOrder",shippingOrder);
////			setShippingOrder(shippingOrder);
//			
//			//getSession().put("SHIPMENT_WINDOW_STATUS","hide");
//			return SUCCESS;
//		}
	
	public String execute() throws Exception {
		MenusDAO menusDAO = (MenusDAO) MmrBeanLocator.getInstance().findBean("menusDAO");
		try{
			log.debug("-----execute------");
			getSession().remove("shippingOrder");
			getSession().remove("PackageType");
			String method=request.getParameter("method");
			if(method==null || !(method.equals("update"))){
				getSession().remove("EDIT_ORDER_ID");
			}
			String orderId=(String)getSession().get("EDIT_ORDER_ID");
			
			log.debug("-----orderId------"+orderId);			
			Long customerId = getLoginUser().getCustomerId();
			log.debug("-----customerId------"+customerId);
			String fromCountry = "";
			String toCountry = "";
			
			List<Province> toProvinces;
			List<Province> fromProvinces;
			Address addressbookFrom =null;
			Address addressbookTo=null;
			List<PackageTypes> packagetypes;
			long l_default_from_add = 0;
			long l_default_to_add = 0;
			
			//setting the role in req attribute to enable or disable address fields based on role.
			if(getLoginUser().getUserRole().equalsIgnoreCase(ShiplinxConstants.ROLE_CUSTOMER_SHIPPER))
				request.setAttribute("USERROLE", "shipper");
			if(orderId==null){
				l_default_from_add = getLoginUser().getDefaultFromAddressId();
				l_default_to_add = getLoginUser().getDefaultToAddressId();
				//set default addresses for the user if set
				if(l_default_from_add>0)
				{
					addressbookFrom = addressService.findAddressById(l_default_from_add+"");
				}
				else
				{
					addressbookFrom = addressService.findDefaultFromAddressForCustomer(customerId);
				}
				if(l_default_to_add>0)
				{
					addressbookTo = addressService.findAddressById(l_default_to_add+"");
				}
				else
				{
					addressbookTo = addressService.findDefaultToAddressForCustomer(customerId);
				}
				if(addressbookFrom!=null)
					addressbookFrom.setCustomerId(0);
				if(addressbookTo!=null)
					addressbookTo.setCustomerId(0);
			}
			else{				
				ShippingOrder order = shippingService.getShippingOrder(Long.valueOf(orderId));
				addressbookFrom = order.getFromAddress();
				addressbookTo = order.getToAddress();
			}
			
			if(addressbookFrom==null)
				addressbookFrom = new Address();
			if(addressbookTo==null)
				addressbookTo = new Address();
			
			
			ShippingOrder shippingOrder = getShippingOrder();
			shippingOrder.setToAddress(addressbookTo);
			shippingOrder.setFromAddress(addressbookFrom);
			shippingOrder.setCustomerId(getLoginUser().getCustomerId());
			
			getSession().put("SHIP_MODE", "SHIP");
			

			
			if(addressbookFrom != null)
				fromCountry = addressbookFrom.getCountryCode();
			if(addressbookTo != null)
				toCountry = addressbookTo.getCountryCode();
			
			//if not set set as default
			if(fromCountry==null ||"".equals(fromCountry)){
				fromCountry = UserUtil.getMmrUser().getBusiness().getAddress().getCountryCode();
				addressbookFrom.setCountryCode(fromCountry);
			}
			if(toCountry==null ||"".equals(toCountry)){
				toCountry = UserUtil.getMmrUser().getBusiness().getAddress().getCountryCode();
				addressbookTo.setCountryCode(toCountry);
			}
			
			toProvinces=addressService.getProvinces(toCountry);
			fromProvinces=addressService.getProvinces(fromCountry);
			
			PackageTypes pt = new PackageTypes();
			pt.setCustomerId(Long.valueOf(customerId));
			packagetypes = productManagerService.searchPackageTypes(pt);
			
			List<DangerousGoods> dangerousGoodsList = new ArrayList<DangerousGoods>();
			
			dangerousGoodsList = shippingService.getDangerousGoodsAll();
			getSession().put("Fromprovinces", fromProvinces);
			getSession().put("Toprovinces", toProvinces);
			getSession().put("ToCountry", toCountry);
			getSession().put("FromCountry", fromCountry);
			getSession().put("PackageTypes",packagetypes);
			getSession().put("DGList", dangerousGoodsList);
		
			countries=MessageUtil.getCountriesList();

			getSession().put("CountryList", countries);
//			getSession().put("shippingOrder",shippingOrder);
			
			packageTypeList = shippingService.getPackages();
			List<PackageType> listPackages = shippingService.getPackages();
			
			List<HashMap<String, String>> packageOption = new ArrayList<HashMap<String, String>>();
			
			// To generate packages radio buttons on jsp
			//short packeCounter=0;
			for(PackageType pType :listPackages){
				HashMap<String, String> packages = new HashMap<String, String>();
				packages.put(pType.getType(),pType.getName());
				packageOption.add(packages);
			}

			PackageType pType = new PackageType();
			pType.setType(ShiplinxConstants.PACKAGE_TYPE_PACKAGE_STRING);
			shippingOrder.setPackageTypeId(pType);
			
			getSession().put("PackageType",shippingOrder.getPackageTypeId().getType());
			getSession().put("packageOptions", packageOption);
			getSession().put("listPackages", listPackages);
			//default quantity is 1
			ArrayList<Package> packages = new ArrayList<Package>();
			if(shippingOrder.getQuantity()==null)
				shippingOrder.setQuantity(1);
			
			for(int i=0;i<(int)shippingOrder.getQuantity(); i++){
				Package pack = new Package();
				pack.setLength( new BigDecimal(1.0));
				pack.setWeight( new Float(1.0));
				pack.setHeight( new BigDecimal(1.0));
				pack.setWidth( new BigDecimal(1.0));
				pack.setCodAmount( new Float(0.0));
				pack.setInsuranceAmount( new Float(0.0));
				packages.add(pack);
			}

			shippingOrder.setPackages(packages);
			//Package packageArray[] = new Package[1];
			//shippingOrder.setPackageArray(packageArray);

			
			}catch (Exception e) {
				log.debug("-----------------Exception----------------"+e);
				e.printStackTrace();
			}
			
			//set customer info for shipment
//			ShiplinxConstants.setCustomer(customerService.getCustomerInfoByCustomerId(getLoginUser().getCustomerId()));
			if(getSession().get("WINDOW_STATUS")==null){
				getSession().put("WINDOW_STATUS","show");
			}
//			getSession().put("shippingOrder",shippingOrder);
			//for Quick Ship option
			initCarrierListANY();
			
			if (getSession().get("SERVICES") == null) {
				List<Service>services = getCarrierServices(-1L);
				getSession().put("SERVICES", services);
			}
			//getSession().put("SHIPMENT_WINDOW_STATUS","hide");
			if(UserUtil.getMmrUser().getCustomerId()> 0 && customerService.isWarehouseCustomer(UserUtil.getMmrUser().getCustomerId()) && request.getParameter("shipment")==null)
			{
				return "success2";
			}
			else
			{
				return SUCCESS;
			}
		}
	
	public String processShipment() throws Exception {
		try {
			log.debug("Inside processShipment of ShipmentAction");
			getSession().remove("shippingOrder");
			getSession().remove("PackageType");
			ShippingOrder order = null;
			String orderId=request.getParameter("order_id");
			if (!StringUtil.isEmpty(orderId)) 
			{
				order = shippingService.getShippingOrder(Long.valueOf(orderId));
			}
			else
			{
				order = this.getShippingOrder();
			}
				if (order != null) {
					
					setOrderAddress(order);
			
					setOrderPackages(order);
					
					Package packageArray[] = new Package[1];
					order.setPackageArray(packageArray);
					}
					
					if (getSession().get("customersList") == null) {
						this.populateCustomersList();
					}
					if(getLoginUser().getCustomerId()>0)
						order.setCustomerId(getLoginUser().getCustomerId());
					this.setShippingOrder(order);
				//}
			//}
		}
			catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		}
		if(getSession().get("WINDOW_STATUS")==null){
			getSession().put("WINDOW_STATUS","show");
		}

		return SUCCESS;
	}
	
	public String repeatOrder()
	{
		log.debug("Inside repeat of ShipmentAction");
		
		getSession().remove("shippingOrder");
		getSession().remove("PackageType");
		ShippingOrder newShippingOrder = new ShippingOrder();
		ShippingOrder order = null;
		String orderId=request.getParameter("order_id");
		if(orderId == null)
			orderId = (String)request.getAttribute("order_id");
		
		if (!StringUtil.isEmpty(orderId)) 
		{
			//Implement in Service layer
			newShippingOrder = shippingService.repeatOrder(orderId);
			
			//Set packages
			setOrderPackages(newShippingOrder);
			//clear the previous errors
			clearActionErrors();
			//Setting address
			setOrderAddress(newShippingOrder);
			//set shippingorder
			this.setShippingOrder(newShippingOrder);
		}
		
		return SUCCESS;
	}
	
	private void setOrderPackages(ShippingOrder order)
	{
		packageTypeList = shippingService.getPackages();
		List<PackageType> listPackages = shippingService.getPackages();
		
		List<HashMap<String, String>> packageOption = new ArrayList<HashMap<String, String>>();
		
		// To generate packages radio buttons on jsp
		//short packeCounter=0;
		for(PackageType pType :listPackages){
			HashMap<String, String> packages = new HashMap<String, String>();
			packages.put(pType.getType(),pType.getName());
			packageOption.add(packages);
		}
		if (order.getPackageTypeId() == null) 
		{
			PackageType pType = new PackageType();
			pType.setType(ShiplinxConstants.PACKAGE_TYPE_PACKAGE_STRING);
			order.setPackageTypeId(pType);
		}
		getSession().put("PackageType", order.getPackageTypeId().getType());
		getSession().put("packageOptions", packageOption);
		getSession().put("listPackages", listPackages);
		if (order.getPackages() == null || (order.getPackages()!=null && order.getPackages().size()==0)) 
		{
			//default quantity is 1
			ArrayList<Package> packages = new ArrayList<Package>();
			if(order.getQuantity()==null)
				order.setQuantity(1);
			
			for(int i=0;i<(int)order.getQuantity(); i++)
			{
				Package pack = new Package();
				pack.setLength( new BigDecimal(1.0));
				pack.setWeight( new Float(1.0));
				pack.setHeight( new BigDecimal(1.0));
				pack.setWidth( new BigDecimal(1.0));
				pack.setCodAmount( new Float(0.0));
				pack.setInsuranceAmount( new Float(0.0));
				packages.add(pack);
			}

			order.setPackages(packages);
		}
		/*else 
		{
			ArrayList<Package> newPackages = new ArrayList<Package>();
			order.setQuantity(order.getPackages().size());
			
			for(Package p: order.getPackages())
			{
				Package pack = new Package();
				pack.setLength(p.getLength());
				pack.setWeight(p.getWeight());
				pack.setHeight(p.getHeight());
				pack.setWidth(p.getWidth());
				pack.setCodAmount(p.getCodAmount());
				pack.setInsuranceAmount(p.getInsuranceAmount());
				newPackages.add(pack);
			}
			
			order.setPackages(newPackages);
		}*/
	}
	
	private void setOrderAddress(ShippingOrder order)
	{
		String fromCountry = "";
		String toCountry = "";
		
		List<Province> toProvinces;
		List<Province> fromProvinces;

		getSession().put("SHIP_MODE", "SHIP");

		if (order.getFromAddress() != null && order.getFromAddress().getCountryCode() != null)
			fromCountry = order.getFromAddress().getCountryCode();
		if (order.getToAddress() != null && order.getToAddress().getCountryCode() != null)
			toCountry = order.getToAddress().getCountryCode();

		//if not set set as default
		if(fromCountry==null ||"".equals(fromCountry)){
			fromCountry = UserUtil.getMmrUser().getBusiness().getAddress().getCountryCode();
		}
		if(toCountry==null ||"".equals(toCountry)){
			toCountry = UserUtil.getMmrUser().getBusiness().getAddress().getCountryCode();
		}

		toProvinces=addressService.getProvinces(toCountry);
		fromProvinces=addressService.getProvinces(fromCountry);

		getSession().put("Fromprovinces", fromProvinces);
		getSession().put("Toprovinces", toProvinces);
		getSession().put("ToCountry", toCountry);
		getSession().put("FromCountry", fromCountry);

		countries=MessageUtil.getCountriesList();

		getSession().put("CountryList", countries);
	}
	
	public String setCustomer() throws Exception {
		String customerId=request.getParameter("customerId"); 
		ShippingOrder shippingOrder = getShippingOrder();
		shippingOrder.setWebCustomerId(Long.valueOf(customerId));
		//setting customer
		shippingOrder.setCustomer(this.customerService.getCustomerInfoByCustomerId(shippingOrder.getCustomerId()));
		
		List<Province> toProvinces;
		List<Province> fromProvinces;
		Address addressbookFrom;
		Address addressbookTo;

		if(shippingOrder.getId()==null || shippingOrder.getId()==0){
			addressbookFrom = addressService.findDefaultFromAddressForCustomer(shippingOrder.getCustomerId());
			addressbookTo = addressService.findDefaultToAddressForCustomer(shippingOrder.getCustomerId());
			shippingOrder.setToAddress(addressbookTo);
			shippingOrder.setFromAddress(addressbookFrom);
		}
		else{
			addressbookFrom = shippingOrder.getFromAddress();
			addressbookTo = shippingOrder.getToAddress();
		}
		
		if(addressbookFrom==null)
			addressbookFrom = new Address();
		if(addressbookTo==null)
			addressbookTo = new Address();

		String fromCountry = "";
		String toCountry = "";
	
		if(addressbookFrom != null)
			fromCountry = addressbookFrom.getCountryCode();
		if(addressbookTo != null)
			toCountry = addressbookTo.getCountryCode();
		
		//if not set set as default
		if(fromCountry==null ||"".equals(fromCountry)){
			fromCountry = UserUtil.getMmrUser().getBusiness().getAddress().getCountryCode();
			addressbookFrom.setCountryCode(fromCountry);
		}
		if(toCountry==null ||"".equals(toCountry)){
			toCountry = UserUtil.getMmrUser().getBusiness().getAddress().getCountryCode();
			addressbookTo.setCountryCode(toCountry);
		}
		
		if(fromCountry==null ||"".equals(fromCountry)){
			fromCountry = UserUtil.getMmrUser().getBusiness().getAddress().getCountryCode();
			addressbookFrom.setCountryCode(fromCountry);
		}
		if(toCountry==null ||"".equals(toCountry)){
			toCountry = UserUtil.getMmrUser().getBusiness().getAddress().getCountryCode();
			addressbookTo.setCountryCode(toCountry);
		}

		toProvinces=addressService.getProvinces(toCountry);
		fromProvinces=addressService.getProvinces(fromCountry);
		
		getSession().put("Fromprovinces", fromProvinces);
		getSession().put("Toprovinces", toProvinces);
		getSession().put("ToCountry", toCountry);
		getSession().put("FromCountry", fromCountry);
	
		return SUCCESS;
	}
	
	
	public String listFromProvience() throws Exception {
		String country;
		country = request.getParameter("value");
		if(country == null || "".equals(country))
			country = ShiplinxConstants.CANADA;
		getShippingOrder().getFromAddress().setCountryCode(country);
		provinces=addressService.getProvinces(country);
		getSession().put("Fromprovinces", provinces);
		return SUCCESS;
	}
	
	public String listPickupProvience() throws Exception {
		String country;
		country = request.getParameter("value");
		if(country == null || "".equals(country))
			country = ShiplinxConstants.CANADA;
		//getShippingOrder().getFromAddress().setCountryCode(country);
		provinces=addressService.getProvinces(country);
		getSession().put("provinces", provinces);
		return SUCCESS;
	}
	
	public String listToProvience() throws Exception {
		String country;
		country = request.getParameter("value");
		if(country == null || "".equals(country))
			country = ShiplinxConstants.CANADA;
		
		String type = request.getParameter("type");
		if(type!=null && type.equalsIgnoreCase("broker")){
			getShippingOrder().getCustomsInvoice().getBrokerAddress().setCountryCode(country);
			getSession().put("brokerProvinces", addressService.getProvinces(country));
			return "success2";
		}
		if(type!=null && type.equalsIgnoreCase("billTo")){
			getShippingOrder().getCustomsInvoice().getBillToAddress().setCountryCode(country);
			getSession().put("billToProvinces", addressService.getProvinces(country));
			return "success3";
		}
		getShippingOrder().getToAddress().setCountryCode(country);
		getShippingOrder().getToAddress().setZipCodeRequired(false);
		provinces=addressService.getProvinces(country);
		getSession().put("Toprovinces", provinces);
		return SUCCESS;
	}

	public String getAddressSuggest() throws Exception {

		String postalCode = (String)request.getParameter("postalCode");
		String country = (String)request.getParameter("countryCode");
		String type = (String)request.getParameter("type");
		
		Address address = new Address();
		address.setPostalCode(postalCode);
		address.setCountryCode(country);
		
		ServiceAvailabilityWebServiceClient zipCodeValidator = new ServiceAvailabilityWebServiceClient();
		address = zipCodeValidator.getSuggestedAddress(address);
		
		if(address!=null){
			inputStream = new StringBufferInputStream(address.getCity());	
			if(type.equalsIgnoreCase("from"))
				this.getShippingOrder().getFromAddress().setProvinceCode(address.getProvinceCode());
			else if(type.equalsIgnoreCase("to")){
				this.getShippingOrder().getToAddress().setProvinceCode(address.getProvinceCode());
			}
			else if(type.equalsIgnoreCase("pickup"))
				this.getPickup().getAddress().setProvinceCode(address.getProvinceCode());
		}
		return SUCCESS;
	}

	public String stageOne() throws Exception {
		ShippingOrder shippingOrder = getShippingOrder();
//		if(getSession().containsKey("shippingOrder"))
//			shippingOrder = (ShippingOrder) getSession().get("shippingOrder");
		
		getSession().put("PackageType",shippingOrder.getPackageTypeId().getType());
		return SUCCESS;
	}
	
	public String backToShipment() throws Exception {
		log.debug("-----backToDimension--SSK------");
		ShippingOrder shippingOrder = getShippingOrder();
		
		// Start Sumanth Kulkarni 14 Oct 2011
		// Removed Comments for the lines to get the ShippingOrder.
		log.debug("-----backToDimension--SSK----1--");
		if(getSession().containsKey("shippingOrder"))
			shippingOrder = (ShippingOrder) getSession().get("shippingOrder");
	//	log.debug("package type::::::::::::"+shippingOrder.getPackageTypeId().getType());
		log.debug("-----backToDimension--SSK----2--");
		//End Sumanth Kulkarni
		
		// Start Sumanth Kulkarni 14 Oct 2011
		//commented code
		/*
		if(shippingOrder.getQuantity()==null || shippingOrder.getPackages().size() == 0){
			//Set the default dimension to the package
			ArrayList<Package> packages = new ArrayList<Package>();
			//default quantity is 1
			if(shippingOrder.getQuantity()==null)
				shippingOrder.setQuantity(1);
			
			for(int i=0;i<(int)shippingOrder.getQuantity(); i++){
				Package pack = new Package();
				pack.setLength( new BigDecimal(1.0));
				pack.setWeight( new Float(1.0));
				pack.setHeight( new BigDecimal(1.0));
				pack.setWidth( new BigDecimal(1.0));
				pack.setCodAmount( new BigDecimal(0.0));
				pack.setInsuranceAmount( new BigDecimal(0.0));
				packages.add(pack);
			}
			
			shippingOrder.setPackages(packages);
			
		}*/
		//End Sumanth Kulkarni
		
		//Code to display additional fields when package type is 'Pallet'
		if("type_pallet".equals(shippingOrder.getPackageTypeId().getType()))
		{
			request.setAttribute("pallet", true);
		}
		String mode = (String)getSession().get("SHIP_MODE");
		String switchFlag = (String)request.getParameter("switch");
		if(switchFlag!=null){
			if(mode==null || mode.equalsIgnoreCase("SHIP")){
				getSession().put("SHIP_MODE", "QUOTE");
				mode = new String("QUOTE");
			}
			else{
				getSession().put("SHIP_MODE", "SHIP");
				mode = new String("SHIP");
			}
		}
		
		//for quick shipment: to populate the service if the carrier was selected before.
		List<Service> services=null;
		services=new ArrayList();
		Service ser=new Service();
//		if(shippingOrder.isQuickShipRequired())
//		{
//			
//			if(shippingOrder.getCarrierId_web()>0)	//check if carrier selected is 'ANY', then dont include 'ANY' option in the service dropdown.
//			{
//				ser.setName("ANY");
//				ser.setId(-1L);
//			}
//			else 
//			{
//				ser.setName("");
//				ser.setId(0L);
//			}
//		}
//		else
//		{
//			ser.setName("------Select------");
//			ser.setId(0l);
//		}
//		services.add(ser);
		//setting the role in req attribute to enable or disable address fields based on role.
		if(getLoginUser().getUserRole().equalsIgnoreCase(ShiplinxConstants.ROLE_CUSTOMER_SHIPPER))
			request.setAttribute("USERROLE", "shipper");
		if(shippingOrder.getCarrierId_web()!=null &&  shippingOrder.getCarrierId_web()>0){
			services.addAll(carrierServiceManager.getServicesForCarrier(shippingOrder.getCarrierId_web()));
		}
		getSession().put("SERVICES", services);
		if(mode==null || mode.equalsIgnoreCase("SHIP"))
			return SUCCESS;
		return "successQuote";
	}
	
	public String backToPackageInformation() throws Exception {
		ShippingOrder shippingOrder = getShippingOrder();
//		if(getSession().containsKey("shippingOrder"))
//			shippingOrder = (ShippingOrder) getSession().get("shippingOrder");
		
		getSession().put("PackageType",shippingOrder.getPackageTypeId().getType());
		
		return SUCCESS;
	}
	/**
	 * Package information
	 * @return
	 * @throws Exception
	 */
//	public String packageInformation() throws Exception {
//		String packageType=ShiplinxConstants.PACKAGE_TYPE_ENVELOPE_STRING; 
//
//		try{
//			List<PackageType> listPackages = shippingService.getPackages();
//			List<HashMap<String, String>> packageOption = new ArrayList<HashMap<String, String>>();
//			
//			// To generate packages radio buttons on jsp
//			for(PackageType pType :listPackages){
//				HashMap<String, String> packages = new HashMap<String, String>();
//				packages.put(pType.getType(),pType.getName());
//				packageOption.add(packages);
//			}
//			getSession().put("packageOptions", packageOption);
//			
//			//if("".equals(request.getParameter("method")) && getSession().containsKey("shippingOrder"))
//			//	shippingOrder = (ShippingOrder) getSession().get("shippingOrder");
//		
//			ShippingOrder shippingOrder = getShippingOrder();
//				//set the default package
//				if(shippingOrder != null && shippingOrder.getPackageTypeId() != null){
//
//					if(shippingOrder.getPackageTypeId().getType() == null || "".equalsIgnoreCase(shippingOrder.getPackageTypeId().getType())){	
//						shippingOrder.setPackageTypeId(shippingService.findPackageType(packageType));
//					}
//					else{	
//						packageType = shippingOrder.getPackageTypeId().getType();	
//					}
//				}
//			
//				Long fromId = null;
//				Long toId = null;
////			if(shippingOrder.isSaveFromAddress() != null && shippingOrder.isSaveFromAddress()){
////				log.debug("------------------Save address as from address----------------");
////				Address address = shippingOrder.getFromAddress();
////				address.setDefaultFromAddress(true);
////				address.setCustomerId(getLoginUser().getCustomerId());
////				address.setIsCustomerOwnInfo("N");
////				
////				fromId  = addressBookService.add(addressbook);
////				log.debug("------addressbook.getId()------"+fromId);
////				shippingOrder.setShipFromId(addressBookService.findAddressById(fromId+""));
////			}
////		
////			if(shippingOrder.isSaveToAddress() != null && shippingOrder.isSaveToAddress()){
////				log.debug("------------------Save address as to address----------------");
////				Address addressbook = shippingOrder.getShipToId();
////				addressbook.setDefaultToAddress(true);
////				addressbook.setCustomerId(getLoginUser().getCustomerId());
////				addressbook.setIsCustomerOwnInfo("N");
////				
////				toId = addressBookService.add(addressbook);
////				log.debug("------addressbook.getId()------"+toId);
////				shippingOrder.setShipToId(addressBookService.findAddressById(toId+""));
////			}
//		
//			//Set the default dimension to the package
//			ArrayList<Package> packages = new ArrayList<Package>();
//			Package packageArray[] = new Package[1];
//			
//			
//			// *FKhan* - Oct. 4 -2011 - There is no need for the following code
////			if(((ShippingOrder)getSession().get("shippingOrder")) != null){
////				
////				ShippingOrder shippingOrder1=(ShippingOrder)getSession().get("shippingOrder");
////				
////				shippingOrder.setQuantity(shippingOrder1.getQuantity());
////				shippingOrder.setPackageTypeId(shippingOrder1.getPackageTypeId());
////				shippingOrder.setReferenceCode(shippingOrder1.getReferenceCode());
////				shippingOrder.setSatDelivery(shippingOrder1.getSatDelivery());
////				shippingOrder.setHoldForPickupRequired(shippingOrder1.getHoldForPickupRequired());
////				shippingOrder.setInsideDelivery(shippingOrder1.getInsideDelivery());
////				shippingOrder.setPickupTime(shippingOrder1.getPickupTime());
////				shippingOrder.setSpecialEquipment(shippingOrder1.getSpecialEquipment());
////				shippingOrder.setCODPayment(shippingOrder1.getCODPayment());
////				shippingOrder.setCODValue(shippingOrder1.getCODValue());
////				shippingOrder.setCODCurrency(shippingOrder1.getCODCurrency());
////				shippingOrder.setCODPayableTO(shippingOrder1.getCODPayableTO());
////				shippingOrder.setDangerousGoods(shippingOrder1.getDangerousGoods());
////				shippingOrder.setReqDeliveryDate(shippingOrder1.getReqDeliveryDate());
////				shippingOrder.setScheduledShipDate(shippingOrder1.getScheduledShipDate());
////				shippingOrder.setPickUpNum(shippingOrder1.getPickUpNum());
////				shippingOrder.setReferenceOne(shippingOrder1.getReferenceOne());
////			}
//				
//			//default quantity is 1
//			if(shippingOrder.getQuantity()==null)
//				shippingOrder.setQuantity(1);
//			
//			packageArray = new Package[(int)shippingOrder.getQuantity()];
//			
//			for(int i=0;i<(int)shippingOrder.getQuantity(); i++){
//				List<Package> packages1=shippingOrder.getPackages();
//				if(i<packages1.size()){
//					log.debug("--getWeight--"+packages1.get(i).getWeight());
//					packages.add(packages1.get(i));
//				}else{
//					Package pack = new Package();
//					pack.setLength( new BigDecimal(1.0));
//					pack.setWeight( new Float(1.0));
//					pack.setHeight( new BigDecimal(1.0));
//					pack.setWidth( new BigDecimal(1.0));
//					pack.setCodAmount( new BigDecimal(0.0));
//					pack.setInsuranceAmount( new BigDecimal(0.0));
//					packages.add(pack);
//					packageArray[i] = pack;
//				}
//			}
//	
////			RIZM - COMMENTING OUT IN ORDER TO REFACTOR ADDRESS OBJECT
////			if(((ShippingOrder)getSession().get("shippingOrder")).getShipFromId() != null)
////			shippingorder.getFromAddress().setId(((ShippingOrder)getSession().get("shippingOrder")).getShipFromId().getId());
////			if(((ShippingOrder)getSession().get("shippingOrder")).getShipToId() != null)
////			shippingOrder.getShipToId().setId(((ShippingOrder)getSession().get("shippingOrder")).getShipToId().getId());
////	
////			if(fromId != null)
////					shippingorder.getFromAddress().setId(fromId);
////			if(toId != null)
////				shippingOrder.getShipToId().setId(toId);
//			
//			shippingOrder.setPackageArray(packageArray);
//			shippingOrder.setPackages(packages);
//				
//			String orderId=(String)getSession().get("EDIT_ORDER_ID");
//			if(orderId!=null){
//			
//				ShippingOrder shippingOrder1=shippingService.getShippingOrder(Long.parseLong(orderId));
//								shippingOrder.setReferenceCode(shippingOrder1.getReferenceCode());
//								shippingOrder.setSatDelivery(shippingOrder1.getSatDelivery());
//								shippingOrder.setHoldForPickupRequired(shippingOrder1.getHoldForPickupRequired());
//								shippingOrder.setInsideDelivery(shippingOrder1.getInsideDelivery());
//								shippingOrder.setPickupTime(shippingOrder1.getPickupTime());
//								shippingOrder.setSpecialEquipment(shippingOrder1.getSpecialEquipment());
//								shippingOrder.setCODPayment(shippingOrder1.getCODPayment());
//								shippingOrder.setCODValue(shippingOrder1.getCODValue());
//								shippingOrder.setCODCurrency(shippingOrder1.getCODCurrency());
//								shippingOrder.setCODPayableTO(shippingOrder1.getCODPayableTO());
//								shippingOrder.setDangerousGoods(shippingOrder1.getDangerousGoods());
//								shippingOrder.setReqDeliveryDate(shippingOrder1.getReqDeliveryDate());
//								shippingOrder.setScheduledShipDate(shippingOrder1.getScheduledShipDate());
//								shippingOrder.setPickUpNum(shippingOrder1.getPickUpNum());
//								shippingOrder.setReferenceOne(shippingOrder1.getReferenceOne());
//								shippingOrder.setReferenceTwo(shippingOrder1.getReferenceTwo());
//								shippingOrder.setMasterTrackingNum(shippingOrder1.getMasterTrackingNum());
//								shippingOrder.setRes(shippingOrder1.getRes());
//								shippingOrder.setFromTailgate(shippingOrder1.getFromTailgate());
//								shippingOrder.setToTailgate(shippingOrder1.getToTailgate());
//								shippingOrder.setFromAttention(shippingOrder1.getFromAttention());
//								shippingOrder.setToAttention(shippingOrder1.getToAttention());
//								shippingOrder.setNotifyRecipient(shippingOrder1.getNotifyRecipient());
//								shippingOrder.setConfirmDelivery(shippingOrder1.getConfirmDelivery());
//			}
////			getSession().put("shippingOrder",shippingOrder);
//			if("dimensionInformation".equals(request.getParameter("method"))){
//				return "DIMENSSION_PAGE";
//			}
//		
//		}catch (Exception e) {
//			log.debug("----Exception-----"+e);
//			e.printStackTrace();
//		}
//			return SUCCESS; //default is type_env
//		
//	}
	
	/**
	 * Dimension information
	 * @return
	 * @throws Exception
	 */
	public String dimensionInformation() throws Exception {
		ShippingOrder shippingOrder = getShippingOrder();
//		shippingOrder = (ShippingOrder)getSession().get("shippingOrder");
		//Set the default dimension to the package
		ArrayList<Package> packages = new ArrayList<Package>();
		Package packageArray[] = new Package[1];

		String quantity = request.getParameter("quantity");
		String type = request.getParameter("type");
		
		try{
		
		//default quantity is 1
		if(quantity!=null){
			shippingOrder.setQuantity(Integer.valueOf(quantity));
		}
		if(type!=null){
			if(type.equalsIgnoreCase(ShiplinxConstants.PACKAGE_TYPE_ENVELOPE_STRING) || type.equalsIgnoreCase(ShiplinxConstants.PACKAGE_TYPE_PAK_STRING))
				shippingOrder.setQuantity(1);

			PackageType packageType  = new PackageType();		
			packageType  = shippingService.findPackageType(type);			
			shippingOrder.setPackageTypeId(packageType);

		}
		
		packageArray = new Package[(int)shippingOrder.getQuantity()];
		
		for(int i=0;i<(int)shippingOrder.getQuantity(); i++){
			List<Package> packages1=((ShippingOrder)getSession().get("shippingOrder")).getPackages();
			if(i<packages1.size()){
				log.debug("--getWeight--"+request.getParameter("shippingOrder.packageArray["+ String.valueOf(i) + "].weight"));
				packages.add(packages1.get(i));
			}else{
				Package pack = new Package();
				pack.setLength( new BigDecimal(1.0));
				pack.setWeight( new Float(1.0));
				pack.setHeight( new BigDecimal(1.0));
				pack.setWidth( new BigDecimal(1.0));
				pack.setCodAmount( new Float(0.0));
				pack.setInsuranceAmount( new Float(0.0));
				packages.add(pack);
				packageArray[i] = pack;
			}
		}

		
		shippingOrder.setPackageArray(packageArray);
		shippingOrder.setPackages(packages);
		
		
		}catch (Exception e) {
			log.debug("exception-------------"+e);
			e.printStackTrace();
		}
		
		getSession().put("shippingOrder",shippingOrder);
		
		return SUCCESS;
	}
	
//	public String stageTwo() throws Exception {
//		String packageType=""; 
//		ShippingOrder shippingOrder = getShippingOrder();
//		try{
//			//set the default package type
//			if(shippingOrder != null && shippingOrder.getPackageTypeId() != null){
//				
//				if(shippingOrder.getPackageTypeId().getType() == null || "".equalsIgnoreCase(shippingOrder.getPackageTypeId().getType())){	
//					packageType= "type_env"; //default
//				}
//				else
//					packageType = shippingOrder.getPackageTypeId().getType();
//			}
//			
//			PackageType packType =  shippingService.findPackageType(packageType);
//			shippingOrder.setPackageTypeId(packType);
//			
//			//find package by name selected
//			shippingOrder.setPackageTypeId(shippingOrder.getPackageTypeId());
//			
//			//Set the default dimension to the package
//			ArrayList<Package> packages = new ArrayList<Package>();
//			
//			//default quantity is 1
//			if(shippingOrder.getQuantity()==null)
//				shippingOrder.setQuantity(1);
//
//			String orderId=(String)getSession().get("EDIT_ORDER_ID");
//		
//			if(orderId==null){
//			
//			for(int i=0;i<(int)shippingOrder.getQuantity(); i++){
//				
//				List<Package> packages1=((ShippingOrder)getSession().get("shippingOrder")).getPackages();
//				if(i<packages1.size()){
//					log.debug("--getWeight--"+packages1.get(i).getWeight());
//					packages.add(packages1.get(i));
//				}else{
//					Package pack = new Package();
//					pack.setLength( new BigDecimal(1.0));
//					pack.setWeight( new Float(1.0));
//					pack.setHeight( new BigDecimal(1.0));
//					pack.setWidth( new BigDecimal(1.0));
//					pack.setCodAmount( new BigDecimal(0.0));
//					pack.setInsuranceAmount( new BigDecimal(0.0));
//					packages.add(pack);
//				}
//			}
//			
//			}else{
//				ArrayList<Package> packages1=(ArrayList<Package>)shippingService.getShippingPackages(Long.parseLong(orderId));
//				for(int i=0;i<(int)shippingOrder.getQuantity(); i++){
//					if(i<packages1.size()){
//						packages.add(packages1.get(i));
//					}else{
//						Package pack = new Package();
//						pack.setLength( new BigDecimal(1.0));
//						pack.setWeight( new Float(1.0));
//						pack.setHeight( new BigDecimal(1.0));
//						pack.setWidth( new BigDecimal(1.0));
//						pack.setCodAmount( new BigDecimal(0.0));
//						pack.setInsuranceAmount( new BigDecimal(0.0));
//						packages.add(pack);
//					}
//				}
//			}
//			
//			shippingOrder.setShipFromId(((ShippingOrder)getSession().get("shippingOrder")).getShipFromId());
//			shippingOrder.setShipToId(((ShippingOrder)getSession().get("shippingOrder")).getShipToId());
//			shippingOrder.setRecidential(((ShippingOrder)getSession().get("shippingOrder")).getRecidential());
//			shippingOrder.setNotifyRecipient(((ShippingOrder)getSession().get("shippingOrder")).getNotifyRecipient());
//			shippingOrder.setConfirmDelivery(((ShippingOrder)getSession().get("shippingOrder")).getConfirmDelivery());
//			shippingOrder.setToTailgate(((ShippingOrder)getSession().get("shippingOrder")).getToTailgate());
//			shippingOrder.setFromTailgate(((ShippingOrder)getSession().get("shippingOrder")).getFromTailgate());
//			
//			shippingOrder.setPackages(packages);
//			getSession().put("shippingOrder",shippingOrder);
//			
//			//to maintain the values while click on tabs
//			if("stageOne".equals(request.getParameter("method"))){
//				return "STAGE_ONE";
//			}
//			if("backToDimension".equals(request.getParameter("method"))){
//				return "DIMENSSION_PAGE";
//			}
//			if(packageType.equalsIgnoreCase("type_pallet") || packageType.equalsIgnoreCase("type_package"))
//				return "DIMENSSION_PAGE";
//		
//		}catch (Exception e) {
//			e.printStackTrace();
//			log.debug("----Exception-----"+e);
//			addActionError(getText("error.input"));
//			return INPUT;
//		}
//			return SUCCESS; //default is type_env
//		
//	}
	
	public String setShippingAddress() throws Exception {
		String fromCountry = "";
		String toCountry = "";
		List<Province> fromProvinces;
		List<Province> toProvinces;
		String type = request.getParameter("type");
		String addressId = request.getParameter("addressid");
		String strReturn = null;
		if(type==null || addressId==null)
			return SUCCESS;
		
//		if(getSession().containsKey("shippingOrder"))
//			shippingOrder = (ShippingOrder) getSession().get("shippingOrder");
		ShippingOrder shippingOrder = getShippingOrder();
		if("fromAddress".equals(type)){
			Address address = addressService.findAddressById(addressId);
			
			fromCountry = address.getCountryCode();
			shippingOrder.setFromAddress(address);
			shippingOrder.getFromAddress().setCustomerId(0);
			getSession().put("shippingOrder",shippingOrder);
			//request.setAttribute("shippingOrder",shippingOrder);
			getSession().put("FromCountry", fromCountry);
			fromProvinces=addressService.getProvinces(fromCountry);
			getSession().put("Fromprovinces", fromProvinces);
			strReturn = "success1";
			}
		else if("toAddress".equals(type)){
			Address address = addressService.findAddressById(request.getParameter("addressid"));
			
			toCountry = address.getCountryCode();
			shippingOrder.setToAddress(address);
			shippingOrder.getToAddress().setCustomerId(0);
			getSession().put("shippingOrder",shippingOrder);
			//request.setAttribute("shippingOrder",shippingOrder);
			getSession().put("ToCountry", toCountry);
			toProvinces=addressService.getProvinces(toCountry);
			getSession().put("Toprovinces", toProvinces);
			strReturn = "success2";
		}
		else 	//pickup address
		{	
			Pickup pickup = this.getPickup();
			Address address = addressService.findAddressById(request.getParameter("addressid"));
			
			toCountry = address.getCountryCode();
			//shippingOrder.setToAddress(address);
			//shippingOrder.getToAddress().setCustomerId(0);
			pickup.setAddress(address);
			//getSession().put("shippingOrder",shippingOrder);
			//request.setAttribute("shippingOrder",shippingOrder);
			getSession().put("ToCountry", toCountry);
			toProvinces=addressService.getProvinces(toCountry);
			getSession().put("Toprovinces", toProvinces);
			strReturn = "success3";
			
		}
		getSession().remove("type");
		return strReturn; 
	}
	
	
	
	/**
	 * Rating
	 * @return
	 * @throws Exception
	 */
	public String stageThree() throws Exception {
		log.debug("-----------stageThree-------------");
		ShippingOrder shippingOrder = getShippingOrder();
		List<Package> packageList = shippingOrder.getPackages();
		Package packageArray[] = shippingOrder.getPackageArray();
		log.debug("packageArray.length::::"+packageArray.length);
		if(shippingOrder.getCustomsInvoice()==null)
			shippingOrder.setCustomsInvoice(new CustomsInvoice());
		
		CustomsInvoice customsInvoice = shippingOrder.getCustomsInvoice();
		CustomsInvoiceProduct customsInvoiceProduct = null;
		if (customsInvoice != null && customsInvoice.getId() >=0 && customsInvoice.getProducts() != null && customsInvoice.getProducts().size() >0 ) {
			customsInvoiceProduct = customsInvoice.getProducts().get(0);
		} else {
			customsInvoiceProduct = new CustomsInvoiceProduct();
		}
		
		//set the total value of the customs invoice, it should be done in business layer, but this is a fix for now
		if(shippingOrder.getCustomsInvoice().getTotalValue()!=null && shippingOrder.getCustomsInvoice().getTotalValue()==0 && shippingOrder.getCustomsInvoice().getProducts()!=null && shippingOrder.getCustomsInvoice().getProducts().size()>0){
			for(CustomsInvoiceProduct cip: shippingOrder.getCustomsInvoice().getProducts()){
				shippingOrder.getCustomsInvoice().setTotalValue(FormattingUtil.add(shippingOrder.getCustomsInvoice().getTotalValue().doubleValue(), cip.getProductTotalPrice()).doubleValue());
			}
		}
		
		if(customsInvoice.getTotalWeight() == null || customsInvoice.getTotalWeight().doubleValue() == 0){
			customsInvoice.setTotalWeight(shippingOrder.getTotalWeight().longValue());
		}
		
		if(shippingOrder.getCustomsInvoice().getBrokerAddress() == null){
			shippingOrder.getCustomsInvoice().setBrokerAddress(new Address());
			shippingOrder.getCustomsInvoice().getBrokerAddress().setCountryCode(shippingOrder.getToAddress().getCountryCode());
			
		}
		if (shippingOrder.getCustomsInvoice().getBillToAddress() == null) {
			shippingOrder.getCustomsInvoice().setBillToAddress(new Address());
		}
		if(shippingOrder.getCustomsInvoice().getBillToAddress().getCountryCode()==null || shippingOrder.getCustomsInvoice().getBillToAddress().getCountryCode().length()==0)
			shippingOrder.getCustomsInvoice().getBillToAddress().setCountryCode(shippingOrder.getToAddress().getCountryCode());
		getSession().put("brokerProvinces", addressService.getProvinces(shippingOrder.getCustomsInvoice().getBrokerAddress().getCountryCode()));
		getSession().put("billToProvinces", addressService.getProvinces(shippingOrder.getCustomsInvoice().getBillToAddress().getCountryCode()));
			
		
		//get session stored values
//		shippingOrder = (ShippingOrder) getSession().get("shippingOrder");
		if(packageList.size() != 0)
			shippingOrder.setPackages(packageList);
		
		//todo : accept from UI
		shippingOrder.setCODValue(0d);
//		shippingOrder.setCustomerId(getLoginUser().getCustomerId());
		
		PackageType packageType  = new PackageType();		
		packageType  = shippingService.findPackageType(shippingOrder.getPackageTypeId().getType());		
		shippingOrder.setPackageTypeId(packageType);
		
		//set default quantity
		if(shippingOrder.getQuantity()==null){
			shippingOrder.setQuantity(1);
		}
		
		
		for(int i=0;i<(int)shippingOrder.getQuantity(); i++){
			
			List<Package> packages1=shippingOrder.getPackages();
				
		}
		
		//customsInvoice.setBillToAddress(shippingOrder.getToAddress());	
		customsInvoice.getBillToAddress().copyAddress(shippingOrder.getToAddress());
		customsInvoiceProduct.setCustomsInvoiceId(customsInvoice.getId());
				
		//to maintain the values while click on tabs
		if("stageOne".equals(request.getParameter("method"))){
			getSession().put("shippingOrder",shippingOrder);
			return "STAGE_ONE";
		}
		if("packageInformation".equals(request.getParameter("method"))){
			getSession().put("shippingOrder",shippingOrder);
			return "PACKAGE_INFO";
		}
		
		//reset the service if it has been set
		shippingOrder.setService(null);
		
		//set the User GL if set
		if(!StringUtil.isEmpty(getLoginUser().getUserGLOrRefNumber()))
		{
			shippingOrder.setReferenceTwo(getLoginUser().getUserGLOrRefNumber());	//reference2
			shippingOrder.setReferenceTwoName(ShiplinxConstants.USER_GL);			//reference2_name
		}
		
		try{
			//clear the previous errors
			clearActionErrors();
			
			List<Rating> ratingList = carrierServiceManager.rateShipment(shippingOrder);
			
			//Get the error messages returned from the carriers
			for(CarrierErrorMessage carrierErrorMessage  :carrierServiceManager.getErrorMessages()){
				addActionError(carrierErrorMessage.getMessage());
			}
			//setting temp id for rate list
			int counter =1;
			for(Rating r : ratingList){
				r.setId(counter++);
			}
			shippingOrder.setRateList(ratingList);
			
			List<HashMap<String, String>> rateOption = new ArrayList<HashMap<String,String>>();
			
			//to select the rate (radio option)
			Iterator iterator = ratingList.iterator();
			for(int i=0;i<ratingList.size();i++){
				HashMap rate = new HashMap();
				rate.put("rateIndex"+i, "");
				rateOption.add(rate);
			}	
					
			shippingOrder.setCustomsInvoice(customsInvoice);
			
			getSession().put("RateOption", rateOption);
			getSession().put("shippingOrder",shippingOrder);
			
			//setting autoprint value for the login user
			User user = userService.findUserByUsername(UserUtil.getMmrUser().getUsername());
			//setting the print config values for the logged user
			//request.setAttribute("no_of_lbls", user.getPrintNoOfLabels());
			//request.setAttribute("no_of_ci", user.getPrintNoOfCI());
			request.setAttribute("autoprint", user.isAutoPrint());
			
			if(ratingList==null || ratingList.size() == 0){
				
				if(shippingOrder.getCarrierId_web()!=null && shippingOrder.getCarrierId_web()>0)
					getSession().put("SERVICES", carrierServiceManager.getServicesForCarrier(shippingOrder.getCarrierId_web()));
				addActionError(getText("shippingOrder.rate.empty"));	
				return INPUT;
			}

		
		}catch (Exception e) {
			e.printStackTrace();
			for(CarrierErrorMessage carrierErrorMessage  :carrierServiceManager.getErrorMessages()){
				addActionError(carrierErrorMessage.getMessage());
			}
			addActionError(getText("shippingOrder.rate.error"));
		}
		
		//if quick ship option is chosen, then redirect to ship
		if((shippingOrder.getServiceId_web()!=null && shippingOrder.getServiceId_web()>0) || shippingOrder.isCheapestMethod() || shippingOrder.isFastestMethod())
			if(shippingOrder.getRateList().size()==1){
				return "ship";
			}
		
		if(shippingOrder.getBillToType()!=null && (shippingOrder.getBillToType().equalsIgnoreCase(ShiplinxConstants.BILL_TO_THIRD_PARTY) || shippingOrder.getBillToType().equalsIgnoreCase(ShiplinxConstants.BILL_TO_COLLECT)))
			//getSession().put("BillToType", shippingOrder.getBillToType());
			request.setAttribute("BillToType", shippingOrder.getBillToType()+" - "+shippingOrder.getBillToAccountNum());
		return SUCCESS;
		
	}
	
	public String performRating(){
		
		return SUCCESS;
		
	}
	
	public String additionalServices(){
		String packageType= request.getParameter("type");
		//get the id from package type table
		if(packageType.equalsIgnoreCase(ShiplinxConstants.PACKAGE_TYPE_PAK_STRING)){
			return "SUCCESS_PAK";
		}else if(packageType.equalsIgnoreCase(ShiplinxConstants.PACKAGE_TYPE_PALLET_STRING)){
			return "SUCCESS_PALLATE";
		}else if(packageType.equalsIgnoreCase(ShiplinxConstants.PACKAGE_TYPE_PACKAGE_STRING)){
			return "SUCCESS_PACKAGE";
		}else
			return SUCCESS; //default is type_env
			
			

	}
	
	public String save(){
		log.debug("-------------Save Shipment -------------");
		ShippingOrder shippingOrder = getShippingOrder();
		boolean saveCI = true;
		
		//if in "QUOTE" mode, then need to capture detailed shipping information
		String mode = (String)getSession().get("SHIP_MODE");
		if(mode!=null && mode.equalsIgnoreCase("QUOTE")){
			getSession().put("SHIP_MODE", "SHIP");
			this.addActionMessage(getText("shippingOrder.ship.info"));
			return ERROR;
		}
						
		boolean CIreqd = shippingOrder.isCustomsInvoiceRequired();
		if(CIreqd)
		{
			if(shippingOrder.getCustomsInvoice().getBillTo().equals("1"))
				shippingOrder.getCustomsInvoice().setBillTo("Shipper");
			else if(shippingOrder.getCustomsInvoice().getBillTo().equals("2"))
				shippingOrder.getCustomsInvoice().setBillTo("Consignee");
			else if(shippingOrder.getCustomsInvoice().getBillTo().equals("3"))
				shippingOrder.getCustomsInvoice().setBillTo("Third Party");
		}
				
		try{	
		
			String rateIndex = shippingOrder.getRateIndex();
			
			//set the default rate index if not selected, as first rate.
			if(rateIndex == null || "".equals(rateIndex))
				rateIndex = "0";
			
	//		shippingOrder = (ShippingOrder) getSession().get("shippingOrder");
			
			//get the rating/surcharges/charges for selected index from the list
			Rating r = shippingOrder.getRateList().get(Integer.parseInt(rateIndex));
			//Charge shippingCharge = r.getCharge();
			
			if(getLoginUser().getCustomerId()>0)
				shippingOrder.setCustomerId(getLoginUser().getCustomerId());
			shippingOrder.setCarrierId(r.getCarrierId());
			shippingOrder.setServiceId(r.getServiceId());
			shippingOrder.setRateIndex(rateIndex);
			shippingOrder.setService(this.shippingService.getServiceById(shippingOrder.getServiceId()));
			shippingOrder.setCharges(r.getCharges());
			log.debug("---r.getFuelSurcharge()---"+r.getFuelSurcharge());
			
			//shippingOrder.setFuelCharges(r.getFuelSurcharge());
			shippingOrder.setCarrierName(r.getCarrierName());
			//shippingOrder.setTotalCharge(r.getTotal());
			
			List<Rating> rateList = new ArrayList<Rating>();
			rateList.add(r);
			shippingOrder.setRateList(rateList);
			
			clearActionErrors();
	
			if(shippingOrder.getCustomer().getPaymentType()==ShiplinxConstants.PAYMENT_TYPE_CREDIT_CARD && shippingOrder.getCustomer().getCreditCardActive()==null){ //This is a credit card customer and we do not have profile stored on file (i.e with processor)
				if(shippingOrder.getCreditCard()==null)
					shippingOrder.setCreditCard(new CreditCard());
				if(shippingOrder.getCreditCard().getCcNumber()==null || shippingOrder.getCreditCard().getCcNumber().length()==0){ //credit card information not entered
					shippingOrder.setPaymentRequired(true);
					return viewShipment();
				}
			}
		
			String orderId=(String)getSession().get("EDIT_ORDER_ID");
			if(orderId!=null){
				ShippingOrder shippingOrderDelete=shippingService.getShippingOrder(Long.parseLong(orderId));
				shippingOrderDelete.setId(Long.parseLong(orderId));
				boolean isAdmin = UserUtil.getMmrUser().getUserRole().equals("busadmin");
				if(carrierServiceManager.cancelOrder(Long.parseLong(orderId), isAdmin)){
//					shippingService.updateShippingOrder(Long.parseLong(orderId));
					//save shipping order
					CarrierService CarrierService = carrierServiceManager.shipOrder(shippingOrder,  r);

				}else{
					throw new Exception(getText("shippingOrder.save.error"));
				}
			}else{
				if(shippingOrder.getBillToType().equalsIgnoreCase(ShiplinxConstants.BILL_TO_THIRD_PARTY) || shippingOrder.getBillToType().equalsIgnoreCase(ShiplinxConstants.BILL_TO_COLLECT))
					shippingOrder.getCharges().removeAll(shippingOrder.getCharges());
				carrierServiceManager.shipOrder(shippingOrder,  r);
				if(shippingOrder.getToAddress().isSendNotification() || shippingOrder.getFromAddress().isSendNotification()){
					if(shippingService.sendShipmentNotificationMail(shippingOrder,UserUtil.getMmrUser().getBusiness()))
						addActionMessage(MessageUtil.getMessage("shipment.notification.mail.success"));
					else
						addActionError(MessageUtil.getMessage("shipment.notification.mail.failure"));
				}
			}
		}
		catch (CardProcessingException cpe) { //this is if card could not be authorized
			addActionError(getText("creditCard.payment.notProcessed") + " " + cpe.getMessage());
			//go back to pay/details page
			return ERROR;
		}
		catch (Exception e) {
			log.debug("Shipping Error!", e);
			addActionError(getText("shippingOrder.save.error"));
			//Get the error messages returned from the carriers
			for(CarrierErrorMessage carrierErrorMessage  :carrierServiceManager.getErrorMessages()){
				addActionError(carrierErrorMessage.getMessage());
			}
			return ERROR;
		}
		
		//if order created successfully
		if(carrierServiceManager.getErrorMessages().size() == 0)
			addActionMessage(getText("shippingOrder.save.successfully"));
			
		return viewShipment();
	}
	
	public Pickup getPickup() {
		Pickup pickup = (Pickup)getSession().get("pickup");
		if (pickup == null) {
			pickup = new Pickup();
			pickup.setBusinessId(UserUtil.getMmrUser().getBusinessId());
			pickup.setCustomerId(UserUtil.getMmrUser().getCustomerId());
			setPickup(pickup);
		}
		return pickup;
	}
	public void setPickup(Pickup pickup) {

		getSession().put("pickup", pickup);
	}
	
	public ShippingOrder getShippingOrder() {
		ShippingOrder order = (ShippingOrder)getSession().get("shippingOrder");
		if (order == null) {
			order = new ShippingOrder();
			order.setBusinessId(UserUtil.getMmrUser().getBusinessId());
			
			setShippingOrder(order);
		}
		return order;
	}
	public void setShippingOrder(ShippingOrder shippingOrder) {

		getSession().put("shippingOrder", shippingOrder);
	}
	

	public List<OrderStatus> getOrderStatusList() {
		return (List<OrderStatus>) getSession().get("orderStatusList");

	}
	public void setOrderStatusList(List<OrderStatus> orderStatusList) {
		if (orderStatusList != null) {
			orderStatusList.add(0, new OrderStatus(-1, ""));
		}
		getSession().put("orderStatusList", orderStatusList);
	}
	
	public List<OrderStatus> getOrderStatusOptionsList() {
		return (List<OrderStatus>) getSession().get("orderStatusOptionsList");

	}
	public void setOrderStatusOptionsList(List<OrderStatus> orderStatusOptionsList) {
		if (orderStatusOptionsList != null) {
			orderStatusOptionsList.add(0, new OrderStatus(-1, ""));
		}
		getSession().put("orderStatusOptionsList", orderStatusOptionsList);
	}
	
	public Charge getNewQuotedCharge() {
		Charge newCharge = (Charge) getSession().get("newQuotedCharge");
		if (newCharge == null) {
			newCharge = new Charge();
			setNewQuotedCharge(newCharge);
		}
		return newCharge;
	}

	public void setNewQuotedCharge(Charge newCharge) {
		getSession().put("newQuotedCharge", newCharge);
	}
	
	public Charge getNewActualCharge() {
		Charge newCharge = (Charge) getSession().get("newActualCharge");
		if (newCharge == null) {
			newCharge = new Charge();
			setNewActualCharge(newCharge);
		}
		return newCharge;
	}

	public void setNewActualCharge(Charge newCharge) {
		getSession().put("newActualCharge", newCharge);
	}	
	
	public ShippingOrder getSelectedOrder() {
		ShippingOrder order = (ShippingOrder)getSession().get("selectedOrder");
		return order;
	}
	public void setSelectedOrder(ShippingOrder selectedOrder) {
		getSession().put("selectedOrder", selectedOrder);
	}	

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public AddressManager getAddressService() {
		return addressService;
	}

	public void setAddressService(AddressManager addressService) {
		this.addressService = addressService;
	}

	public ShippingService getShippingService() {
		return shippingService;
	}

	public void setShippingService(ShippingService shippingService) {
		this.shippingService = shippingService;
	}
	
	public List<Country> getCountries() {
		return countries;
	}
	public void setCountries(List<Country> countries) {
		this.countries = countries;
	}

	public List getPackageTypeList() {
		return packageTypeList;
	}
	public void setPackageTypeList(List packageType) {
		this.packageTypeList = packageType;
	}
	public List<Province> getProvinces() {
		return provinces;
	}
	public void setProvinces(List<Province> provinces) {
		this.provinces = provinces;
	}

	public CarrierServiceManager getCarrierServiceManager() {
		return carrierServiceManager;
	}

	public void setCarrierServiceManager(CarrierServiceManager carrierServiceManager) {
		this.carrierServiceManager = carrierServiceManager;
	}

	
	public String trackShipment(){
			String strReturn = null;
		long orderId = 0;
		if(request.getParameter("id") != null)
			orderId = new Long(request.getParameter("id"));
		String trackingURL = this.shippingService.getTrackingURL(orderId);
		if(trackingURL!=null && trackingURL.length()>0){
			nextAction = new String(trackingURL);
			strReturn = "success";
		}
		else
		{
			nextAction = request.getContextPath()+"/admin/view.shipment.action?notrackurl='true'&viewShipmentId="+orderId;
			strReturn = "success";
		}
		return strReturn;
	}
	
	//TODO : Incomplete , need to complete to display for order detail from tracking page
	public String trackShipmentDetail(){
		Long orderId = 0L;
		
		if(request.getParameter("id") != null)
			orderId = new Long(request.getParameter("id"));
		
		log.debug("-----trackShipmentDetail----"+orderId);
		ShippingOrder shippingOrder = shippingService.getShippingOrder(orderId);
		
		Service s = shippingService.getServiceById(shippingOrder.getServiceId());
		
		PackageType packageType  = shippingService.findOrderPackageType(orderId);
		
		List<Package> packagesList = shippingService.getShippingPackages(orderId);
		
		shippingOrder.setServiceName(s.getName());
		shippingOrder.setPackageTypeId(packageType);
		shippingOrder.setQuantity(packagesList.size());
		shippingOrder.setPackages(packagesList);
		shippingOrder.setCarrierName(shippingOrder.getCarrierId()==ShiplinxConstants.CARRIER_FEDEX ?
					ShiplinxConstants.CARRIER_FEDEX_STRING : ((shippingOrder.getCarrierId()==ShiplinxConstants.CARRIER_UPS ? ShiplinxConstants.CARRIER_UPS_STRING :"")));
		shippingOrder.setScheduledShipDate(shippingOrder.getScheduledShipDate());
		shippingOrder.setCharges(shippingService.getShippingOrderCharges(orderId));
		
		List<Charge> chargesList = shippingService.getShippingOrderCharges(orderId);
		List<Rating> ratingList = new ArrayList<Rating>();
		
		List<Charge> chargesList2 = new ArrayList<Charge>(); 
		
		Rating rating = new Rating();
		for(Charge c :chargesList){
			Charge charge = new Charge();
			double amount = c.getCost();
			String chargeName = c.getName();
			
			log.debug("-----c.getName()::"+c.getName()+"-----c.getAmount()::"+c.getCost());
			
			if(ShiplinxConstants.Charge_Total.equalsIgnoreCase(chargeName))
				rating.setTotalCost(amount);
			else if(ShiplinxConstants.Charge_BaseCharge.equalsIgnoreCase(chargeName))
				rating.setBaseCharge(amount);
			else{
				charge.setCost(amount);
				charge.setName(chargeName);
				chargesList2.add(charge);
			}
		}
		
		rating.setCharges(chargesList2);
		ratingList.add(rating);
		shippingOrder.setRateList(ratingList);
		setShippingOrder(shippingOrder);
				
		return SUCCESS;
	}
	
	public String listCarrierServices(){
		String carrierId;
		carrierId = request.getParameter("value");
		List<Service> services=null;
		if(carrierId!=null && carrierId.length()>0){
			services=new ArrayList();
			Service ser=new Service();
			if(request.getParameter("quickship")!=null)
			{
				if(Long.parseLong(carrierId)>0)	//check if carrier selected is 'ANY', then dont include 'ANY' option in the service dropdown.
				{
					ser.setName("ANY");
					ser.setId(-1L);
				}
				else 
				{
					ser.setName("");
					ser.setId(0L);
				}
			}
			else
			{
				ser.setName("------Select------");
				ser.setId(0l);
			}
			services.add(ser);
			
			if(!carrierId.equals("0")){
				services.addAll(carrierServiceManager.getServicesForCarrier(Long.parseLong(carrierId)));
			}
			getSession().put("services", services);
			
		}
		
		if(request.getParameter("quickship")!=null)
			return "success2";
		
		return SUCCESS;
		
	}
	
	public String listSearchedShipment(){
		
		ShippingOrder shippingOrder=this.getShippingOrder();
		
		if(shippingOrder.getCarrierId()==0){
			shippingOrder.setCarrierId(null);
		}
		if(shippingOrder.getServiceId()==0){
			shippingOrder.setServiceId(null);
		}
		if(shippingOrder.getStatusId()==0){
			shippingOrder.setStatusId(null);
		}
		if(shippingOrder.getMasterTrackingNum()==null || shippingOrder.getMasterTrackingNum().trim().equals("")){
			shippingOrder.setMasterTrackingNum(null);
		}

		orderList=shippingService.getSearchOrderResult(shippingOrder);
		getSession().put("ORDERLISTSIZE",orderList.size());
		

	
		return SUCCESS;
	}	
	
	
	public String listReferenceShipments()
	{
		log.debug("Inside listReferenceShipments of Shipment Action");
		ShippingOrder order = this.getShippingOrder();
		try {
			if(UserUtil.getMmrUser().getUserRole().equals("customer_admin"))
				order.setCustomerId(UserUtil.getMmrUser().getCustomerId());
			else
			{
				order.setCustomerId(0L);
			}
			
			List<ShippingOrder> listSO = new ArrayList<ShippingOrder>();
			listSO = shippingService.searchReferenceShipments(order);
			
			if(listSO.size()==0)
			{
				addActionError(getText("ref.search.mismatch"));
				ShippingOrder shippingOrder = getShippingOrder();
				this.setShippingOrder(shippingOrder);
			}
			else
			{
				//if there are any that are ready to process, then load the first one we find that is ready to process, else perform a repeat of the last one created
				ShippingOrder foundRTP = null;
				for(ShippingOrder o: listSO){
					if(o.getStatusId()==ShiplinxConstants.STATUS_READYTOPROCESS){
						foundRTP = o;
						//persist the input value after reloading the page
						//setOrderPackages(listSO.get(0));
						setOrderAddress(shippingService.getShippingNewOrder(foundRTP.getId()));
						this.setShippingOrder(shippingService.getShippingNewOrder(foundRTP.getId()));
						getShippingOrder().setReferenceValue(order.getReferenceValue());
						break;
					}
				}
				
				if(foundRTP == null){ //repeat the last one in the list
					request.setAttribute("order_id", listSO.get(listSO.size()-1).getId().toString());
					repeatOrder();
				}
				
			}
			log.debug("Shipping Order Ref Val: "+ order.getReferenceValue());
			
		} catch (Exception e) {
			log.error("Error occured while fetching reference lookup results",e);
		}
		return SUCCESS;
	}
	
	
	public List<String> getOrderList(){
		return orderList;
	}
	
	public String getShippingLabel(){
		
	
		//shippingOrder = (ShippingOrder)getSession().get("shippingOrder");
		log.debug("----getShippingLabel-----");
		String id = request.getParameter("id");
		ShippingOrder shippingOrder = null;
	
		try{
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			String logMessage = "";
			if(id!=null)
			{
				shippingOrder = shippingService.getShippingOrder(new Long(id));
				carrierServiceManager.getShippingLabel(shippingOrder, baos);
				logUpdateAction(shippingOrder.getId(),logMessage);
			}
			else
			{
				int sl_copies = Integer.parseInt(request.getParameter("slcopies"));
				int ci_copies = Integer.parseInt(request.getParameter("cicopies"));
				if(sl_copies > 0)
					logMessage = ShiplinxConstants.TEXT_SHIPPING_LABEL + sl_copies + ShiplinxConstants.SPACE + ShiplinxConstants.TEXT_COPIES;
				if(ci_copies > 0)
					logMessage = logMessage + ShiplinxConstants.COMMA + ShiplinxConstants.SPACE + ShiplinxConstants.TEXT_CUSTOMS_INVOICE + ci_copies + ShiplinxConstants.SPACE + ShiplinxConstants.TEXT_COPIES;
				String strOrdersSelected = request.getParameter("arrayOrders");
				
				List<String> listOrders = new ArrayList<String>(Arrays.asList(strOrdersSelected.split(",")));
				
				//shippingOrder = shippingService.getShippingOrder(new Long(listOrders.get(i)));
				carrierServiceManager.getShippingLabels(listOrders, baos, sl_copies, ci_copies);
				
				for(int i=0;i<listOrders.size();i++)
				{
					if(!StringUtil.isEmpty(listOrders.get(i)))
						logUpdateAction(new Long(listOrders.get(i)),logMessage);
				}
			}
			
			response.setHeader("Cache-Control", "no-cache"); 
//			response.setHeader("Content-Disposition","attachment; filename=label.pdf"); 
			response.setHeader("Expires", "0"); 
			response.setHeader("Cache-Control", 
			"must-revalidate, post-check=0, pre-check=0"); 
			response.setHeader("Pragma", "public"); 
			response.setContentLength(baos.size());
			ByteArrayInputStream bis=new ByteArrayInputStream(baos.toByteArray());
			inputStream = bis;
			
		} catch (Exception e) {			
			log.error("-------------",e);
		}
		
		return SUCCESS;
		
	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}

	public CustomerManager getCustomerService() {
		return customerService;
	}

	public void setCustomerService(CustomerManager customerService) {
		this.customerService = customerService;
	}
	
	public String listCancelShipment(){
		long customerId=getLoginUser().getCustomerId();
		orderList=shippingService.getTodaysOrderResult(customerId);
		getSession().put("TODAYSORDERLISTSIZE",orderList.size());
		return SUCCESS;
	}
	
	public String editShipment(){
		String orderId = request.getParameter("order_id");
		if(orderId!=null && orderId.length()>0){
			getSession().put("EDIT_ORDER_ID",orderId);				
		}
		
		return SUCCESS;
	}
	
	public String cancelShipment(){
		String orderId = request.getParameter("orderId");
		if(orderId!=null && orderId.length()>0){
			long order_id = Long.parseLong(orderId);
			
			if(order_id==0){
				//shipment has not been created
				addActionMessage(getText("shippingOrder.cancel.successful"));
				return INPUT;
			}
			
			boolean isAdmin = UserUtil.getMmrUser().getUserRole().equals("busadmin");
			if(carrierServiceManager.cancelOrder(order_id, isAdmin))
			{
				if(this.getShippingOrder().getWarehouseProducts()!= null && this.getShippingOrder().getWarehouseProducts().size()>0)
				{	
					//fetch the products assigned to the order and inject it to the ShippingOrder object
					//shippingOrder.setWarehouseProducts(productManagerService.getProductsByOrderAndCustomer(order_id, shippingOrder.getCustomerId()));
					//update the product counts when the shipment is getting canceled
					productManagerService.updateProductsCounts(this.getShippingOrder(), ShiplinxConstants.STATUS_CANCELLED);
				}
				getShippingOrder().setStatusId((long)ShiplinxConstants.STATUS_CANCELLED);
				addActionMessage(getText("shippingOrder.cancel.successful"));
			}
			else
				addActionMessage(getText("shippingOrder.cancel.error"));
	
			setSelectedOrder(shippingService.getShippingOrder(order_id));
		}
		//to populate the updated logged events on cancellation of the shipment order
		LoggedEvent loggedEvent = new LoggedEvent();
		loggedEvent.setEntityId(Long.valueOf(this.getSelectedOrder().getId()));
		loggedEvent.setEntityType(Long.valueOf(ShiplinxConstants.ENTITY_TYPE_ORDER_VALUE));
		if(!UserUtil.getMmrUser().getUserRole().equals("busadmin"))
		{
			loggedEvent.setPrivateMessage(false);
			loggedEvent.setDeletedMessage(false);
			loggedList = loggedEventService.getLoggedEventInfo(loggedEvent,false);
		}
		else
		{
			loggedList = loggedEventService.getLoggedEventInfo(loggedEvent,true);
		}
		return SUCCESS;
	}


	public String getConsigneeName() {
		return consigneeName;
	}

	public String getId() {
		return id;
	}

	public String getCity() {
		return city;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public String getState() {
		return state;
	}

	public String newQuote() throws Exception {
		
		log.debug("-----newQuote------");
		List<Province> toProvinces;
		List<Province> fromProvinces;
		String fromCountry = "";
		String toCountry = "";
		Address addressbookFrom;
		Address addressbookTo;
		Long customerId = getLoginUser().getCustomerId();
		
		getSession().remove("shippingOrder");
		getSession().remove("PackageType");
		ShippingOrder shippingOrder = getShippingOrder();
		getSession().put("SHIP_MODE", "QUOTE");

		shippingOrder.setCustomerId(getLoginUser().getCustomerId());
		shippingOrder.setPackageTypeId(shippingService.findPackageType(ShiplinxConstants.PACKAGE_TYPE_ENVELOPE_STRING));
		
		addressbookFrom = addressService.findDefaultFromAddressForCustomer(customerId);
		addressbookTo = addressService.findDefaultToAddressForCustomer(customerId);
		
		if(addressbookFrom==null)
			addressbookFrom = new Address();
		if(addressbookTo==null)
			addressbookTo = new Address();

		if(addressbookFrom != null)
			fromCountry = addressbookFrom.getCountryCode();
		if(addressbookTo != null)
			toCountry = addressbookTo.getCountryCode();
		
		//if not set set as default
		if(fromCountry==null ||"".equals(fromCountry)){
			fromCountry = UserUtil.getMmrUser().getBusiness().getAddress().getCountryCode();
			addressbookFrom.setCountryCode(fromCountry);
		}
		if(toCountry==null ||"".equals(toCountry)){
			toCountry = UserUtil.getMmrUser().getBusiness().getAddress().getCountryCode();
			addressbookTo.setCountryCode(toCountry);
		}
		
		
		shippingOrder.setToAddress(addressbookTo);
		shippingOrder.setFromAddress(addressbookFrom);
		
		
		toProvinces=addressService.getProvinces(toCountry);
		fromProvinces=addressService.getProvinces(fromCountry);
		
		getSession().put("Fromprovinces", fromProvinces);
		getSession().put("Toprovinces", toProvinces);
		getSession().put("ToCountry", toCountry);
		getSession().put("FromCountry", fromCountry);
	
		countries=MessageUtil.getCountriesList();

		getSession().put("CountryList", countries);
		getSession().put("shippingOrder",shippingOrder);
		
		packageTypeList = shippingService.getPackages();
		List<PackageType> listPackages = shippingService.getPackages();
		
		List<HashMap<String, String>> packageOption = new ArrayList<HashMap<String, String>>();
		
		// To generate packages radio buttons on jsp
		//short packeCounter=0;
		for(PackageType pType :listPackages){
			HashMap<String, String> packages = new HashMap<String, String>();
			packages.put(pType.getType(),pType.getName());
			packageOption.add(packages);
		}
		
		getSession().put("packageOptions", packageOption);
		
		PackageType pType = new PackageType();
		pType.setType(ShiplinxConstants.PACKAGE_TYPE_PACKAGE_STRING);
		shippingOrder.setPackageTypeId(pType);
		
		getSession().put("PackageType",shippingOrder.getPackageTypeId().getType());
		getSession().put("packageOptions", packageOption);
		getSession().put("listPackages", listPackages);
		//default quantity is 1
		ArrayList<Package> packages = new ArrayList<Package>();
		if(shippingOrder.getQuantity()==null)
			shippingOrder.setQuantity(1);
		
		for(int i=0;i<(int)shippingOrder.getQuantity(); i++){
			Package pack = new Package();
			pack.setLength( new BigDecimal(1.0));
			pack.setWeight( new Float(1.0));
			pack.setHeight( new BigDecimal(1.0));
			pack.setWidth( new BigDecimal(1.0));
			pack.setCodAmount( new Float(0.0));
			pack.setInsuranceAmount( new Float(0.0));
			packages.add(pack);
		}

		shippingOrder.setPackages(packages);
		Package packageArray[] = new Package[1];
		shippingOrder.setPackageArray(packageArray);
		
		if(getLoginUser().getUserRole().equalsIgnoreCase(ShiplinxConstants.ROLE_CUSTOMER_SHIPPER))
			request.setAttribute("USERROLE", "shipper");

		return SUCCESS;
	}
	
	
//	public String packageInformationQuote() throws Exception {
//
//		try{
////			ShiplinxConstants.setCustomer(customerService.getCustomerInfoByCustomerId(getLoginUser().getCustomerId()));
//			
//			String packageType=ShiplinxConstants.PACKAGE_TYPE_ENVELOPE_STRING; 
//
//			//Set the default dimension to the package
//			ArrayList<Package> packages = new ArrayList<Package>();
//			Package packageArray[];
//			ShippingOrder shippingOrder = getShippingOrder();
//			//default quantity is 1
//			if(shippingOrder.getQuantity()==null)
//				shippingOrder.setQuantity(1);
//			
//			//set the default package
//			if(shippingOrder != null && shippingOrder.getPackageTypeId() != null){
//
//				
//				if(shippingOrder.getPackageTypeId().getType() == null || "".equalsIgnoreCase(shippingOrder.getPackageTypeId().getType())){
//					
//					shippingOrder.setPackageTypeId(shippingService.findPackageType(packageType));
//				}
//				else{	
//					
//					packageType = shippingOrder.getPackageTypeId().getType();	
//					
//					shippingOrder.setPackageTypeId(shippingService.findPackageType(packageType));
//				}
//			}
//			
//			
//			packageArray = new Package[(int)shippingOrder.getQuantity()];
//			
//			for(int i=0;i<(int)shippingOrder.getQuantity(); i++){
//				List<Package> packages1=((ShippingOrder)getSession().get("shippingOrder")).getPackages();
//				if(i<packages1.size()){
//					log.debug("--getWeight--"+packages1.get(i).getWeight());
//					packages.add(packages1.get(i));
//				}else{
//					Package pack = new Package();
//					pack.setLength( new BigDecimal(1.0));
//					pack.setWeight( new Float(1.0));
//					pack.setHeight( new BigDecimal(1.0));
//					pack.setWidth( new BigDecimal(1.0));
//					pack.setCodAmount( new BigDecimal(0.0));
//					pack.setInsuranceAmount( new BigDecimal(0.0));
//					packages.add(pack);
//					packageArray[i] = pack;
//				}
//			}
//			
//			shippingOrder.getFromAddress().setAddress1("");
//			shippingOrder.getToAddress().setAddress1("");
//			
////			RIZM COMENTING OUT TO REFACTOR ADDRESS OBJECT
////			if(((ShippingOrder)getSession().get("shippingOrder")).getShipFromId() != null)
////				shippingorder.getFromAddress().setId(((ShippingOrder)getSession().get("shippingOrder")).getShipFromId().getId());
////			else{
////				shippingOrder.setShipFromId(shippingorder.getFromAddress());
////			}
////				
////			if(((ShippingOrder)getSession().get("shippingOrder")).getShipToId() != null)
////				shippingOrder.getShipToId().setId(((ShippingOrder)getSession().get("shippingOrder")).getShipToId().getId());
////			else{
////				shippingOrder.setShipToId(shippingOrder.getShipToId());
////			}
//	
//			shippingOrder.setPackageArray(packageArray);
//			shippingOrder.setPackages(packages);
//			shippingOrder.setPackageTypeId(shippingService.findPackageType(packageType));
//			
//			getSession().put("shippingOrder",shippingOrder);
//			
//			if(packageType.equalsIgnoreCase(ShiplinxConstants.PACKAGE_TYPE_PACKAGE_STRING)
//					|| packageType.equalsIgnoreCase(ShiplinxConstants.PACKAGE_TYPE_PALLET_STRING)){
//				return "DIMENSSION_PAGE";
//			}
//		
//		}catch (Exception e) {
//			log.debug("----Exception-----"+e);
//			e.printStackTrace();
//	
//		}
//			return SUCCESS; //default is type_env
//	}

	public List<Address> getAddresses() {
		return addressService.findAddressesByCustomer(new Long(4134));
	}

	public void setAddresses(List<Address> addresses) {
		this.addresses = addresses;
	}
	
//	public List<Carrier> getCarriers() {
//		return carriers;
//	}
//
//	public void setCarriers(List<Carrier> carriers) {
//		this.carriers = carriers;
//	}

	public List<ShippingOrder> getShipments() {
//		return shipments;
		return (List<ShippingOrder>) getSession().get("shipments");
	}

	public void setShipments(List<ShippingOrder> shipments) {
//		this.shipments = shipments;
		getSession().put("shipments", shipments);
	}
	
	public List<Pickup> getPickups() {
//		return shipments;
		return (List<Pickup>) getSession().get("listpickups");
	}

	public void setPickups(List<Pickup> lstpickups) {
//		this.shipments = shipments;
		getSession().put("listpickups", lstpickups);
	}

	public String searchShipment(){
		try {
			getSession().remove("shipments");
			getSession().remove("shippingOrder");
			ShippingOrder so = this.getShippingOrder();
			
			//default the date range to a week ago from the current date
			int month = Calendar.getInstance().get(Calendar.MONTH);
			int year = Calendar.getInstance().get(Calendar.YEAR);
			int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);	
			
			Calendar calendar = new GregorianCalendar(year, month, day);			
			so.setToDate(FormattingUtil.getFormattedDate(calendar.getTime(),FormattingUtil.DATE_FORMAT_WEB));
			day = day - 1;
			calendar.set(Calendar.DAY_OF_MONTH, day);
			so.setFromDate(FormattingUtil.getFormattedDate(calendar.getTime(),FormattingUtil.DATE_FORMAT_WEB));	
			
			if (this.carrierServiceManager != null) {
				if (getSession().get("CARRIERS") == null) {
					initCarrierList();
					so.setCarrierId(-1L);
				}
				if (getSession().get("SERVICES") == null) {
					List<Service>services = getCarrierServices(so.getCarrierId());
					getSession().put("SERVICES", services);
				}
			}
			if (this.getOrderStatusList() == null) {
				this.setOrderStatusList(this.shippingService.getShippingOrdersAllStatus());
			}
			
			if(this.getBillingStatusList() == null){
				this.setBillingStatusList(this.shippingService.getShippingBillingAllStatus());
			}
			//Calls the list Shipments that gets the shipments on the load event
//			if(!UserUtil.getMmrUser().getUserRole().equals(ShiplinxConstants.ROLE_ADMIN))
//				listShipment();
			
	    } catch (Exception e) {
	    	e.printStackTrace();
	    	addActionError(getText("content.error.unexpected"));
		}
		
		return SUCCESS;
	}	

	public String listShipment(){
		try {
			
//			if (shipments != null)
//				shipments.clear();
			ShippingOrder so = this.getShippingOrder();
			
			if (so != null) {
				// Ajax calls goes to to listService.jsp and serviceId gets updated in markup.serviceId
				// therefore it needs to be updated back in shippingOrder
				String s = this.request.getParameter("markup.serviceId");
				if ( !StringUtil.isEmpty(s) )
					so.setServiceId(Long.parseLong(s));
				
				if((so.getCustomerId()==null || so.getCustomerId()==0) 
						&& getLoginUser().getCustomerId()>0)
					so.setCustomerId(getLoginUser().getCustomerId());
				
				//so.setBillingStatus(null);
			}
			if(UserUtil.getMmrUser().getUserRole().equals(ShiplinxConstants.ROLE_SALES))
			{
				so.setSalesAgentUsername(UserUtil.getMmrUser().getUsername());
			}
			else if(UserUtil.getMmrUser().getUserRole().equals(ShiplinxConstants.ROLE_CUSTOMER_SHIPPER))
			{
				so.setCreatedBy(UserUtil.getMmrUser().getUsername());
			} 
			
			//if the user belongs to a branch, then search only those shipments that belong to customers of that branch
			if(!StringUtil.isEmpty(UserUtil.getMmrUser().getBranch()))
				so.setBranch(UserUtil.getMmrUser().getBranch());
			
			if (this.shippingService != null) {
				this.setShipments( this.shippingService.getShipments(so) );
//				getSession().put("shipments", shipments);
				//set tracking url
				for(ShippingOrder sho: this.getShipments())
				{
					if(!StringUtil.isEmpty(sho.getTrackingURL()))
						continue;
					String strTrackingUrl = this.shippingService.getTrackingURL(sho.getId());
					sho.setTrackingURL(strTrackingUrl);
				}
			}
			User user = userService.findUserByUsername(UserUtil.getMmrUser().getUsername());
			//setting the print config values for the logged user
			request.setAttribute("no_of_lbls", user.getPrintNoOfLabels());
			request.setAttribute("no_of_ci", user.getPrintNoOfCI());
			request.setAttribute("autoprint", user.isAutoPrint());
			//Setting the Attribute for Carts
			request.setAttribute("fromCart", "false");
			
	    } catch (Exception e) {
	    	e.printStackTrace();
	    	addActionError(getText("content.error.unexpected"));
		}
		
		return SUCCESS;
	}	
	
	public String searchPickups()
	{
		log.debug("Inside searchPickups method of ShipmentAction");
		
		getSession().remove("pickup");
		getSession().remove("listpickups");
		Pickup pickup = this.getPickup();
		//Provide ALL option to Carriers list.
		initCarrierListAll();
		
		//setting default date to fromDate of pickup
		int month = Calendar.getInstance().get(Calendar.MONTH);
		int year = Calendar.getInstance().get(Calendar.YEAR);
		int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);	
		
		Calendar calendar = new GregorianCalendar(year, month, day);
		pickup.setFromDate(FormattingUtil.getFormattedDate(calendar.getTime(),FormattingUtil.DATE_FORMAT_WEB));
		
		if (getSession().get("SERVICES") == null) {
			List<Service>services = getCarrierServices(-1L);
			getSession().put("SERVICES", services);
		}
		//set pickup
		this.setPickup(pickup);
		return SUCCESS;
	}
	
	public String createPickup()
	{

		long pickupresId = 0;
		try 
		{
			Pickup pickup = this.getPickup();
			
			if(pickup.getPickupDate_web()!=null && pickup.getPickupDate_web().length()>0){
				Date date = FormattingUtil.getDate(pickup.getPickupDate_web(), FormattingUtil.DATE_FORMAT_WEB);
				pickup.setPickupDate(new Timestamp(date.getTime()));
			}
			
			pickupresId = carrierServiceManager.createPickup(pickup);
			if(pickupresId > 0){
				StringBuilder stb = new StringBuilder(getText("pickup.success"));
				if(!StringUtil.isEmpty(pickup.getConfirmationNum()))
					stb.append(" Conf #: " + pickup.getConfirmationNum());
				addActionMessage(stb.toString());
			}

		} 
		catch (Exception e) 
		{
			log.error("Error occured in creating a pickup",e);
			addActionError(getText("error.pickup.fail"));
			for(CarrierErrorMessage carrierErrorMessage  :carrierServiceManager.getErrorMessages()){
				addActionError(carrierErrorMessage.getMessage());
			}
			return INPUT;
		}
		//to redirect to search page, first calling searchPickups() to reset the pickup.
		searchPickups();
		//show the list with the new pickup created.
		return listPickups();
	}
	
	public String cancelPickup()
	{
		log.debug("Inside cancelPickup method of ShipmentAction");
		long pickupId =0L;
		boolean boolresult = false;
		try 
		{
			if(request.getParameter("pickupid")!=null)
			{
				pickupId = Long.parseLong(request.getParameter("pickupid"));
				
			
			Pickup pickup = pickupService.getPickupById(pickupId);
			boolresult = carrierServiceManager.cancelPickup(pickup);
			}
			
			if(boolresult)
				addActionMessage(getText("cancel.pickup.success"));
			
		} 
		catch (Exception e) {
			log.error("Error occured in cancelling a pickup",e);
			addActionError(getText("error.cancel.pickup.fail"));
		}
		return listPickups();
	}
	
	public String listPickups()
	{
		log.debug("Inside listPickups method of ShipmentAction");
		
		getSession().remove("listpickups");
		Pickup pickup = this.getPickup();
		
		log.debug("From date: "+pickup.getFromDate());
		log.debug("To Date: "+pickup.getToDate());
		log.debug("Carrier Id: "+pickup.getCarrierId());
		log.debug("Service Id: "+pickup.getServiceId());
		log.debug("Carrier conf num:"+pickup.getConfirmationNum()+"::");
		log.debug("Status: "+pickup.getStatus());
		
		try 
		{
			if(pickup.getConfirmationNum()!=null && "".equals(pickup.getConfirmationNum()))
				pickup.setConfirmationNum(null);
			if("".equals(pickup.getToDate()))
				pickup.setToDate(null);
			else if(pickup.getToDate()!=null && !pickup.getToDate().contains(ShiplinxConstants.HH_MM_SS_END))
				pickup.setToDate(pickup.getToDate()+ShiplinxConstants.HH_MM_SS_END);
						
			this.setPickups(pickupService.getPickups(pickup));
			
		} catch (Exception e) {
			log.error("Error occured in listPickups()",e);
		}
		
		return SUCCESS;
	}
	
	public String goToCreatePickup()
	{
		log.debug("Inside goToCreatePickup.........");
		
		getSession().remove("pickup");
		getSession().remove("listpickups");
		Pickup pickup = this.getPickup();
		//Setting default customer Address if any
		Address fromAddress;
		
		fromAddress = addressService.findDefaultFromAddressForCustomer(pickup.getCustomerId());
		if(fromAddress!=null)
			pickup.setAddress(fromAddress);
		//setting the destination country
		pickup.setDestinationCountryCode(UserUtil.getMmrUser().getBusiness().getAddress().getCountryCode());
		//setting current date to pickup date as default.
		int month = Calendar.getInstance().get(Calendar.MONTH);
		int year = Calendar.getInstance().get(Calendar.YEAR);
		int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);	
		
		Calendar calendar = new GregorianCalendar(year, month, day);
		pickup.setPickupDate_web(FormattingUtil.getFormattedDate(calendar.getTime(),FormattingUtil.DATE_FORMAT_WEB));
		//Date date = FormattingUtil.getDate(FormattingUtil.getFormattedDate(calendar.getTime(),FormattingUtil.DATE_FORMAT_WEB));
		//pickup.setPickupDate(new Timestamp(date.getTime()));
		//populate the list of carriers - exclude ALL
		initCarrierList();
		//populate provinces
		provinces=addressService.getProvinces(UserUtil.getMmrUser().getBusiness().getAddress().getCountryCode());
		getSession().put("provinces", provinces);
		
		return SUCCESS;
	}
	
	private void initCarrierList() {
		// TODO Auto-generated method stub
		List<Carrier> cList = this.carrierServiceManager.getCarriersForBusiness(UserUtil.getMmrUser().getBusinessId());
		Carrier c = new Carrier();
		c.setId(-1L);
		c.setName("");
		cList.add(0, c);
		getSession().put("CARRIERS", cList);
	}
	
	private void initCarrierListAll() {
		// TODO Auto-generated method stub
		List<Carrier> cList = this.carrierServiceManager.getCarriersForBusiness(UserUtil.getMmrUser().getBusinessId());
		Carrier c = new Carrier();
		c.setId(-1L);
		c.setName("");
		cList.add(0, c);
		Carrier cAll = new Carrier();
		cAll.setId(-2L);
		cAll.setName("ALL");
		cList.add(1, cAll);
		getSession().put("CARRIERS", cList);
	}
	
	private void initCarrierListANY() {
		// TODO Auto-generated method stub
		List<Carrier> cList = this.carrierServiceManager.getCarriersForBusiness(UserUtil.getMmrUser().getBusinessId());
//		Carrier c = new Carrier();
//		c.setId(-1L);
//		c.setName("ANY");
//		cList.add(0, c);
		getSession().put("CARRIERS", cList);
	}
	
	private List<Service> getCarrierServices(Long carrierCode) {
		// TODO Auto-generated method stub
		ShippingOrder so = this.getShippingOrder();
		List<Service> sList = this.carrierServiceManager.getServicesForCarrier(carrierCode);
		Service s = new Service();
		s.setId(-1L);
		s.setName("");
		s.setCarrierId(carrierCode);
		sList.add(0, s);
		getSession().put("SERVICES", sList);

		return sList;
	}
	public String saveShipment(){
		try {
			ShippingOrder so = this.getShippingOrder();
			if (so != null) {
				// Ajax calls goes to to listService.jsp and serviceId gets updated in markup.serviceId
				// therefore it needs to be updated back in shippingOrder
				String s = this.request.getParameter("markup.serviceId");
				if ( !StringUtil.isEmpty(s) )
					so.setServiceId(Long.parseLong(s));
				
				if(getLoginUser().getCustomerId()>0)
					so.setCustomerId(getLoginUser().getCustomerId());
			}
//			if (this.shippingService != null) {

//			}
	    } catch (Exception e) {
	    	e.printStackTrace();
	    	addActionError(getText("content.error.unexpected"));
		}
		
		return SUCCESS;
	}

	public String autolinkedShipment(){
		try {
//			if (shipments != null)
//				shipments.clear();
			getSession().remove("shippingOrder");
			ShippingOrder so = this.getShippingOrder();
			if (so != null) {
				so.setEdiInvoiceNumber(request.getParameter("ediInvoiceNumber"));
				so.setBillingStatus(ShiplinxConstants.BILLING_STATUS_AWAITING_CONFIRMATION);
				so.setCustomerId(getLoginUser().getCustomerId());
			}
			if (this.shippingService != null) {
				this.setShipments( this.shippingService.getShipments(so) );
			}
	    } catch (Exception e) {
	    	e.printStackTrace();
	    	addActionError(getText("content.error.unexpected"));
		}
		
		return SUCCESS;
	}	
	
	public String unlinkedShipment(){
		try {
//			if (shipments != null)
//				shipments.clear();
			getSession().remove("shippingOrder");
			ShippingOrder so = this.getShippingOrder();
			if (so != null) {
				so.setEdiInvoiceNumber(request.getParameter("ediInvoiceNumber"));
				so.setBillingStatus(ShiplinxConstants.BILLING_STATUS_ORPHAN);
				so.setCustomerId(getLoginUser().getCustomerId());
			}
			if (this.shippingService != null) {
				this.setShipments( this.shippingService.getShipments(so) );
			}
	    } catch (Exception e) {
	    	e.printStackTrace();
	    	addActionError(getText("content.error.unexpected"));
		}
		
		return SUCCESS;
	}
	
	private void assignShipments(){
		List<Long> shipmentIds = new ArrayList<Long>();
		for ( int i = 0; i < select.size(); i++ ) {
		    // If this checkbox was selected:
		    if ( select.get(i) != null && select.get(i) ) {
		    	// Get the matching test scenario:
		    	ShippingOrder so = this.getSelectedShipments().get(i);
		    	// ...and launch it:
		    	shipmentIds.add(so.getId());
		    } 		
		}			
		this.shippingService.assignCustomerToShipments(shipmentIds, this.getShippingOrder().getCustomerId());
	}

	public String assignCustomerToUnlinkedShipments(){
		try {
			assignShipments();
	    } catch (Exception e) {
	    	e.printStackTrace();
	    	addActionError(getText("content.error.unexpected"));
		}

		return unlinkedShipment();
	}
	
	public String reassignCustomerToAutolinkedShipments(){
		try {
			assignShipments();
	    } catch (Exception e) {
	    	e.printStackTrace();
	    	addActionError(getText("content.error.unexpected"));
		}
		
		return autolinkedShipment();
	}	

	public String acceptShipments(){
		try {
			List<Long> shipmentIds = new ArrayList<Long>();
			for ( int i = 0; i < select.size(); i++ ) {
			    // If this checkbox was selected:
			    if ( select.get(i) != null && select.get(i) ) {
			    	// Get the matching test scenario:
			    	ShippingOrder so = this.getSelectedShipments().get(i);
			    	// ...and launch it:
			    	shipmentIds.add(so.getId());
			    } 		
			}			
			this.shippingService.setShipmentsReadyForInvoice(shipmentIds);

	    } catch (Exception e) {
	    	e.printStackTrace();
	    	addActionError(getText("content.error.unexpected"));
		}
		
		return autolinkedShipment();
	}
	
	public List<ShippingOrder> getSelectedShipments() {
		return selectedShipments;
	}

	public void setSelectedShipments(List<ShippingOrder> selectedShipments) {
		this.selectedShipments = selectedShipments;
	}

	public List<Boolean> getSelect() {
		return select;
	}

	public void setSelect(List<Boolean> select) {
		this.select = select;
	}
	
	public String viewShipment(){
		try {
			log.debug("Inside----------------------viewShipment()---------------------");
			String shipmentId=request.getParameter("viewShipmentId");
			ShippingOrder selectedOrder;
			
			if (shipmentId != null) {
				long id = Long.parseLong(shipmentId);
				selectedOrder = this.shippingService.getShippingOrder(id);
				if (selectedOrder != null) {
					this.setSelectedOrder(selectedOrder);
				}
				if(this.getSelectedOrder().getCustomer().getPrimaryWarehouse()!=null)
					request.setAttribute("def_wh", this.getSelectedOrder().getCustomer().getPrimaryWarehouse().getWarehouseName());
				//populate the warehouse products for the order if its a warehouse order
				this.getSelectedOrder().setWarehouseProducts(populateOrderWarehouseProducts(Long.valueOf(shipmentId), this.getSelectedOrder().getCustomerId()));
				
				if(request.getParameter("notrackurl")!=null)
					request.setAttribute("notrackurl","true");
			}
			//else is executed for a new shipment.
			else{
				this.setSelectedOrder(this.getShippingOrder());
				if(this.getSelectedOrder().getCustomer().getPrimaryWarehouse()!=null)
					request.setAttribute("def_wh", this.getSelectedOrder().getCustomer().getPrimaryWarehouse().getWarehouseName());
				//set an attribute to avoid loading of the label popup.
				request.setAttribute("nopopup", "true");
			}
			//set provinces:
			provinces = this.addressService.getProvinces(this.getSelectedOrder().getCustomer().getAddress().getCountryCode());
			getSession().put("provinces", provinces);
			if (this.getSelectedOrder() != null && this.getSelectedOrder().getStatusId() != null &&
					this.getSelectedOrder().getStatusId().longValue() == ShiplinxConstants.STATUS_EXCEPTION &&
					!StringUtil.isEmpty(this.getSelectedOrder().getMessage())) {
				addActionError(getText("shipment.failed.following.error"));
				StringTokenizer st = new StringTokenizer(this.getSelectedOrder().getMessage(), "|");
				while (st.hasMoreTokens()) {
					addActionError(st.nextToken().trim());
				}
				
			}
			//setting all the shipping status in the list
			//if (this.getOrderStatusList() == null) {
			this.setOrderStatusList(this.shippingService.getShippingOrdersAllStatus());
			//}
			if(this.getSelectedOrder().getStatusId()!=null)
				this.setOrderStatusOptionsList(this.shippingService.getShippingOrdersStatusOptions(this.getSelectedOrder().getStatusId()));
			
			/*//Get the Products added to the order and inject
			this.getSelectedOrder().setWarehouseProducts(getShippingOrder().getWarehouseProducts());
			List<Products> prodlist1 = this.getSelectedOrder().getWarehouseProducts();
			this.getSelectedOrder().setWarehouseProducts(this.getSelectedOrder().getWarehouseProducts());
			List<Products> prodlist2 = this.getSelectedOrder().getWarehouseProducts();*/
			
			if(this.getSelectedOrder().getId()!=null)
			{
				LoggedEvent loggedEvent = new LoggedEvent();
				loggedEvent.setEntityId(Long.valueOf(this.getSelectedOrder().getId()));
				loggedEvent.setEntityType(Long.valueOf(ShiplinxConstants.ENTITY_TYPE_ORDER_VALUE));
				if(!UserUtil.getMmrUser().getUserRole().equals("busadmin"))
				{
					loggedEvent.setPrivateMessage(false);
					loggedEvent.setDeletedMessage(false);
					loggedList = loggedEventService.getLoggedEventInfo(loggedEvent,false);
				}
				else
				{
					loggedList = loggedEventService.getLoggedEventInfo(loggedEvent,true);
				}
			}
			
			User user = userService.findUserByUsername(UserUtil.getMmrUser().getUsername());
			//setting the print config values for the logged user
			request.setAttribute("no_of_lbls", user.getPrintNoOfLabels());
			request.setAttribute("no_of_ci", user.getPrintNoOfCI());
			request.setAttribute("autoprint", user.isAutoPrint());
			
	    } catch (Exception e) {
	    	e.printStackTrace();
	    	addActionError(getText("content.error.unexpected"));
		}

	    	return SUCCESS;
	}

	public String processPayment(){
		ShippingOrder selectedOrder = this.getSelectedOrder();
		CCTransaction transaction = null;	
		
		if(selectedOrder.getId()!=null && selectedOrder.getId() > 0){ //this is a post-payment
			transaction = shippingService.processPayment(selectedOrder, null);
			selectedOrder.getCcTransactions().add(transaction);

		}
		else{ //payment is part of the shipping process
			try{		
				carrierServiceManager.shipOrder(selectedOrder,  selectedOrder.getRateList().get(0));
				transaction = selectedOrder.getCcTransactions().get(0);
			}
			catch (CardProcessingException cpe) { //this is if card could not be authorized
				addActionError(getText("creditCard.payment.notProcessed") + " " + cpe.getMessage());
				//go back to pay/details page
				return SUCCESS;
			}
			catch (Exception e) {
				log.debug("Shipping Error!", e);
				addActionError(getText("shippingOrder.save.error"));
				//Get the error messages returned from the carriers
				for(CarrierErrorMessage carrierErrorMessage  :carrierServiceManager.getErrorMessages()){
					addActionError(carrierErrorMessage.getMessage());
				}
				return ERROR;
			}
			
			if(carrierServiceManager.getErrorMessages().size() == 0)
				addActionMessage(getText("shippingOrder.save.successfully"));
			
		}
		
		if(transaction == null || transaction.getStatus() == CCTransaction.VOID_STATUS)
			addActionError(getText("creditCard.payment.notProcessed"));
		else if(transaction.getStatus() == CCTransaction.PROCESSED_STATUS)
			addActionMessage(getText("creditCard.payment.processed"));
		else 
			addActionError(getText("creditCard.payment.notProcessed") + ". Message: " + transaction.getProcMessage());

		return SUCCESS;
	}
	
	public String addActualCharge(){
		try {
			Charge newCharge = this.getNewActualCharge();
			if (newCharge != null) {
				CarrierChargeCode carrierCharge = getCarrierCharge(newCharge.getChargeId());
				if (carrierCharge != null) {
					newCharge.setChargeCode(carrierCharge.getChargeCode());
					newCharge.setChargeCodeLevel2(carrierCharge.getChargeCodeLevel2());
					newCharge.setName(carrierCharge.getChargeName());
					newCharge.setCurrency(this.getSelectedOrder().getCurrency());
					newCharge.setOrderId(this.getSelectedOrder().getId());
//					newCharge.setStatus(ShiplinxConstants.CHARGE_READY_TO_INVOICE);
					newCharge.setDiscountAmount(new Double(0.0));
					newCharge.setTariffRate(new Double(0.0));
					newCharge.setType(ShiplinxConstants.CHARGE_TYPE_ACTUAL);
					newCharge.setId( this.getShippingService().saveCharge(newCharge) );
					this.getSelectedOrder().getCharges().add(newCharge);
					getSession().remove("newActualCharge");
					this.getNewActualCharge();
					addActionMessage(getText("charge.added.successfully"));
				}
			}
	    } catch (Exception e) {
	    	e.printStackTrace();
	    	addActionError(getText("content.error.unexpected"));
		}
		
		return SUCCESS;
	}	
	
	public String copyToActualCharges()
	{
		log.debug("Inside copyToActualCharges method...");
		ShippingOrder so = this.getSelectedOrder();
		String chargeStatusText = this.request.getParameter("quotedChargeStatusText");
		List<Charge> quotedToActualChargesList = new ArrayList<Charge>();
		List<Charge> quotedChargesList = new ArrayList<Charge>();
		try 
		{
			quotedToActualChargesList = so.getQuotedCharges();
			
 			for(Charge c: so.getQuotedCharges())
			{
				Charge newCharge = this.getNewActualCharge();
				if (newCharge != null) {
						newCharge.setChargeCode(c.getChargeCode());
						newCharge.setChargeCodeLevel2(c.getChargeCodeLevel2());
						newCharge.setName(c.getName());
						newCharge.setCurrency(this.getSelectedOrder().getCurrency());
						newCharge.setOrderId(this.getSelectedOrder().getId());
						//set the status to "Ready to Invoice" if the selected status is "Quick Invoice"
						if(chargeStatusText.equalsIgnoreCase(ShiplinxConstants.CHARGE_QUICK_INVOICE))
							newCharge.setStatusText(ShiplinxConstants.CHARGE_STATUS_TEXT[2]);
						else
							newCharge.setStatusText(chargeStatusText);
						newCharge.setCost(c.getCost());
						newCharge.setCharge(c.getCharge());
						newCharge.setDiscountAmount(c.getDiscountAmount());
						newCharge.setTariffRate(c.getTariffRate());
						newCharge.setType(ShiplinxConstants.CHARGE_TYPE_ACTUAL);
						newCharge.setId(this.getShippingService().saveCharge(newCharge));
						//this.getShippingService().saveCharge(newCharge);
						this.getSelectedOrder().getCharges().add(newCharge);
						getSession().remove("newActualCharge");
						this.getNewActualCharge();
				}
			}
 			
 			if(chargeStatusText.equalsIgnoreCase(ShiplinxConstants.CHARGE_QUICK_INVOICE))
 			{
 				//create Invoice logic
 				List<ShippingOrder> shippingOrderList = new ArrayList<ShippingOrder>();
 				shippingOrderList.add(this.getSelectedOrder());
 				Invoice i = invoiceManagerService.createInvoice(shippingOrderList, new Invoice(), so.getCustomerId(), so.getCurrency());
 				if(i!=null)
 				{
 					//set the invoice num
 					for(Charge c: so.getCharges())
 					{
 						c.setInvoiceNum(i.getInvoiceNum());
 					}
 					addActionMessage(MessageUtil.getMessage("invoice.order.generated")+" "+String.valueOf(this.getSelectedOrder().getId()));
 				}
 				else
 					addActionError(MessageUtil.getMessage("invoice.order.generation.fail"));
 			}
 			else
 				addActionMessage(MessageUtil.getMessage("charges.copied.actual"));
 			
		} 
		catch (Exception e) 
		{
			log.error("Error Occured while copying to Actual Charges",e);
	    	addActionError(getText("content.error.unexpected"));
		}
		return SUCCESS;
	}
	
	public String clearExceptionStatus() {
		log.debug("Inside clearExceptionStatus method...");
		try {
			String statusId = request.getParameter("status_id");
			ShippingOrder so = this.getSelectedOrder();
			if (!StringUtil.isEmpty(statusId) && !statusId.equals("-1") && 
					so != null && so.getId() != null && so.getId().longValue() > 0) {
				so.setStatusIdFromString(statusId);
				shippingService.updateOrderStatus(so.getId(), so.getStatusId());
				getSession().remove("selectedOrder");
				ShippingOrder order = shippingService.getShippingOrder(so.getId());
				this.setSelectedOrder(order);
				addActionMessage(getText("shippingOrder.updated.successfully"));
			}
		} catch (Exception e) {
			log.error("Exception occured while clearing exception of the order: "+e);
			e.printStackTrace();
			addActionError(getText("shippingOrder.update.error"));
		}
		return SUCCESS;
	}	
	
	public String addQuotedCharge(){
		try {
			Charge newCharge = this.getNewQuotedCharge();
			if (newCharge != null) {
				CarrierChargeCode carrierCharge = getCarrierCharge(newCharge.getChargeId());
				if (carrierCharge != null) {
					newCharge.setChargeCode(carrierCharge.getChargeCode());
					newCharge.setChargeCodeLevel2(carrierCharge.getChargeCodeLevel2());
					newCharge.setName(carrierCharge.getChargeName());
					newCharge.setCurrency(this.getSelectedOrder().getCurrency());
					newCharge.setOrderId(this.getSelectedOrder().getId());
//					newCharge.setStatus(ShiplinxConstants.CHARGE_READY_TO_INVOICE);
					newCharge.setDiscountAmount(new Double(0.0));
					newCharge.setTariffRate(new Double(0.0));
					newCharge.setType(ShiplinxConstants.CHARGE_TYPE_QUOTED);
					newCharge.setId( this.getShippingService().saveCharge(newCharge) );
					this.getSelectedOrder().getCharges().add(newCharge);
					getSession().remove("newQuotedCharge");
					this.getNewQuotedCharge();
					addActionMessage(getText("charge.added.successfully"));
				}
			}
	    } catch (Exception e) {
	    	e.printStackTrace();
	    	addActionError(getText("content.error.unexpected"));
		}
		
		return SUCCESS;
	}	
	
	public String updateActualCharge(){
		try {
			ShippingOrder order = this.getSelectedOrder();
			System.out.println(order);
			if (order != null) {
				String [] ids = this.request.getParameterValues("actualChargeIds");
				String [] userCharges = this.request.getParameterValues("actualCharge");
				String [] userCosts = this.request.getParameterValues("actualChargeCost");
				String [] userNames = this.request.getParameterValues("actualChargeName");
				String [] userStatusTexts = this.request.getParameterValues("actualChargeStatusText");				
				for (int i=0; i<ids.length; i++) {
					Long id = Long.parseLong(ids[i]);
					Charge soCharge = getCharge(this.getSelectedOrder(), id);
					if (soCharge != null) {
						Double ch = StringUtil.getDouble(userCharges[i]); // Double.parseDouble(userCharges[i]);
						Double cost = StringUtil.getDouble(userCosts[i]); // Double.parseDouble(userCosts[i]);
						String name = userNames[i];
						String statusText = userStatusTexts[i];
						if (isChargeDirty(soCharge, ch, cost, name, statusText)) {
							this.getShippingService().updateCharge(soCharge);
							addActionMessage(getText("charges.save.successfully"));
						}
					}
				}
			}
	    } catch (Exception e) {
	    	e.printStackTrace();
	    	addActionError(getText("content.error.unexpected"));
		}
		
		return SUCCESS;
	}	
	
	public String updateQuotedCharge(){
		try {
			ShippingOrder order = this.getSelectedOrder();
			System.out.println(order);
			if (order != null) {
				String [] ids = this.request.getParameterValues("quotedChargeIds");
				String [] userCharges = this.request.getParameterValues("quotedCharge");
				String [] userCosts = this.request.getParameterValues("quotedChargeCost");
				String [] userNames = this.request.getParameterValues("quotedChargeName");
				String [] userStatusTexts = this.request.getParameterValues("quotedChargeStatusText");
				for (int i=0; i<ids.length; i++) {
					Long id = Long.parseLong(ids[i]);
					Charge soCharge = getCharge(this.getSelectedOrder(), id);
					if (soCharge != null) {
						Double ch = StringUtil.getDouble(userCharges[i]); // Double.parseDouble(userCharges[i]);
						Double cost = StringUtil.getDouble(userCosts[i]); // Double.parseDouble(userCosts[i]);
						String name = userNames[i];
						String statusText = userStatusTexts[i];
						if (isChargeDirty(soCharge, ch, cost, name, statusText)) {
							this.getShippingService().updateCharge(soCharge);
							addActionMessage(getText("charges.save.successfully"));
						}
					}
				}
			}
	    } catch (Exception e) {
	    	e.printStackTrace();
	    	addActionError(getText("content.error.unexpected"));
		}
		
		return SUCCESS;
	}	
	
	
	private Charge getCharge(ShippingOrder so, Long id) {
		// TODO Auto-generated method stub
		for (Charge c:so.getCharges()) {
			if (c.getId().longValue() == id.longValue())
				return c;
		}
		return null;
	}

	private boolean isChargeDirty(Charge soCharge, Double charge, Double cost, String name, String statusText) {
		// TODO Auto-generated method stub
		boolean isDirty = false;
		if (!soCharge.getName().equalsIgnoreCase(name)) {
			isDirty = true;
			soCharge.setName(name);
		}
		if ( !soCharge.getStatusText().equals(statusText) ) {
			isDirty = true;
			soCharge.setStatusText(statusText);
		}
		if ((soCharge.getCharge() == null && charge != null) ||
			(charge != null && soCharge.getCharge().doubleValue() != charge.doubleValue())	) {
			isDirty = true;
			soCharge.setCharge(charge);			
		}
//		if (soCharge.getCharge() != null && charge != null && 
//				soCharge.getCharge().doubleValue() != charge.doubleValue()) {
//			isDirty = true;
//			soCharge.setCharge(charge);
//		}
		if ((soCharge.getCost() == null && cost != null) || 
			(cost != null && soCharge.getCost().doubleValue() != cost.doubleValue()) ) {
			isDirty = true;
			soCharge.setCost(cost);
		}		
//		if (soCharge.getCost() != null && cost != null &&
//				soCharge.getCost().doubleValue() != cost.doubleValue()) {
//			isDirty = true;
//			soCharge.setCost(cost);
//		}
		return isDirty;
	}

	private CarrierChargeCode getCarrierCharge(Long chargeId) {
		// TODO Auto-generated method stub
		if (carrierChargesList != null) {
			for (CarrierChargeCode c:carrierChargesList) {
				if (c.getId().longValue() == chargeId.longValue())
					return c;
			}
		}
		return null;
	}

	public Map<String, Long> getCarrierChargesSearchResult() {
		return carrierChargesSearchResult;
	}

	public void setCarrierChargesSearchResult(
			Map<String, Long> carrierChargesSearchResult) {
		this.carrierChargesSearchResult = carrierChargesSearchResult;
	}

	public String listCarrierCharges(){
		String searchParameter = request.getParameter("searchString");
		log.debug("Search string is : " + searchParameter);
		
		if ((carrierChargesList == null && this.getSelectedOrder().getCarrierId() != null) ||
				(carrierChargesList != null && carrierChargesList.size()>0 && this.getSelectedOrder().getCarrierId() != null &&
						carrierChargesList.get(0).getCarrierId() != this.getSelectedOrder().getCarrierId().longValue())) {
			carrierChargesList = this.getShippingService().getChargeListByCarrierAndCodes(
																	this.getSelectedOrder().getCarrierId(), null, null);
		}
		if (carrierChargesList != null) {
			for(CarrierChargeCode c: carrierChargesList){
				carrierChargesSearchResult.put(c.getChargeName(), c.getId());
			}
		}
		
		return SUCCESS;
	}	
	
	public String deleteCharge() throws Exception {
		try {
			String s=request.getParameter("id");
			Long id = 0L;
			if (s != null)
				id = Long.parseLong(s);
			if (id.longValue() > 0) {
				this.getShippingService().deleteCharge(id);
				for (int i=0; i<this.getSelectedOrder().getCharges().size(); i++)
					if (this.getSelectedOrder().getCharges().get(i).getId().longValue() == id.longValue()) {
						this.getSelectedOrder().getCharges().remove(i);
						break;
					}
				addActionMessage(getText("charge.deleted.successfully"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			addActionError(getText("content.error.unexpected"));
		}
		return SUCCESS;
	}
	
	public String sendEmailNotificationOfRates(){
		try {
			log.debug("In sendEmailNotificationOfRates method.");		
			int selected = Integer.parseInt(String.valueOf(request.getParameter("selected")));
			boolean emailsent = false;
			
			ShippingOrder shippingorder=(ShippingOrder)getSession().get("shippingOrder");
			emailsent = shippingService.sendCustomerEmailNotification(UserUtil.getMmrUser(),shippingorder, selected);
			
			if(emailsent)
	    		this.addActionMessage(getText("email.customer.rates.notification.sent")+" "+UserUtil.getMmrUser().getEmail());
			else
				this.addActionError(getText("email.customer.rates.notification.not.sent")+" "+UserUtil.getMmrUser().getEmail());
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}		
		
		return SUCCESS; 	
	}
	
	public String getNextAction() {
		return nextAction;
	}
	public void setNextAction(String nextAction) {
		this.nextAction = nextAction;
	}	
	
	public String addProductDetails() throws Exception {
		log.debug("---------------------In addProductDetails method.------------------------------");		
		
		ShippingOrder shippingOrder = getShippingOrder();
		
		String prod_desc = request.getParameter("desc");
		String prod_hcode = request.getParameter("hcode");
		String prod_origin = request.getParameter("origin_country");
		String prod_quantity = request.getParameter("quantity");
		String prod_unit_price = request.getParameter("unit_price");
		//String prod_weight = request.getParameter("weight");
		
		CustomsInvoice customsInvoice = shippingOrder.getCustomsInvoice();
		CustomsInvoiceProduct customsInvoiceProduct = new CustomsInvoiceProduct();		
		
		try{
			customsInvoiceProduct.setCustomsInvoiceId(customsInvoice.getId());
			customsInvoiceProduct.setProductDesc(prod_desc);
			customsInvoiceProduct.setProductHC(prod_hcode);
			customsInvoiceProduct.setProductOrigin(prod_origin);
			customsInvoiceProduct.setProductQuantity(Integer.parseInt(prod_quantity));
			customsInvoiceProduct.setProductUnitPrice(Double.parseDouble(prod_unit_price));
//			customsInvoiceProduct.setProductWeight(Long.valueOf(prod_weight));
			List<CustomsInvoiceProduct> listproducts = new ArrayList<CustomsInvoiceProduct>();
			
			if(customsInvoice.getProducts()!=null)
				customsInvoice.getProducts().add(customsInvoiceProduct);
			else
			{
				listproducts.add(customsInvoiceProduct);
				customsInvoice.setProducts(listproducts);				
			}
			
			shippingOrder.setCustomsInvoice(customsInvoice);
			getSession().put("shippingOrder",shippingOrder);
			
			
			//reset all the values
			/*customsInvoiceProduct.setCustomsInvoiceId(0);
			customsInvoiceProduct.setProductDesc("");
			customsInvoiceProduct.setProductHC("");
			customsInvoiceProduct.setProductOrigin("");
			customsInvoiceProduct.setProductQuantity(Integer.parseInt("0"));
			customsInvoiceProduct.setProductUnitPrice(Double.parseDouble("0"));
			customsInvoiceProduct.setProductWeight(Long.valueOf("0"));*/
		
		}catch (Exception e) {
			log.debug("exception-------------"+e);
			e.printStackTrace();
		}
		
		
		
		return SUCCESS;
	}
	
	public String addProductToShipment()
	{
		log.debug("---------------------In addProductToShipment method.------------------------------");
		
		warehouseProductsList = new ArrayList<Products>();
		ShippingOrder shippingOrder = this.getShippingOrder();
		long lProductId = Long.valueOf(request.getParameter("prodid"));
		
		long lQuantity = Long.valueOf(request.getParameter("quantity"));
		
		int iret = 0;
		
		try 
		{
			
			Products products = new Products();
			products.setProductId(lProductId);
			shippingOrder.setCustomerId(getLoginUser().getCustomerId());
			products.setCustomerId(shippingOrder.getCustomerId());
			warehouseProductsList = productManagerService.searchProducts(products, true);
			if(warehouseProductsList.size()>0)
			{
				shippingOrder.setWarehouseProductEach(warehouseProductsList.get(0));
				shippingOrder.getWarehouseProductEach().setOrderedQuantity(lQuantity);
			}
			if(!isProductAdded(shippingOrder,lProductId))
			{
				if(shippingOrder.getWarehouseProducts()!=null)
				{
					shippingOrder.getWarehouseProducts().add(shippingOrder.getWarehouseProductEach());
				}
				else
				{
					this.warehouseAllProdList.add(shippingOrder.getWarehouseProductEach());
					shippingOrder.setWarehouseProducts(warehouseAllProdList);
				}
				
			}
			else
				return "success2";
			
			getSession().put("shippingOrder",shippingOrder);
			
		} catch (Exception e) {
			log.error("Error occured in adding a product to Order:"+e);
		}
		return SUCCESS;	
	}
	
	private boolean isProductAdded(ShippingOrder so, long productId)
	{
		boolean boolret= false;
		if(so.getWarehouseProducts()!=null)
		{
			for(Products p: so.getWarehouseProducts())
			{
				if(p.getProductId()==productId)
					boolret=true;
			}
		}
		return boolret;
	}
	
	public String setBillToAdd()
	{
		String selected = request.getParameter("selected");
		ShippingOrder shippingOrder = getShippingOrder();
		
		if(selected.equals("1"))
		{
			shippingOrder.getCustomsInvoice().getBillToAddress().copyAddress(shippingOrder.getFromAddress());
			request.setAttribute("address", "From");
		}
		else if(selected.equals("2"))
		{
			shippingOrder.getCustomsInvoice().getBillToAddress().copyAddress(shippingOrder.getToAddress());
			request.setAttribute("address", "To");
		}
		getSession().put("billToProvinces", addressService.getProvinces(shippingOrder.getCustomsInvoice().getBillToAddress().getCountryCode()));
		
		return SUCCESS;
	}
	
	public File getUpload() {
		return upload;
	}
	public void setUpload(File upload) {
		this.upload = upload;
	}
	
	public String getUploadFileName() {
		return uploadFileName;
	}
	public void setUploadFileName(String uploadFileName) {
		this.uploadFileName = uploadFileName;
	}
	public BatchShipmentInfo getBatchShipmentInfo() {
		return (BatchShipmentInfo) getSession().get("batchShipmentInfo");
	}

	@SuppressWarnings("unchecked")
	public void setBatchShipmentInfo(BatchShipmentInfo batchInfo) {
		getSession().put("batchShipmentInfo", batchInfo);
	}	
	
	public String batchShipment(){
		try {
//			getSession().remove("batchShipmentInfo");
			
			BatchShipmentInfo batchShipmentInfo = this.getBatchShipmentInfo();
			if (batchShipmentInfo == null) {
				batchShipmentInfo = new BatchShipmentInfo();
				batchShipmentInfo.setBusinessId(UserUtil.getMmrUser().getBusinessId());
				this.setBatchShipmentInfo(batchShipmentInfo);
			}
			
			if (this.carrierServiceManager != null) {
				if (getSession().get("CARRIERS") == null) {
					initCarrierList();
					batchShipmentInfo.setCarrierId(-1L);
				}
				if (getSession().get("SERVICES") == null) {
					List<Service>services = getCarrierServices(batchShipmentInfo.getCarrierId());
					getSession().put("SERVICES", services);
				}
			}
			//Calls the list Shipments that gets the shipments on the load event
			//listShipment();
			
			
			//Setting the Attribute for Carts
			request.setAttribute("fromCart", "false");
			
	    } catch (Exception e) {
	    	e.printStackTrace();
	    	addActionError(getText("content.error.unexpected"));
	    	addActionError(e.getMessage());
		}
		
		return SUCCESS;
	}	
	
	public String batchShipmentFileUpload() throws Exception {
		try {
			BatchShipmentInfo batchShipmentInfo = this.getBatchShipmentInfo();
			if (batchShipmentInfo != null && this.getUpload() != null && this.getUploadFileName() != null) {
				batchShipmentInfo.setFileName(this.getUploadFileName());
				InputStream is = new DataInputStream(new FileInputStream(this.getUpload()));
				if (is != null) {
					List<ShippingOrder> batchShipments = this.shippingService.uploadBatchShipmentFile(
																batchShipmentInfo, is);
					if (batchShipments == null) {
						addActionError(getText("content.error.unexpected"));
					} else {
						setShipments(batchShipments);
//						getSession().put("BATCH_SHIPMENTS", batchShipments);
					}
				}
			} else {
				addActionError(getText("select.valid.batch.shipment.file"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			addActionError(getText("content.error.unexpected"));
			addActionError(getText(e.getMessage()));
		}
		return batchShipment();
	}	
	
	public String createBatchShipments() throws Exception {
		try {
			if (select != null && select.size() > 0) {
				List<ShippingOrder> saveShipments = new ArrayList<ShippingOrder>();
				List<ShippingOrder> shipments = this.getShipments();
				for ( int i = 0; i < select.size(); i++ ) {
				    // If this checkbox was selected:
				    if ( select.get(i) != null && select.get(i) ) {
				    	ShippingOrder so = shipments.get(i);
				    	saveShipments.add(so);
				    } 		
				}	
				if (saveShipments.size() > 0) {
					shipments = this.shippingService.createBatchShipments(saveShipments);
					if (shipments != null) {
						getSession().remove("shipments");
						this.setShipments(shipments);
					} else {
						addActionError(getText("select.valid.shipments.for.creation"));
					}
				} else {
					addActionError(getText("select.valid.shipments.for.creation"));
				}
			} else {
				addActionError(getText("select.valid.shipments.for.creation"));
			}
	    } catch (Exception e) {
	    	e.printStackTrace();
	    	addActionError(getText("content.error.unexpected"));
	    	addActionError(getText(e.getMessage()));
		}		
		return batchShipment();
	}	
	
	public String processBatchShipments() throws Exception {
		try {
			BatchShipmentInfo batchInfo = this.getBatchShipmentInfo();
			String s = this.request.getParameter("markup.serviceId");
			if ( !StringUtil.isEmpty(s) )
				batchInfo.setServiceId(Long.parseLong(s));
			if (batchInfo != null && batchInfo.getCarrierId() != null && batchInfo.getServiceId() != null &&
					batchInfo.getCarrierId().longValue() > 0 && batchInfo.getServiceId().longValue() > 0) {
				List<ShippingOrder> processShipments = new ArrayList<ShippingOrder>();
				List<ShippingOrder> shipments = this.getShipments();
				if (select != null && select.size() > 0) {
					for ( int i = 0; i < select.size(); i++ ) {
					    // If this checkbox was selected:
					    if ( select.get(i) != null && select.get(i) ) {
					    	ShippingOrder so = shipments.get(i);
					    	processShipments.add(so);
					    } 		
					}			
					if (processShipments.size() > 0) {
						shipments = this.shippingService.processBatchShipments(processShipments, batchInfo);
						if (shipments != null) {
							addActionMessage(getText("shipment.processing.started.check.track.search"));
							this.setShipments(shipments);
						}
					} else {
						addActionError(getText("no.valid.shipment.found.for.batch.shipment.processing"));
					}
				} else {
					addActionError(getText("select.valid.shipments.for.processing"));
				}
			} else {
				addActionError(getText("select.carrier.service.for.batch.shipment.processing"));
			}
	    } catch (Exception e) {
	    	e.printStackTrace();
	    	addActionError(getText("content.error.unexpected"));
	    	addActionError(getText(e.getMessage()));
		}		
		return batchShipment();
	}	
	
	public String updateShipment() throws Exception {
		try {
			ShippingOrder so = this.getShippingOrder();
			if (so != null && this.shippingService != null) {
				// Check if Customer needs to be reassigned
				if (so.getWebCustomerId() != null && so.getWebCustomerId().longValue() > 0 && 
						so.getCustomerId().longValue() != so.getWebCustomerId().longValue()) {
					// Check if charges are not invoiced yet, customer can only be changed if charges are not invoiced yet
					for (Charge c:so.getCharges()) {
						if (c.getStatus().intValue() == ShiplinxConstants.CHARGE_INVOICED) {
							addActionError(getText("failed.to.reassign.shipment.to.another.customer"));
							so.setWebCustomerId(so.getCustomerId());
							return INPUT;
						}
					}
				} else if (so.getWebCustomerId() != null && so.getWebCustomerId().longValue() == 0L) {
					// User want to make it to orphan shipment
					so.setCustomer(null);
					so.setCustomerId(0L);
					so.setBillingStatus(ShiplinxConstants.BILLING_STATUS_ORPHAN);
				}
				
		        if(so.getScheduledShipDate_web()!=null && so.getScheduledShipDate_web().length()>0){
					Date date = FormattingUtil.getDate(so.getScheduledShipDate_web(), FormattingUtil.DATE_FORMAT_WEB);
					so.setScheduledShipDate(new Timestamp(date.getTime()));
				}				

		        this.shippingService.update(so);
				getSession().remove("shippingOrder");
				ShippingOrder order = shippingService.getShippingOrder(so.getId());
				this.setShippingOrder(order);
				addActionMessage(getText("shippingOrder.updated.successfully"));
			}
	    } catch (Exception e) {
	    	e.printStackTrace();
	    	addActionError(getText("content.error.unexpected"));
	    	return INPUT;
		}
		
		return SUCCESS;
	}	
	
	public void populateCustomersList(){
		String searchParameter = "";
		
		Customer c = new Customer();
		c.setName(searchParameter);
		c.setBusinessId(UserUtil.getMmrUser().getBusinessId());		
		List<Customer> customers = this.customerService.search(c);
		
		// First record is empty 
		customerSearchResult.put("", 0L);
		
		for(Customer cust: customers){
			customerSearchResult.put(cust.getName(), cust.getId());
		}
		
		getSession().put("customersList", customerSearchResult);
	}	
	

	public String deleteProductDetails() throws Exception {
		log.debug("---------------------In deleteProductDetails method.------------------------------");		
		
		ShippingOrder shippingOrder = getShippingOrder();
		
		int indexSelected = Integer.parseInt(String.valueOf(request.getParameter("index")));
		
		CustomsInvoice customsInvoice = shippingOrder.getCustomsInvoice();
		
		List<CustomsInvoiceProduct> listproducts = customsInvoice.getProducts();
		
		listproducts.remove(indexSelected);
		
		customsInvoice.setProducts(listproducts);
		shippingOrder.setCustomsInvoice(customsInvoice);
		getSession().put("shippingOrder",shippingOrder);
		
		return SUCCESS;
	}
	
	public String deleteProductFromShipment()
	{
		log.debug("---------------------In deleteProductFromShipment method.------------------------------");
		
		int iIndex = Integer.parseInt(request.getParameter("index"));
		
		ShippingOrder shippingOrder = getShippingOrder();
		
		List<Products> productslist = shippingOrder.getWarehouseProducts();
		
		productslist.remove(iIndex);
		
		shippingOrder.setWarehouseProducts(productslist);
		this.setShippingOrder(shippingOrder);
		
		getSession().put("shippingOrder",shippingOrder);
		
		return SUCCESS;
	}
	
	public String showProductSummary()
	{
		log.debug("Inside----------showProductSummary() method----------");
		
		long prodid = Long.valueOf(request.getParameter("prodid"));
		ShippingOrder shippingOrder = getShippingOrder();
		Products products = shippingOrder.getWarehouseProduct();
		
		products.setProductId(prodid);
		products.setCustomerId(UserUtil.getMmrUser().getCustomerId());
		List<Products> prodslist = productManagerService.searchProducts(products, true);
		shippingOrder.setWarehouseProduct(prodslist.get(0));
		getSession().put("shippingOrder",shippingOrder);
		return SUCCESS;
	}
	
	public String submitToWarehouse()
	{
		log.debug("Inside----------submitToWarehouse() method----------");
		ShippingOrder shippingOrder = this.getShippingOrder();
		int ierror = 0;
		try 
		{
			
			shippingOrder.setCustomerId(getLoginUser().getCustomerId());
			//setting customer info to the shipment
			shippingOrder.setCustomer(this.customerService.getCustomerInfoByCustomerId(shippingOrder.getCustomerId()));
			shippingOrder.setStatusId(Long.valueOf(ShiplinxConstants.STATUS_SENT_TOWAREHOUSE));
			
			this.setSelectedOrder(shippingOrder);
			this.setShippingOrder(shippingOrder);
			
			//SubmitWarehouse Logic Implementation + email + Logging Event details
			shippingService.submitToWarehouse(shippingOrder);
			/*//update product count
			if(productManagerService.updateProductsCounts(shippingOrder, ShiplinxConstants.STATUS_SENT_TOWAREHOUSE))
			{
			//update a field "fulfilled" in shipping_order table to 1, default is 0. This means the shipment order has been fulfilled.
			shippingOrder.setFulfilled(1); // active
			//update the Shipping order only if everything is ok
			shippingService.updateShippingOrder(shippingOrder);
			}*/
		}
		catch (Exception e) 
		{
			log.debug("Shipping Error!", e);
			addActionError(getText("shippingOrder.save.error"));
			ierror++;
		}
		if(ierror==0)
		addActionMessage(getText("shippingOrder.save.successfully"));
		
		getSession().put("shippingOrder",shippingOrder);
		
		if(ierror>0)
			return "failure";
		
		return viewShipment();
	}
	
	public String updateOrderStatus()
	{
		log.debug("Inside----------updateOrderStatus() method----------");
		String strOrderId = request.getParameter("order_id");
		ShippingOrder shippingOrder = this.shippingService.getShippingOrder(Long.valueOf(strOrderId));
		String strComment = request.getParameter("comment");
		boolean boolPrivate = Boolean.valueOf(request.getParameter("pvt"));
		String strStatusId = request.getParameter("statusId");
		Long lstatusId = 0L;
		LoggedEvent loggedEvent = new LoggedEvent();
		try 
		{
			loggedEvent.setEntityId(Long.valueOf(strOrderId));
			loggedEvent.setEntityType(ShiplinxConstants.ENTITY_TYPE_ORDER_VALUE);
			if(strComment==null)
			{
				lstatusId = Long.valueOf(ShiplinxConstants.STATUS_RECEIVED_BYWAREHOUSE);
				shippingService.updateOrderStatus(Long.valueOf(strOrderId), lstatusId);
			}
			else
			{
				lstatusId = Long.valueOf(strStatusId);
				shippingService.updateOrderStatus(Long.valueOf(strOrderId), lstatusId, strComment, boolPrivate);
			}
			//populate warehouse products for the orders if they are warehouse orders
			if(shippingOrder.getCustomer().isWarehouseCustomer())
			{
				List<Products> listproducts = populateOrderWarehouseProducts(shippingOrder.getId(), shippingOrder.getCustomerId());
				if(listproducts.size()>0)
				{
					shippingOrder.setWarehouseProducts(listproducts);
					productManagerService.updateProductsCounts(shippingOrder, Integer.parseInt(String.valueOf(lstatusId)));
				}
			}
			//populate the updated logs.
			if(!UserUtil.getMmrUser().getUserRole().equals("busadmin"))
			{
				loggedEvent.setPrivateMessage(false);
				loggedEvent.setDeletedMessage(false);
				loggedList = loggedEventService.getLoggedEventInfo(loggedEvent,false);
			}
			else
			{
				loggedList = loggedEventService.getLoggedEventInfo(loggedEvent,true);
			}
			addActionMessage(getText("shippingOrder.updated.successfully"));
		} 
		catch (Exception e) 
		{
			log.error("Exception occured while updating the status of the order: "+e);
			e.printStackTrace();
			addActionError(getText("shippingOrder.update.error"));
		}
		return SUCCESS;
	}
	
	public List<LoggedEvent> getLoggedList() {
		return loggedList;
	}
	public void setLoggedList(List<LoggedEvent> loggedList) {
		this.loggedList = loggedList;
	}
	public LoggedEventService getLoggedEventService() {
		return loggedEventService;
	}
	public void setLoggedEventService(LoggedEventService loggedEventService) {
		this.loggedEventService = loggedEventService;
	}
	public ProductManager getProductManagerService() {
		return productManagerService;
	}
	public void setProductManagerService(ProductManager productManagerService) {
		this.productManagerService = productManagerService;
	}
	public List<Products> getWarehouseProductsList() {
		return warehouseProductsList;
	}
	public void setWarehouseProductsList(List<Products> warehouseProductsList) {
		this.warehouseProductsList = warehouseProductsList;
	}
	public String saveCurrentShipment() throws Exception {
		try {
			ShippingOrder so = this.getShippingOrder();
			if (so != null && this.shippingService != null) {
//				// Check if Customer needs to be reassigned
//				if (so.getWebCustomerId() != null && so.getWebCustomerId().longValue() > 0 && 
//						so.getCustomerId().longValue() != so.getWebCustomerId().longValue()) {
//					// Check if charges are not invoiced yet, customer can only be changed if charges are not invoiced yet
//					for (Charge c:so.getCharges()) {
//						if (c.getStatus().intValue() == ShiplinxConstants.CHARGE_INVOICED) {
//							addActionError(getText("failed.to.reassign.shipment.to.another.customer"));
//							so.setWebCustomerId(so.getCustomerId());
//							return INPUT;
//						}
//					}
//				} else if (so.getWebCustomerId() != null && so.getWebCustomerId().longValue() == 0L) {
//					// User want to make it to orphan shipment
//					so.setCustomer(null);
//					so.setCustomerId(0L);
//					so.setBillingStatus(ShiplinxConstants.BILLING_STATUS_ORPHAN);
//				}
		        if(so.getScheduledShipDate_web()!=null && so.getScheduledShipDate_web().length()>0){
					Date date = FormattingUtil.getDate(so.getScheduledShipDate_web(), FormattingUtil.DATE_FORMAT_WEB);
					so.setScheduledShipDate(new Timestamp(date.getTime()));
				}				
				if (so.getId() != null && so.getId().longValue() > 0) {
					this.shippingService.update(so);
				} else {
					PackageType packageType  = new PackageType();		
					packageType  = shippingService.findPackageType(so.getPackageTypeId().getType());		
					so.setPackageTypeId(packageType);
					so.setStatusId(new Long(ShiplinxConstants.STATUS_READYTOPROCESS));
					//remove the charges if the bill to type is 'Third Party' or 'Collect'
					if(so.getBillToType().equalsIgnoreCase(ShiplinxConstants.BILL_TO_THIRD_PARTY) || so.getBillToType().equalsIgnoreCase(ShiplinxConstants.BILL_TO_COLLECT))
						so.getCharges().removeAll(so.getCharges());
					this.shippingService.save(so);
				}
				getSession().remove("shippingOrder");
				ShippingOrder order = shippingService.getShippingOrder(so.getId());
				this.setShippingOrder(order);
				addActionMessage(getText("shippingOrder.updated.successfully"));
			}
	    } catch (Exception e) {
	    	e.printStackTrace();
	    	addActionError(getText("content.error.unexpected"));
	    	return INPUT;
		}
		
		return SUCCESS;
	}	
	
	private List<Products> populateOrderWarehouseProducts(long orderId, long customerId)
	{
		List<Products> productslist = new ArrayList<Products>();
		List<OrderProduct> orderProducts = this.shippingService.getOrderProducts(orderId);
		if(orderProducts.size()>0)
		{
			for(OrderProduct op: orderProducts)
			{
				Products products = new Products();
				products=productManagerService.getProductByProductIdAndCustomerId(op.getProductId(), customerId);
				products.setOrderedQuantity(op.getOrderedQuantity());
				productslist.add(products);
			}
		}
		return productslist;
	}
	public PickupManager getPickupService() {
		return pickupService;
	}
	public void setPickupService(PickupManager pickupService) {
		this.pickupService = pickupService;
	}
	public List<Pickup> getListPickups() {
		return listPickups;
	}
	public void setListPickups(List<Pickup> listPickups) {
		this.listPickups = listPickups;
	}
	
	private void logUpdateAction(long OrderId, String logMsg)
	{
		//Logged Event Initialization
		LoggedEvent loggedEvent = new LoggedEvent();
		// Set the loggedEvent Details
		loggedEvent.setEntityType(ShiplinxConstants.ENTITY_TYPE_ORDER_VALUE); //Entity - Warehouse Order
		loggedEvent.setEntityId(OrderId);//Order ID
		loggedEvent.setEventDateTime(new Date()); //Current Date
		loggedEvent.setEventUsername(UserUtil.getMmrUser().getUsername()); //Current User
		String systemLog = MessageUtil.getMessage("label.system.log.docs.accessed");
		loggedEvent.setSystemLog(systemLog); //System generated Message Log
		if(UserUtil.getMmrUser().getUserRole().equals("busadmin"))
			loggedEvent.setPrivateMessage(true);
		else
			loggedEvent.setPrivateMessage(false);
		loggedEvent.setMessage(logMsg);
		//Log the Event into the DB
		loggedEventService.addLoggedEventInfo(loggedEvent); //Added Event Log into DB
		
	}

	/*
	 * This method retrieves the list of [cities, zipcode] beginning with queried letters for city and selected country by user.
	 * And write the list into response.
	 * @param Nothing.
	 * @return String
	 * @Exception IOException, Exception
	 */

	public String getCitySuggest(){
		String cityString = request.getParameter("q");
		if(cityString != null && cityString.trim().length() > 0)
		{
			ShippingOrder order = getShippingOrder();
			
			String addressType = request.getParameter("addressType");
			if(addressType.equalsIgnoreCase("from")){
				if(order.getFromAddress().getCountryCode().equalsIgnoreCase(ShiplinxConstants.US) || order.getFromAddress().getCountryCode().equalsIgnoreCase(ShiplinxConstants.CANADA))
					return SUCCESS;
				citySuggestList = addressService.getCitySuggest(order.getFromAddress().getCountryCode(), cityString);
			}
			else{
				if(order.getToAddress().getCountryCode().equalsIgnoreCase(ShiplinxConstants.US) || order.getToAddress().getCountryCode().equalsIgnoreCase(ShiplinxConstants.CANADA))
					return SUCCESS;
				citySuggestList = addressService.getCitySuggest(order.getToAddress().getCountryCode(), cityString);
			}
				
			if(citySuggestList == null || citySuggestList.size() == 0)
				return SUCCESS;

			try {
				HttpServletResponse response = ServletActionContext.getResponse();
				response.setContentType("text/html");

				PrintWriter out = response.getWriter();
				for(String strFilteredCity:citySuggestList)
					out.println(strFilteredCity+"|"+strFilteredCity);
			} 
			catch (IOException e) {
				log.error("Exception occured while retrieving details for getCitySuggest  "+e);
				e.printStackTrace();
				addActionError(getText("shippingOrder.autocompleter.city.error"));
			}  
			catch (Exception e) {
				log.error("Exception occured while retrieving details for getCitySuggest  "+e);
				e.printStackTrace();
				addActionError(getText("shippingOrder.autocompleter.city.error"));
			}
		}
		return SUCCESS;
	}
	
	/*
	 * This method retrieves the list of [zipcode, cities] beginning with queried letters for zipcode and selected country by user.
	 * And write the list into response.
	 * @param Nothing.
	 * @return String
	 * @Exception IOException, Exception
	 */

	public String getZipSuggest(){
		String zipString = request.getParameter("q");
		
		if(zipString != null && zipString.trim().length() > 0)
		{
			ShippingOrder order = getShippingOrder();
			
			String addressType = request.getParameter("addressType");
			if(addressType.equalsIgnoreCase("from"))
			{
				if(order.getFromAddress().getCountryCode().equalsIgnoreCase(ShiplinxConstants.US) || order.getFromAddress().getCountryCode().equalsIgnoreCase(ShiplinxConstants.CANADA))
					return SUCCESS;
				zipSuggestList = addressService.getZipSuggest(order.getFromAddress().getCountryCode(), zipString);
			}
			else
			{
				if(order.getToAddress().getCountryCode().equalsIgnoreCase(ShiplinxConstants.US) || order.getToAddress().getCountryCode().equalsIgnoreCase(ShiplinxConstants.CANADA))
					return SUCCESS;
				
				zipSuggestList = addressService.getZipSuggest(order.getToAddress().getCountryCode(), zipString);
			}

			if(zipSuggestList == null || zipSuggestList.size() == 0)
				return SUCCESS;

			try {
					HttpServletResponse response = ServletActionContext.getResponse();
					response.setContentType("text/html");

					PrintWriter out = response.getWriter();
					for(String strFilteredZip:zipSuggestList)
						out.println(strFilteredZip+"|"+strFilteredZip);
				
			} 
			catch (IOException e) {
				log.error("Exception occured while retrieving details for getZipSuggest  "+e);
				e.printStackTrace();
				addActionError(getText("shippingOrder.autocompleter.zip.error"));
			}  
			catch (Exception e) {
				log.error("Exception occured while retrieving details for getZipSuggest  "+e);
				e.printStackTrace();
				addActionError(getText("shippingOrder.autocompleter.zip.error"));
			}  
		}

		return SUCCESS;
	}

	public List<String> getCitySuggestList() {
		return citySuggestList;
	}
	public void setCitySuggestList(List<String> citySuggestList) {
		this.citySuggestList = citySuggestList;
	}
	
	public UserService getUserService() {
		return userService;
	}
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public List<String> getZipSuggestList() {
		return zipSuggestList;
	}
	public void setZipSuggestList(List<String> zipSuggestList) {
		this.zipSuggestList = zipSuggestList;
	}
	
	public List<BillingStatus> getBillingStatusList() {
		return (List<BillingStatus>) getSession().get("billingStatusList");

	}
	public void setBillingStatusList(List<BillingStatus> billingStatusList) {
		if (billingStatusList != null) {
			billingStatusList.add(0, new BillingStatus(-1, ""));
		}
		getSession().put("billingStatusList", billingStatusList);
	}
	public InvoiceManager getInvoiceManagerService() {
		return invoiceManagerService;
	}
	public void setInvoiceManagerService(InvoiceManager invoiceManagerService) {
		this.invoiceManagerService = invoiceManagerService;
	}
}
