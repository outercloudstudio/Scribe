package com.outercloud.scribe.data;

import com.outercloud.scribe.Scribe;
import net.minecraft.client.particle.AnimatedParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;

public class DataDrivenParticle extends AnimatedParticle {
    DataDrivenParticle(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, SpriteProvider spriteProvider) {
        super(world, x, y, z, spriteProvider, 0.0F);

        this.maxAge = 6 * 20;

        this.setSpriteForAge(spriteProvider);
    }

    public void ApplyData(DataDrivenParticleData data){
        this.scale = data.GetScale().floatValue();

        Scribe.LOGGER.info("Particle scale " + this.scale);
    }

    public int getBrightness(float tint) {
        BlockPos blockPos = new BlockPos(this.x, this.y, this.z);

        return this.world.isChunkLoaded(blockPos) ? WorldRenderer.getLightmapCoordinates(this.world, blockPos) : 0;
    }

    public static class Factory implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            Identifier identifier = Registry.PARTICLE_TYPE.getId(defaultParticleType);

            Scribe.LOGGER.info("Creating particle " + identifier.getPath());

            Scribe.LOGGER.info(String.valueOf(Scribe.dataDrivenParticles.size()));
            Scribe.LOGGER.info(String.valueOf(Scribe.dataDrivenParticles.containsKey(identifier)));
            Scribe.LOGGER.info(identifier.toString());
            Scribe.LOGGER.info(Scribe.dataDrivenParticles.keySet().toArray()[0].toString());

            DataDrivenParticleData data = Scribe.GetDataDrivenParticle(identifier);

            Scribe.LOGGER.info("Got data " + data.GetScale());

            try {
                DataDrivenParticle particle = new DataDrivenParticle(clientWorld, d, e, f, 0, 0, 0, this.spriteProvider);

                particle.ApplyData(data);

                return particle;
            }catch (Exception ex){
                Scribe.LOGGER.error(String.valueOf(ex));
            }

            return null;
        }
    }
}