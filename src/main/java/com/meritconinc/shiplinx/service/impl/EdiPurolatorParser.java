package com.meritconinc.shiplinx.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.meritconinc.mmr.utilities.StringUtil;
import com.meritconinc.shiplinx.dao.AddressDAO;
import com.meritconinc.shiplinx.dao.CustomerDAO;
import com.meritconinc.shiplinx.dao.EdiDAO;
import com.meritconinc.shiplinx.model.Address;
import com.meritconinc.shiplinx.model.CarrierChargeCode;
import com.meritconinc.shiplinx.model.Charge;
import com.meritconinc.shiplinx.model.EdiInfo;
import com.meritconinc.shiplinx.model.EdiItem;
import com.meritconinc.shiplinx.model.LoggedEvent;
import com.meritconinc.shiplinx.model.Package;
import com.meritconinc.shiplinx.model.PackageType;
import com.meritconinc.shiplinx.model.Service;
import com.meritconinc.shiplinx.model.ShippingOrder;
import com.meritconinc.shiplinx.service.LoggedEventService;
import com.meritconinc.shiplinx.service.MarkupManager;
import com.meritconinc.shiplinx.service.ShippingService;
import com.meritconinc.shiplinx.utils.FormattingUtil;
import com.meritconinc.shiplinx.utils.ShiplinxConstants;

public class EdiPurolatorParser extends EdiParser {
	private static final Logger log = LogManager.getLogger(EdiPurolatorParser.class);

	private static final String REC_TYPE				="2_REC_TYPE";
	private static final String INVOICE_NUMBER			="3_INV_NUM";
	private static final String INVOICE_DATE			="4_INV_DATE";
	private static final String ACCOUNT_NUMBER			="5_PAYER_ACC";
	private static final String ACCOUNT_COUNTRY			="12_PAYER_COUNTRY";
	private static final String INVOICE_AMOUNT			="23_INV_TOTAL_AMT";
	private static final String INVOICE_CURRENCY		="24_CURRENCY";
	
	private static final String PACKAGE_QUANTITY		="28_NUM_OF_PCS";
	private static final String TRANSACTION_DATE		="79_SERVICE_DATE";
	private static final String LEAD_SHIPMENT_NUMBER	="81_SHIPMENT_PIN";
	private static final String ACTUAL_PRODUCT_NUM		="85_ACTUAL_PRODUCT_NUM";
	private static final String ACTUAL_WGT				="87_ACTUAL_WGT";
	private static final String ACTUAL_WGT_UOM			="88_ACTUAL_WGT_UOM";
	private static final String DECLARED_WGT			="93_DECLARED_WGT";
	private static final String DECLARED_WGT_UOM		="94_DECLARED_WGT_UOM";

	private static final String CUSTOMER_REF1			="96_CUSTOMER_REF1";
	private static final String CUSTOMER_REF2			="97_CUSTOMER_REF2";
	private static final String CUSTOMER_REF3			="98_CUSTOMER_REF3";
	private static final String CUSTOMER_REF4			="99_CUSTOMER_REF4";
	private static final String CUSTOMER_REF5			="100_CUSTOMER_REF5";
	
	private static final String TRACKING_NUMBER			="157_PIECE_PIN";
	
	private static final String PCS_ACTUAL_WGT			="158_PCS_ACTUAL_WGT";
	private static final String PCS_ACTUAL_WGT_UOM		="159_PCS_ACTUAL_WGT_UOM";
	private static final String PACKAGE_HEIGHT			="161_ACTUAL_HEIGHT";
	private static final String PACKAGE_WIDTH			="162_ACTUAL_WIDTH";
	private static final String PACKAGE_LENGTH			="163_ACTUAL_LENGTH";
	private static final String PCS_DECLARED_WGT		="165_PCS_DECLARED_WGT";
	
	private static final String ORIGIN_ZONE_ID			="175_ORIGIN_ZONE_ID";
	
