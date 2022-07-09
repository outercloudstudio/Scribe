package com.outercloud.scribe;

import com.outercloud.scribe.config.Config;
import com.outercloud.scribe.data.DataCache;
import com.outercloud.scribe.data.DataDrivenParticle;
import com.outercloud.scribe.data.DataDrivenParticleData;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.fabricmc.fabric.api.registry.OxidizableBlocksRegistry;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.registry.Registry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

public class Scribe implements ModInitializer, ClientModInitializer {
	private static final String NAMESPACE = "scribe";

	public static final Logger LOGGER = LoggerFactory.getLogger("scribe");

	private static Map<String, Block> blocks = new HashMap<String, Block>();
	private static Map<String, BlockEntityType<?>> blockEntities = new HashMap<String, BlockEntityType<?>>();
	private static Map<String, Item> items = new HashMap<String, Item>();
	private static Map<String, DefaultParticleType> particles = new HashMap<String, DefaultParticleType>();
	public static Map<Identifier, DataDrivenParticleData> dataDrivenParticles = new HashMap<Identifier, DataDrivenParticleData>();
	public static Map<String, Consumer<DataDrivenParticle>> dataDrivenParticleTicks = new HashMap<String, Consumer<DataDrivenParticle>>();

	public static Config config;

	@Override
	public void onInitialize() {
		RegisterParticle(new Identifier(NAMESPACE, "test_particle"));
	}

	@Override
	public void onInitializeClient() {
		InitializeDataDrivenFeatures();

		RegisterDataDrivenClientParticle(new Identifier(NAMESPACE, "test_particle"));

		RegisterDataDrivenClientParticleTick(new Identifier(NAMESPACE, "test_particle_tick"), particle -> {
			particle.setVelocity(1, 0, 0);
		});
	}

	public static void InitializeDataDrivenFeatures(){
		ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new IdentifiableResourceReloadListener() {
			@Override
			public Identifier getFabricId() {
				return new Identifier(NAMESPACE, "data_driven_features");
			}

			@Override
			public CompletableFuture<Void> reload(
					Synchronizer synchronizer,
					ResourceManager manager,
					Profiler prepareProfiler,
					Profiler applyProfiler,
					Executor prepareExecutor,
					Executor applyExecutor
			) {
				return DataCache.reload(synchronizer, manager, prepareProfiler, applyProfiler, prepareExecutor, applyExecutor);
			}
		});
	}

	//Config
	public static void LoadConfig(String relativePath){
		config = new Config();
		config.Load(relativePath);
	}

	//Items
	public static Item GetItem(Identifier identifier){
		return items.get(identifier.toString());
	}

	//Blocks
	public static Block GetBlock(Identifier identifier){
		return blocks.get(identifier.toString());
	}

	public static BlockEntityType<?> GetBlockEntity(Identifier identifier){
		return blockEntities.get(identifier.toString());
	}

	public static Block RegisterBlock(Identifier identifier, Block block){
		blocks.put(identifier.toString(), block);

		Registry.register(Registry.BLOCK, identifier, GetBlock(identifier));

		return block;
	}

	public static Block RegisterBlockWithItem(Identifier identifier, Block block, ItemGroup group){
		blocks.put(identifier.toString(), block);

		Registry.register(Registry.BLOCK, identifier, GetBlock(identifier));

		items.put(identifier.toString(), new BlockItem(GetBlock(identifier), new Item.Settings().group(group)));

		Registry.register(Registry.ITEM, identifier, GetItem(identifier));

		return block;
	}

	public static void RegisterBlockWithItem(Identifier identifier, Identifier itemIdentifier, Block block, ItemGroup group){
		blocks.put(identifier.toString(), block);

		Registry.register(Registry.BLOCK, identifier, GetBlock(identifier));

		items.put(itemIdentifier.toString(), new BlockItem(GetBlock(identifier), new Item.Settings().group(group)));

		Registry.register(Registry.ITEM, itemIdentifier, GetItem(itemIdentifier));
	}

	public static void RegisterBlockLayer(Identifier identifier, RenderLayer layer){
		BlockRenderLayerMap.INSTANCE.putBlock(GetBlock(identifier), RenderLayer.getCutout());
	}

	public static BlockEntityType<?> RegisterBlockEntity(Identifier identifier, FabricBlockEntityTypeBuilder.Factory blockEntity, Identifier ... blocks){
		Block[] blockClasses = new Block[blocks.length];

		for (int i = 0; i < blocks.length; i++) {
			blockClasses[i] = GetBlock(blocks[i]);
		}

		blockEntities.put(identifier.toString(), Registry.register(Registry.BLOCK_ENTITY_TYPE, identifier,  FabricBlockEntityTypeBuilder.create(blockEntity, blockClasses).build(null)));

		return GetBlockEntity(identifier);
	}

	public static void RegisterOxidizablePair(Identifier from, Identifier to){
		OxidizableBlocksRegistry.registerOxidizableBlockPair(GetBlock(from), GetBlock(to));
	}

	//Particles
	public static DefaultParticleType GetParticle(Identifier identifier){
		return particles.get(identifier.toString());
	}

	public static DataDrivenParticleData GetDataDrivenParticle(Identifier identifier){
		return dataDrivenParticles.get(identifier);
	}

	public static Consumer<DataDrivenParticle> GetDataDrivenParticleTick(Identifier identifier){
		return dataDrivenParticleTicks.get(identifier.toString());
	}

	public static DefaultParticleType RegisterParticle(Identifier identifier){
		particles.put(identifier.toString(), FabricParticleTypes.simple());

		Registry.register(Registry.PARTICLE_TYPE, identifier, GetParticle(identifier));

		return GetParticle(identifier);
	}

	public static void RegisterClientParticle(Identifier identifier,  ParticleFactoryRegistry.PendingParticleFactory factory){
		ParticleFactoryRegistry.getInstance().register(GetParticle(identifier), factory);
	}

	public static void RegisterDataDrivenClientParticle(Identifier identifier){
		ParticleFactoryRegistry.getInstance().register(GetParticle(identifier), DataDrivenParticle.Factory::new);
	}

	public static void RegisterDataDrivenClientParticleTick(Identifier identifier, Consumer<DataDrivenParticle> tick){
		dataDrivenParticleTicks.put(identifier.toString(), tick);
	}
}
