
package com.meritconinc.shiplinx.carrier.purolator.ws.shipping.proxy;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for BusinessRelationship.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="BusinessRelationship">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Related"/>
 *     &lt;enumeration value="NotRelated"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "BusinessRelationship")
@XmlEnum
public enum BusinessRelationship {


    /**
     * Related
     * 
     */
    @XmlEnumValue("Related")
    RELATED("Related"),

    /**
     * NotRelated
     * 
     */
    @XmlEnumValue("NotRelated")
    NOT_RELATED("NotRelated");
    private final String value;

    BusinessRelationship(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static BusinessRelationship fromValue(String v) {
        for (BusinessRelationship c: BusinessRelationship.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
