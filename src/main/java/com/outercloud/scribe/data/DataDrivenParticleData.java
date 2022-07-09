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
}
