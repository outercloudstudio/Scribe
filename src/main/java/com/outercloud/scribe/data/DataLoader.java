package com.outercloud.scribe.data;

import com.google.gson.JsonParser;
import com.outercloud.scribe.Scribe;
import com.outercloud.scribe.data.animation.DataDrivenAnimationData;
import com.outercloud.scribe.data.particle.DataDrivenParticleData;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class DataLoader {
    public static DataDrivenParticleData loadParticle(ResourceManager resourceManager, Identifier identifier) {
        return new DataDrivenParticleData(JsonParser.parseString(getResourceAsString(identifier, resourceManager)));
    }

    public static DataDrivenAnimationData loadAnimation(ResourceManager resourceManager, Identifier identifier) {
        return new DataDrivenAnimationData(JsonParser.parseString(getResourceAsString(identifier, resourceManager)));
    }

    public static String getResourceAsString(Identifier identifier, ResourceManager resourceManager) {
        try {
            InputStream inputStream = resourceManager.getResourceOrThrow(identifier).getInputStream();

            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            Scribe.LOGGER.error(String.valueOf(e));

            return null;
        }
    }
}
