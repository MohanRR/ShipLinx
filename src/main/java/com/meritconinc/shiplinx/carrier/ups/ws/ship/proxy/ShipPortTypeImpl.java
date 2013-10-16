
/**
 * Please modify this class to meet your needs
 * This class is not complete
 */

package com.meritconinc.shiplinx.carrier.ups.ws.ship.proxy;

import java.util.logging.Logger;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * This class was generated by Apache CXF 2.5.0
 * 2011-12-08T22:11:53.368-05:00
 * Generated source version: 2.5.0
 * 
 */

@javax.jws.WebService(
                      serviceName = "ShipService",
                      portName = "ShipPort",
                      targetNamespace = "http://www.ups.com/WSDL/XOLTWS/Ship/v1.0",
                      wsdlLocation = "Ship.wsdl",
                      endpointInterface = "com.meritconinc.shiplinx.carrier.ups.ws.ship.proxy.ShipPortType")
                      
public class ShipPortTypeImpl implements ShipPortType {

    private static final Logger LOG = Logger.getLogger(ShipPortTypeImpl.class.getName());

    /* (non-Javadoc)
     * @see com.meritconinc.shiplinx.carrier.ups.ws.ship.proxy.ShipPortType#processShipment(com.meritconinc.shiplinx.carrier.ups.ws.ship.proxy.ShipmentRequest  body ,)com.meritconinc.shiplinx.carrier.ups.ws.ship.proxy.UPSSecurity  upsSecurity )*
     */
    public com.meritconinc.shiplinx.carrier.ups.ws.ship.proxy.ShipmentResponse processShipment(ShipmentRequest body,UPSSecurity upsSecurity) throws ShipmentErrorMessage    { 
        LOG.info("Executing operation processShipment");
        System.out.println(body);
        System.out.println(upsSecurity);
        try {
            com.meritconinc.shiplinx.carrier.ups.ws.ship.proxy.ShipmentResponse _return = null;
            return _return;
        } catch (java.lang.Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
        //throw new ShipmentErrorMessage("ShipmentErrorMessage...");
    }

    /* (non-Javadoc)
     * @see com.meritconinc.shiplinx.carrier.ups.ws.ship.proxy.ShipPortType#processShipAccept(com.meritconinc.shiplinx.carrier.ups.ws.ship.proxy.ShipAcceptRequest  body ,)com.meritconinc.shiplinx.carrier.ups.ws.ship.proxy.UPSSecurity  upsSecurity )*
     */
    public com.meritconinc.shiplinx.carrier.ups.ws.ship.proxy.ShipAcceptResponse processShipAccept(ShipAcceptRequest body,UPSSecurity upsSecurity) throws ShipAcceptErrorMessage    { 
        LOG.info("Executing operation processShipAccept");
        System.out.println(body);
        System.out.println(upsSecurity);
        try {
            com.meritconinc.shiplinx.carrier.ups.ws.ship.proxy.ShipAcceptResponse _return = null;
            return _return;
        } catch (java.lang.Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
        //throw new ShipAcceptErrorMessage("ShipAcceptErrorMessage...");
    }

    /* (non-Javadoc)
     * @see com.meritconinc.shiplinx.carrier.ups.ws.ship.proxy.ShipPortType#processShipConfirm(com.meritconinc.shiplinx.carrier.ups.ws.ship.proxy.ShipConfirmRequest  body ,)com.meritconinc.shiplinx.carrier.ups.ws.ship.proxy.UPSSecurity  upsSecurity )*
     */
    public com.meritconinc.shiplinx.carrier.ups.ws.ship.proxy.ShipConfirmResponse processShipConfirm(ShipConfirmRequest body,UPSSecurity upsSecurity) throws ShipConfirmErrorMessage    { 
        LOG.info("Executing operation processShipConfirm");
        System.out.println(body);
        System.out.println(upsSecurity);
        try {
            com.meritconinc.shiplinx.carrier.ups.ws.ship.proxy.ShipConfirmResponse _return = null;
            return _return;
        } catch (java.lang.Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
        //throw new ShipConfirmErrorMessage("ShipConfirmErrorMessage...");
    }

}