	private static final String SENDER_COMP_NAME		="101_SHIP-FROM_COMPANY";
	private static final String SENDER_NAME				="102_SHIP-FROM_ATTN";
	private static final String SENDER_ADDRESS			="103_SHIP-FROM_ADDR";
	private static final String SENDER_CITY				="104_SHIP-FROM_CITY";
	private static final String SENDER_STATE			="105_SHIP-FROM_PROV";
	private static final String SENDER_POSTAL			="106_SHIP-FROM_POSTAL_CODE";
	private static final String SENDER_COUNTRY			="107_SHIP-FROM_COUNTRY";	
	private static final String RECEIVER_COMP_NAME		="108_SHIP-TO_COMPANY";
	private static final String RECEIVER_NAME			="109_SHIP-TO_ATTN";
	private static final String RECEIVER_ADDRESS		="110_SHIP-TO_ADDR";
	private static final String RECEIVER_CITY			="111_SHIP-TO_CITY";
	private static final String RECEIVER_STATE			="112_SHIP-TO_PROV";
	private static final String RECEIVER_POSTAL			="113_SHIP-TO_POSTAL_CODE";
	private static final String RECEIVER_COUNTRY		="114_SHIP-TO_COUNTRY";	
	
	private static final String SHIPMENT_BASE_AMT						="115_SHIPMENT_BASE_AMT";
	private static final String TAX_SHIPMENT_GST						="116_SHIPMENT_GST";
	private static final String TAX_SHIPMENT_QST						="118_SHIPMENT_QST";
	private static final String TAX_SHIPMENT_HST						="119_SHIPMENT_HST";
	private static final String SHIPMENT_TOTAL_AMT						="120_SHIPMENT_TOTAL_AMT";
	private static final String SURCHARGE_FUEL							="122_FUEL_SURCHARGE";
	private static final String SURCHARGE_DNGR_GOODS					="123_DNGR_GOODS";
	private static final String SURCHARGE_COS							="126_COS_SURCHARGE";
	private static final String SURCHARGE_EXPRESS_CHEQUE				="129_EXPRESS_CHEQUE";
	private static final String SURCHARGE_SATURDAY_DELIVERY				="132_SATURDAY_DELIVERY";
	private static final String SURCHARGE_SATURDAY_PICKUP				="135_SATURDAY_PICKUP";
	private static final String SURCHARGE_SPECIAL_HANDLING				="138_SPECIAL_HANDLING";
	private static final String SURCHARGE_COLLECT_3RDPARTY				="141_COLLECT_3RDPARTY_SURCHARGE";
	private static final String SURCHARGE_DECLARED_VALUE				="144_DECLARED_VALUE_SURCHARGE";
	private static final String SURCHARGE_MULTI_PIECE					="147_MULTI_PIECE_SURCHARGE";
	private static final String SURCHARGE_RESIDENTIAL_DELIVERY			="148_RESIDENTIAL_DELIVERY_SURCHARGE";
	private static final String SURCHARGE_BEYOND_ORIGIN					="149_BEYOND_ORIGIN_SURCHARGE";
	private static final String SURCHARGE_BEYOND_DEST					="150_BEYOND_DEST_SURCHARGE";
	private static final String SURCHARGE_MISSING_ACCOUNT				="151_MISSING_ACCOUNT_SURCHARGE";
	private static final String SURCHARGE_AIR_CARGO_NAVCAN				="152_AIR_CARGO_NAVCAN_SURCHARGE";
	private static final String SURCHARGE_AIR_CARGO_FUEL				="153_AIR_CARGO_FUEL_SURCHARGE";
	private static final String SURCHARGE_AIR_CARGO_DNGR_GD				="154_AIR_CARGO_DNGR_GD_SURCHARGE";
	private static final String SURCHARGE_RESIDENTIAL_PICKUP			="172_RESIDENTIAL_PICKUP_SURCHARGE";
	private static final String SURCHARGE_RESIDENTIAL_SIGNATURE			="173_RESIDENTIAL_SIGNATURE";
	private static final String SURCHARGE_MANUAL_SHPT_FEE				="174_MANUAL_SHPT_FEE";
	
