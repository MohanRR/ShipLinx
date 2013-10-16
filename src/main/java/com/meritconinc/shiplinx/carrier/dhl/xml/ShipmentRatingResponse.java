//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.02.01 at 06:34:59 PM EST 
//


package com.meritconinc.shiplinx.carrier.dhl.xml;

import java.math.BigDecimal;
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
 *         &lt;element name="Response" type="{http://www.dhl.com/datatypes}Response"/>
 *         &lt;element name="Note" type="{http://www.dhl.com/datatypes}Note"/>
 *         &lt;element name="Rated" type="{http://www.dhl.com/datatypes}YesNo"/>
 *         &lt;element name="ShippingCharge" type="{http://www.dhl.com/datatypes}Money" minOccurs="0"/>
 *         &lt;element name="SaturdayDeliveryCharge" type="{http://www.dhl.com/datatypes}Money" minOccurs="0"/>
 *         &lt;element name="ProofOfDeliveryCharge" type="{http://www.dhl.com/datatypes}Money" minOccurs="0"/>
 *         &lt;element name="DutyPayCharge" type="{http://www.dhl.com/datatypes}Money" minOccurs="0"/>
 *         &lt;element name="OnForwardCharge" type="{http://www.dhl.com/datatypes}Money" minOccurs="0"/>
 *         &lt;element name="InsuranceCharge" type="{http://www.dhl.com/datatypes}Money" minOccurs="0"/>
 *         &lt;element name="PackageCharge" type="{http://www.dhl.com/datatypes}Money" minOccurs="0"/>
 *         &lt;element name="ChargeableWeight" type="{http://www.dhl.com/datatypes}Weight" minOccurs="0"/>
 *         &lt;element name="DimensionalWeight" type="{http://www.dhl.com/datatypes}Weight" minOccurs="0"/>
 *         &lt;element name="OriginServiceArea" type="{http://www.dhl.com/ratingdatatypes}RatingServiceArea"/>
 *         &lt;element name="DestinationServiceArea" type="{http://www.dhl.com/ratingdatatypes}RatingServiceArea"/>
 *         &lt;element name="CurrencyCode" type="{http://www.dhl.com/datatypes}CurrencyCode"/>
 *         &lt;element name="WeightUnit" type="{http://www.dhl.com/datatypes}WeightUnit"/>
 *         &lt;element name="CountryCode" type="{http://www.dhl.com/datatypes}CountryCode"/>
 *         &lt;element name="Surcharge" type="{http://www.dhl.com/datatypes}Money" minOccurs="0"/>
 *         &lt;element name="ZoneID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    "response",
    "note",
    "rated",
    "shippingCharge",
    "saturdayDeliveryCharge",
    "proofOfDeliveryCharge",
    "dutyPayCharge",
    "onForwardCharge",
    "insuranceCharge",
    "packageCharge",
    "chargeableWeight",
    "dimensionalWeight",
    "originServiceArea",
    "destinationServiceArea",
    "currencyCode",
    "weightUnit",
    "countryCode",
    "surcharge",
    "zoneID"
})
@XmlRootElement(name = "ShipmentRatingResponse", namespace = "http://www.dhl.com")
public class ShipmentRatingResponse {

    @XmlElement(name = "Response", required = true)
    protected Response response;
    @XmlElement(name = "Note", required = true)
    protected Note note;
    @XmlElement(name = "Rated", required = true)
    protected YesNo rated;
    @XmlElement(name = "ShippingCharge")
    protected Float shippingCharge;
    @XmlElement(name = "SaturdayDeliveryCharge")
    protected Float saturdayDeliveryCharge;
    @XmlElement(name = "ProofOfDeliveryCharge")
    protected Float proofOfDeliveryCharge;
    @XmlElement(name = "DutyPayCharge")
    protected Float dutyPayCharge;
    @XmlElement(name = "OnForwardCharge")
    protected Float onForwardCharge;
    @XmlElement(name = "InsuranceCharge")
    protected Float insuranceCharge;
    @XmlElement(name = "PackageCharge")
    protected Float packageCharge;
    @XmlElement(name = "ChargeableWeight")
    protected BigDecimal chargeableWeight;
    @XmlElement(name = "DimensionalWeight")
    protected BigDecimal dimensionalWeight;
    @XmlElement(name = "OriginServiceArea", required = true)
    protected RatingServiceArea originServiceArea;
    @XmlElement(name = "DestinationServiceArea", required = true)
    protected RatingServiceArea destinationServiceArea;
    @XmlElement(name = "CurrencyCode", required = true)
    protected String currencyCode;
    @XmlElement(name = "WeightUnit", required = true)
    protected WeightUnit weightUnit;
    @XmlElement(name = "CountryCode", required = true)
    protected String countryCode;
    @XmlElement(name = "Surcharge")
    protected Float surcharge;
    @XmlElement(name = "ZoneID")
    protected String zoneID;

