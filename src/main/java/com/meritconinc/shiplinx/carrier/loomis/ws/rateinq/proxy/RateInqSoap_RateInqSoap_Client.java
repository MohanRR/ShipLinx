
package com.meritconinc.shiplinx.carrier.loomis.ws.rateinq.proxy;

/**
 * Please modify this class to meet your needs
 * This class is not complete
 */

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

/**
 * This class was generated by Apache CXF 2.5.0
 * 2012-02-11T21:55:33.594-05:00
 * Generated source version: 2.5.0
 * 
 */
public final class RateInqSoap_RateInqSoap_Client {

    private static final QName SERVICE_NAME = new QName("http://tempuri.org/RATEINQ/Service1", "RateInq");

    private RateInqSoap_RateInqSoap_Client() {
    }

    public static void main(String args[]) throws java.lang.Exception {
        URL wsdlURL = RateInq_Service.WSDL_LOCATION;
        if (args.length > 0 && args[0] != null && !"".equals(args[0])) { 
            File wsdlFile = new File(args[0]);
            try {
                if (wsdlFile.exists()) {
                    wsdlURL = wsdlFile.toURI().toURL();
                } else {
                    wsdlURL = new URL(args[0]);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
      
        RateInq_Service ss = new RateInq_Service(wsdlURL, SERVICE_NAME);
        RateInqSoap port = ss.getRateInqSoap();  
        
        {
        System.out.println("Invoking rateInq...");
        java.lang.String _rateInq_loginName = "";
        java.lang.String _rateInq_password = "";
        java.lang.String _rateInq_shipperAccountNumber = "";
        java.lang.String _rateInq_fromPostalCode = "";
        java.lang.String _rateInq_toPostalCode = "";
        java.lang.String _rateInq_toCity = "";
        java.lang.String _rateInq_toProvinceCode = "";
        java.lang.String _rateInq_toCountryCode = "";
        java.lang.String _rateInq_serviceType = "";
        javax.xml.datatype.XMLGregorianCalendar _rateInq_pickupDate = null;
        java.lang.String _rateInq_uom = "";
        com.meritconinc.shiplinx.carrier.loomis.ws.rateinq.proxy.ArrayOfPiece _rateInq_pieces = null;
        double _rateInq_valuationAmount = 0.0;
        boolean _rateInq_isNonPack = false;
        boolean _rateInq_isDangerousGood = false;
        boolean _rateInq_isSaturdayDelivery = false;
        boolean _rateInq_isFragile = false;
        boolean _rateInq_isResidential = false;
        boolean _rateInq_isDutiable = false;
        boolean _rateInq_isDTP = false;
        java.lang.String _rateInq_language = "";
        com.meritconinc.shiplinx.carrier.loomis.ws.rateinq.proxy.ReturnRates _rateInq__return = port.rateInq(_rateInq_loginName, _rateInq_password, _rateInq_shipperAccountNumber, _rateInq_fromPostalCode, _rateInq_toPostalCode, _rateInq_toCity, _rateInq_toProvinceCode, _rateInq_toCountryCode, _rateInq_serviceType, _rateInq_pickupDate, _rateInq_uom, _rateInq_pieces, _rateInq_valuationAmount, _rateInq_isNonPack, _rateInq_isDangerousGood, _rateInq_isSaturdayDelivery, _rateInq_isFragile, _rateInq_isResidential, _rateInq_isDutiable, _rateInq_isDTP, _rateInq_language);
        System.out.println("rateInq.result=" + _rateInq__return);


        }

        System.exit(0);
    }

}