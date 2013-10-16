package com.meritconinc.shiplinx.carrier.ups.ws.freightpickup.proxy;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.Service;

/**
 * This class was generated by Apache CXF 2.5.0
 * 2011-12-09T23:07:23.636-05:00
 * Generated source version: 2.5.0
 * 
 */
@WebServiceClient(name = "FreightPickupService", 
                  wsdlLocation = "FreightPickup.wsdl",
                  targetNamespace = "http://www.ups.com/WSDL/XOLTWS/FreightPickup/v1.0") 
public class FreightPickupService extends Service {

    public final static URL WSDL_LOCATION;

    public final static QName SERVICE = new QName("http://www.ups.com/WSDL/XOLTWS/FreightPickup/v1.0", "FreightPickupService");
    public final static QName FreightPickupPort = new QName("http://www.ups.com/WSDL/XOLTWS/FreightPickup/v1.0", "FreightPickupPort");
    static {
        URL url = null;
        try {
            url = new URL("FreightPickup.wsdl");
        } catch (MalformedURLException e) {
            java.util.logging.Logger.getLogger(FreightPickupService.class.getName())
                .log(java.util.logging.Level.INFO, 
                     "Can not initialize the default wsdl from {0}", "FreightPickup.wsdl");
        }
        WSDL_LOCATION = url;
    }

    public FreightPickupService(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public FreightPickupService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public FreightPickupService() {
        super(WSDL_LOCATION, SERVICE);
    }
    

    /**
     *
     * @return
     *     returns FreightPickupPortType
     */
    @WebEndpoint(name = "FreightPickupPort")
    public FreightPickupPortType getFreightPickupPort() {
        return super.getPort(FreightPickupPort, FreightPickupPortType.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns FreightPickupPortType
     */
    @WebEndpoint(name = "FreightPickupPort")
    public FreightPickupPortType getFreightPickupPort(WebServiceFeature... features) {
        return super.getPort(FreightPickupPort, FreightPickupPortType.class, features);
    }

}
