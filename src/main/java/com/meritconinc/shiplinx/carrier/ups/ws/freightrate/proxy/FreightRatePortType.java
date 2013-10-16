package com.meritconinc.shiplinx.carrier.ups.ws.freightrate.proxy;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * This class was generated by Apache CXF 2.5.0
 * 2011-12-08T21:34:08.995-05:00
 * Generated source version: 2.5.0
 * 
 */
@WebService(targetNamespace = "http://www.ups.com/WSDL/XOLTWS/FreightRate/v1.0", name = "FreightRatePortType")
@XmlSeeAlso({ObjectFactory.class})
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
public interface FreightRatePortType {

    @WebResult(name = "FreightRateResponse", targetNamespace = "http://www.ups.com/XMLSchema/XOLTWS/FreightRate/v1.0", partName = "Body")
    @WebMethod(operationName = "ProcessFreightRate", action = "http://onlinetools.ups.com/webservices/FreightRateBinding/v1.0")
    public FreightRateResponse processFreightRate(
        @WebParam(partName = "Body", name = "FreightRateRequest", targetNamespace = "http://www.ups.com/XMLSchema/XOLTWS/FreightRate/v1.0")
        FreightRateRequest body,
        @WebParam(partName = "UPSSecurity", name = "UPSSecurity", targetNamespace = "http://www.ups.com/XMLSchema/XOLTWS/UPSS/v1.0", header = true)
        UPSSecurity upsSecurity
    ) throws RateErrorMessage;
}
