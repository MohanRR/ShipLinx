
package com.meritconinc.shiplinx.carrier.purolator.ws.shipping.proxy;

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
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.Action;
import javax.xml.ws.FaultAction;

/**
 * This class was generated by Apache CXF 2.5.2
 * 2012-07-13T18:50:30.665-04:00
 * Generated source version: 2.5.2
 * 
 */
public final class ShippingServiceContract_ShippingServiceEndpoint_Client {

    private static final QName SERVICE_NAME = new QName("http://purolator.com/pws/service/v1", "ShippingService");

    private ShippingServiceContract_ShippingServiceEndpoint_Client() {
    }

    public static void main(String args[]) throws java.lang.Exception {
        URL wsdlURL = ShippingService.WSDL_LOCATION;
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
      
        ShippingService ss = new ShippingService(wsdlURL, SERVICE_NAME);
        ShippingServiceContract port = ss.getShippingServiceEndpoint();  
        
        {
        System.out.println("Invoking validateShipment...");
        com.meritconinc.shiplinx.carrier.purolator.ws.shipping.proxy.ValidateShipmentRequestContainer _validateShipment_validateShipmentRequest = null;
        try {
            com.meritconinc.shiplinx.carrier.purolator.ws.shipping.proxy.ValidateShipmentResponseContainer _validateShipment__return = port.validateShipment(_validateShipment_validateShipmentRequest);
            System.out.println("validateShipment.result=" + _validateShipment__return);

        } catch (ShippingServiceContractValidateShipmentValidationFaultFaultFaultMessage e) { 
            System.out.println("Expected exception: ShippingServiceContract_ValidateShipment_ValidationFaultFault_FaultMessage has occurred.");
            System.out.println(e.toString());
        }
            }
        {
        System.out.println("Invoking voidShipment...");
        com.meritconinc.shiplinx.carrier.purolator.ws.shipping.proxy.VoidShipmentRequestContainer _voidShipment_voidShipmentRequest = null;
        try {
            com.meritconinc.shiplinx.carrier.purolator.ws.shipping.proxy.VoidShipmentResponseContainer _voidShipment__return = port.voidShipment(_voidShipment_voidShipmentRequest);
            System.out.println("voidShipment.result=" + _voidShipment__return);

        } catch (ShippingServiceContractVoidShipmentValidationFaultFaultFaultMessage e) { 
            System.out.println("Expected exception: ShippingServiceContract_VoidShipment_ValidationFaultFault_FaultMessage has occurred.");
            System.out.println(e.toString());
        }
            }
        {
        System.out.println("Invoking consolidate...");
        com.meritconinc.shiplinx.carrier.purolator.ws.shipping.proxy.ConsolidateRequestContainer _consolidate_consolidateRequest = null;
        try {
            com.meritconinc.shiplinx.carrier.purolator.ws.shipping.proxy.ConsolidateResponseContainer _consolidate__return = port.consolidate(_consolidate_consolidateRequest);
            System.out.println("consolidate.result=" + _consolidate__return);

        } catch (ShippingServiceContractConsolidateValidationFaultFaultFaultMessage e) { 
            System.out.println("Expected exception: ShippingServiceContract_Consolidate_ValidationFaultFault_FaultMessage has occurred.");
            System.out.println(e.toString());
        }
            }
        {
        System.out.println("Invoking createShipment...");
        com.meritconinc.shiplinx.carrier.purolator.ws.shipping.proxy.CreateShipmentRequestContainer _createShipment_createShipmentRequest = null;
        try {
            com.meritconinc.shiplinx.carrier.purolator.ws.shipping.proxy.CreateShipmentResponseContainer _createShipment__return = port.createShipment(_createShipment_createShipmentRequest);
            System.out.println("createShipment.result=" + _createShipment__return);

        } catch (ShippingServiceContractCreateShipmentValidationFaultFaultFaultMessage e) { 
            System.out.println("Expected exception: ShippingServiceContract_CreateShipment_ValidationFaultFault_FaultMessage has occurred.");
            System.out.println(e.toString());
        }
            }

        System.exit(0);
    }

}