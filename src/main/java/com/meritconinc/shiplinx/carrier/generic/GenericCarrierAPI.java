package com.meritconinc.shiplinx.carrier.generic;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import au.com.bytecode.opencsv.CSVReader;

import com.fedex.ship.stub.NotificationSeverityType;
import com.meritconinc.mmr.constants.Constants;
import com.meritconinc.mmr.utilities.MessageUtil;
import com.meritconinc.mmr.utilities.MmrBeanLocator;
import com.meritconinc.mmr.utilities.StringUtil;
import com.meritconinc.mmr.utilities.WebUtil;
import com.meritconinc.mmr.utilities.mail.MailHelper;
import com.meritconinc.shiplinx.carrier.CarrierService;
import com.meritconinc.shiplinx.carrier.utils.FedExException;
import com.meritconinc.shiplinx.dao.BusinessDAO;
import com.meritconinc.shiplinx.dao.CustomerDAO;
import com.meritconinc.shiplinx.dao.MarkupManagerDAO;
import com.meritconinc.shiplinx.dao.ShippingDAO;
import com.meritconinc.shiplinx.exception.ShiplinxException;
import com.meritconinc.shiplinx.model.Address;
import com.meritconinc.shiplinx.model.Attachment;
import com.meritconinc.shiplinx.model.Business;
import com.meritconinc.shiplinx.model.Charge;
import com.meritconinc.shiplinx.model.Customer;
import com.meritconinc.shiplinx.model.CustomerCarrier;
import com.meritconinc.shiplinx.model.FuelSurcharge;
import com.meritconinc.shiplinx.model.LtlPoundRate;
import com.meritconinc.shiplinx.model.LtlSkidRate;
import com.meritconinc.shiplinx.model.Pickup;
import com.meritconinc.shiplinx.model.Rating;
import com.meritconinc.shiplinx.model.Service;
import com.meritconinc.shiplinx.model.ShippingOrder;
import com.meritconinc.shiplinx.model.Zone;
import com.meritconinc.shiplinx.service.FuelSurchargeService;
import com.meritconinc.shiplinx.service.impl.InvoiceManagerImpl;
import com.meritconinc.shiplinx.utils.CarrierErrorMessage;
import com.meritconinc.shiplinx.utils.ShiplinxConstants;

public class GenericCarrierAPI implements CarrierService {
	private static final Logger log = LogManager.getLogger(GenericCarrierAPI.class);

	private static final long SERVICE_TYPE_LTL_POUND = 1;
	private static final long SERVICE_TYPE_LTL_SKID = 2;

	private static final int LTL_POUND_TOT_COLUMNS = 25;
	private static final int LTL_SKID_TOT_COLUMNS = 43;
	
	private static final int COL_FROM_CITY = 0;
	private static final int COL_FROM_PROVINCE = 1;
	private static final int COL_FROM_COUNTRY = 2;
	private static final int COL_TO_CITY = 3;
	private static final int COL_TO_PROVINCE = 4;
	private static final int COL_TO_COUNTRY = 5;
	private static final int COL_EQUIPMENT = 6;
	private static final int COL_CURRENCY = 7;
	private static final int COL_FSC = 8;
	private static final int COL_WEIGHT_FLAT_RATE = 9;
	
	private static final int COL_SKID_1 = 10;
	private static final int COL_SKID_2 = 11;
	private static final int COL_SKID_3 = 12;
	private static final int COL_SKID_4 = 13;
	private static final int COL_SKID_5	= 14;
	private static final int COL_SKID_6 = 15;
	private static final int COL_SKID_7	= 16;
	private static final int COL_SKID_8 = 17;
	private static final int COL_SKID_9	= 18;
	private static final int COL_SKID_10 = 19;
	private static final int COL_SKID_11 = 20;
	private static final int COL_SKID_12 = 21;
	private static final int COL_SKID_13 = 22;
	private static final int COL_SKID_14 = 23;
	private static final int COL_SKID_15 = 24;
	private static final int COL_SKID_16 = 25;
	private static final int COL_SKID_17 = 26;
	private static final int COL_SKID_18 = 27;
	private static final int COL_SKID_19 = 28;
	private static final int COL_SKID_20 = 29;
	private static final int COL_SKID_21 = 30;
	private static final int COL_SKID_22 = 31;
	private static final int COL_SKID_23 = 32;
	private static final int COL_SKID_24 = 33;
	private static final int COL_SKID_25 = 34;
	private static final int COL_SKID_26 = 35;
	
	
	private static final int COL_TTM1 = 36;
	private static final int COL_TTM2 = 37;
	private static final int COL_EFF_DATE = 38;
	private static final int COL_EXP_DATE = 39;
	private static final int COL_P1 = 40;
	private static final int COL_P2 = 41;
	private static final int COL_P3 = 42;
	