    /**
     * Gets the value of the response property.
     * 
     * @return
     *     possible object is
     *     {@link Response }
     *     
     */
    public Response getResponse() {
        return response;
    }

    /**
     * Sets the value of the response property.
     * 
     * @param value
     *     allowed object is
     *     {@link Response }
     *     
     */
    public void setResponse(Response value) {
        this.response = value;
    }

    /**
     * Gets the value of the note property.
     * 
     * @return
     *     possible object is
     *     {@link Note }
     *     
     */
    public Note getNote() {
        return note;
    }

    /**
     * Sets the value of the note property.
     * 
     * @param value
     *     allowed object is
     *     {@link Note }
     *     
     */
    public void setNote(Note value) {
        this.note = value;
    }

    /**
     * Gets the value of the rated property.
     * 
     * @return
     *     possible object is
     *     {@link YesNo }
     *     
     */
    public YesNo getRated() {
        return rated;
    }

    /**
     * Sets the value of the rated property.
     * 
     * @param value
     *     allowed object is
     *     {@link YesNo }
     *     
     */
    public void setRated(YesNo value) {
        this.rated = value;
    }

    /**
     * Gets the value of the shippingCharge property.
     * 
     * @return
     *     possible object is
     *     {@link Float }
     *     
     */
    public Float getShippingCharge() {
        return shippingCharge;
    }

    /**
     * Sets the value of the shippingCharge property.
     * 
     * @param value
     *     allowed object is
     *     {@link Float }
     *     
     */
    public void setShippingCharge(Float value) {
        this.shippingCharge = value;
    }

    /**
     * Gets the value of the saturdayDeliveryCharge property.
     * 
     * @return
     *     possible object is
     *     {@link Float }
     *     
     */
    public Float getSaturdayDeliveryCharge() {
        return saturdayDeliveryCharge;
    }

    /**
     * Sets the value of the saturdayDeliveryCharge property.
     * 
     * @param value
     *     allowed object is
     *     {@link Float }
     *     
     */
    public void setSaturdayDeliveryCharge(Float value) {
        this.saturdayDeliveryCharge = value;
    }

    /**
     * Gets the value of the proofOfDeliveryCharge property.
     * 
     * @return
     *     possible object is
     *     {@link Float }
     *     
     */
    public Float getProofOfDeliveryCharge() {
        return proofOfDeliveryCharge;
    }

    /**
     * Sets the value of the proofOfDeliveryCharge property.
     * 
     * @param value
     *     allowed object is
     *     {@link Float }
     *     
     */
    public void setProofOfDeliveryCharge(Float value) {
        this.proofOfDeliveryCharge = value;
    }

    /**
     * Gets the value of the dutyPayCharge property.
     * 
     * @return
     *     possible object is
     *     {@link Float }
     *     
     */
    public Float getDutyPayCharge() {
        return dutyPayCharge;
    }

    /**
     * Sets the value of the dutyPayCharge property.
     * 
     * @param value
     *     allowed object is
     *     {@link Float }
     *     
     */
    public void setDutyPayCharge(Float value) {
        this.dutyPayCharge = value;
    }

    /**
     * Gets the value of the onForwardCharge property.
     * 
     * @return
     *     possible object is
     *     {@link Float }
     *     
     */
    public Float getOnForwardCharge() {
        return onForwardCharge;
    }

