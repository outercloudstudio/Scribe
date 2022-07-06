package com.outercloud.scribe.data;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.BiConsumer;
import java.util.function.Function;

import com.outercloud.scribe.Scribe;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceReloader;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
public class DataCache {
    private Map<Identifier, DataDrivenParticle> particles = Collections.emptyMap();
    private static DataCache INSTANCE;
    private final ParticleLoader particleLoader;

    public Map<Identifier, DataDrivenParticle> getParticles() {
        return particles;
    }

    public DataCache() {
        this.particleLoader = new ParticleLoader();
    }

    public static DataCache getInstance() {
        if (INSTANCE == null) INSTANCE = new DataCache();

        return INSTANCE;
    }

    public CompletableFuture<Void> reload(
            ResourceReloader.Synchronizer stage,
            ResourceManager resourceManager,
            Profiler preparationsProfiler,
            Profiler reloadProfiler,
            Executor backgroundExecutor,
            Executor gameExecutor
    ) {
        Map<Identifier, DataDrivenParticle> particles = new HashMap<>();

        return CompletableFuture.allOf(
                loadResources(
                    backgroundExecutor,
                    resourceManager,
                    "data_driven_particle",
                    resource -> particleLoader.load(resourceManager, resource),
                    particles::put
                )
        ).thenCompose(stage::whenPrepared).thenAcceptAsync(empty -> {
            this.particles = particles;
        }, gameExecutor);
    }

    private static <T> CompletableFuture<Void> loadResources(
        Executor executor,
        ResourceManager resourceManager,
        String type,
        Function<Identifier, T> loader,
        BiConsumer<Identifier, T> map
    ) {
        return CompletableFuture.supplyAsync(
            () -> {
                Scribe.LOGGER.info("Loading resources of type! " + type);

                Map<Identifier, Resource> resources = resourceManager.findResources(type, fileName -> fileName.toString().endsWith(".json"));

                Scribe.LOGGER.info("Done loading resources of type!");

                return resources;
            },
            executor
        ).thenApplyAsync(resources -> {
            Map<Identifier, CompletableFuture<T>> tasks = new HashMap<>();

            for (Identifier resource : resources.keySet()) {
                CompletableFuture<T> existing = tasks.put(resource, CompletableFuture.supplyAsync(() -> loader.apply(resource), executor));

                if (existing != null) {
                    System.err.println("Duplicate resource for " + resource);
                    existing.cancel(false);
                }
            }

            return tasks;
        }, executor).thenAcceptAsync(tasks -> {
            for (Entry<Identifier, CompletableFuture<T>> entry : tasks.entrySet()) {
                map.accept(entry.getKey(), entry.getValue().join());
            }
        }, executor);
    }
}