	// Per Pound columns where index is different
	
	private static final int COL_PP_MINIMUM = 9;
	private static final int COL_PP_RATE_RANGE_START = 10;
	private static final int COL_PP_RATE_RANGE_END = 17;
//	private static final int COL_PP_RATE_RANGE_1 = 10;
//	private static final int COL_PP_RATE_RANGE_2 = 11;
//	private static final int COL_PP_RATE_RANGE_3 = 12;
//	private static final int COL_PP_RATE_RANGE_4 = 13;
//	private static final int COL_PP_RATE_RANGE_5 = 14;
//	private static final int COL_PP_RATE_RANGE_6 = 15;
//	private static final int COL_PP_RATE_RANGE_7 = 16;
//	private static final int COL_PP_RATE_RANGE_8 = 17;
	private static final int COL_PP_TTM1 = 18;
	private static final int COL_PP_TTM2 = 19;
	private static final int COL_PP_EFF_DATE = 20;
	private static final int COL_PP_EXP_DATE = 21;
	private static final int COL_PP_P1 = 22;
	private static final int COL_PP_P2 = 23;
	private static final int COL_PP_P3 = 24;
	
	private CustomerDAO customerDAO;
	private ShippingDAO shippingDAO;
	private BusinessDAO businessDAO;
	private MarkupManagerDAO markupDAO;
	private FuelSurchargeService fuelSurchargeService = null;
	
	public static final String DATE_FORMAT = "yyyy-MM-dd"; // 2013-12-31

//	private static final String FUEL_CHARGE_CODE = "FUE";

	private PlatformTransactionManager txManager;

	public boolean cancelOrder(ShippingOrder order, CustomerCarrier customerCarrier) {
		return true;
	}

	public void requestPickup(Pickup pickup) {
	}
	
	public long getCarrierId() {
		return ShiplinxConstants.CARRIER_GENERIC;
//		return 100;		// LTL Per Pound
//		return 101;		// LTL Per Skid
	}

	public List<CarrierErrorMessage> getErrorMessages() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setCustomerDAO(CustomerDAO customerDAO) {
		this.customerDAO = customerDAO;
	}

	public void setBusinessDAO(BusinessDAO businessDAO) {
		this.businessDAO = businessDAO;
	}

	public void setShippingDAO(ShippingDAO shippingDAO) {
		this.shippingDAO = shippingDAO;
	}

	public MarkupManagerDAO getMarkupDAO() {
		return markupDAO;
	}

	public void setMarkupDAO(MarkupManagerDAO markupDAO) {
		this.markupDAO = markupDAO;
	}
	
	public FuelSurchargeService getFuelSurchargeService() {
		return fuelSurchargeService;
	}

	public void setFuelSurchargeService(FuelSurchargeService fuelSurchargeService) {
		this.fuelSurchargeService = fuelSurchargeService;
	}
	
	public Object getShippingOrderStatus(ShippingOrder order) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getTrackingURL(ShippingOrder o) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Rating> rateShipment(ShippingOrder shippingOrder, List<Service> services, long carrierId, CustomerCarrier customerCarrier) {
		// *FKhan* - 31 Dec. 2012
		if(shippingOrder.getPackageTypeId().getPackageTypeId()!=ShiplinxConstants.PACKAGE_TYPE_PALLET){
			return null;
		}
		
		List<Rating> ratingList = new ArrayList<Rating>();
		for (Service s:services) { 
//			if (s.getCarrierId().longValue() == carrierId) {
				if (s.getServiceType() == SERVICE_TYPE_LTL_POUND) {
					// LTL Per pound Service 
					Rating r = ratePoundShipment(shippingOrder, s);
					if (r != null) {
						ratingList.add(r);
					}
				} else if (s.getServiceType() == SERVICE_TYPE_LTL_SKID) {
					// LTL Per Skid service
					Rating r = rateSkidShipment(shippingOrder, s);
					if (r != null) {
						ratingList.add(r);
					}
				}
//			}
		}
		if (ratingList.size() > 0)	
			return ratingList;
		
		// ---------------------------------------------------
		
		log.debug("------------------rateShipment----------------");

		try{
			for(Service service: services){
				Rating rating = new Rating();
				rating.setServiceId(service.getId());
				rating.setCarrierId(this.getCarrierId());
				rating.setServiceName(service.getName());
				rating.setCarrierName(shippingOrder.getBusiness().getName());
				ratingList.add(rating);
			}
		}catch (Exception e) {
			e.printStackTrace();
			log.debug(e);
			throw new ShiplinxException(e.getMessage());

		}

		return ratingList;
	}