	private static final String [][] chargeCodeMap = { 	
		{SHIPMENT_BASE_AMT, 				"FRT"},
		{TAX_SHIPMENT_GST, 					"GST"}, 
		{TAX_SHIPMENT_QST, 					"QST"},
		{TAX_SHIPMENT_HST, 					"HST"}, //To Address - based HST mapping
		{SURCHARGE_FUEL, 					"FUE"},
		{SURCHARGE_DNGR_GOODS, 				"OTH"},
		{SURCHARGE_COS, 					"OTH"},
		{SURCHARGE_EXPRESS_CHEQUE, 			"EXPC"},
		{SURCHARGE_SATURDAY_DELIVERY, 		"SATDLV"},
		{SURCHARGE_SATURDAY_PICKUP, 		"OTH"},
		{SURCHARGE_SPECIAL_HANDLING, 		"SPCLHNDLG"},
		{SURCHARGE_COLLECT_3RDPARTY, 		"OTH"},
		{SURCHARGE_DECLARED_VALUE, 			"INS"},
		{SURCHARGE_MULTI_PIECE, 			"MULTI"},
		{SURCHARGE_RESIDENTIAL_DELIVERY, 	"RESI"},
		{SURCHARGE_BEYOND_ORIGIN, 			"BEY"},
		{SURCHARGE_BEYOND_DEST, 			"BEY"},
		{SURCHARGE_MISSING_ACCOUNT, 		"OTH"},
		{SURCHARGE_AIR_CARGO_NAVCAN, 		"OTH"},	
		{SURCHARGE_AIR_CARGO_FUEL, 			"OTH"},
		{SURCHARGE_AIR_CARGO_DNGR_GD, 		"OTH"},
		{SURCHARGE_RESIDENTIAL_PICKUP, 		"PICKUP"},
		{SURCHARGE_RESIDENTIAL_SIGNATURE, 	"SIG"},
		{SURCHARGE_MANUAL_SHPT_FEE, 		"OTH"}
	};

//If Service description contains "Pack" then package type id =2
//if Service description contains "Envelope" then package_type_id=1
//otherwise package_type_id=3
	private static List<PackageType> packageTypes = null;
	private static final String defaultPackageType = "type_package";
//1, 'env', 'type_env', 'env description'
//2, 'pak', 'type_pak', 'pak description'
//3, 'package', 'type_package', 'pacakge description'
//4, 'pallet', 'type_pallet', 'pallate description'	
	private static final String [][] packageTypeMap = { 	
														{"Envelope", 	"type_env"}, 
														{"Pack", 		"type_pak"}
													};

	
	public static final String DATE_FORMAT = "yyyyMMdd"; //YYYYMMDD
	private static final String REC_TYPE_SHIPMENT = "S"; // S = Shipment or Accessorial, P = Piece Detail Information
	private static final String ADDRESS_CORR_SUFFIX = "AC";
	private static final Object ADDRESS_CORR_CODE = "482";
	private static final Object CHARGE_NAME_FREIGHT = "Freight";
	private static final String CHARGE_ADDRESS_CORRECTION = "Address Correction";
	
	private boolean recTypeShipment = false;
	
	public EdiPurolatorParser(EdiInfo ediInfo, EdiDAO ediDAO, ShippingService shippingService, 
			CustomerDAO customerDAO, AddressDAO addressDAO, 
			MarkupManager markupManagerService,
			LoggedEventService loggedService) {
		super(ediInfo, ediDAO, shippingService, customerDAO, addressDAO, 
				markupManagerService, loggedService);
	}	

	@Override
	protected List<String> performOperation() throws Exception {
		if (ediInfo.getEdiFolder() != null) {
			fields = populateFields();
			if (fields != null) {
				List<String> files = processEdiFiles();
				return files;
			}
		}
		return null;
	}

