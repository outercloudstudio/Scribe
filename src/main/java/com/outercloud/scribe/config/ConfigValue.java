package com.outercloud.scribe.config;

public class ConfigValue {
    public static enum ValueType{
        UNKNOWN,
        NUMBER,
        GROUP,
        STRING,
        BOOL,
        ARRAY
    }

    public Object value;
    public ValueType valueType = ValueType.UNKNOWN;

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

    public Number GetNumberValue(){
        if(valueType != ValueType.NUMBER) return 0;

        return (Number)value;
    }

    public ConfigGroup GetGroupValue(){
        if(valueType != ValueType.GROUP) return null;

        return (ConfigGroup)value;
    }

    public String GetStringValue(){
        if(valueType != ValueType.STRING) return "";

        return (String)value;
    }

    public Boolean GetBoolValue(){
        if(valueType != ValueType.BOOL) return false;

        return (Boolean)value;
    }

    public ConfigGroup GetArrayValue(){
        if(valueType != ValueType.ARRAY) return null;

        return (ConfigGroup) value;
    }
}