	private Rating rateSkidShipment(ShippingOrder shippingOrder, Service s) {
		Zone fromZone = getZone(s.getZoneStructureId(), shippingOrder.getFromAddress());
		Zone toZone = getZone(s.getZoneStructureId(), shippingOrder.getToAddress());
		if (fromZone != null && toZone != null) {
			LtlSkidRate skidRateTobeSearched = LtlSkidRate.getObject(shippingOrder.getCustomerId(), 
													shippingOrder.getBusinessId(), s.getId(), 
													fromZone.getZoneName(), toZone.getZoneName());
			LtlSkidRate sr = this.markupDAO.getSkidRate(skidRateTobeSearched);
			if (sr == null) {
				// Customer Rate did not found, try retrieving default rate
				skidRateTobeSearched.setCustomerId(0L);
				sr = this.markupDAO.getSkidRate(skidRateTobeSearched);
			}
			if (sr != null) {
				Rating rating = new Rating();
				rating.setServiceId(s.getId());
				rating.setCarrierId(s.getCarrierId());
				rating.setServiceName(s.getName());
				rating.setCarrierName(s.getCarrier().getName());
				double rate = 0;
				Double totWeight = shippingOrder.getTotalWeight();
				if (totWeight != null && totWeight > 0 && totWeight < 500 &&
					sr.getRateFlatWeight() != null && sr.getRateFlatWeight().doubleValue() > 0) {
					rate = sr.getRateFlatWeight().doubleValue();
				} else if (shippingOrder.getQuantity() != null && 
							shippingOrder.getQuantity().intValue() > 0) {
					rate = sr.getSkidRate(shippingOrder.getQuantity().intValue());
				}
				rating.getCharges().add(getFreightCharge(rate, sr.getCurrency()));
				rating.getCharges().add(getFuelCharge(rate, sr.getCurrency(), s));

				rating.setBillWeight(totWeight);
				
				if (sr.getTtm1() != null)
					rating.setTransitDaysMin(sr.getTtm1());
				if (sr.getTtm2() != null && sr.getTtm2() > 0)
					rating.setTransitDays(sr.getTtm2());
				else
					rating.setTransitDays(sr.getTtm1());
				
				return rating;
			}
		}
		return null;
	}

	private Zone getZone(Long zoneStructureId, Address address) {
		Zone z = this.markupDAO.findZone(zoneStructureId, address);
		if (z == null) {
			// Add as new zone
			z = Zone.getObject(zoneStructureId, address);
			z = this.markupDAO.addZone(z);
		}
		return z;
	}

	private Rating ratePoundShipment(ShippingOrder shippingOrder, Service s) {
		Zone fromZone = getZone(s.getZoneStructureId(), shippingOrder.getFromAddress());
		Zone toZone = getZone(s.getZoneStructureId(), shippingOrder.getToAddress());
		if (fromZone != null && toZone != null) {
			LtlPoundRate poundRateTobeSearched = LtlPoundRate.getObject(shippingOrder.getCustomerId(), 
													shippingOrder.getBusinessId(), s.getId(), 
													fromZone.getZoneName(), toZone.getZoneName());
			LtlPoundRate pr = this.markupDAO.getPoundRate(poundRateTobeSearched, shippingOrder.getTotalWeight());
			if (pr == null) {
				// Customer Rate did not found, try retrieving default rate
				poundRateTobeSearched.setCustomerId(0L);
				pr = this.markupDAO.getPoundRate(poundRateTobeSearched, shippingOrder.getTotalWeight());
			}
			if (pr != null) {
				// 4 Jan. 2013 - Try to retrieve next per pound weight rate
				// it will be used to compare cheaper rates
				LtlPoundRate prRatedAs = null;
				double weightAsRated = 0;
				if (pr.getRangeTo() != null) {
					weightAsRated = pr.getRangeTo().intValue() + 1;
					prRatedAs = this.markupDAO.getPoundRate(poundRateTobeSearched, weightAsRated);
				}
				Rating rating = new Rating();
				rating.setServiceId(s.getId());
				rating.setCarrierId(s.getCarrierId());
				rating.setServiceName(s.getName());
				rating.setCarrierName(s.getCarrier().getName());
				
				Charge c = getFreightCharge(pr.getRate(), pr.getCurrency());
				//charge is per pound, and minimum needs to be checked/applied
				c.setCost(c.getCost() * shippingOrder.getTotalWeight());
				
				// 4 Jan. 2013 - Try to retrieve next per pound weight rate
				// it will be used to compare cheaper rates
				// if Rated rates are cheaper it will be used for Rating
				if (prRatedAs != null) {
					Charge chargeRatedAs = getFreightCharge(prRatedAs.getRate(), pr.getCurrency());
					chargeRatedAs.setCost(chargeRatedAs.getCost() * weightAsRated);
					if (chargeRatedAs.getCost().doubleValue() < c.getCost().doubleValue() ) {
						c = chargeRatedAs;
						pr = prRatedAs;
						rating.setRatedAsWeight(weightAsRated);
					}
				}
				
	
				if(c.getCost() < pr.getMinimum())
					c.setCost(pr.getMinimum());
				rating.getCharges().add(c);
				rating.getCharges().add(getFuelCharge(c.getCost(), pr.getCurrency(), s));
				rating.setBillWeight(shippingOrder.getTotalWeight());
				
				if (pr.getTtm1() != null)
					rating.setTransitDaysMin(pr.getTtm1());
				if (pr.getTtm2() != null && pr.getTtm2() > 0)
					rating.setTransitDays(pr.getTtm2());
				else
					rating.setTransitDays(pr.getTtm1());
				
				return rating;
			}
		}
		return null;
	}

