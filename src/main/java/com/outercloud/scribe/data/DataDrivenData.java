package com.outercloud.scribe.data;

import com.google.gson.JsonElement;
import net.minecraft.util.math.random.Random;

import java.util.Iterator;
import java.util.List;

public class DataDrivenData {
    public JsonElement data;

    public DataDrivenData(JsonElement data){
        this.data = data;
    }

    public float ReadFloat(JsonElement element){
        if (element.isJsonObject()){
            if (element.getAsJsonObject().has("uniform")){
                if(!element.getAsJsonObject().get("uniform").isJsonObject()) return 0;

                if(!element.getAsJsonObject().get("uniform").getAsJsonObject().has("min")) return 0;

                if(!element.getAsJsonObject().get("uniform").getAsJsonObject().get("min").isJsonPrimitive()) return 0;

                if(!element.getAsJsonObject().get("uniform").getAsJsonObject().get("min").getAsJsonPrimitive().isNumber()) return 0;

                if(!element.getAsJsonObject().get("uniform").getAsJsonObject().has("max")) return 0;

                if(!element.getAsJsonObject().get("uniform").getAsJsonObject().get("max").isJsonPrimitive()) return 0;

                if(!element.getAsJsonObject().get("uniform").getAsJsonObject().get("max").getAsJsonPrimitive().isNumber()) return 0;

                float min = element.getAsJsonObject().get("uniform").getAsJsonObject().get("min").getAsNumber().floatValue();
                float max = element.getAsJsonObject().get("uniform").getAsJsonObject().get("max").getAsNumber().floatValue();

                return Random.create().nextFloat() * (max - min) + min;
            } else if (element.getAsJsonObject().has("select")){
                if(!element.getAsJsonObject().get("select").isJsonArray()) return 0;

                int maxWeight = 0;

                Iterator<JsonElement> iterator = element.getAsJsonObject().get("select").getAsJsonArray().iterator();

                while(iterator.hasNext()){
                    JsonElement nextElement = iterator.next();

                    if(!nextElement.isJsonObject()) return 0;

                    if(!nextElement.getAsJsonObject().has("weight")) return 0;

                    if(!nextElement.getAsJsonObject().get("weight").isJsonPrimitive()) return 0;

                    if(!nextElement.getAsJsonObject().get("weight").getAsJsonPrimitive().isNumber()) return 0;

                    if(!nextElement.getAsJsonObject().has("result")) return 0;

                    if(!nextElement.getAsJsonObject().get("result").isJsonPrimitive()) return 0;

                    if(!nextElement.getAsJsonObject().get("result").getAsJsonPrimitive().isNumber()) return 0;

                    maxWeight += nextElement.getAsJsonObject().get("weight").getAsNumber().intValue();
                }

                iterator = element.getAsJsonObject().get("select").getAsJsonArray().iterator();

                int random = Random.create().nextInt(maxWeight);

                while(iterator.hasNext()){
                    JsonElement nextElement = iterator.next();

                    random -= nextElement.getAsJsonObject().get("weight").getAsNumber().intValue();

                    if(random < 0){
                        return nextElement.getAsJsonObject().get("result").getAsNumber().floatValue();
                    }
                }
            }
        } else if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isNumber()) {
            return element.getAsNumber().floatValue();
        }

        return 0;
    }

    public boolean ReadBool(JsonElement element){
        if (element.isJsonObject()){
            if (element.getAsJsonObject().has("select")){
                if(!element.getAsJsonObject().get("select").isJsonArray()) return false;

                int maxWeight = 0;

                Iterator<JsonElement> iterator = element.getAsJsonObject().get("select").getAsJsonArray().iterator();

                while(iterator.hasNext()){
                    JsonElement nextElement = iterator.next();

                    if(!nextElement.isJsonObject()) return false;

                    if(!nextElement.getAsJsonObject().has("weight")) return false;

                    if(!nextElement.getAsJsonObject().get("weight").isJsonPrimitive()) return false;

                    if(!nextElement.getAsJsonObject().get("weight").getAsJsonPrimitive().isNumber()) return false;

                    if(!nextElement.getAsJsonObject().has("result")) return false;

                    if(!nextElement.getAsJsonObject().get("result").isJsonPrimitive()) return false;

                    if(!nextElement.getAsJsonObject().get("result").getAsJsonPrimitive().isBoolean()) return false;

                    maxWeight += nextElement.getAsJsonObject().get("weight").getAsNumber().intValue();
                }

                iterator = element.getAsJsonObject().get("select").getAsJsonArray().iterator();

                int random = Random.create().nextInt(maxWeight);

                while(iterator.hasNext()){
                    JsonElement nextElement = iterator.next();

                    random -= nextElement.getAsJsonObject().get("weight").getAsNumber().intValue();

                    if(random < 0){
                        return nextElement.getAsJsonObject().get("result").getAsBoolean();
                    }
                }
            }
        } else if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isBoolean()) {
            return element.getAsBoolean();
        }

        return false;
    }

    public String ReadString(JsonElement element){
        if (element.isJsonObject()){
            if (element.getAsJsonObject().has("select")){
                if(!element.getAsJsonObject().get("select").isJsonArray()) return "";

                int maxWeight = 0;

                Iterator<JsonElement> iterator = element.getAsJsonObject().get("select").getAsJsonArray().iterator();

                while(iterator.hasNext()){
                    JsonElement nextElement = iterator.next();

                    if(!nextElement.isJsonObject()) return "";

                    if(!nextElement.getAsJsonObject().has("weight")) return "";

                    if(!nextElement.getAsJsonObject().get("weight").isJsonPrimitive()) return "";

                    if(!nextElement.getAsJsonObject().get("weight").getAsJsonPrimitive().isNumber()) return "";

                    if(!nextElement.getAsJsonObject().has("result")) return "";

                    if(!nextElement.getAsJsonObject().get("result").isJsonPrimitive()) return "";

                    if(!nextElement.getAsJsonObject().get("result").getAsJsonPrimitive().isString()) return "";

                    maxWeight += nextElement.getAsJsonObject().get("weight").getAsNumber().intValue();
                }

                iterator = element.getAsJsonObject().get("select").getAsJsonArray().iterator();

                int random = Random.create().nextInt(maxWeight);

                while(iterator.hasNext()){
                    JsonElement nextElement = iterator.next();

                    random -= nextElement.getAsJsonObject().get("weight").getAsNumber().intValue();

                    if(random < 0){
                        return nextElement.getAsJsonObject().get("result").getAsString();
                    }
                }
            }
        } else if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isString()) {
            return element.getAsString();
        }

        return "";
    }
}
