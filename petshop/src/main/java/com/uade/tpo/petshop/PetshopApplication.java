package com.uade.tpo.petshop;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
public class PetshopApplication {
	@PostConstruct
	public static void debugUploads() {
		Path base = Paths.get("uploads");
		Path file = base.resolve("productos").resolve("2_images.png");
		System.out.println("[DEBUG-UPLOADS] base=" + base.toAbsolutePath());
		System.out.println("[DEBUG-UPLOADS] exists(base)=" + java.nio.file.Files.exists(base));
		System.out.println("[DEBUG-UPLOADS] exists(file)=" + java.nio.file.Files.exists(file));
	}

	public static void main(String[] args) {
		SpringApplication.run(PetshopApplication.class, args);
		System.out.println("user.dir = " + System.getProperty("user.dir"));
		debugUploads();
	}



}