	private Charge getFuelCharge(Double rate, String currency, Service s) {
		FuelSurcharge f = new FuelSurcharge();
		f.setCarrierId(s.getCarrierId());
		f.setFromCountry("ANY"); //fromCountry);
		List<FuelSurcharge> fuelsurcharges = fuelSurchargeService.getFuelSurcharge(f);		
		f = fuelsurcharges.get(0);
		
		Charge c = new Charge();
		c.setChargeCode(ShiplinxConstants.GROUP_FUEL_CHARGE);
//		c.setTariffRate(tariff_rate * f.getValue()/100);
		c.setCost(rate.doubleValue() * f.getValue() / 100);
		c.setCurrency(currency);
		
		return c;
	}

	private Charge getFreightCharge(Double rate, String currency) {
		Charge c = new Charge();
		c.setChargeCode(ShiplinxConstants.GROUP_FREIGHT_CHARGE);
		c.setName(ShiplinxConstants.FREIGHT_STRING);
//		c.setTariffRate(tariff_rate);
		c.setCost(rate);
		c.setCurrency(currency);
		return c;
	}

	public void shipOrder(ShippingOrder order, Rating rate, CustomerCarrier customerCarrier) throws FedExException {
		
		StringBuffer stb = new StringBuffer();
		String subject = MessageUtil.getMessage(
				"label.shipment.notification");
		String message = MessageUtil.getMessage("content.ltl.shipment");
		
		Customer customer = customerDAO.getCustomerInfoByCustomerId(order.getCustomerId(), order.getBusinessId());
		Business business = businessDAO.getBusiessById(customer.getBusinessId());
		
		message = new String(message.replaceAll("%CUSTOMERNAME", customer.getName()));
		message = new String(message.replaceAll("%ORDERNUM", order.getOrderNum()));
		message = new String(message.replaceAll("%ORDERSIZE", String.valueOf(order.getPackages().size())));
		message = new String(message.replaceAll("%TOTALWEIGHT", String.valueOf(order.getTotalWeight())));
		
	    List<Attachment> attachs = new ArrayList<Attachment>();

		try {
			File shippingLabels = new File(File.createTempFile("shippingLabel","").getAbsoluteFile() + ".pdf");	
			shippingLabels.deleteOnExit();
			BufferedOutputStream sbos = new BufferedOutputStream(new FileOutputStream(shippingLabels));
			Attachment attach = new Attachment();
			attach.setFile(shippingLabels);
			attach.setContentType("pdf");
			attachs.add(attach);
			
			try {							
				this.generateShippingLabel(sbos, order);
				sbos.close();
											
			}catch(Exception e) {
				e.printStackTrace();
				log.error("Could not create shipping label for notification attachment!", e);
			}
			
			
		}catch(Exception e) {
			log.error(e);
		}

	       
		try {
			MailHelper.sendEmailNow2(business.getSmtpHost(), business.getSmtpUsername(), business.getSmtpPassword(), business.getName(), business.getSmtpPort(), business.getAddress().getEmailAddress(), business.getAddress().getEmailAddress(), null, subject, message, attachs, true);
		} catch (Exception e) {
			log.error("Error sending email: " + e.getMessage());
		}

	}

	public void setCustomerCarrierData(CustomerCarrier carrierData) {
		// TODO Auto-generated method stub

	}

	public void generateShippingLabel(OutputStream outputStream, long orderId, CustomerCarrier customerCarrier) {
		ShippingOrder order = shippingDAO.getShippingOrder(orderId);
		generateShippingLabel(outputStream, order);

	}
	
