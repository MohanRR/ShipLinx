//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.02.01 at 06:34:59 PM EST 
//


package com.meritconinc.shiplinx.carrier.dhl.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SrvCombType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SrvCombType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="GlobalServiceName" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;length value="45"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="GlobalServiceCode" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;length value="6"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="LocalServiceCode" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;length value="3"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="LocalServiceTypeName" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;length value="45"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="ChargeCodeType" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;length value="3"/>
 *               &lt;enumeration value="FEE"/>
 *               &lt;enumeration value="SCH"/>
 *               &lt;enumeration value="XCH"/>
 *               &lt;enumeration value="NRI"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="SOfferedCustAgreement">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;length value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="SrvComb" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Prod" type="{http://www.dhl.com/DCTResponsedatatypes}ProdType" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SrvCombType", namespace = "http://www.dhl.com/DCTResponsedatatypes", propOrder = {
    "globalServiceName",
    "globalServiceCode",
    "localServiceCode",
    "localServiceTypeName",
    "chargeCodeType",
    "sOfferedCustAgreement",
    "srvComb"
})
public class SrvCombTypeMain {

    @XmlElement(name = "GlobalServiceName")
    protected String globalServiceName;
    @XmlElement(name = "GlobalServiceCode")
    protected String globalServiceCode;
    @XmlElement(name = "LocalServiceCode")
    protected String localServiceCode;
    @XmlElement(name = "LocalServiceTypeName")
    protected String localServiceTypeName;
    @XmlElement(name = "ChargeCodeType")
    protected String chargeCodeType;
    @XmlElement(name = "SOfferedCustAgreement", required = true)
    protected String sOfferedCustAgreement;
    @XmlElement(name = "SrvComb")
    protected SrvCombTypeMain.SrvComb srvComb;

    /**
     * Gets the value of the globalServiceName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGlobalServiceName() {
        return globalServiceName;
    }

    /**
     * Sets the value of the globalServiceName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGlobalServiceName(String value) {
        this.globalServiceName = value;
    }

    /**
     * Gets the value of the globalServiceCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGlobalServiceCode() {
        return globalServiceCode;
    }

    /**
     * Sets the value of the globalServiceCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGlobalServiceCode(String value) {
        this.globalServiceCode = value;
    }

    /**
     * Gets the value of the localServiceCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLocalServiceCode() {
        return localServiceCode;
    }

    /**
     * Sets the value of the localServiceCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLocalServiceCode(String value) {
        this.localServiceCode = value;
    }

    /**
     * Gets the value of the localServiceTypeName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLocalServiceTypeName() {
        return localServiceTypeName;
    }

    /**
     * Sets the value of the localServiceTypeName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLocalServiceTypeName(String value) {
        this.localServiceTypeName = value;
    }

    /**
     * Gets the value of the chargeCodeType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getChargeCodeType() {
        return chargeCodeType;
    }

    /**
     * Sets the value of the chargeCodeType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setChargeCodeType(String value) {
        this.chargeCodeType = value;
    }

    /**
     * Gets the value of the sOfferedCustAgreement property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSOfferedCustAgreement() {
        return sOfferedCustAgreement;
    }

    /**
     * Sets the value of the sOfferedCustAgreement property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSOfferedCustAgreement(String value) {
        this.sOfferedCustAgreement = value;
    }

    /**
     * Gets the value of the srvComb property.
     * 
     * @return
     *     possible object is
     *     {@link SrvCombTypeMain.SrvComb }
     *     
     */
    public SrvCombTypeMain.SrvComb getSrvComb() {
        return srvComb;
    }

    /**
     * Sets the value of the srvComb property.
     * 
     * @param value
     *     allowed object is
     *     {@link SrvCombTypeMain.SrvComb }
     *     
     */
    public void setSrvComb(SrvCombTypeMain.SrvComb value) {
        this.srvComb = value;
    }


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
     *         &lt;element name="Prod" type="{http://www.dhl.com/DCTResponsedatatypes}ProdType" minOccurs="0"/>
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
        "prod"
    })
    public static class SrvComb {

        @XmlElement(name = "Prod")
        protected ProdType prod;

        /**
         * Gets the value of the prod property.
         * 
         * @return
         *     possible object is
         *     {@link ProdType }
         *     
         */
        public ProdType getProd() {
            return prod;
        }

        /**
         * Sets the value of the prod property.
         * 
         * @param value
         *     allowed object is
         *     {@link ProdType }
         *     
         */
        public void setProd(ProdType value) {
            this.prod = value;
        }

    }

}
