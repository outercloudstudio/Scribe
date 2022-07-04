package com.outercloud.scribe.config;

public class ConfigValue {
    public static enum ValueType{
        UNKNOWN,
        NUMBER,
        GROUP
    }

    public Object value;
    public ValueType valueType = ValueType.UNKNOWN;

    public ConfigValue(Number value){
        this.value = value;
        this.valueType = ValueType.NUMBER;
    }

    public ConfigValue(ConfigGroup value){
        this.value = value;
        this.valueType = ValueType.GROUP;
    }

    public Number GetNumberValue(){
        if(valueType != ValueType.NUMBER) return 0;

        return (Number)value;
    }

    public ConfigGroup GetGroupValue(){
        if(valueType != ValueType.GROUP) return null;

        return (ConfigGroup)value;
    }
}
