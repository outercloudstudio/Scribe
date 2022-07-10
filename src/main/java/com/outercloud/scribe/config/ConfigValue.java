package com.outercloud.scribe.config;

public class ConfigValue {
    public enum ValueType{
        UNKNOWN,
        NUMBER,
        GROUP,
        STRING,
        BOOL,
        ARRAY
    }

    public Object value;
    public ValueType valueType;

    public ConfigValue(Number value){
        this.value = value;
        this.valueType = ValueType.NUMBER;
    }

    public ConfigValue(ConfigGroup value){
        this.value = value;

        if (value.isArray) {
            this.valueType = ValueType.ARRAY;
        }else {
            this.valueType = ValueType.GROUP;
        }
    }

    public ConfigValue(String value){
        this.value = value;
        this.valueType = ValueType.STRING;
    }

    public ConfigValue(Boolean value){
        this.value = value;
        this.valueType = ValueType.BOOL;
    }

    public Number getNumberValue(){
        if(valueType != ValueType.NUMBER) return 0;

        return (Number)value;
    }

    public ConfigGroup getGroupValue(){
        if(valueType != ValueType.GROUP) return null;

        return (ConfigGroup)value;
    }

    public String getStringValue(){
        if(valueType != ValueType.STRING) return "";

        return (String)value;
    }

    public Boolean getBoolValue(){
        if(valueType != ValueType.BOOL) return false;

        return (Boolean)value;
    }

    public ConfigGroup getArrayValue(){
        if(valueType != ValueType.ARRAY) return null;

        return (ConfigGroup) value;
    }
}
