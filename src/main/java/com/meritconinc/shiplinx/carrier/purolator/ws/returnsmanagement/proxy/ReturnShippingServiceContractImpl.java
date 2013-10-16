
/**
 * Please modify this class to meet your needs
 * This class is not complete
 */

package com.meritconinc.shiplinx.carrier.purolator.ws.returnsmanagement.proxy;

import java.util.logging.Logger;
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
 * 2012-07-13T18:47:27.326-04:00
 * Generated source version: 2.5.2
 * 
 */

@javax.jws.WebService(
                      serviceName = "ReturnsManagementService",
                      portName = "ReturnsManagementServiceEndpoint",
                      targetNamespace = "http://purolator.com/pws/service/v1",
                      wsdlLocation = "ReturnsManagementService.wsdl",
                      endpointInterface = "com.meritconinc.shiplinx.carrier.purolator.ws.returnsmanagement.proxy.ReturnShippingServiceContract")
                      
public class ReturnShippingServiceContractImpl implements ReturnShippingServiceContract {

    private static final Logger LOG = Logger.getLogger(ReturnShippingServiceContractImpl.class.getName());

    /* (non-Javadoc)
     * @see com.meritconinc.shiplinx.carrier.purolator.ws.returnsmanagement.proxy.ReturnShippingServiceContract#validateReturnsManagementShipment(com.meritconinc.shiplinx.carrier.purolator.ws.returnsmanagement.proxy.ValidateReturnsManagementShipmentRequestContainer  validateReturnsManagementShipmentRequest )*
     */
    public com.meritconinc.shiplinx.carrier.purolator.ws.returnsmanagement.proxy.ValidateReturnsManagementShipmentResponseContainer validateReturnsManagementShipment(ValidateReturnsManagementShipmentRequestContainer validateReturnsManagementShipmentRequest) throws ReturnShippingServiceContractValidateReturnsManagementShipmentValidationFaultFaultFaultMessage    { 
        LOG.info("Executing operation validateReturnsManagementShipment");
        System.out.println(validateReturnsManagementShipmentRequest);
        try {
            com.meritconinc.shiplinx.carrier.purolator.ws.returnsmanagement.proxy.ValidateReturnsManagementShipmentResponseContainer _return = null;
            return _return;
        } catch (java.lang.Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
        //throw new ReturnShippingServiceContractValidateReturnsManagementShipmentValidationFaultFaultFaultMessage("ReturnShippingServiceContract_ValidateReturnsManagementShipment_ValidationFaultFault_FaultMessage...");
    }

    /* (non-Javadoc)
     * @see com.meritconinc.shiplinx.carrier.purolator.ws.returnsmanagement.proxy.ReturnShippingServiceContract#createReturnsManagementShipment(com.meritconinc.shiplinx.carrier.purolator.ws.returnsmanagement.proxy.CreateReturnsManagementShipmentRequestContainer  createReturnsManagementShipmentRequest )*
     */
    public com.meritconinc.shiplinx.carrier.purolator.ws.returnsmanagement.proxy.CreateReturnsManagementShipmentResponseContainer createReturnsManagementShipment(CreateReturnsManagementShipmentRequestContainer createReturnsManagementShipmentRequest) throws ReturnShippingServiceContractCreateReturnsManagementShipmentValidationFaultFaultFaultMessage    { 
        LOG.info("Executing operation createReturnsManagementShipment");
        System.out.println(createReturnsManagementShipmentRequest);
        try {
            com.meritconinc.shiplinx.carrier.purolator.ws.returnsmanagement.proxy.CreateReturnsManagementShipmentResponseContainer _return = null;
            return _return;
        } catch (java.lang.Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
        //throw new ReturnShippingServiceContractCreateReturnsManagementShipmentValidationFaultFaultFaultMessage("ReturnShippingServiceContract_CreateReturnsManagementShipment_ValidationFaultFault_FaultMessage...");
    }

}
