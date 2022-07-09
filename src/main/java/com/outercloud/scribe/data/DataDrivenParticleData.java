package com.outercloud.scribe.data;

import com.google.gson.JsonElement;

public class DataDrivenParticleData extends DataDrivenData {
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

    public boolean GetShouldWander(){
        if(!data.isJsonObject()) return false;

        return data.getAsJsonObject().has("movement:wander");
    }

    public Number GetWanderMagnitude(){
        if(!data.isJsonObject()) return 1;

        if(!data.getAsJsonObject().has("movement:wander")) return 1;

        if(!data.getAsJsonObject().get("movement:wander").isJsonObject()) return 1;

        return data.getAsJsonObject().get("movement:wander").getAsJsonObject().get("magnitude").getAsNumber();
    }

    public Number GetWanderSmoothness(){
        if(!data.isJsonObject()) return 300;

        if(!data.getAsJsonObject().has("movement:wander")) return 300;

        if(!data.getAsJsonObject().get("movement:wander").isJsonObject()) return 300;

        return data.getAsJsonObject().get("movement:wander").getAsJsonObject().get("smoothness").getAsNumber();
    }
}
