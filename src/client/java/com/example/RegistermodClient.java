package com.example;

import com.example.confg.MyConfig;
import com.example.register.RegisterGui;
import com.example.register.RegisterScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.MinecraftClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;


@Environment(EnvType.CLIENT)
public class RegistermodClient implements ClientModInitializer {
	private static boolean isLoggedIn = false;
	public static final String MOD_ID = "register-mod";
	private static final String ALLOWED_SERVER_IP = "localhost";
	private static MyConfig config = new MyConfig();

	@Override
	public void onInitializeClient() {
		config.loadConfig();

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (client.getCurrentServerEntry() == null) {
				return;
			}

			String serverIp = client.getCurrentServerEntry().address;
			if (!ALLOWED_SERVER_IP.equals(serverIp)) {
				client.world.disconnect();
			} else if (!isLoggedIn && client.currentScreen == null && client.player != null) {
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
