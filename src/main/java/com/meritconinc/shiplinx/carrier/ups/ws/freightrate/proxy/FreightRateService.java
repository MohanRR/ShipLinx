package com.meritconinc.shiplinx.carrier.ups.ws.freightrate.proxy;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.Service;

/**
 * This class was generated by Apache CXF 2.5.0
 * 2011-12-08T21:34:09.009-05:00
 * Generated source version: 2.5.0
 * 
 */
@WebServiceClient(name = "FreightRateService", 
                  wsdlLocation = "FreightRate.wsdl",
                  targetNamespace = "http://www.ups.com/WSDL/XOLTWS/FreightRate/v1.0") 
public class FreightRateService extends Service {

    public final static URL WSDL_LOCATION;

    public final static QName SERVICE = new QName("http://www.ups.com/WSDL/XOLTWS/FreightRate/v1.0", "FreightRateService");
    public final static QName FreightRatePort = new QName("http://www.ups.com/WSDL/XOLTWS/FreightRate/v1.0", "FreightRatePort");
    static {
        URL url = null;
        try {
            url = new URL("FreightRate.wsdl");
        } catch (MalformedURLException e) {
            java.util.logging.Logger.getLogger(FreightRateService.class.getName())
                .log(java.util.logging.Level.INFO, 
                     "Can not initialize the default wsdl from {0}", "FreightRate.wsdl");
        }
        WSDL_LOCATION = url;
    }

    public FreightRateService(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public FreightRateService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public FreightRateService() {
        super(WSDL_LOCATION, SERVICE);
    }
    

    /**
     *
     * @return
     *     returns FreightRatePortType
     */
    @WebEndpoint(name = "FreightRatePort")
    public FreightRatePortType getFreightRatePort() {
        return super.getPort(FreightRatePort, FreightRatePortType.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns FreightRatePortType
     */
    @WebEndpoint(name = "FreightRatePort")
    public FreightRatePortType getFreightRatePort(WebServiceFeature... features) {
        return super.getPort(FreightRatePort, FreightRatePortType.class, features);
    }

}