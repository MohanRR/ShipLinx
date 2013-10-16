
package com.meritconinc.shiplinx.ws.proxy.datatypes;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RatingWSType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RatingWSType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="BillWeight" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="CarrierName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Charges" type="{http://www.proxy.ws.shiplinx.meritconinc.com/datatypes}ChargesWSType" minOccurs="0"/>
 *         &lt;element name="Currency" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ModeTransport" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Service" type="{http://www.proxy.ws.shiplinx.meritconinc.com/datatypes}ServiceWSType"/>
 *         &lt;element name="TariffRate" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 *         &lt;element name="Total" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="TransitDays" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RatingWSType", propOrder = {
    "billWeight",
    "carrierName",
    "charges",
    "currency",
    "modeTransport",
    "service",
    "tariffRate",
    "total",
    "transitDays"
})
public class RatingWSType {

    @XmlElement(name = "BillWeight")
    protected double billWeight;
    @XmlElement(name = "CarrierName")
    protected String carrierName;
    @XmlElement(name = "Charges")
    protected ChargesWSType charges;
    @XmlElement(name = "Currency")
    protected String currency;
    @XmlElement(name = "ModeTransport")
    protected String modeTransport;
    @XmlElement(name = "Service", required = true)
    protected ServiceWSType service;
    @XmlElement(name = "TariffRate")
    protected Double tariffRate;
    @XmlElement(name = "Total")
    protected double total;
    @XmlElement(name = "TransitDays")
    protected int transitDays;

    /**
     * Gets the value of the billWeight property.
     * 
     */
    public double getBillWeight() {
        return billWeight;
    }

    /**
     * Sets the value of the billWeight property.
     * 
     */
    public void setBillWeight(double value) {
        this.billWeight = value;
    }

    /**
     * Gets the value of the carrierName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCarrierName() {
        return carrierName;
    }

    /**
     * Sets the value of the carrierName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCarrierName(String value) {
        this.carrierName = value;
    }

    /**
     * Gets the value of the charges property.
     * 
     * @return
     *     possible object is
     *     {@link ChargesWSType }
     *     
     */
    public ChargesWSType getCharges() {
        return charges;
    }

    /**
     * Sets the value of the charges property.
     * 
     * @param value
     *     allowed object is
     *     {@link ChargesWSType }
     *     
     */
    public void setCharges(ChargesWSType value) {
        this.charges = value;
    }

    /**
     * Gets the value of the currency property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * Sets the value of the currency property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCurrency(String value) {
        this.currency = value;
    }

    /**
     * Gets the value of the modeTransport property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getModeTransport() {
        return modeTransport;
    }

    /**
     * Sets the value of the modeTransport property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setModeTransport(String value) {
        this.modeTransport = value;
    }

    /**
     * Gets the value of the service property.
     * 
     * @return
     *     possible object is
     *     {@link ServiceWSType }
     *     
     */
    public ServiceWSType getService() {
        return service;
    }

    /**
     * Sets the value of the service property.
     * 
     * @param value
     *     allowed object is
     *     {@link ServiceWSType }
     *     
     */
    public void setService(ServiceWSType value) {
        this.service = value;
    }

    /**
     * Gets the value of the tariffRate property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getTariffRate() {
        return tariffRate;
    }

    /**
     * Sets the value of the tariffRate property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setTariffRate(Double value) {
        this.tariffRate = value;
    }

    /**
     * Gets the value of the total property.
     * 
     */
    public double getTotal() {
        return total;
    }

    /**
     * Sets the value of the total property.
     * 
     */
    public void setTotal(double value) {
        this.total = value;
    }

    /**
     * Gets the value of the transitDays property.
     * 
     */
    public int getTransitDays() {
        return transitDays;
    }

    /**
     * Sets the value of the transitDays property.
     * 
     */
    public void setTransitDays(int value) {
        this.transitDays = value;
    }

}
