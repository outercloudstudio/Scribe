package com.outercloud.scribe.config;

public class ConfigValue {
    public static enum ValueType{
        UNKNOWN,
        INT,
        GROUP
    }

    public Object value;
    public ValueType valueType = ValueType.UNKNOWN;

    public ConfigValue(int value){
        this.value = value;
        this.valueType = ValueType.INT;
    }

    public ConfigValue(ConfigGroup value){
        this.value = value;
        this.valueType = ValueType.GROUP;
    }

    public int GetInt(){
        if(valueType != ValueType.INT) return 0;

        return (int)value;
    }

    public ConfigGroup GetGroup(){
        if(valueType != ValueType.GROUP) return null;

        return (ConfigGroup)value;
    }
}
