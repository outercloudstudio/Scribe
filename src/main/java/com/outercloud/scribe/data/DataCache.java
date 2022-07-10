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
import com.outercloud.scribe.data.animation.DataDrivenAnimationData;
import com.outercloud.scribe.data.particle.DataDrivenParticleData;
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
        Map<Identifier, DataDrivenAnimationData> animationsLoaded = new HashMap<>();

        return CompletableFuture.allOf(
                loadResources(
                        backgroundExecutor,
                        resourceManager,
                        "data_driven_particle",
                        resource -> DataLoader.loadParticle(resourceManager, resource),
                        particlesLoaded::put
                ),
                loadResources(
                        backgroundExecutor,
                        resourceManager,
                        "data_driven_animation",
                        resource -> DataLoader.loadAnimation(resourceManager, resource),
                        animationsLoaded::put
                )
        ).thenCompose(stage::whenPrepared).thenAcceptAsync(empty -> {
            Map<Identifier, DataDrivenParticleData> particlesRemapped = new HashMap<>();
            Map<Identifier, DataDrivenAnimationData> animationsRemapped = new HashMap<>();

            Iterator<Entry<Identifier, DataDrivenParticleData>> particleIterator = particlesLoaded.entrySet().iterator();
            Iterator<Entry<Identifier, DataDrivenAnimationData>> animationIterator = animationsLoaded.entrySet().iterator();

            while(particleIterator.hasNext()){
                Entry<Identifier, DataDrivenParticleData> entry = particleIterator.next();

                Identifier identifier = entry.getKey();

                Identifier remappedIdentifier = new Identifier(identifier.getNamespace(), identifier.getPath().substring(21, identifier.getPath().length() - 5));

                particlesRemapped.put(remappedIdentifier, entry.getValue());
            }

            while(animationIterator.hasNext()){
                Entry<Identifier, DataDrivenAnimationData> entry = animationIterator.next();

                Identifier identifier = entry.getKey();

                Identifier remappedIdentifier = new Identifier(identifier.getNamespace(), identifier.getPath().substring(22, identifier.getPath().length() - 5));

                animationsRemapped.put(remappedIdentifier, entry.getValue());
            }

            Scribe.dataDrivenParticles = particlesRemapped;
            Scribe.dataDrivenAnimations = animationsRemapped;
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
