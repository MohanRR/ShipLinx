
package com.meritconinc.shiplinx.carrier.purolator.ws.pickup.proxy;

import javax.xml.ws.WebFault;


/**
 * This class was generated by Apache CXF 2.5.2
 * 2012-07-13T18:43:59.862-04:00
 * Generated source version: 2.5.2
 */

@WebFault(name = "ValidationFault", targetNamespace = "http://www.microsoft.com/practices/EnterpriseLibrary/2007/01/wcf/validation")
public class PickUpServiceContractVoidPickUpValidationFaultFaultFaultMessage extends Exception {
    
    private com.meritconinc.shiplinx.carrier.purolator.ws.pickup.proxy.ValidationFault validationFault;

    public PickUpServiceContractVoidPickUpValidationFaultFaultFaultMessage() {
        super();
    }
    
    public PickUpServiceContractVoidPickUpValidationFaultFaultFaultMessage(String message) {
        super(message);
    }
    
    public PickUpServiceContractVoidPickUpValidationFaultFaultFaultMessage(String message, Throwable cause) {
        super(message, cause);
    }

    public PickUpServiceContractVoidPickUpValidationFaultFaultFaultMessage(String message, com.meritconinc.shiplinx.carrier.purolator.ws.pickup.proxy.ValidationFault validationFault) {
        super(message);
        this.validationFault = validationFault;
    }

    public PickUpServiceContractVoidPickUpValidationFaultFaultFaultMessage(String message, com.meritconinc.shiplinx.carrier.purolator.ws.pickup.proxy.ValidationFault validationFault, Throwable cause) {
        super(message, cause);
        this.validationFault = validationFault;
    }

    public com.meritconinc.shiplinx.carrier.purolator.ws.pickup.proxy.ValidationFault getFaultInfo() {
        return this.validationFault;
    }
}
