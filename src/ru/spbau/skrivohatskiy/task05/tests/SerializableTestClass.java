/**
 * 
 */
package ru.spbau.skrivohatskiy.task05.tests;

/**
 * @author Sergey Krivohatskiy
 *
 */
@SuppressWarnings("javadoc")
public class SerializableTestClass {

    private String strVal;
    private long longVal = 123;
    private int intVal;
    private short shortVal = 7;
    private byte byteVal = 12;
    private char charVal = '0';
    private boolean booleanVal = false;
    private float floatVal = 1.23f;
    private double doubleVal = 1.24;

    public SerializableTestClass() {
	this(42, "Hello World!");
    }

    public SerializableTestClass(int intVal, String strVal) {
	this.intVal = intVal;
	this.strVal = strVal;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + (booleanVal ? 1231 : 1237);
	result = prime * result + byteVal;
	result = prime * result + charVal;
	long temp;
	temp = Double.doubleToLongBits(doubleVal);
	result = prime * result + (int) (temp ^ (temp >>> 32));
	result = prime * result + Float.floatToIntBits(floatVal);
	result = prime * result + intVal;
	result = prime * result + (int) (longVal ^ (longVal >>> 32));
	result = prime * result + shortVal;
	result = prime * result + ((strVal == null) ? 0 : strVal.hashCode());
	return result;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj) {
	    return true;
	}
	if (obj == null) {
	    return false;
	}
	if (getClass() != obj.getClass()) {
	    return false;
	}
	SerializableTestClass other = (SerializableTestClass) obj;
	if (booleanVal != other.booleanVal) {
	    return false;
	}
	if (byteVal != other.byteVal) {
	    return false;
	}
	if (charVal != other.charVal) {
	    return false;
	}
	if (Double.doubleToLongBits(doubleVal) != Double
		.doubleToLongBits(other.doubleVal)) {
	    return false;
	}
	if (Float.floatToIntBits(floatVal) != Float
		.floatToIntBits(other.floatVal)) {
	    return false;
	}
	if (intVal != other.intVal) {
	    return false;
	}
	if (longVal != other.longVal) {
	    return false;
	}
	if (shortVal != other.shortVal) {
	    return false;
	}
	if (strVal == null) {
	    if (other.strVal != null) {
		return false;
	    }
	} else if (!strVal.equals(other.strVal)) {
	    return false;
	}
	return true;
    }

    public String getStrVal() {
	return strVal;
    }

    public void setStrVal(String strVal) {
	this.strVal = strVal;
    }

    public long getLongVal() {
	return longVal;
    }

    public void setLongVal(long longVal) {
	this.longVal = longVal;
    }

    public int getIntVal() {
	return intVal;
    }

    public void setIntVal(int intVal) {
	this.intVal = intVal;
    }

    public short getShortVal() {
	return shortVal;
    }

    public void setShortVal(short shortVal) {
	this.shortVal = shortVal;
    }

    public byte getByteVal() {
	return byteVal;
    }

    public void setByteVal(byte byteVal) {
	this.byteVal = byteVal;
    }

    public char getCharVal() {
	return charVal;
    }

    public void setCharVal(char charVal) {
	this.charVal = charVal;
    }

    public boolean getBooleanVal() {
	return booleanVal;
    }

    public void setBooleanVal(boolean booleanVal) {
	this.booleanVal = booleanVal;
    }

    public float getFloatVal() {
	return floatVal;
    }

    public void setFloatVal(float floatVal) {
	this.floatVal = floatVal;
    }

    public double getDoubleVal() {
	return doubleVal;
    }

    public void setDoubleVal(double doubleVal) {
	this.doubleVal = doubleVal;
    }
}