	@Override
	protected EdiItem populateEdiItem(String fileName) throws Exception {
		// In Purolator EDI File, first record is a header, therefore needs to be ignored
		if (this.rowData[0].equalsIgnoreCase("REC_NUM"))
			return null;
		
		setRecTypeShipment(getEdiField(REC_TYPE));
		
		String ediInvoiceDate = getEdiField(INVOICE_DATE);
		String ediInvoiceNumber = getEdiField(INVOICE_NUMBER);
		if (ediInvoiceDate != null && ediInvoiceNumber != null) {
			Date invoiceDate = getDateTime(ediInvoiceDate, null, DATE_FORMAT);
			EdiItem item = findEdiItem(ediInvoiceNumber);
			
			if (isRecTypeShipment()) {
				Date processedDate = new Date();
				if (item == null) {
					// New record
					item = new EdiItem();
					item.setBusinessId(ediInfo.getBusinessId());
					item.setCarrierId(ediInfo.getCarrierId());
					item.setEdiFileName(fileName);
					item.setMessage("text.message.status.process.inprogress");
					item.setStatus(ShiplinxConstants.STATUS_INPROGRESS);
					item.setInvoiceDate(invoiceDate);
					item.setInvoiceNumber(ediInvoiceNumber);
					item.setProcessedDate(processedDate);
					item.setAccountNumber(getEdiField(ACCOUNT_NUMBER));
					item.setTotInvoiceAmount(Double.parseDouble(getEdiField(INVOICE_AMOUNT)));
					
					long key = this.ediDAO.addEdiItem(item);
					if (key > 0) {
						item.setId(key);
						getEdiItems().add(item);
					}				
				} else {
					// Update Record 
					if (item.getStatus() != ShiplinxConstants.STATUS_INPROGRESS) {
						item.setMessage("text.message.status.process.inprogress");
						item.setStatus(ShiplinxConstants.STATUS_INPROGRESS);
						item.setInvoiceDate(invoiceDate);
						item.setInvoiceNumber(ediInvoiceNumber);
						item.setProcessedDate(processedDate);
						item.setStartTime(new Date());
						item.setAccountNumber(getEdiField(ACCOUNT_NUMBER));
						item.setTotInvoiceAmount(Double.parseDouble(getEdiField(INVOICE_AMOUNT)));
						
						this.ediDAO.updateEdiItem(item);
					}
				}
			}
			
			return item;
		}
		return null;

	}

