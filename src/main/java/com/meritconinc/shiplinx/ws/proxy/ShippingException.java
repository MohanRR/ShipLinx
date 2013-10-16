
package com.meritconinc.shiplinx.ws.proxy;

import javax.xml.ws.WebFault;


/**
 * This class was generated by Apache CXF 2.5.2
 * 2013-03-06T21:55:32.971-05:00
 * Generated source version: 2.5.2
 */

@WebFault(name = "ShippingErrors", targetNamespace = "http://www.proxy.ws.shiplinx.meritconinc.com/datatypes")
public class ShippingException extends Exception {
    
    private com.meritconinc.shiplinx.ws.proxy.datatypes.ErrorsWSType shippingErrors;

    public ShippingException() {
        super();
    }
    
    public ShippingException(String message) {
        super(message);
    }
    
    public ShippingException(String message, Throwable cause) {
        super(message, cause);
    }

    public ShippingException(String message, com.meritconinc.shiplinx.ws.proxy.datatypes.ErrorsWSType shippingErrors) {
        super(message);
        this.shippingErrors = shippingErrors;
    }

    public ShippingException(String message, com.meritconinc.shiplinx.ws.proxy.datatypes.ErrorsWSType shippingErrors, Throwable cause) {
        super(message, cause);
        this.shippingErrors = shippingErrors;
    }

    public com.meritconinc.shiplinx.ws.proxy.datatypes.ErrorsWSType getFaultInfo() {
        return this.shippingErrors;
    }
}