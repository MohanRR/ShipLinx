
package com.meritconinc.shiplinx.carrier.purolator.ws.pickup.proxy;

import javax.xml.ws.Endpoint;

/**
 * This class was generated by Apache CXF 2.5.2
 * 2012-07-13T18:43:59.912-04:00
 * Generated source version: 2.5.2
 * 
 */
 
public class PickUpServiceContract_PickUpServiceEndpoint_Server{

    protected PickUpServiceContract_PickUpServiceEndpoint_Server() throws java.lang.Exception {
        System.out.println("Starting Server");
        Object implementor = new PickUpServiceContractImpl();
        String address = "http://iptv1530ws:8009/PWS/V1/Pickup/PickUpService.asmx";
        Endpoint.publish(address, implementor);
    }
    
    public static void main(String args[]) throws java.lang.Exception { 
        new PickUpServiceContract_PickUpServiceEndpoint_Server();
        System.out.println("Server ready..."); 
        
        Thread.sleep(5 * 60 * 1000); 
        System.out.println("Server exiting");
        System.exit(0);
    }
}