	@Override
	protected ShippingOrder populateShipment(EdiItem item) throws Exception {
		ShippingOrder shipment = new ShippingOrder();

		shipment.setBusinessId(item.getBusinessId());
		shipment.setCarrierId(item.getCarrierId());

		shipment.setMasterTrackingNum(getEdiField(LEAD_SHIPMENT_NUMBER));
		
		boolean isAddressCorrection = false;
		// Address Correction - Tracking # End with 'AC'
		String transitCode = getEdiField(ACTUAL_PRODUCT_NUM);
		if (!StringUtil.isEmpty(transitCode) && !StringUtil.isEmpty(shipment.getMasterTrackingNum()) &&
			transitCode.equals(ADDRESS_CORR_CODE) && shipment.getMasterTrackingNum().endsWith(ADDRESS_CORR_SUFFIX) ) {
			shipment.setMasterTrackingNum(shipment.getMasterTrackingNum().replace(ADDRESS_CORR_SUFFIX, ""));
			isAddressCorrection = true;
		}

		
		Service service = this.getServiceType(shipment.getCarrierId(), transitCode);
		
		if (!isRecTypeShipment()) {
			Package pkg = populatePackage(item, shipment, service);
			shipment.getPackages().add(pkg);
		}
		
		ShippingOrder dbShipment = findShipment(shipment);
		if(dbShipment != null)
			shipment.setDbShipment(dbShipment);
		
		if (isRecTypeShipment()) {
			shipment.setScheduledShipDate(getDateTime(getEdiField(TRANSACTION_DATE), null, DATE_FORMAT));
			shipment.setReferenceOne(getEdiField(CUSTOMER_REF1));
			shipment.setReferenceTwo(getEdiField(CUSTOMER_REF2));
			shipment.setQuantity(Integer.parseInt(getEdiField(PACKAGE_QUANTITY)));
			shipment.setCurrency(getEdiField(INVOICE_CURRENCY));
			shipment.setFromZone(getEdiField(ORIGIN_ZONE_ID)); 
			
			//first try and set service id from dbShipment
			if(shipment.getDbShipment()!=null && shipment.getDbShipment().getServiceId() > 0)
				shipment.setServiceId(shipment.getDbShipment().getServiceId());
			else {
				if (service != null)
					shipment.setServiceId(service.getId());
			}
			
			Address shipFrom = populateShipFromAddress();
			shipment.setFromAddress(shipFrom);
	
			Address shipTo = populateShipToAddress();
			shipment.setToAddress(shipTo);	
			
			populateCustomer(shipment, item.getAccountNumber());
			
			populateCharges(shipment, item, isAddressCorrection);
			
			// Verify Total Shipment
			double ediShipmentTotAmount = StringUtil.getDouble(getEdiField(SHIPMENT_TOTAL_AMT));
			double shipmentTotCharges = shipment.getTotalCostActual();
			if (ediShipmentTotAmount > 0 && ediShipmentTotAmount != shipmentTotCharges) {
				// Shipment Total Amount did not match with charges
				String msg = "Shipment Total Amount [" + ediShipmentTotAmount + "] Shipment Total Charges [" + 
										shipmentTotCharges + "] did not match. Master Tracking #[" + shipment.getMasterTrackingNum() + "]";
				log.warn(msg);
			}
				
			
			if(getEdiField(ACTUAL_WGT_UOM)!=null && getEdiField(ACTUAL_WGT_UOM).length()>0){
				if(getEdiField(ACTUAL_WGT_UOM).equalsIgnoreCase("LB"))
						shipment.setBilledWeightUOM(ShiplinxConstants.WEIGHT_UNITS_LBS);
				else
					if(getEdiField(ACTUAL_WGT_UOM).equalsIgnoreCase("KG"))
						shipment.setBilledWeightUOM(ShiplinxConstants.WEIGHT_UNITS_KGS);
			}
			shipment.setBilledWeight(Float.parseFloat(getEdiField(ACTUAL_WGT)));
			
			if(getEdiField(DECLARED_WGT_UOM)!=null && getEdiField(DECLARED_WGT_UOM).length()>0){
				if(getEdiField(DECLARED_WGT_UOM).equalsIgnoreCase("LB"))
						shipment.setQuotedWeightUOM(ShiplinxConstants.WEIGHT_UNITS_LBS);
				else
					if(getEdiField(DECLARED_WGT_UOM).equalsIgnoreCase("KG"))
						shipment.setQuotedWeightUOM(ShiplinxConstants.WEIGHT_UNITS_KGS);
			}
			shipment.setQuotedWeight(Float.parseFloat(getEdiField(DECLARED_WGT)));
		}
		
		return shipment;

	}
	
	private Service getServiceType( Long carrierId, String transitCode ) {
		if (!StringUtil.isEmpty(transitCode) && carrierId != null && carrierId.longValue() > 0) {
			return this.shippingService.getServiceByCarrierAndTransitCode(carrierId, transitCode);
		}
		return null;
	}	
	
	private void populateCharges(ShippingOrder shipment, EdiItem item, boolean isAddressCorrection) {
		Charge charge = null;
		for (int i=0; i<chargeCodeMap.length; i++) {
			charge = populateCharge(shipment, item, chargeCodeMap[i], isAddressCorrection);
			if (charge != null) {
				if(charge!=null && charge.getCost()>0) //if cost is 0, then don't add charge. Example: Freight Collect
					shipment.getCharges().add(charge); 
			}
		}
	}	
	
