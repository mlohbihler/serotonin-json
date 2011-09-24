package com.serotonin.json.type;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Extends JsonValue to represent a number.
 * 
 * @author Matthew Lohbihler
 */
public class JsonNumber extends JsonValue {
    private String value;

    public JsonNumber(byte value) {
        this.value = Byte.toString(value);
    }

    public JsonNumber(short value) {
        this.value = Short.toString(value);
    }

    public JsonNumber(int value) {
        this.value = Integer.toString(value);
    }

    public JsonNumber(long value) {
        this.value = Long.toString(value);
    }

    public JsonNumber(float value) {
        this.value = Float.toString(value);
    }

    public JsonNumber(double value) {
        this.value = Double.toString(value);
    }

    public JsonNumber(Number value) {
        if (value == null)
            this.value = "0";
        else
            this.value = value.toString();
    }

    public JsonNumber(String value) throws NumberFormatException {
        // Test the value.
        Double.parseDouble(value);
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Byte getByteValue() {
        return Byte.parseByte(value);
    }

    public Short getShortValue() {
        return Short.parseShort(value);
    }

    public Integer getIntValue() {
        return Integer.parseInt(value);
    }

    public Long getLongValue() {
        return Long.parseLong(value);
    }

    public Float getFloatValue() {
        return Float.parseFloat(value);
    }

    public Double getDoubleValue() {
        return Double.parseDouble(value);
    }

    public BigInteger getBigInteger() {
        return new BigInteger(value);
    }

    public BigDecimal getBigDecimal() {
        return new BigDecimal(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
