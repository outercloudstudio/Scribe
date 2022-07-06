package com.outercloud.scribe.data;

import com.outercloud.scribe.Scribe;
import net.minecraft.client.particle.AnimatedParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class DataDrivenParticle extends AnimatedParticle {
    DataDrivenParticleData data;

    DataDrivenParticle(ClientWorld world, double x, double y, double z, SpriteProvider spriteProvider, Identifier identifier) {
        super(world, x, y, z, spriteProvider, 0.0F);

        this.setSpriteForAge(spriteProvider);

        data = Scribe.GetDataDrivenParticle(identifier);

        this.maxAge = Math.round(data.GetLifetime().floatValue()) * 20;

        this.scale = data.GetScale().floatValue();
    }

    @Override
    public void tick() {
        super.tick();

        Scribe.LOGGER.info(String.valueOf(data.GetShouldWander()));

        if(data.GetShouldWander()) {
            velocityX += random.nextBetween((int) -data.GetWanderMagnitude().doubleValue(), (int) data.GetWanderMagnitude().doubleValue()) / data.GetWanderSmoothness().doubleValue();
            velocityY += random.nextBetween((int) -data.GetWanderMagnitude().doubleValue(), (int) data.GetWanderMagnitude().doubleValue()) / data.GetWanderSmoothness().doubleValue();
            velocityZ += random.nextBetween((int) -data.GetWanderMagnitude().doubleValue(), (int) data.GetWanderMagnitude().doubleValue()) / data.GetWanderSmoothness().doubleValue();
        }
    }

    public static class Factory implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            Identifier identifier = Registry.PARTICLE_TYPE.getId(defaultParticleType);

            return new DataDrivenParticle(clientWorld, d, e, f, this.spriteProvider, identifier);
        }
    }
}