	private Charge populateCharge(ShippingOrder shipment, EdiItem item, String[] chargeCodeMapInfo, boolean isAddressCorrection) {
		// TODO Auto-generated method stub
		Charge charge = new Charge();
		String chargeGroupCode = null;
		
		String chargeCode = chargeCodeMapInfo[1];
		String chargeCodeLevel2 = chargeCodeMapInfo[1];
		String chargeName = chargeCodeMapInfo[0];
		//if this is HST,  we need to set to appropriate HST (ON,BC,NS,NB,NF) as the charge code in UPS EDI file does not indicate which HST
		if (chargeCodeLevel2.equalsIgnoreCase(ShiplinxConstants.TAX_HST)) {
			chargeCodeLevel2 = new String(ShiplinxConstants.TAX_HST + " " + shipment.getFromAddress().getProvinceCode());
		}
		if (chargeCode.equals(ShiplinxConstants.TAX_GST) || 
			chargeCode.equals(ShiplinxConstants.TAX_HST) || 
			chargeCode.equals(ShiplinxConstants.TAX_QST))
			chargeCode = "TAX";
		List<CarrierChargeCode> chargeCodes = shippingService.getChargeListByCarrierAndCodes(
									item.getCarrierId(), chargeCode, chargeCodeLevel2);
		if (chargeCodes != null && chargeCodes.size() > 0) {
			chargeCode = chargeCodes.get(0).getChargeCode();
			chargeCodeLevel2 = chargeCodes.get(0).getChargeCodeLevel2();
			chargeGroupCode = chargeCodes.get(0).getGroupCode(); 	
			chargeName = chargeCodes.get(0).getChargeName();
		} else {
			chargeCode = ShiplinxConstants.CHARGE_CODE_PURO_ACC;
			chargeCodeLevel2 = ShiplinxConstants.CHARGE_CODE_LEVEL_2_PURO_OTH;
			chargeName = chargeCodeMapInfo[0];
			
		}
		
//		charge.setCharge(new Double(0.0));
		charge.setChargeCode(chargeCode);
		charge.setChargeCodeLevel2(chargeCodeLevel2);
		// If charge code is "OTH" then charge name should be the name of the field
		if (chargeCode.equals(ShiplinxConstants.CHARGE_CODE_PURO_ACC))
			chargeName = chargeCodeMapInfo[0];
		// If it's address correction and charge name is freight, then change the charge name to "Address Correction"
		if (isAddressCorrection && chargeName.equals(CHARGE_NAME_FREIGHT))
			chargeName = CHARGE_ADDRESS_CORRECTION;
		charge.setName(chargeName);
		charge.setCurrency(shipment.getCurrency());
		charge.setDiscountAmount(new Double(0.0)); 
		charge.setCost(StringUtil.getDouble(getEdiField(chargeCodeMapInfo[0]))); 
		//Tariff=DiscountAmount+cost
		charge.setTariffRate(FormattingUtil.add(charge.getCost(), charge.getDiscountAmount()).doubleValue());
		charge.setEdiInvoiceNumber(item.getInvoiceNumber());
		charge.setStatus(ShiplinxConstants.CHARGE_PENDING_RELEASE);
		
		if (chargeGroupCode != null && (chargeGroupCode.equals(ShiplinxConstants.GROUP_FUEL_CHARGE) ||
										chargeGroupCode.equals(ShiplinxConstants.GROUP_FREIGHT_CHARGE)	)) {
			charge.setCharge( applyMarkup(shipment, charge, item) );
		} else {
			charge.setCharge(charge.getCost());
		}
		charge.setType(ShiplinxConstants.CHARGE_TYPE_ACTUAL);
		return charge;
	}

	
	private Package populatePackage(EdiItem item, ShippingOrder ediShipment, Service service) {
		// TODO Auto-generated method stub
		Package pkg = new Package();
		pkg.setTrackingNumber(getEdiField(TRACKING_NUMBER));
		pkg.setReference1(getEdiField(CUSTOMER_REF3));
		pkg.setReference2(getEdiField(CUSTOMER_REF4));
		pkg.setReference3(getEdiField(CUSTOMER_REF5));
		//don't over write declared weight from EDI file
		//pkg.setWeight(Float.parseFloat(getEdiField(PCS_DECLARED_WGT)));
		pkg.setBilledWeight(Float.parseFloat(getEdiField(PCS_ACTUAL_WGT)));
		pkg.setWeightUOM(getEdiField(PCS_ACTUAL_WGT_UOM));
		pkg.setLength(new BigDecimal(getEdiField(PACKAGE_LENGTH)));
		pkg.setWidth(new BigDecimal(getEdiField(PACKAGE_WIDTH)));
		pkg.setHeight(new BigDecimal(getEdiField(PACKAGE_HEIGHT)));			
		PackageType pkgType = getPackageType(service.getDescription());
		
		if(pkg.getLength()!=null && pkg.getWidth()!=null && pkg.getHeight()!=null){
			String length = getEdiField(PACKAGE_LENGTH);
			String width = getEdiField(PACKAGE_WIDTH);
			String height = getEdiField(PACKAGE_HEIGHT);
			if(length!=null && length.length()>0 && width!=null && width.length()>0 && height!=null && height.length()>0)
				pkg.setDimmedString(length + " x " + width + " x " + height);
			else
				pkg.setDimmedString(null);
		}
		
		if (pkgType != null) {
			ediShipment.setPackageTypeId(pkgType);
			pkg.setType(pkgType.getType());
		}
	

		return pkg;
	}
	private PackageType getPackageType(String serviceDesc) {
		String pkgType = defaultPackageType;
		for (int i=0; i<packageTypeMap.length; i++)
			if (serviceDesc.indexOf(packageTypeMap[i][0]) > 0)
				pkgType = packageTypeMap[i][1];
		
		if (pkgType != null) {
			if (packageTypes == null)
				packageTypes = this.shippingService.getAllPackages();
			for(PackageType pt:packageTypes)
				if (pt.getType().equals(pkgType)) 
					return pt;
		}
		
		return null;
	}	
	
