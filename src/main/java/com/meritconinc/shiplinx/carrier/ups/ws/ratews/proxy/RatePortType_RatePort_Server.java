
package com.meritconinc.shiplinx.carrier.ups.ws.ratews.proxy;

import javax.xml.ws.Endpoint;

/**
 * This class was generated by Apache CXF 2.5.0
 * 2011-12-10T07:51:11.493-05:00
 * Generated source version: 2.5.0
 * 
 */
 
public class RatePortType_RatePort_Server{

    protected RatePortType_RatePort_Server() throws java.lang.Exception {
        System.out.println("Starting Server");
        Object implementor = new RatePortTypeImpl();
        String address = "https://wwwcie.ups.com/webservices/Rate";
        Endpoint.publish(address, implementor);
    }
    
    public static void main(String args[]) throws java.lang.Exception { 
        new RatePortType_RatePort_Server();
        System.out.println("Server ready..."); 
        
        Thread.sleep(5 * 60 * 1000); 
        System.out.println("Server exiting");
        System.exit(0);
    }
}
