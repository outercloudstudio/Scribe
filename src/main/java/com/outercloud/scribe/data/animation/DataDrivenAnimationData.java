package com.outercloud.scribe.data.animation;

import com.google.gson.JsonElement;
import com.outercloud.scribe.data.DataDrivenData;

import java.util.Iterator;
import java.util.Map;

public class DataDrivenAnimationData extends DataDrivenData {
    public enum InterpolationType {
        LINEAR,
        CATMULLROM
    }

    public DataDrivenAnimationData(JsonElement data) {
        super(data);
    }

    public Float GetLength(){
        if(!data.isJsonObject()) return 1F;

        if(!data.getAsJsonObject().has("length")) return 1F;

        return ReadFloat(data.getAsJsonObject().get("length"));
    }

    public Iterator<Map.Entry<String, JsonElement>> GetBones(){
        if(!data.isJsonObject()) return null;

        if(!data.getAsJsonObject().has("bones")) return null;

        if(!data.getAsJsonObject().get("bones").isJsonObject()) return null;

        return data.getAsJsonObject().get("bones").getAsJsonObject().entrySet().iterator();
    }

    public Iterator<Map.Entry<String, JsonElement>> GetOperations(String bone){
        if(!data.isJsonObject()) return null;

        if(!data.getAsJsonObject().has("bones")) return null;

        if(!data.getAsJsonObject().get("bones").isJsonObject()) return null;

        if(!data.getAsJsonObject().get("bones").getAsJsonObject().has(bone)) return null;

        if(!data.getAsJsonObject().get("bones").getAsJsonObject().get(bone).isJsonObject()) return null;

        return data.getAsJsonObject().get("bones").getAsJsonObject().get(bone).getAsJsonObject().entrySet().iterator();
    }

    public Iterator<Map.Entry<String, JsonElement>> GetKeyframes(String bone, String operation){
        if(!data.isJsonObject()) return null;

        if(!data.getAsJsonObject().has("bones")) return null;

        if(!data.getAsJsonObject().get("bones").isJsonObject()) return null;

        if(!data.getAsJsonObject().get("bones").getAsJsonObject().has(bone)) return null;

        if(!data.getAsJsonObject().get("bones").getAsJsonObject().get(bone).isJsonObject()) return null;

        if(!data.getAsJsonObject().get("bones").getAsJsonObject().get(bone).getAsJsonObject().has(operation)) return null;

        if(!data.getAsJsonObject().get("bones").getAsJsonObject().get(bone).getAsJsonObject().get(operation).isJsonObject()) return null;

        return data.getAsJsonObject().get("bones").getAsJsonObject().get(bone).getAsJsonObject().get(operation).getAsJsonObject().entrySet().iterator();
    }

    public int GetKeyframeCount(String bone, String operation){
        if(!data.isJsonObject()) return 0;

        if(!data.getAsJsonObject().has("bones")) return 0;

        if(!data.getAsJsonObject().get("bones").isJsonObject()) return 0;

        if(!data.getAsJsonObject().get("bones").getAsJsonObject().has(bone)) return 0;

        if(!data.getAsJsonObject().get("bones").getAsJsonObject().get(bone).isJsonObject()) return 0;

        if(!data.getAsJsonObject().get("bones").getAsJsonObject().get(bone).getAsJsonObject().has(operation)) return 0;

        if(!data.getAsJsonObject().get("bones").getAsJsonObject().get(bone).getAsJsonObject().get(operation).isJsonObject()) return 0;

        return data.getAsJsonObject().get("bones").getAsJsonObject().get(bone).getAsJsonObject().get(operation).getAsJsonObject().entrySet().size();
    }

