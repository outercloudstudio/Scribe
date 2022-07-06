package com.outercloud.scribe.data;

import com.google.gson.JsonElement;
import com.outercloud.scribe.Scribe;

public class DataDrivenParticleData {
    JsonElement data;

    public DataDrivenParticleData(JsonElement data){
        this.data = data;
    }

    public Number GetScale(){
        if(!data.isJsonObject()) return 1;

        if(!data.getAsJsonObject().has("scale")) return 1;

        return data.getAsJsonObject().get("scale").getAsNumber();
    }
}
