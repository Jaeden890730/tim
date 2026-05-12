package com.mxic.oiplus.util;

import java.util.Vector;

public class AttributeObject {
  Vector value;
  String name;

  public AttributeObject(String aName, String aValue) {
    value = new Vector();
    name = "";
    name = aName;
    addValue(aValue);
  }

  public AttributeObject(String aName, String values[]) {
    value = new Vector();
    name = "";
    name = aName;
    for (int i = 0; i < values.length; i++)
      addValue(values[i]);
  }

  public void addValue(String aValue) {
    value.add(aValue);
  }

  public String getValue() {
    return (String) value.get(0);
  }

  public boolean isMultiple() {
    return value.size() > 1;
  }

  public String getName() {
    return name;
  }

  public Vector getValuesVector() {
    return value;
  }

  public String[] getValues() {
    Object v[] = value.toArray();
    String stringArray[] = new String[v.length];

    for (int i = 0; i < v.length; i++)
      stringArray[i] = (String) v[i];

    return stringArray;
  }
}
