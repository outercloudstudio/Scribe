package com.outercloud.scribe.data;

import com.google.gson.JsonElement;

public class DataDrivenParticleData extends DataDrivenData {
    public enum MovementType {
        NONE,
        WANDER,
        LINEAR,
        DIFFUSE,
        CUSTOM
    }

    public DataDrivenParticleData(JsonElement data) {
        super(data);
    }

    public Number GetScale(){
        if(!data.isJsonObject()) return 1;

        if(!data.getAsJsonObject().has("scale")) return 1;

        return ReadFloat(data.getAsJsonObject().get("scale"));
    }

    public Number GetLifetime(){
        if(!data.isJsonObject()) return 1;

        if(!data.getAsJsonObject().has("lifetime")) return 1;

        return ReadFloat(data.getAsJsonObject().get("lifetime"));
    }

    public MovementType GetMovementType(){
        if(!data.isJsonObject()) return MovementType.NONE;

        MovementType currentType = MovementType.NONE;

        if(data.getAsJsonObject().has("wander")) currentType = MovementType.WANDER;

        if(data.getAsJsonObject().has("linear")) currentType = MovementType.LINEAR;

        if(data.getAsJsonObject().has("diffuse")) currentType = MovementType.DIFFUSE;

        return currentType;
    }

    public float GetWanderMagnitude(){
        if(!data.isJsonObject()) return 1;

        if(!data.getAsJsonObject().has("wander")) return 1;

        if(!data.getAsJsonObject().get("wander").isJsonObject()) return 1;

        if(!data.getAsJsonObject().get("wander").getAsJsonObject().has("magnitude")) return 1;

        return ReadFloat(data.getAsJsonObject().get("wander").getAsJsonObject().get("magnitude"));
    }

    public float GetWanderSmoothness(){
        if(!data.isJsonObject()) return 1;

        if(!data.getAsJsonObject().has("wander")) return 300;

        if(!data.getAsJsonObject().get("wander").isJsonObject()) return 300;

        if(!data.getAsJsonObject().get("wander").getAsJsonObject().has("smoothness")) return 300;

        return ReadFloat(data.getAsJsonObject().get("wander").getAsJsonObject().get("smoothness"));
    }

    public float GetLinearCoord(String coord){
        if(!data.isJsonObject()) return 1;

        if(!data.getAsJsonObject().has("linear")) return 1;

        if(!data.getAsJsonObject().get("linear").isJsonObject()) return 1;

        if(!data.getAsJsonObject().get("linear").getAsJsonObject().has(coord)) return 1;

        return ReadFloat(data.getAsJsonObject().get("linear").getAsJsonObject().get(coord));
    }

    public float ScaleOverLifetime(float scale){
        if(!data.isJsonObject()) return scale;

        if(!data.getAsJsonObject().has("scale_over_lifetime")) return scale;

        if(!data.getAsJsonObject().get("scale_over_lifetime").isJsonObject()) return scale;

        float offset = 0;
        float factor = 1;

        if (data.getAsJsonObject().get("scale_over_lifetime").getAsJsonObject().has("offset")) offset = ReadFloat(data.getAsJsonObject().get("scale_over_lifetime").getAsJsonObject().get("offset"));
        if (data.getAsJsonObject().get("scale_over_lifetime").getAsJsonObject().has("factor")) factor = ReadFloat(data.getAsJsonObject().get("scale_over_lifetime").getAsJsonObject().get("factor"));

        return scale * factor + offset;
    }

    public float AlphaOverLifetime(float alpha){
        if(!data.isJsonObject()) return alpha;

        if(!data.getAsJsonObject().has("alpha_over_lifetime")) return alpha;

        if(!data.getAsJsonObject().get("alpha_over_lifetime").isJsonObject()) return alpha;

        float offset = 0;
        float factor = 1;

        if (data.getAsJsonObject().get("alpha_over_lifetime").getAsJsonObject().has("offset")) offset = ReadFloat(data.getAsJsonObject().get("alpha_over_lifetime").getAsJsonObject().get("offset"));
        if (data.getAsJsonObject().get("alpha_over_lifetime").getAsJsonObject().has("factor")) factor = ReadFloat(data.getAsJsonObject().get("alpha_over_lifetime").getAsJsonObject().get("factor"));

        return alpha * factor + offset;
    }
}
