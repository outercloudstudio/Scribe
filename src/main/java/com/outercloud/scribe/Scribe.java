package com.outercloud.scribe;

import com.outercloud.scribe.config.Config;
import com.outercloud.scribe.data.DataCache;
import com.outercloud.scribe.data.animation.DataDrivenAnimationData;
import com.outercloud.scribe.data.particle.DataDrivenParticle;
import com.outercloud.scribe.data.particle.DataDrivenParticleData;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.fabricmc.fabric.api.registry.OxidizableBlocksRegistry;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.SpawnEggItem;
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

public class Scribe {
	public static final String NAMESPACE = "scribe";

	public static final Logger LOGGER = LoggerFactory.getLogger("scribe");

	private static Map<String, Block> blocks = new HashMap<String, Block>();
	private static Map<String, BlockEntityType<?>> blockEntities = new HashMap<String, BlockEntityType<?>>();
	private static Map<String, Item> items = new HashMap<String, Item>();
	private static Map<String, DefaultParticleType> particles = new HashMap<String, DefaultParticleType>();
	public static Map<Identifier, DataDrivenParticleData> dataDrivenParticles = new HashMap<Identifier, DataDrivenParticleData>();
	public static Map<Identifier, DataDrivenAnimationData> dataDrivenAnimations = new HashMap<Identifier, DataDrivenAnimationData>();
	public static Map<String, Consumer<DataDrivenParticle>> dataDrivenParticleTicks = new HashMap<String, Consumer<DataDrivenParticle>>();
	private static Map<String, EntityType<?>> entities = new HashMap<String, EntityType<?>>();

	public static Config config;

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

	public static Item RegisterItem(Identifier identifier, Item item, ItemGroup group){
		items.put(identifier.toString(), item);

		Registry.register(Registry.ITEM, identifier, GetItem(identifier));

		return GetItem(identifier);
	}

	//Entities
	public static EntityType<?> GetEntity(Identifier identifier){
		return entities.get(identifier.toString());
	}

	public static DataDrivenAnimationData GetDataDrivenAnimation(Identifier identifier){
		return dataDrivenAnimations.get(identifier);
	}

	public static EntityType RegisterEntity(Identifier identifier, EntityType<?> entityType){
		entities.put(identifier.toString(), entityType);

		Registry.register(Registry.ENTITY_TYPE, identifier, entityType);

		return GetEntity(identifier);
	}

	public static EntityType RegisterEntity(Identifier identifier, EntityType<? extends LivingEntity> entityType, DefaultAttributeContainer.Builder attributes){
		entities.put(identifier.toString(), entityType);

		Registry.register(Registry.ENTITY_TYPE, identifier, entityType);

		FabricDefaultAttributeRegistry.register(entityType, attributes);

		return GetEntity(identifier);
	}

	public static Item RegisterSpawnEgg(Identifier identifier, SpawnEggItem spawnEggItem, ItemGroup group){
		items.put(identifier.toString(), spawnEggItem);

		Registry.register(Registry.ITEM, identifier, GetItem(identifier));

		return GetItem(identifier);
	}

	public static <E extends Entity> void RegisterClientEntity(Identifier identifier, EntityRendererFactory<E> entityRendererFactory){
		EntityRendererRegistry.register((EntityType<? extends E>) GetEntity(identifier), entityRendererFactory);
	}

	public static <E extends Entity> void RegisterClientEntity(Identifier identifier, EntityRendererFactory<E> entityRendererFactory, EntityModelLayer layer, EntityModelLayerRegistry.TexturedModelDataProvider texturedModelDataProvider){
		EntityRendererRegistry.register((EntityType<? extends E>) GetEntity(identifier), entityRendererFactory);

		EntityModelLayerRegistry.registerModelLayer(layer, texturedModelDataProvider);
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

		return GetBlock(identifier);
	}

	public static Block RegisterBlockWithItem(Identifier identifier, Identifier itemIdentifier, Block block, ItemGroup group){
		blocks.put(identifier.toString(), block);

		Registry.register(Registry.BLOCK, identifier, GetBlock(identifier));

		items.put(itemIdentifier.toString(), new BlockItem(GetBlock(identifier), new Item.Settings().group(group)));

		Registry.register(Registry.ITEM, itemIdentifier, GetItem(itemIdentifier));

		return GetBlock(identifier);
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