	private Address populateShipToAddress() {
		// TODO Auto-generated method stub
		Address shipTo = new Address();
		shipTo.setDefaultFromAddress(false);
		shipTo.setDefaultToAddress(false);
		shipTo.setResidential(true);		
		shipTo.setContactName(getEdiField(RECEIVER_NAME));
		shipTo.setAbbreviationName(getEdiField(RECEIVER_COMP_NAME));
		shipTo.setAddress1(getEdiField(RECEIVER_ADDRESS));
//		shipTo.setAddress2(getEdiField(RECEIVER_ADDRESS_2));
		shipTo.setCity(getEdiField(RECEIVER_CITY));
		shipTo.setProvinceCode(getEdiField(RECEIVER_STATE));
		shipTo.setPostalCode(getEdiField(RECEIVER_POSTAL));
		shipTo.setCountryCode(getEdiField(RECEIVER_COUNTRY));

		return shipTo;
	}

	private Address populateShipFromAddress() {
		// TODO Auto-generated method stub
		Address shipFrom = new Address();
		shipFrom.setDefaultFromAddress(false);
		shipFrom.setDefaultToAddress(false);
		shipFrom.setResidential(true);
		shipFrom.setContactName(getEdiField(SENDER_NAME));
		shipFrom.setAbbreviationName(getEdiField(SENDER_COMP_NAME));
		shipFrom.setAddress1(getEdiField(SENDER_ADDRESS));
//		shipFrom.setAddress2(getEdiField(SENDER_ADDRESS_2));
		shipFrom.setCity(getEdiField(SENDER_CITY));
		shipFrom.setProvinceCode(getEdiField(SENDER_STATE));
		shipFrom.setPostalCode(getEdiField(SENDER_POSTAL));
		shipFrom.setCountryCode(getEdiField(SENDER_COUNTRY));
		
		if(shipFrom.getCountryCode()==null || shipFrom.getCountryCode().length()==0){ //from address not in EDI file. Make assumption that shipment origin is same as country of account
			shipFrom.setCountryCode(getEdiField(ACCOUNT_COUNTRY));
		}

		return shipFrom;
	}

	public boolean isRecTypeShipment() {
		return recTypeShipment;
	}

	public void setRecTypeShipment(String recType) {
		if (!StringUtil.isEmpty(recType) && recType.equalsIgnoreCase(REC_TYPE_SHIPMENT))
			this.recTypeShipment = true;
		else
			this.recTypeShipment = false;
	}
	

