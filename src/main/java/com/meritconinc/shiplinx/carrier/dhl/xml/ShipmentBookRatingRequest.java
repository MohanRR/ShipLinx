//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.02.01 at 06:34:59 PM EST 
//


package com.meritconinc.shiplinx.carrier.dhl.xml;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Request" type="{http://www.dhl.com/datatypes}Request"/>
 *         &lt;element name="Shipper" type="{http://www.dhl.com/ratingdatatypes}RatingShipper"/>
 *         &lt;element name="Consignee" type="{http://www.dhl.com/ratingdatatypes}RatingConsignee"/>
 *         &lt;element name="ShipmentDetails" type="{http://www.dhl.com/ratingdatatypes}RatingShipmentDetails"/>
 *         &lt;element name="SpecialService" type="{http://www.dhl.com/ratingdatatypes}RatingSpecialService" maxOccurs="6" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "request",
    "shipper",
    "consignee",
    "shipmentDetails",
    "specialService"
})
@XmlRootElement(name = "ShipmentBookRatingRequest", namespace = "http://www.dhl.com")
public class ShipmentBookRatingRequest {

    @XmlElement(name = "Request", required = true)
    protected Request request;
    @XmlElement(name = "Shipper", required = true)
    protected RatingShipper shipper;
    @XmlElement(name = "Consignee", required = true)
    protected RatingConsignee consignee;
    @XmlElement(name = "ShipmentDetails", required = true)
    protected RatingShipmentDetails shipmentDetails;
    @XmlElement(name = "SpecialService")
    protected List<RatingSpecialService> specialService;

    /**
     * Gets the value of the request property.
     * 
     * @return
     *     possible object is
     *     {@link Request }
     *     
     */
    public Request getRequest() {
        return request;
    }

    /**
     * Sets the value of the request property.
     * 
     * @param value
     *     allowed object is
     *     {@link Request }
     *     
     */
    public void setRequest(Request value) {
        this.request = value;
    }

    /**
     * Gets the value of the shipper property.
     * 
     * @return
     *     possible object is
     *     {@link RatingShipper }
     *     
     */
    public RatingShipper getShipper() {
        return shipper;
    }

    /**
     * Sets the value of the shipper property.
     * 
     * @param value
     *     allowed object is
     *     {@link RatingShipper }
     *     
     */
    public void setShipper(RatingShipper value) {
        this.shipper = value;
    }

    /**
     * Gets the value of the consignee property.
     * 
     * @return
     *     possible object is
     *     {@link RatingConsignee }
     *     
     */
    public RatingConsignee getConsignee() {
        return consignee;
    }

    /**
     * Sets the value of the consignee property.
     * 
     * @param value
     *     allowed object is
     *     {@link RatingConsignee }
     *     
     */
    public void setConsignee(RatingConsignee value) {
        this.consignee = value;
    }

    /**
     * Gets the value of the shipmentDetails property.
     * 
     * @return
     *     possible object is
     *     {@link RatingShipmentDetails }
     *     
     */
    public RatingShipmentDetails getShipmentDetails() {
        return shipmentDetails;
    }

    /**
     * Sets the value of the shipmentDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link RatingShipmentDetails }
     *     
     */
    public void setShipmentDetails(RatingShipmentDetails value) {
        this.shipmentDetails = value;
    }

    /**
     * Gets the value of the specialService property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the specialService property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSpecialService().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RatingSpecialService }
     * 
     * 
     */
    public List<RatingSpecialService> getSpecialService() {
        if (specialService == null) {
            specialService = new ArrayList<RatingSpecialService>();
        }
        return this.specialService;
    }

}