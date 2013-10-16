
package com.meritconinc.shiplinx.carrier.ups.ws.freightpickup.proxy;

import javax.xml.ws.WebFault;


/**
 * This class was generated by Apache CXF 2.5.0
 * 2011-12-09T23:07:23.595-05:00
 * Generated source version: 2.5.0
 */

@WebFault(name = "Errors", targetNamespace = "http://www.ups.com/XMLSchema/XOLTWS/Error/v1.1")
public class FreightCancelPickupErrorMessage extends Exception {
    
    private com.meritconinc.shiplinx.carrier.ups.ws.freightpickup.proxy.Errors errors;

    public FreightCancelPickupErrorMessage() {
        super();
    }
    
    public FreightCancelPickupErrorMessage(String message) {
        super(message);
    }
    
    public FreightCancelPickupErrorMessage(String message, Throwable cause) {
        super(message, cause);
    }

    public FreightCancelPickupErrorMessage(String message, com.meritconinc.shiplinx.carrier.ups.ws.freightpickup.proxy.Errors errors) {
        super(message);
        this.errors = errors;
    }

    public FreightCancelPickupErrorMessage(String message, com.meritconinc.shiplinx.carrier.ups.ws.freightpickup.proxy.Errors errors, Throwable cause) {
        super(message, cause);
        this.errors = errors;
    }

    public com.meritconinc.shiplinx.carrier.ups.ws.freightpickup.proxy.Errors getFaultInfo() {
        return this.errors;
    }
}
