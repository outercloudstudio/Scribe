package com.outercloud.scribe;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class Scribe implements ModInitializer, ClientModInitializer {
	private static final String NAMESPACE = "scribe";

	private static final Logger LOGGER = LoggerFactory.getLogger("scribe");

	private static Map<String, DefaultParticleType> particles = new HashMap<String, DefaultParticleType>();

	@Override
	public void onInitialize() {
		LOGGER.info("Initializing Scribe!");
	}

	@Override
	public void onInitializeClient() {
		LOGGER.info("Initializing Client Scribe!");
	}

	private static void InitializeParticleOptions(Identifier identifier){
		particles.put(identifier.toString(), FabricParticleTypes.simple());
	}

	public static DefaultParticleType Particle(Identifier identifier){
		return particles.get(identifier);
	}

	public static void InitializeParticle(Identifier identifier){
		InitializeParticleOptions(identifier);

		Registry.register(Registry.PARTICLE_TYPE, identifier, Particle(identifier));
	}

	public static void InitializeParticleClient(Identifier identifier,  ParticleFactoryRegistry.PendingParticleFactory factory){
		ClientSpriteRegistryCallback.event(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE).register(((atlasTexture, registry) -> {
			registry.register(new Identifier(identifier.getNamespace(), "particle/" + identifier.getPath()));
		}));

		ParticleFactoryRegistry.getInstance().register(Particle(identifier), factory);
	}
}
