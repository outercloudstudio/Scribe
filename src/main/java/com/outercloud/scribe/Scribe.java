package com.outercloud.scribe;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.fabricmc.fabric.api.registry.OxidizableBlocksRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class Scribe {
	private static final String NAMESPACE = "scribe";

	private static final Logger LOGGER = LoggerFactory.getLogger("scribe");

	private static Map<String, Block> blocks = new HashMap<String, Block>();
	private static Map<String, BlockEntityType<?>> blockEntities = new HashMap<String, BlockEntityType<?>>();
	private static Map<String, DefaultParticleType> particles = new HashMap<String, DefaultParticleType>();

	//Blocks
	public static Block GetBlock(Identifier identifier){
		return blocks.get(identifier.toString());
	}

	public static BlockEntityType<?> GetBlockEntity(Identifier identifier){
		return blockEntities.get(identifier.toString());
	}

	public static void RegisterBlock(Identifier identifier, Block block){
		blocks.put(identifier.toString(), block);

		Registry.register(Registry.BLOCK, identifier, GetBlock(identifier));
	}

	public static void RegisterBlockLayer(Identifier identifier, RenderLayer layer){
		BlockRenderLayerMap.INSTANCE.putBlock(GetBlock(identifier), RenderLayer.getCutout());
	}

	public static void RegisterBlockEntity(Identifier identifier, FabricBlockEntityTypeBuilder.Factory blockEntity, Identifier ... blocks){
		Block[] blockClasses = new Block[blocks.length];

		for (int i = 0; i < blocks.length; i++) {
			blockClasses[i] = GetBlock(blocks[i]);
		}

		blockEntities.put(identifier.toString(), Registry.register(Registry.BLOCK_ENTITY_TYPE, identifier,  FabricBlockEntityTypeBuilder.create(blockEntity, blockClasses).build(null)));
	}

	public static void RegisterOxidizablePair(Identifier from, Identifier to){
		OxidizableBlocksRegistry.registerOxidizableBlockPair(GetBlock(from), GetBlock(to));
	}

	//Particles
	private static void RegisterParticleOptions(Identifier identifier){
		particles.put(identifier.toString(), FabricParticleTypes.simple());
	}

	public static DefaultParticleType GetParticle(Identifier identifier){
		return particles.get(identifier.toString());
	}

	public static void RegisterParticle(Identifier identifier){
		RegisterParticleOptions(identifier);

		Registry.register(Registry.PARTICLE_TYPE, identifier, GetParticle(identifier));
	}

	public static void RegisterClientParticle(Identifier identifier,  ParticleFactoryRegistry.PendingParticleFactory factory){
		ClientSpriteRegistryCallback.event(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE).register(((atlasTexture, registry) -> {
			registry.register(new Identifier(identifier.getNamespace(), "particle/" + identifier.getPath()));
		}));

		ParticleFactoryRegistry.getInstance().register(GetParticle(identifier), factory);
	}
}
