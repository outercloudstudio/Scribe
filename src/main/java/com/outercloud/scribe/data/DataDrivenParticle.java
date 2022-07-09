package com.outercloud.scribe.data;

import com.outercloud.scribe.Scribe;
import net.minecraft.client.particle.AnimatedParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3f;
import net.minecraft.util.registry.Registry;

public class DataDrivenParticle extends AnimatedParticle {
    DataDrivenParticleData data;

    float wanderMagnitude;
    float wanderSmoothness;

    float virtualAlpha;

    DataDrivenParticle(ClientWorld world, double x, double y, double z, SpriteProvider spriteProvider, Identifier identifier) {
        super(world, x, y, z, spriteProvider, 0.0F);

        setSpriteForAge(spriteProvider);

        data = Scribe.GetDataDrivenParticle(identifier);
        maxAge = Math.round(data.GetLifetime().floatValue()) * 20;

        scale = data.GetScale().floatValue();

        if(data.GetMovementType() == DataDrivenParticleData.MovementType.WANDER) {
            wanderMagnitude = data.GetWanderMagnitude();
            wanderSmoothness = data.GetWanderSmoothness();
        } else if(data.GetMovementType() == DataDrivenParticleData.MovementType.LINEAR) {
            velocityX = data.GetLinearCoord("x");
            velocityY = data.GetLinearCoord("y");
            velocityZ = data.GetLinearCoord("z");
        }

        virtualAlpha = 1;

        collidesWithWorld = data.GetDoesCollision();

        gravityStrength = data.GetGravity();
    }

    @Override
    public void tick() {
        super.tick();

        if(data.GetMovementType() == DataDrivenParticleData.MovementType.WANDER) {
            velocityX += random.nextBetween((int)-wanderMagnitude, (int)wanderMagnitude) / wanderSmoothness;
            velocityY += random.nextBetween((int)-wanderMagnitude, (int)wanderMagnitude) / wanderSmoothness;
            velocityZ += random.nextBetween((int)-wanderMagnitude, (int)wanderMagnitude) / wanderSmoothness;
        }

        scale = data.ScaleOverLifetime(scale);

        virtualAlpha = data.AlphaOverLifetime(virtualAlpha);

        alpha = virtualAlpha;

        velocityX = data.AccelerationDrag((float)velocityX);
        velocityY = data.AccelerationDrag((float)velocityY);
        velocityZ = data.AccelerationDrag((float)velocityZ);
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