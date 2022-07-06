package com.outercloud.scribe.data;

import com.google.gson.JsonElement;
import com.outercloud.scribe.Scribe;

public class DataDrivenParticle {
    public DataDrivenParticle(JsonElement data){
        Scribe.LOGGER.info("NEW DATA DRIVEN PARTICLE!");
    }
}
