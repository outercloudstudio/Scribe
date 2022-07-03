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

import java.util.Hashtable;

public class Scribe implements ModInitializer, ClientModInitializer {
	private static final String NAMESPACE = "scribe";

	private static final Logger LOGGER = LoggerFactory.getLogger("scribe");

	public static final DefaultParticleType PARTICLE = FabricParticleTypes.simple();

	@Override
	public void onInitialize() {
		LOGGER.info("Initializing Scribe!");
	}

	@Override
	public void onInitializeClient() {
		LOGGER.info("Initializing Client Scribe!");
	}

	public static void InitializeParticle(Identifier identifier){
		Registry.register(Registry.PARTICLE_TYPE, identifier, PARTICLE);
	}

	public static void InitializeParticleClient(Identifier identifier, ParticleFactory factory){
		ClientSpriteRegistryCallback.event(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE).register(((atlasTexture, registry) -> {
			registry.register(new Identifier(identifier.getNamespace(), "particle/" + identifier.getPath()));
		}));

		ParticleFactoryRegistry.getInstance().register(PARTICLE, factory);
	}
}
