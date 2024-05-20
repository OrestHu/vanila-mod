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

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

@Environment(EnvType.CLIENT)
public class RegistermodClient implements ClientModInitializer {
	private static boolean isLoggedIn = false;
	public static final String MOD_ID = "register-mod";
	private static final String ALLOWED_SERVER_IP = "localhost";
	private static MyConfig config = new MyConfig();

	@Override
	public void onInitializeClient() {
		config.loadConfig();

		ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
			if (client.player != null) {
				String playerUuid = client.player.getUuid().toString();
				requestAccess(playerUuid);
			}
		});

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
				if (client.getCurrentServerEntry() == null) {
					return;
				}

				String serverIp = client.getCurrentServerEntry().address;
				if (!ALLOWED_SERVER_IP.equals(serverIp)) {
					client.world.disconnect();
				} else if (!isLoggedIn && client.currentScreen == null && client.player != null) {
					try {
						System.out.println(MinecraftClient.getInstance().player.getUuid().toString());
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

	public void requestAccess(String uuid) {
		try {
			URL url = new URL("http://ec2-16-171-4-211.eu-north-1.compute.amazonaws.com:8091/api/v1/access/requestAccess/" + uuid);
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Accept", "application/json");
			int code = conn.getResponseCode();
			if (code == 200) {
				System.out.println("Connected");
			} else {
				System.out.println("Error during sign in!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
