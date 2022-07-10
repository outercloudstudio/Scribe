package com.outercloud.scribe.config;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ConfigGroup {
    public ConfigGroup parent;
    public Config parentConfig;

    public boolean isArray;
    public int length;

    public Map<String, ConfigValue> values = new HashMap<String, ConfigValue>();

    public ConfigGroup (JsonObject group, ConfigGroup parent){
        this.parent = parent;

        if(group != null) setup(group);
    }

    public ConfigGroup (JsonObject group, Config parentConfig){
        this.parentConfig = parentConfig;

        if(group != null) setup(group);
    }

    public ConfigGroup (JsonArray array, ConfigGroup parent){
        this.parent = parent;

        this.isArray = true;

        if(array != null) setup(array);
    }

    public ConfigGroup (JsonArray array, Config parentConfig){
        this.parentConfig = parentConfig;

        this.isArray = true;

        if(array != null) setup(array);
    }

    private void setup(JsonObject jsonObject){
        Iterator<Map.Entry<String, JsonElement>> entries = jsonObject.entrySet().iterator();

        while (entries.hasNext()){
            Map.Entry<String, JsonElement> entry = entries.next();

            JsonElement value = jsonObject.get(entry.getKey());

            if(value.isJsonObject()){
                values.put(entry.getKey(), new ConfigValue(new ConfigGroup(value.getAsJsonObject(), this)));
            } else if(value.isJsonArray()){
                values.put(entry.getKey(), new ConfigValue(new ConfigGroup(value.getAsJsonArray(), this)));
            } else if(value.getAsJsonPrimitive().isNumber()){
                values.put(entry.getKey(), new ConfigValue(value.getAsNumber()));
            } else if(value.getAsJsonPrimitive().isString()){
                values.put(entry.getKey(), new ConfigValue(value.getAsString()));
            } else if(value.getAsJsonPrimitive().isBoolean()){
                values.put(entry.getKey(), new ConfigValue(value.getAsBoolean()));
            }
        }
    }

    private void setup(JsonArray jsonArray){
        Iterator<JsonElement> elements = jsonArray.iterator();

        int index = 0;

        while (elements.hasNext()){
            JsonElement element = elements.next();

            if(element.isJsonObject()){
                values.put(String.valueOf(index), new ConfigValue(new ConfigGroup(element.getAsJsonObject(), this)));
            } else if(element.isJsonArray()){
                values.put(String.valueOf(index), new ConfigValue(new ConfigGroup(element.getAsJsonArray(), this)));
            } else if(element.getAsJsonPrimitive().isNumber()){
                values.put(String.valueOf(index), new ConfigValue(element.getAsNumber()));
            } else if(element.getAsJsonPrimitive().isString()){
                values.put(String.valueOf(index), new ConfigValue(element.getAsString()));
            } else if(element.getAsJsonPrimitive().isBoolean()){
                values.put(String.valueOf(index), new ConfigValue(element.getAsBoolean()));
            }

            index++;
        }

        length = index;
    }

    public boolean hasKey(String key){
        return values.containsKey(key);
    }

    public void save(){
        if(parent != null) parent.save();
        if(parentConfig != null) parentConfig.save();
    }


    //Update
    public void update(String key, Number value){
        if(isArray) return;

        if(hasKey(key)) {
            values.replace(key, new ConfigValue(value));
        }else{
            values.put(key, new ConfigValue(value));
        }

        save();
    }

    public void update(int index, Number value){
        if(!isArray) return;

        if(hasKey(String.valueOf(index))) {
            values.replace(String.valueOf(index), new ConfigValue(value));
        }else{
            values.put(String.valueOf(index), new ConfigValue(value));
        }

        save();
    }

    public void update(String key, String value){
        if(isArray) return;

        if(hasKey(key)) {
            values.replace(key, new ConfigValue(value));
        }else{
            values.put(key, new ConfigValue(value));
        }

        save();
    }

    public void update(int index, String value){
        if(!isArray) return;

        if(hasKey(String.valueOf(index))) {
            values.replace(String.valueOf(index), new ConfigValue(value));
        }else{
            values.put(String.valueOf(index), new ConfigValue(value));
        }

        save();
    }

    public void update(String key, Boolean value){
        if(isArray) return;

        if(hasKey(key)) {
            values.replace(key, new ConfigValue(value));
        }else{
            values.put(key, new ConfigValue(value));
        }

        save();
    }

    public void update(int index, Boolean value){
        if(!isArray) return;

        if(hasKey(String.valueOf(index))) {
            values.replace(String.valueOf(index), new ConfigValue(value));
        }else{
            values.put(String.valueOf(index), new ConfigValue(value));
        }

        save();
    }

    public void update(String key, ConfigGroup value){
        if(isArray) return;

        if(hasKey(key)) {
            values.replace(key, new ConfigValue(value));
        }else{
            values.put(key, new ConfigValue(value));
        }

        save();
    }

    public void update(int index, ConfigGroup value){
        if(!isArray) return;

        if(hasKey(String.valueOf(index))) {
            values.replace(String.valueOf(index), new ConfigValue(value));
        }else{
            values.put(String.valueOf(index), new ConfigValue(value));
        }

        save();
    }

    public void updateEmtpyGroup(String key){
        if(isArray) return;

        if(hasKey(key)) {
            values.replace(key, new ConfigValue(new ConfigGroup((JsonObject)null, this)));
        }else{
            values.put(key, new ConfigValue(new ConfigGroup((JsonObject)null, this)));
        }

        save();
    }

    public void updateEmtpyArray(String key){
        if(isArray) return;

        if(hasKey(key)) {
            values.replace(key, new ConfigValue(new ConfigGroup((JsonArray)null, this)));
        }else{
            values.put(key, new ConfigValue(new ConfigGroup((JsonArray)null, this)));
        }

        save();
    }

    public void updateEmtpyGroup(int index){
        if(!isArray) return;

        if(hasKey(String.valueOf(index))) {
            values.replace(String.valueOf(index), new ConfigValue(new ConfigGroup((JsonObject)null, this)));
        }else{
            values.put(String.valueOf(index), new ConfigValue(new ConfigGroup((JsonObject)null, this)));
        }

        save();
    }

    public void updateEmtpyArray(int index){
        if(hasKey(String.valueOf(index))) {
            values.replace(String.valueOf(index), new ConfigValue(new ConfigGroup((JsonArray)null, this)));
        }else{
            values.put(String.valueOf(index), new ConfigValue(new ConfigGroup((JsonArray)null, this)));
        }

        save();
    }


    //Insert
    public void insert(int index, Number value){
        if(!isArray) return;

        for (int i = index; i < length; i++) {
            ConfigValue oldValue = values.remove(String.valueOf(i));
            values.put(String.valueOf(i + 1), oldValue);
        }

        values.put(String.valueOf(index), new ConfigValue(value));

        length++;

        save();
    }

    public void insert(int index, String value){
        if(!isArray) return;

        for (int i = index; i < length; i++) {
            ConfigValue oldValue = values.remove(String.valueOf(i));
            values.put(String.valueOf(i + 1), oldValue);
        }

        values.put(String.valueOf(index), new ConfigValue(value));

        length++;

        save();
    }

    public void insert(int index, Boolean value){
        if(!isArray) return;

        for (int i = index; i < length; i++) {
            ConfigValue oldValue = values.remove(String.valueOf(i));
            values.put(String.valueOf(i + 1), oldValue);
        }

        values.put(String.valueOf(index), new ConfigValue(value));

        length++;

        save();
    }

    public void insert(int index, ConfigGroup value){
        if(!isArray) return;

        for (int i = index; i < length; i++) {
            ConfigValue oldValue = values.remove(String.valueOf(i));
            values.put(String.valueOf(i + 1), oldValue);
        }

        values.put(String.valueOf(index), new ConfigValue(value));

        length++;

        save();
    }

    public void insertEmptyGroup(int index){
        if(!isArray) return;

        for (int i = index; i < length; i++) {
            ConfigValue oldValue = values.remove(String.valueOf(i));
            values.put(String.valueOf(i + 1), oldValue);
        }

        values.put(String.valueOf(index), new ConfigValue(new ConfigGroup((JsonObject)null, this)));

        length++;

        save();
    }

    public void insertEmptyArray(int index){
        if(!isArray) return;

        for (int i = index; i < length; i++) {
            ConfigValue oldValue = values.remove(String.valueOf(i));
            values.put(String.valueOf(i + 1), oldValue);
        }

        values.put(String.valueOf(index), new ConfigValue(new ConfigGroup((JsonArray) null, this)));

        length++;

        save();
    }


    //Default
    public void setDefault(String key, Number value){
        if(isArray) return;

        if(hasKey(key)) return;

        values.put(key, new ConfigValue(value));

        save();
    }

    public void setDefault(int index, Number value){
        if(!isArray) return;

        if(index > length) return;

        if(index == length) length++;

        if(hasKey(String.valueOf(index))) return;

        values.put(String.valueOf(index), new ConfigValue(value));

        save();
    }

    public void setDefault(String key, String value){
        if(isArray) return;

        if(hasKey(key)) return;

        values.put(key, new ConfigValue(value));

        save();
    }

    public void setDefault(int index, String value){
        if(!isArray) return;

        if(index > length) return;

        if(index == length) length++;

        if(hasKey(String.valueOf(index))) return;

        values.put(String.valueOf(index), new ConfigValue(value));

        save();
    }

    public void setDefault(String key, Boolean value){
        if(isArray) return;

        if(hasKey(key)) return;

        values.put(key, new ConfigValue(value));

        save();
    }

    public void setDefault(int index, Boolean value){
        if(!isArray) return;

        if(index > length) return;

        if(index == length) length++;

        if(hasKey(String.valueOf(index))) return;

        values.put(String.valueOf(index), new ConfigValue(value));

        save();
    }

    public void setDefault(String key, ConfigGroup value){
        if(isArray) return;

        if(hasKey(key)) return;

        values.put(key, new ConfigValue(value));

        save();
    }

    public void setDefault(int index, ConfigGroup value){
        if(!isArray) return;

        if(index > length) return;

        if(index == length) length++;

        if(hasKey(String.valueOf(index))) return;

        values.put(String.valueOf(index), new ConfigValue(value));

        save();
    }

    public void setDefaultEmptyGroup(String key){
        if(isArray) return;

        if(hasKey(key)) return;

        values.put(key, new ConfigValue(new ConfigGroup((JsonObject)null, this)));

        save();
    }

    public void setDefaultEmptyGroup(int index){
        if(!isArray) return;

        if(hasKey(String.valueOf(index))) return;

        values.put(String.valueOf(index), new ConfigValue(new ConfigGroup((JsonObject)null, this)));

        save();
    }

    public void setDefaultEmptyArray(String key){
        if(isArray) return;

        if(hasKey(key)) return;

        values.put(key, new ConfigValue(new ConfigGroup((JsonArray)null, this)));

        save();
    }

    public void setDefaultEmptyArray(int index){
        if(isArray) return;

        if(hasKey(String.valueOf(index))) return;

        values.put(String.valueOf(index), new ConfigValue(new ConfigGroup((JsonArray)null, this)));

        save();
    }


    //Add
    public void add(Number value){
        if(!isArray) return;

        values.put(String.valueOf(length), new ConfigValue(value));

        save();
    }

    public void add(String value){
        if(!isArray) return;

        values.put(String.valueOf(length), new ConfigValue(value));

        save();
    }

    public void add(Boolean value){
        if(!isArray) return;

        values.put(String.valueOf(length), new ConfigValue(value));

        save();
    }

    public void add(ConfigGroup value){
        if(!isArray) return;

        values.put(String.valueOf(length), new ConfigValue(value));

        save();
    }

    public void addEmtpyGroup(){
        if(isArray) return;

        values.put(String.valueOf(length), new ConfigValue(new ConfigGroup((JsonObject)null, this)));

        save();
    }

    public void addEmtpyArray(){
        if(isArray) return;

        values.put(String.valueOf(length), new ConfigValue(new ConfigGroup((JsonArray) null, this)));

        save();
    }

    //Remove
    public void remove(String key){
        if(!hasKey(key)) return;

        values.remove(key);

        save();
    }

    public void remove(int index){
        if(!hasKey(String.valueOf(index))) return;

        values.remove(String.valueOf(index));

        for (int i = index + 1; i < length; i++) {
            ConfigValue oldValue = values.remove(String.valueOf(i));
            values.put(String.valueOf(i - 1), oldValue);
        }

        length--;

        save();
    }


    //Get
    public ConfigValue get(String key){
        if(!values.containsKey(key)) return null;

        return values.get(key);
    }

    public ConfigValue get(int index){
        if(!values.containsKey(String.valueOf(index))) return null;

        return values.get(String.valueOf(index));
    }

    public Number getNumber(String key){
        if(!values.containsKey(key)) return 0;

        return values.get(key).getNumberValue();
    }

    public Number getNumber(int index){
        if(!values.containsKey(String.valueOf(index))) return 0;

        return values.get(String.valueOf(index)).getNumberValue();
    }

    public String getString(String key){
        if(!values.containsKey(key)) return "";

        return values.get(key).getStringValue();
    }

    public String getString(int index){
        if(!values.containsKey(String.valueOf(index))) return "";

        return values.get(String.valueOf(index)).getStringValue();
    }

    public Boolean getBool(String key){
        if(!values.containsKey(key)) return false;

        return values.get(key).getBoolValue();
    }

    public Boolean getBool(int index){
        if(!values.containsKey(String.valueOf(index))) return false;

        return values.get(String.valueOf(index)).getBoolValue();
    }

    public ConfigGroup getGroup(String key){
        if(!values.containsKey(key)) return null;

        return values.get(key).getGroupValue();
    }

    public ConfigGroup getGroup(int index){
        if(!values.containsKey(String.valueOf(index))) return null;

        return values.get(String.valueOf(index)).getGroupValue();
    }

    public ConfigGroup getArray(String key){
        if(!values.containsKey(key)) return null;

        return values.get(key).getArrayValue();
    }

    public ConfigGroup getArray(int index){
        if(!values.containsKey(String.valueOf(index))) return null;

        return values.get(String.valueOf(index)).getArrayValue();
    }


    public JsonElement toJson(){
        if (!isArray) {
            JsonObject jsonObject = new JsonObject();

            Iterator<Map.Entry<String, ConfigValue>> entries = values.entrySet().iterator();

            while (entries.hasNext()){
                Map.Entry<String, ConfigValue> entry = entries.next();

                ConfigValue value = entry.getValue();

                if (value.valueType == ConfigValue.ValueType.GROUP){
                    jsonObject.add(entry.getKey(), value.getGroupValue().toJson());
                } else if (value.valueType == ConfigValue.ValueType.NUMBER) {
                    jsonObject.addProperty(entry.getKey(), value.getNumberValue());
                } else if (value.valueType == ConfigValue.ValueType.ARRAY) {
                    jsonObject.add(entry.getKey(), value.getArrayValue().toJson());
                } else if (value.valueType == ConfigValue.ValueType.STRING) {
                    jsonObject.addProperty(entry.getKey(), value.getStringValue());
                } else if (value.valueType == ConfigValue.ValueType.BOOL) {
                    jsonObject.addProperty(entry.getKey(), value.getBoolValue());
                }
            }

            return jsonObject;
        }else {
            JsonArray jsonArray = new JsonArray();

            for (int i = 0; i < length; i++) {
                ConfigValue value = values.get(String.valueOf(i));

                if (value.valueType == ConfigValue.ValueType.GROUP){
                    jsonArray.add(value.getGroupValue().toJson());
                } else if (value.valueType == ConfigValue.ValueType.NUMBER) {
                    jsonArray.add(value.getNumberValue());
                } else if (value.valueType == ConfigValue.ValueType.ARRAY) {
                    jsonArray.add(value.getArrayValue().toJson());
                } else if (value.valueType == ConfigValue.ValueType.STRING) {
                    jsonArray.add(value.getStringValue());
                } else if (value.valueType == ConfigValue.ValueType.BOOL) {
                    jsonArray.add(value.getBoolValue());
                }
            }

            return jsonArray;
        }
    }
}
