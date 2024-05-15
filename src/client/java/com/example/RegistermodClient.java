package com.example;

import com.example.register.RegisterGui;
import com.example.register.RegisterScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.MinecraftClient;

import java.io.IOException;


@Environment(EnvType.CLIENT)
public class RegistermodClient implements ClientModInitializer {
	private static boolean isLoggedIn = false;

	@Override
	public void onInitializeClient() {
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (!isLoggedIn && client.currentScreen == null && client.player != null) {
                try {
                    MinecraftClient.getInstance().setScreen(new RegisterScreen(new RegisterGui()));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
		});

		ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
			isLoggedIn = false;
		});
	}

	public static void setLoggedIn(boolean loggedIn) {
		isLoggedIn = loggedIn;
	}
}