	protected void updateShipment(ShippingOrder ediShipment, ShippingOrder dbShipment) throws Exception {
		if ( !isRecTypeShipment() ) {
			// Update Package
			Package ediPackage = null;
			if (ediShipment.getPackages() != null && ediShipment.getPackages().size() > 0) {
				ediPackage = ediShipment.getPackages().get(0);
				if (ediPackage != null) {
					Package dbPackage = findPackage(dbShipment, ediPackage
							.getTrackingNumber());
					if (dbPackage == null) {
						addPackage(dbShipment, ediShipment);
					} else {
						updatePackage(ediPackage, dbPackage);
					}
				}
			}
		} else {
			//if service id is set in existing shipment, and not in edi record, then set it
			if(dbShipment.getServiceId() != null && dbShipment.getServiceId()>0 && 
					ediShipment.getServiceId() != null && ediShipment.getServiceId()<=0)
				ediShipment.setServiceId(dbShipment.getServiceId());
	
			// Update Charges
			for(Charge ediCharge: ediShipment.getCharges()){
				if (ediCharge != null) {
					Charge dbCharge = findCharge(dbShipment, ediCharge);
					if (dbCharge == null) {
						addCharge(dbShipment, ediCharge);
					} else {
						updateCharge(ediCharge, dbCharge);
					}
				}
			}
		}
		
		if((dbShipment.getBilledWeight()==null || dbShipment.getBilledWeight()==0) && ediShipment.getBilledWeight()!=null && ediShipment.getBilledWeight() > 0){
			dbShipment.setBilledWeight(ediShipment.getBilledWeight());
			dbShipment.setBilledWeightUOM(ediShipment.getBilledWeightUOM());
		}
		dbShipment.setFromZone(ediShipment.getFromZone());
		
		shippingService.applyAdditionalHandling(dbShipment, null, ShiplinxConstants.CHARGE_TYPE_ACTUAL);
		
		if (!applyExceptionsRules(ediShipment, dbShipment)) {
			// No exception rules were applied
			dbShipment.setStatusId((long)ShiplinxConstants.STATUS_DELIVERED);
		}
		this.shippingService.updateShippingOrder(dbShipment);
	}

	@Override
	protected boolean applyCustomExceptionRules(ShippingOrder ediShipment,
			ShippingOrder dbShipment, List<LoggedEvent> events) {
		String logMsg = null;
		
//		Purolator specific exception:
//		1. 	If the TRACKING_NUMBER is not same as the LEAD_SHIPMENT_NUMBER, and 
//			TRACKING_NUMBER is a master_tracking_num of a shipment in the database 
//			shipping_order table, then set the shipment as an exception with message 
//			"Shipment seems to have been consolidated, tracking #s:  LEAD_SHIPMENT_NUMBER and TRACKING_NUMBER"
		Package ediPackage = null;
		if (ediShipment.getPackages() != null && ediShipment.getPackages().size() > 0) {
			ediPackage = ediShipment.getPackages().get(0);
		}
		if (ediPackage != null) {
			if ( !ediPackage.getTrackingNumber().equals(ediShipment.getMasterTrackingNum()) ) {
				ShippingOrder dbShipment2 = findShipmentByMasterTrackingNumber(
								dbShipment.getCarrierId(), ediPackage.getTrackingNumber());
				// As per discussion with Rizwan on 2 June 2013 @ 4:20 PM
				// both shipments needs to be consolidated and marked as exceptions
				if (dbShipment2 != null) {
					logMsg = "Shipment seems to have been consolidated, tracking #s:  LEAD_SHIPMENT_NUMBER(" +
							 ediShipment.getMasterTrackingNum() + ") and TRACKING_NUMBER(" +
							 ediPackage.getTrackingNumber() + ")";
					events.add(getLoggedEvent(dbShipment.getId().longValue(), logMsg));
					
					// dbShipment2 needs to be marked as exception
					events.add(getLoggedEvent(dbShipment2.getId().longValue(), logMsg));
					dbShipment2.setStatusId(Long.valueOf(ShiplinxConstants.STATUS_EXCEPTION));
					this.shippingService.updateShippingOrder(dbShipment2);
				}
			}
		}
		
		if (logMsg != null)
			return true;

		return false;
	}	

}
