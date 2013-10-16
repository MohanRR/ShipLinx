
package com.meritconinc.shiplinx.carrier.ups.ws.track.proxy;

import javax.xml.ws.Endpoint;

/**
 * This class was generated by Apache CXF 2.5.0
 * 2011-12-08T22:13:47.872-05:00
 * Generated source version: 2.5.0
 * 
 */
 
public class TrackPortType_TrackPort_Server{

    protected TrackPortType_TrackPort_Server() throws java.lang.Exception {
        System.out.println("Starting Server");
        Object implementor = new TrackPortTypeImpl();
        String address = "https://wwwcie.ups.com/webservices/Track";
        Endpoint.publish(address, implementor);
    }
    
    public static void main(String args[]) throws java.lang.Exception { 
        new TrackPortType_TrackPort_Server();
        System.out.println("Server ready..."); 
        
        Thread.sleep(5 * 60 * 1000); 
        System.out.println("Server exiting");
        System.exit(0);
    }
}