    /**
     * Sets the value of the onForwardCharge property.
     * 
     * @param value
     *     allowed object is
     *     {@link Float }
     *     
     */
    public void setOnForwardCharge(Float value) {
        this.onForwardCharge = value;
    }

    /**
     * Gets the value of the insuranceCharge property.
     * 
     * @return
     *     possible object is
     *     {@link Float }
     *     
     */
    public Float getInsuranceCharge() {
        return insuranceCharge;
    }

    /**
     * Sets the value of the insuranceCharge property.
     * 
     * @param value
     *     allowed object is
     *     {@link Float }
     *     
     */
    public void setInsuranceCharge(Float value) {
        this.insuranceCharge = value;
    }

    /**
     * Gets the value of the packageCharge property.
     * 
     * @return
     *     possible object is
     *     {@link Float }
     *     
     */
    public Float getPackageCharge() {
        return packageCharge;
    }

    /**
     * Sets the value of the packageCharge property.
     * 
     * @param value
     *     allowed object is
     *     {@link Float }
     *     
     */
    public void setPackageCharge(Float value) {
        this.packageCharge = value;
    }

    /**
     * Gets the value of the chargeableWeight property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getChargeableWeight() {
        return chargeableWeight;
    }

    /**
     * Sets the value of the chargeableWeight property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setChargeableWeight(BigDecimal value) {
        this.chargeableWeight = value;
    }

    /**
     * Gets the value of the dimensionalWeight property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getDimensionalWeight() {
        return dimensionalWeight;
    }

    /**
     * Sets the value of the dimensionalWeight property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setDimensionalWeight(BigDecimal value) {
        this.dimensionalWeight = value;
    }

    /**
     * Gets the value of the originServiceArea property.
     * 
     * @return
     *     possible object is
     *     {@link RatingServiceArea }
     *     
     */
    public RatingServiceArea getOriginServiceArea() {
        return originServiceArea;
    }

    /**
     * Sets the value of the originServiceArea property.
     * 
     * @param value
     *     allowed object is
     *     {@link RatingServiceArea }
     *     
     */
    public void setOriginServiceArea(RatingServiceArea value) {
        this.originServiceArea = value;
    }

    /**
     * Gets the value of the destinationServiceArea property.
     * 
     * @return
     *     possible object is
     *     {@link RatingServiceArea }
     *     
     */
    public RatingServiceArea getDestinationServiceArea() {
        return destinationServiceArea;
    }

    /**
     * Sets the value of the destinationServiceArea property.
     * 
     * @param value
     *     allowed object is
     *     {@link RatingServiceArea }
     *     
     */
    public void setDestinationServiceArea(RatingServiceArea value) {
        this.destinationServiceArea = value;
    }

    /**
     * Gets the value of the currencyCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCurrencyCode() {
        return currencyCode;
    }

    /**
     * Sets the value of the currencyCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCurrencyCode(String value) {
        this.currencyCode = value;
    }

    /**
     * Gets the value of the weightUnit property.
     * 
     * @return
     *     possible object is
     *     {@link WeightUnit }
     *     
     */
    public WeightUnit getWeightUnit() {
        return weightUnit;
    }

    /**
     * Sets the value of the weightUnit property.
     * 
     * @param value
     *     allowed object is
     *     {@link WeightUnit }
     *     
     */
    public void setWeightUnit(WeightUnit value) {
        this.weightUnit = value;
    }

    /**
     * Gets the value of the countryCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCountryCode() {
        return countryCode;
    }

    /**
     * Sets the value of the countryCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCountryCode(String value) {
        this.countryCode = value;
    }

    /**
     * Gets the value of the surcharge property.
     * 
     * @return
     *     possible object is
     *     {@link Float }
     *     
     */
    public Float getSurcharge() {
        return surcharge;
    }

    /**
     * Sets the value of the surcharge property.
     * 
     * @param value
     *     allowed object is
     *     {@link Float }
     *     
     */
    public void setSurcharge(Float value) {
        this.surcharge = value;
    }

    /**
     * Gets the value of the zoneID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getZoneID() {
        return zoneID;
    }

    /**
     * Sets the value of the zoneID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setZoneID(String value) {
        this.zoneID = value;
    }

}
