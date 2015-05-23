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

    private Integer intVal;
    private String strVal;
    private final Double doubleVal = Math.PI;

    public SerializableTestClass() {
	this(42, "Hello World!");
    }

    public SerializableTestClass(Integer intVal, String strVal) {
	this.intVal = intVal;
	this.strVal = strVal;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((intVal == null) ? 0 : intVal.hashCode());
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
	if (intVal == null) {
	    if (other.intVal != null) {
		return false;
	    }
	} else if (!intVal.equals(other.intVal)) {
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

    @Override
    public String toString() {
	return "SerializableTestClass [intVal=" + intVal + ", strVal=" + strVal
		+ ", doubleVal=" + doubleVal + "]";
    }

    public Integer getIntVal() {
	return intVal;
    }

    public void setIntVal(Integer intVal) {
	this.intVal = intVal;
    }

    public String getStrVal() {
	return strVal;
    }

    public void setStrVal(String strVal) {
	this.strVal = strVal;
    }

    public Double getDoubleVal() {
	return doubleVal;
    }

}
