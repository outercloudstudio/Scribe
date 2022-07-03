package com.outercloud.scribe;

import com.google.gson.Gson;
import com.outercloud.scribe.particles.DataDrivenParticle;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

public class Scribe implements ModInitializer, ClientModInitializer {
	public static final String NAMESPACE = "scribe";

	public static final Logger LOGGER = LoggerFactory.getLogger("scribe");

	@Override
	public void onInitialize() {
		LOGGER.info("Initializing Scribe!");

		RegisterParticles();
	}

	public static final DefaultParticleType DATA_DRIVEN_PARTICLE = FabricParticleTypes.simple();

	public static void RegisterParticles() {
		Registry.register(Registry.PARTICLE_TYPE, new Identifier(NAMESPACE, "data_driven_particle"), DATA_DRIVEN_PARTICLE);

		try {
			File directoryPath = new File("./");

			File filesList[] = directoryPath.listFiles();

			for(File file : filesList) {
				System.out.println("File name: "+file.getName());
				System.out.println("File path: "+file.getAbsolutePath());
				System.out.println("Size :"+file.getTotalSpace());
				System.out.println(" ");
			}

			// create Gson instance
			Gson gson = new Gson();

			// create a reader
			Reader reader = Files.newBufferedReader(Paths.get("./scribe/mason.json"));

			// convert JSON file to map
			Map<?, ?> map = gson.fromJson(reader, Map.class);

			// print map entries
			for (Map.Entry<?, ?> entry : map.entrySet()) {
				System.out.println(entry.getKey() + "=" + entry.getValue());
			}

			// close reader
			reader.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void onInitializeClient() {
		RegisterParticlesClient();
	}

	public static void RegisterParticlesClient() {
		ParticleFactoryRegistry.getInstance().register(DATA_DRIVEN_PARTICLE, DataDrivenParticle.Factory::new);
	}
}
