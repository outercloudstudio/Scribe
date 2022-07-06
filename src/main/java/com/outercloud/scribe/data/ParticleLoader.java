package com.outercloud.scribe.data;

import com.outercloud.scribe.Scribe;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class ParticleLoader {
    public DataDrivenParticle load(ResourceManager resourceManager, Identifier identifier) {
        Scribe.LOGGER.info("Loading particle! " + identifier.getPath());

        getResourceAsString(identifier, resourceManager);

        return new DataDrivenParticle();
    }

    public static String getResourceAsString(Identifier identifier, ResourceManager resourceManager) {
        Scribe.LOGGER.info("Getting resource as string! " + identifier.getPath());

        try {
            InputStream inputStream = resourceManager.getResourceOrThrow(identifier).getInputStream();

            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            Scribe.LOGGER.error(String.valueOf(e));

            return null;
        }
    }
}
