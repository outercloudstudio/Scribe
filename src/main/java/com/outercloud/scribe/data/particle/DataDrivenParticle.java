package com.outercloud.scribe.data.particle;

import com.outercloud.scribe.Scribe;
import net.minecraft.client.particle.AnimatedParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3f;
import net.minecraft.util.registry.Registry;

public class DataDrivenParticle extends AnimatedParticle {
    public DataDrivenParticleData data;

    float wanderMagnitude;
    float wanderSmoothness;

    public float virtualAlpha;

    float timeTillNewParticleSpawn;

    float distanceTillNewParticleSpawn;
    float targetDistanceTillNewParticleSpawn;
    Vec3f lastParticlePos;

    public String tickFunction;

    public float virtualVelX;
    public float virtualVelY;
    public float virtualVelZ;

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
            virtualVelX = data.GetLinearCoord("x");
            virtualVelY = data.GetLinearCoord("y");
            virtualVelZ = data.GetLinearCoord("z");
        }

        virtualAlpha = 1;

        collidesWithWorld = data.GetDoesCollision();

        gravityStrength = data.GetGravity();

        if(data.DoSpawnOverTime()) timeTillNewParticleSpawn = data.GetSpawnOverTimeRate();

        if(data.DoSpawnOverDistance()){
            lastParticlePos = new Vec3f((float) x, (float) y, (float) z);

            targetDistanceTillNewParticleSpawn = data.GetSpawnOverDistanceDistance();
            distanceTillNewParticleSpawn = targetDistanceTillNewParticleSpawn;
        }

        if(data.DoTick()) tickFunction = data.GetTick();
    }

    @Override
    public void tick() {
        super.tick();

        if(data.GetMovementType() == DataDrivenParticleData.MovementType.WANDER) {
            virtualVelX += random.nextBetween((int) -wanderMagnitude, (int) wanderMagnitude) / wanderSmoothness;
            virtualVelY += random.nextBetween((int) -wanderMagnitude, (int) wanderMagnitude) / wanderSmoothness;
            virtualVelZ += random.nextBetween((int) -wanderMagnitude, (int) wanderMagnitude) / wanderSmoothness;
        }

        scale = data.ScaleOverLifetime(scale);

        virtualAlpha = data.AlphaOverLifetime(virtualAlpha);

        alpha = virtualAlpha;

        virtualVelX = data.AccelerationDrag(virtualVelX);
        virtualVelY = data.AccelerationDrag(virtualVelY);
        virtualVelZ = data.AccelerationDrag(virtualVelZ);

        if(data.DoSpawnOverTime()){
            timeTillNewParticleSpawn -= (float) 1 / (float) 20;

            if(timeTillNewParticleSpawn <= 0){
                timeTillNewParticleSpawn = data.GetSpawnOverTimeRate();

                int amount = data.GetSpawnOverTimeAmount();

                for (int i = 0; i < amount; i++) {
                    Identifier identifier = Identifier.splitOn(data.GetSpawnOverTimeIdentifier(), ':');

                    ParticleEffect particleEffect = Scribe.GetParticle(identifier);

                    world.addParticle(particleEffect, x, y, z, 0, 0, 0);
                }
            }
        }

        if(data.DoSpawnOverDistance()){
            float distance = (float) Math.sqrt(Math.pow(x - lastParticlePos.getX(), 2) + Math.pow(y - lastParticlePos.getY(), 2) + Math.pow(z - lastParticlePos.getZ(), 2));

            distanceTillNewParticleSpawn -= distance;

            lastParticlePos = new Vec3f((float) x, (float) y, (float) z);


            if(distanceTillNewParticleSpawn <= 0) {
                while (distanceTillNewParticleSpawn <= 0) {
                    distanceTillNewParticleSpawn += targetDistanceTillNewParticleSpawn;

                    Scribe.LOGGER.info("SPAWN!");

                    int amount = data.GetSpawnOverDistanceAmount();

                    for (int i = 0; i < amount; i++) {
                        Identifier identifier = Identifier.splitOn(data.GetSpawnOverDistanceIdentifier(), ':');

                        ParticleEffect particleEffect = Scribe.GetParticle(identifier);

                        world.addParticle(particleEffect, x, y, z, 0, 0, 0);
                    }
                }

                targetDistanceTillNewParticleSpawn = data.GetSpawnOverDistanceDistance();
                distanceTillNewParticleSpawn = targetDistanceTillNewParticleSpawn;
            }
        }

        if(data.DoTick()){
            Scribe.GetDataDrivenParticleTick(Identifier.splitOn(tickFunction, ':')).accept(this);
        }

        velocityX = virtualVelX;
        velocityY = virtualVelY;
        velocityZ = virtualVelZ;
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