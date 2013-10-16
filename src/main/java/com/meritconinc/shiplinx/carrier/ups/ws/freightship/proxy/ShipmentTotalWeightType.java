
package com.meritconinc.shiplinx.carrier.ups.ws.freightship.proxy;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ShipmentTotalWeightType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ShipmentTotalWeightType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Value" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="UnitOfMeasurement" type="{http://www.ups.com/XMLSchema/XOLTWS/FreightShip/v1.0}FreightShipUnitOfMeasurementType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ShipmentTotalWeightType", propOrder = {
    "value",
    "unitOfMeasurement"
})
public class ShipmentTotalWeightType {

    @XmlElement(name = "Value", required = true)
    protected String value;
    @XmlElement(name = "UnitOfMeasurement", required = true)
    protected FreightShipUnitOfMeasurementType unitOfMeasurement;

    /**
     * Gets the value of the value property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the value of the value property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Gets the value of the unitOfMeasurement property.
     * 
     * @return
     *     possible object is
     *     {@link FreightShipUnitOfMeasurementType }
     *     
     */
    public FreightShipUnitOfMeasurementType getUnitOfMeasurement() {
        return unitOfMeasurement;
    }

    /**
     * Sets the value of the unitOfMeasurement property.
     * 
     * @param value
     *     allowed object is
     *     {@link FreightShipUnitOfMeasurementType }
     *     
     */
    public void setUnitOfMeasurement(FreightShipUnitOfMeasurementType value) {
        this.unitOfMeasurement = value;
    }

}
