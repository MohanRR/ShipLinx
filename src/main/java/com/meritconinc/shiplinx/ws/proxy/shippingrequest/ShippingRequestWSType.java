
package com.meritconinc.shiplinx.ws.proxy.shippingrequest;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import com.meritconinc.shiplinx.ws.proxy.datatypes.OrderWSType;


/**
 * <p>Java class for ShippingRequestWSType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ShippingRequestWSType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Order" type="{http://www.proxy.ws.shiplinx.meritconinc.com/datatypes}OrderWSType"/>
 *         &lt;element name="Action" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ShippingRequestWSType", propOrder = {
    "order",
    "action"
})
@XmlRootElement(name = "ShippingRequestWSType", namespace = "http://www.proxy.ws.shiplinx.meritconinc.com")
public class ShippingRequestWSType {

    @XmlElement(name = "Order", required = true)
    protected OrderWSType order;
    @XmlElement(name = "Action")
    protected String action;

    /**
     * Gets the value of the order property.
     * 
     * @return
     *     possible object is
     *     {@link OrderWSType }
     *     
     */
    public OrderWSType getOrder() {
        return order;
    }

    /**
     * Sets the value of the order property.
     * 
     * @param value
     *     allowed object is
     *     {@link OrderWSType }
     *     
     */
    public void setOrder(OrderWSType value) {
        this.order = value;
    }

    /**
     * Gets the value of the action property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAction() {
        return action;
    }

    /**
     * Sets the value of the action property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAction(String value) {
        this.action = value;
    }

}