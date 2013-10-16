package com.meritconinc.shiplinx.carrier.ups.ws.pickup.proxy;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.Service;

/**
 * This class was generated by Apache CXF 2.5.0
 * 2011-12-08T21:31:27.898-05:00
 * Generated source version: 2.5.0
 * 
 */
@WebServiceClient(name = "PickupService", 
                  wsdlLocation = "Pickup.wsdl",
                  targetNamespace = "http://www.ups.com/WSDL/XOLTWS/Pickup/v1.1") 
public class PickupService extends Service {

    public final static URL WSDL_LOCATION;

    public final static QName SERVICE = new QName("http://www.ups.com/WSDL/XOLTWS/Pickup/v1.1", "PickupService");
    public final static QName PickupPort = new QName("http://www.ups.com/WSDL/XOLTWS/Pickup/v1.1", "PickupPort");
    static {
        URL url = null;
        try {
            url = new URL("Pickup.wsdl");
        } catch (MalformedURLException e) {
            java.util.logging.Logger.getLogger(PickupService.class.getName())
                .log(java.util.logging.Level.INFO, 
                     "Can not initialize the default wsdl from {0}", "Pickup.wsdl");
        }
        WSDL_LOCATION = url;
    }

    public PickupService(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public PickupService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public PickupService() {
        super(WSDL_LOCATION, SERVICE);
    }
    

    /**
     *
     * @return
     *     returns PickupPortType
     */
    @WebEndpoint(name = "PickupPort")
    public PickupPortType getPickupPort() {
        return super.getPort(PickupPort, PickupPortType.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns PickupPortType
     */
    @WebEndpoint(name = "PickupPort")
    public PickupPortType getPickupPort(WebServiceFeature... features) {
        return super.getPort(PickupPort, PickupPortType.class, features);
    }

}
