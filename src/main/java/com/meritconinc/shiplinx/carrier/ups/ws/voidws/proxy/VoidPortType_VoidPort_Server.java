
package com.meritconinc.shiplinx.carrier.ups.ws.voidws.proxy;

import javax.xml.ws.Endpoint;

/**
 * This class was generated by Apache CXF 2.5.0
 * 2011-12-10T08:24:48.698-05:00
 * Generated source version: 2.5.0
 * 
 */
 
public class VoidPortType_VoidPort_Server{

    protected VoidPortType_VoidPort_Server() throws java.lang.Exception {
        System.out.println("Starting Server");
        Object implementor = new VoidPortTypeImpl();
        String address = "https://wwwcie.ups.com/webservices/Void";
        Endpoint.publish(address, implementor);
    }
    
    public static void main(String args[]) throws java.lang.Exception { 
        new VoidPortType_VoidPort_Server();
        System.out.println("Server ready..."); 
        
        Thread.sleep(5 * 60 * 1000); 
        System.out.println("Server exiting");
        System.exit(0);
    }
}