	public void generateShippingLabel(OutputStream outputStream, ShippingOrder order) {
		Customer customer = customerDAO.getCustomerInfoByCustomerId(order.getCustomerId(), order.getBusinessId());

		try{
			InputStream stream = this.getClass().getClassLoader().getResourceAsStream("./jasperreports/Generic_BOL.jasper");				
			if(stream==null) {
				log.error("Stream is NULL!");
			}
			JasperReport jasperReport = (JasperReport)JRLoader.loadObject(stream);

			Map<String, Object> parameters = new HashMap<String, Object>();
			Business business = businessDAO.getBusiessById(order.getBusinessId());

	        String logoPath = "/images/" + business.getLogoHiResFileName();
			URL logo = (InvoiceManagerImpl.class.getResource(logoPath));        	
			
	        parameters.put("logo", logo);
			parameters.put("BUSINESS", business);
			parameters.put("Order", order);
			parameters.put("logo", logo);

			parameters.put("Package", order.getPackages());

			JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(order.getPackages());
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,parameters,ds);
			JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
		}
		catch(Exception e) {
			log.error("Could not generate label for order with id " + order.getId() + " and customer " + customer.getName(), e);
			throw new ShiplinxException();
		}	

	}

	public void checkForShipmentStatusUpdates() {
		
	}

	public boolean cancelPickup(Pickup pickup) {
		return false;
	}
	
	private static boolean isResponseOk(NotificationSeverityType notificationSeverityType) {
		if (notificationSeverityType == null) {
			return false;
		}
		if (notificationSeverityType.equals(NotificationSeverityType.WARNING) ||
			notificationSeverityType.equals(NotificationSeverityType.NOTE)    ||
			notificationSeverityType.equals(NotificationSeverityType.SUCCESS)) {
			return true;
		}
			return false; 
	   }

	@Override
	public void uploadRates(Service service, long customerId, long busId, 
			boolean isOverwrite) throws Exception {
		txManager = (PlatformTransactionManager) 
						MmrBeanLocator.getInstance().findBean("txManager");
		
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
	    def.setName("LtlRatesTransaction");
	    def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
	    TransactionStatus trStatus = txManager.getTransaction(def);
	    try {
	    	if (service != null) {
				if (service.getServiceType() == SERVICE_TYPE_LTL_POUND ||service.getServiceType() == SERVICE_TYPE_LTL_SKID){
					processRateTemplateFiles(service, customerId, busId, isOverwrite);
					txManager.commit(trStatus);
				}
			}
	     } catch (Exception e) {
	    	 this.txManager.rollback(trStatus);
	    	 throw e;
	     }
		
	}

	private List<String> processRateTemplateFiles(Service service, long customerId, 
			long busId, boolean isOverwrite) throws Exception{
		try {
			String inFolder = WebUtil.getProperty(Constants.SYSTEM_SCOPE, "BASE_PATH") + File.separator + Constants.RATE_TEMPLATE_FOLDER +
												File.separator + ShiplinxConstants.IN_FOLDER;
			
			ArrayList<File> filesToBeParsed = getFiles(inFolder);
			if (filesToBeParsed != null && filesToBeParsed.size() > 0) {
				ArrayList<String> fileList = new ArrayList<String>();
				String fileName = "";
				for (File f : filesToBeParsed) {
					if (f.isFile()) {
						try {						
							fileName = f.getName().trim().toUpperCase();
							if (fileName.endsWith(ShiplinxConstants.CSV_EXTENSION)) {
								String currentFileName = f.getName();
								f = renameFileToWIP(f);
								if (f != null) {
									CSVReader csvReader = new CSVReader(new FileReader(f));
									if (csvReader != null) {
										int i = 0;
										String[] rowData = null;
										String[] fields = null;
										List<String> errors = new ArrayList<String>();
										while ((rowData = csvReader.readNext()) != null) {
											i++;
											log.debug("Processing Line: " + i);
											if (i == 1) {	// First row are column names
												if (service.getServiceType() == SERVICE_TYPE_LTL_POUND) {
													fields = rowData;
													if (fields.length != LTL_POUND_TOT_COLUMNS) {
														String errMsg = "LTL Per Pound Service - Numer of columns mismatch. Number of columns must be " + LTL_POUND_TOT_COLUMNS;
														errors.add(getErrorMsg(i, errMsg, rowData));
//														throw new Exception(errMsg);
													}
												} else if (service.getServiceType() == SERVICE_TYPE_LTL_SKID) {
													fields = rowData;
													if (fields.length != LTL_SKID_TOT_COLUMNS) {
														String errMsg = "LTL Per Skid Service - Numer of columns mismatch. Number of columns must be " + LTL_SKID_TOT_COLUMNS;
														errors.add(getErrorMsg(i, errMsg, rowData));
//														throw new Exception(errMsg);
													}
												} else {
													String errMsg = "LTL unknown service - Service Id: " + service.getId();
													errors.add(getErrorMsg(i, errMsg, rowData));
//													throw new Exception(errMsg);
												}
												continue;
											}
											if (rowData.length == fields.length) {
												try {
													processRateRecord(fields, rowData, service, customerId, busId, isOverwrite);
												} catch (Exception e) {
													log.error(fileName + ": " + rowData, e);
													errors.add(getErrorMsg(i, e.getMessage(), rowData));
//													csvReader.close();
//													throw e;
												}
											} else {
												String errMsg = "Data length does not match with fields length, skipped processing Line: " + i;
												log.debug(errMsg);	
												errors.add(getErrorMsg(i, errMsg, rowData));
//												csvReader.close();
//												throw new Exception(errMsg);
											}
											if (errors.size() >= 100)
												break;
										}
										if (errors != null && errors.size()> 0) {
											// Errors, Send Email
											csvReader.close();
											String subject = currentFileName + " Upload Operation Failed.";
											String errMsgs = getMessages(errors);
											sendEmail(busId, subject, errMsgs);
											throw new Exception(errMsgs);
										} else {
											// File Uploaded successfully
											String msg = currentFileName + " Uploaded Successfully.";
											sendEmail(busId, msg, msg);
										}
										
									}
									fileList.add(currentFileName);
									csvReader.close();
								}
							}
							moveFileToOutFolder(f, ShiplinxConstants.OUT_FOLDER);
						} catch (Exception e) {
							log.error(fileName, e);
							moveFileToOutFolder(f, ShiplinxConstants.ERROR_FOLDER);
							throw e;
						}						
					}
				}
				return fileList;
			}
		} catch (Exception e) {
//			e.printStackTrace();
			log.error(e);
			throw e;
		}
		return null;
	}
	

	private void sendEmail(long busId, String subject, String body) {
		try {
			Business business = businessDAO.getBusiessById(busId);
			MailHelper.sendEmailNow2(business.getSmtpHost(), business.getSmtpUsername(), 
					business.getSmtpPassword(), business.getName(), business.getSmtpPort(), 
					business.getAddress().getEmailAddress(), 
					business.getAddress().getEmailAddress(), null, subject, 
					body, null, true);
		} catch (Exception e) {
			log.error("Error sending email: " + e.getMessage());
		}
		
	}

	private String getMessages(List<String> errors) {
		StringBuilder sb = new StringBuilder();
		for (String s:errors) {
			if (sb.length() > 0)
				sb.append("\n");
			sb.append(s);
		}
		return sb.toString();
	}

	private String getErrorMsg(int nLine, String errMsg, String [] rowData) {
		StringBuilder sb = new StringBuilder();
		sb.append("\nLINE #: ");
		sb.append(nLine);
		sb.append(" ERROR: ");
		sb.append(errMsg);
		sb.append("\n");
		sb.append("DATA: ");
		sb.append(getString(rowData));
		return sb.toString();
	}

	private Object getString(String[] rowData) {
		StringBuilder sb = new StringBuilder();
		for (String s:rowData) {
			if (sb.length() > 0)
				sb.append(",");
			sb.append(s);
		}
		return sb.toString();
	}

	private void processRateRecord(String[] fields, String[] rowData, 
					Service service, long customerId, long busId, boolean isOverwrite) throws Exception {
		if (service.getServiceType() == SERVICE_TYPE_LTL_POUND) {
			processPoundRateRecord(fields, rowData, service, customerId, busId, isOverwrite);
		} else if (service.getServiceType() == SERVICE_TYPE_LTL_SKID) {
			processSkidRateRecord(fields, rowData, service, customerId, busId, isOverwrite);
		}
		
	}

	private void processSkidRateRecord(String[] fields, String[] rowData,
			Service service, long customerId, long busId, boolean isOverwrite) throws Exception {
		LtlSkidRate skidRate = populateSkidRate(fields, rowData, service, customerId, busId);
		if (skidRate != null) {
			if (isOverwrite)
				this.markupDAO.deleteSkidRateRecord(skidRate); // if it already exists
			if (this.markupDAO.getSkidRate(skidRate) == null)
				this.markupDAO.addSkidRateRecord(skidRate);	
		}
	}

	private LtlSkidRate populateSkidRate(String[] fields, String[] rowData,
			Service service, long customerId, long busId) throws Exception {
		LtlSkidRate rr = new LtlSkidRate();
		rr.setBusinessId(busId);
		rr.setCustomerId(customerId);
		rr.setServiceId(service.getId());
		
		Address fromAddress = getFromAddress(rowData);
		rr.setFromZone(getZone(service.getZoneStructureId(), fromAddress).getZoneName());

		Address toAddress = getToAddress(rowData);
		rr.setToZone(getZone(service.getZoneStructureId(), toAddress).getZoneName());

		rr.setEquipment(rowData[COL_EQUIPMENT]);
		rr.setCurrency(rowData[COL_CURRENCY]);
		rr.setFscPercent(StringUtil.getDouble(rowData[COL_FSC]));
		
		rr.setRateFlatWeight(getRate(rowData[COL_WEIGHT_FLAT_RATE]));
		
		rr.setRateSkid1(getRate(rowData[COL_SKID_1]));
		rr.setRateSkid2(getRate(rowData[COL_SKID_2]));
		rr.setRateSkid3(getRate(rowData[COL_SKID_3]));
		rr.setRateSkid4(getRate(rowData[COL_SKID_4]));
		rr.setRateSkid5(getRate(rowData[COL_SKID_5]));
		rr.setRateSkid6(getRate(rowData[COL_SKID_6]));
		rr.setRateSkid7(getRate(rowData[COL_SKID_7]));
		rr.setRateSkid8(getRate(rowData[COL_SKID_8]));
		rr.setRateSkid9(getRate(rowData[COL_SKID_9]));
		rr.setRateSkid10(getRate(rowData[COL_SKID_10]));
		rr.setRateSkid11(getRate(rowData[COL_SKID_11]));
		rr.setRateSkid12(getRate(rowData[COL_SKID_12]));
		rr.setRateSkid13(getRate(rowData[COL_SKID_13]));
		rr.setRateSkid14(getRate(rowData[COL_SKID_14]));
		rr.setRateSkid15(getRate(rowData[COL_SKID_15]));
		rr.setRateSkid16(getRate(rowData[COL_SKID_16]));
		rr.setRateSkid17(getRate(rowData[COL_SKID_17]));
		rr.setRateSkid18(getRate(rowData[COL_SKID_18]));
		rr.setRateSkid19(getRate(rowData[COL_SKID_19]));
		rr.setRateSkid20(getRate(rowData[COL_SKID_20]));
		rr.setRateSkid21(getRate(rowData[COL_SKID_21]));
		rr.setRateSkid22(getRate(rowData[COL_SKID_22]));
		rr.setRateSkid23(getRate(rowData[COL_SKID_23]));
		rr.setRateSkid24(getRate(rowData[COL_SKID_24]));
		rr.setRateSkid25(getRate(rowData[COL_SKID_25]));
		rr.setRateSkid26(getRate(rowData[COL_SKID_26]));
		
		rr.setTtm1(StringUtil.getInteger(rowData[COL_TTM1]));
		rr.setTtm2(StringUtil.getInteger(rowData[COL_TTM2]));
		rr.setEffectiveDate(getDateTime(rowData[COL_EFF_DATE], null, DATE_FORMAT));
		rr.setExpiryDate(getDateTime(rowData[COL_EXP_DATE], null, DATE_FORMAT));
		rr.setP1(StringUtil.toBoolean(rowData[COL_P1]));
		rr.setP2(StringUtil.toBoolean(rowData[COL_P2]));
		rr.setP3(StringUtil.toBoolean(rowData[COL_P3]));
		
		return rr;
	}
	
	protected Timestamp getDateTime(String date, String time, String format) throws Exception{
		// TODO Auto-generated method stub
//		try {
			if (date != null && !date.isEmpty() && !date.equals("0")) {
				String s = date;
				if (time != null && !time.isEmpty() && !time.equals("0")) {
					s += time;
				}
				SimpleDateFormat dateFormat = new SimpleDateFormat(format);
				Date d = dateFormat.parse(s);
				return new Timestamp(d.getTime());
			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		return null;
	}	

	private Double getRate(String sRate) {
		if (sRate.isEmpty())
			return new Double(-1);
		if (sRate.equalsIgnoreCase("NA") || sRate.equalsIgnoreCase("N\\A"))
			return new Double(-1);
		
		return new Double(sRate);
	}

	private void processPoundRateRecord(String[] fields, String[] rowData,
			Service service, long customerId, long busId, boolean isOverwrite) throws Exception {
		ArrayList<LtlPoundRate> poundRates = populatePoundRates(fields, rowData, service, customerId, busId);
		if (poundRates != null && poundRates.size() > 0) {
			for (LtlPoundRate poundRate:poundRates) {
				if (isOverwrite)
					this.markupDAO.deletePoundRateRecord(poundRate); // if it already exists
				if (this.markupDAO.getPoundRate(poundRate) == null)
					this.markupDAO.addPoundRateRecord(poundRate);
			}
			
			poundRates.clear();
		}
	}

	private ArrayList<LtlPoundRate> populatePoundRates(String[] fields,
			String[] rowData, Service service, long customerId, long busId) throws Exception{
		ArrayList<LtlPoundRate> rrList = new ArrayList<LtlPoundRate>();
		for (int i=COL_PP_RATE_RANGE_START; i<COL_PP_RATE_RANGE_END; i++) {
			LtlPoundRate rr = new LtlPoundRate();
			
			rr.setBusinessId(busId);
			rr.setCustomerId(customerId);
			rr.setServiceId(service.getId());
			Address fromAddress = getFromAddress(rowData);
			rr.setFromZone(getZone(service.getZoneStructureId(), fromAddress).getZoneName());

			Address toAddress = getToAddress(rowData);
			rr.setToZone(getZone(service.getZoneStructureId(), toAddress).getZoneName());
			
			rr.setEquipment(rowData[COL_EQUIPMENT]);
			rr.setCurrency(rowData[COL_CURRENCY]);
			rr.setFscPercent(StringUtil.getDouble(rowData[COL_FSC]));
			rr.setMinimum(StringUtil.getDouble(rowData[COL_PP_MINIMUM]));
			
			int from = StringUtil.getInteger(fields[i]);
			rr.setRangeFrom(from);
			int to = StringUtil.getInteger(fields[i+1]);
			rr.setRangeTo(to-1);
			rr.setRate(getRate(rowData[i]));
			if (rr.getRate() >= 0) {
				rr.setTtm1(StringUtil.getInteger(rowData[COL_PP_TTM1]));
				rr.setTtm2(StringUtil.getInteger(rowData[COL_PP_TTM2]));
				rr.setEffectiveDate(getDateTime(rowData[COL_PP_EFF_DATE], null, DATE_FORMAT));
				rr.setExpiryDate(getDateTime(rowData[COL_PP_EXP_DATE], null, DATE_FORMAT));
				rr.setP1(StringUtil.toBoolean(rowData[COL_PP_P1]));
				rr.setP2(StringUtil.toBoolean(rowData[COL_PP_P2]));
				rr.setP3(StringUtil.toBoolean(rowData[COL_PP_P3]));			
				
				rrList.add(rr);
			}
		}
		return rrList;
	}

	private Address getToAddress(String[] rowData) {
		Address address = new Address();
		address.setCity(rowData[COL_TO_CITY]);
		address.setProvinceCode(rowData[COL_TO_PROVINCE]);
		address.setCountryCode(rowData[COL_TO_COUNTRY]);
		return address;
	}

	private Address getFromAddress(String[] rowData) {
		Address address = new Address();
		address.setCity(rowData[COL_FROM_CITY]);
		address.setProvinceCode(rowData[COL_FROM_PROVINCE]);
		address.setCountryCode(rowData[COL_FROM_COUNTRY]);
		return address;
	}

	private ArrayList<File> getFiles(String path) {
		if (path != null) {
			File f = new File(path);
			if (f.isDirectory()) {
				ArrayList<File> files = new ArrayList<File>();
				for (int i=0; i<f.listFiles().length; i++) {
					File file = f.listFiles()[i];
					if (file.isFile() && !file.getName().endsWith(ShiplinxConstants.WIP_FILE_EXT))
						files.add(file);
				}
				return files;
			} else {
				f.mkdir();
			}
		}
		return null;
	}	

	private File renameFileToWIP(File f) {
		// TODO Auto-generated method stub
		File f1 = new File(f.getAbsolutePath() + ShiplinxConstants.WIP_FILE_EXT);
		if (f.renameTo(f1))
			return new File(f1.getAbsolutePath());
		return null;
	}

	private void moveFileToOutFolder(File file, String destFolder) {
		// TODO Auto-generated method stub
		if (file != null) {
			String outFolder = WebUtil.getProperty(Constants.SYSTEM_SCOPE, "BASE_PATH") + File.separator + Constants.RATE_TEMPLATE_FOLDER +
										File.separator + ShiplinxConstants.OUT_FOLDER;
			String outFileName = file.getName();
			if (outFileName.endsWith(ShiplinxConstants.WIP_FILE_EXT))
				outFileName = outFileName.replace(ShiplinxConstants.WIP_FILE_EXT, "");
			
			File outFile = new File(outFolder, outFileName);
			if (outFile.exists()) {
				outFile.delete();
				outFile = null;
				outFile = new File(outFolder, outFileName);
			}
			boolean success = file.renameTo(outFile);
			if (!success) {
				String msg = "Failed to moved to out Folder:" + outFolder + " File:" + file.getName();
				log.error( msg );
			}
		}

	}	
	
}
