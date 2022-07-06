package com.outercloud.scribe.data;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.BiConsumer;
import java.util.function.Function;

import com.outercloud.scribe.Scribe;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceReloader;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
public class DataCache {
    public static CompletableFuture<Void> reload(
            ResourceReloader.Synchronizer stage,
            ResourceManager resourceManager,
            Profiler preparationsProfiler,
            Profiler reloadProfiler,
            Executor backgroundExecutor,
            Executor gameExecutor
    ) {
        Map<Identifier, DataDrivenParticleData> particlesLoaded = new HashMap<>();

        return CompletableFuture.allOf(
                loadResources(
                        backgroundExecutor,
                        resourceManager,
                        "data_driven_particle",
                        resource -> ParticleLoader.load(resourceManager, resource),
                        particlesLoaded::put
                )
        ).thenCompose(stage::whenPrepared).thenAcceptAsync(empty -> {
            Map<Identifier, DataDrivenParticleData> particlesRemapped = new HashMap<>();

            Iterator<Entry<Identifier, DataDrivenParticleData>> iterator = particlesLoaded.entrySet().iterator();

            while(iterator.hasNext()){
                Entry<Identifier, DataDrivenParticleData> entry = iterator.next();

                Identifier identifier = entry.getKey();

                Identifier remappedIdentifier = new Identifier(identifier.getNamespace(), identifier.getPath().substring(21, identifier.getPath().length() - 5));

                particlesRemapped.put(remappedIdentifier, entry.getValue());
            }

            Scribe.dataDrivenParticles = particlesRemapped;
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
                () -> resourceManager.findResources(type, fileName -> fileName.toString().endsWith(".json")),
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