    public InterpolationType GetInterpolationType(String bone, String operation, String keyframe){
        if(!data.isJsonObject()) return InterpolationType.LINEAR;

        if(!data.getAsJsonObject().has("bones")) return InterpolationType.LINEAR;

        if(!data.getAsJsonObject().get("bones").isJsonObject()) return InterpolationType.LINEAR;

        if(!data.getAsJsonObject().get("bones").getAsJsonObject().has(bone)) return InterpolationType.LINEAR;

        if(!data.getAsJsonObject().get("bones").getAsJsonObject().get(bone).isJsonObject()) return InterpolationType.LINEAR;

        if(!data.getAsJsonObject().get("bones").getAsJsonObject().get(bone).getAsJsonObject().has(operation)) return InterpolationType.LINEAR;

        if(!data.getAsJsonObject().get("bones").getAsJsonObject().get(bone).getAsJsonObject().get(operation).isJsonObject()) return InterpolationType.LINEAR;

        if(!data.getAsJsonObject().get("bones").getAsJsonObject().get(bone).getAsJsonObject().get(operation).getAsJsonObject().has(keyframe)) return InterpolationType.LINEAR;

        if(!data.getAsJsonObject().get("bones").getAsJsonObject().get(bone).getAsJsonObject().get(operation).getAsJsonObject().get(keyframe).isJsonObject()) return InterpolationType.LINEAR;

        if(!data.getAsJsonObject().get("bones").getAsJsonObject().get(bone).getAsJsonObject().get(operation).getAsJsonObject().get(keyframe).getAsJsonObject().has("interpolation")) return InterpolationType.LINEAR;

        if(!data.getAsJsonObject().get("bones").getAsJsonObject().get(bone).getAsJsonObject().get(operation).getAsJsonObject().get(keyframe).getAsJsonObject().get("interpolation").isJsonPrimitive()) return InterpolationType.LINEAR;

        if(!data.getAsJsonObject().get("bones").getAsJsonObject().get(bone).getAsJsonObject().get(operation).getAsJsonObject().get(keyframe).getAsJsonObject().get("interpolation").getAsJsonPrimitive().isString()) return InterpolationType.LINEAR;

        if(data.getAsJsonObject().get("bones").getAsJsonObject().get(bone).getAsJsonObject().get(operation).getAsJsonObject().get(keyframe).getAsJsonObject().get("interpolation").getAsString() == "linear"){
            return InterpolationType.LINEAR;
        }else {
            return InterpolationType.CATMULLROM;
        }
    }

    public float GetKeyframeCoord(String bone, String operation, String keyframe, String coord){
        if(!data.isJsonObject()) return 0F;

        if(!data.getAsJsonObject().has("bones")) return 0F;

        if(!data.getAsJsonObject().get("bones").isJsonObject()) return 0F;

        if(!data.getAsJsonObject().get("bones").getAsJsonObject().has(bone)) return 0F;

        if(!data.getAsJsonObject().get("bones").getAsJsonObject().get(bone).isJsonObject()) return 0F;

        if(!data.getAsJsonObject().get("bones").getAsJsonObject().get(bone).getAsJsonObject().has(operation)) return 0F;

        if(!data.getAsJsonObject().get("bones").getAsJsonObject().get(bone).getAsJsonObject().get(operation).isJsonObject()) return 0F;

        if(!data.getAsJsonObject().get("bones").getAsJsonObject().get(bone).getAsJsonObject().get(operation).getAsJsonObject().has(keyframe)) return 0F;

        if(!data.getAsJsonObject().get("bones").getAsJsonObject().get(bone).getAsJsonObject().get(operation).getAsJsonObject().get(keyframe).isJsonObject()) return 0F;

        if(!data.getAsJsonObject().get("bones").getAsJsonObject().get(bone).getAsJsonObject().get(operation).getAsJsonObject().get(keyframe).getAsJsonObject().has(coord)) return 0F;

        return ReadFloat(data.getAsJsonObject().get("bones").getAsJsonObject().get(bone).getAsJsonObject().get(operation).getAsJsonObject().get(keyframe).getAsJsonObject().get(coord));
    }
}