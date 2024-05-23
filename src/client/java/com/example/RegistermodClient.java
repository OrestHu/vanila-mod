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
import java.net.Socket;

@Environment(EnvType.CLIENT)
public class RegistermodClient implements ClientModInitializer {
	private static boolean isLoggedIn = false;
	public static final String MOD_ID = "register-mod";
	private static final String ALLOWED_SERVER_IP = "176.31.135.122";
	private static MyConfig config = new MyConfig();

	@Override
	public void onInitializeClient() {
		config.loadConfig();

		ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
			var ip = extractIpFromFullAddress(handler.getConnection().getAddress().toString());

			if(ALLOWED_SERVER_IP.equals(ip)){
				if (client.player != null) {
					try(var socket = new Socket(ALLOWED_SERVER_IP, 8061)){
						var writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
						var message = client.player.getUuid().toString();
						writer.write(message);
						writer.flush();
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
				}

				ClientTickEvents.END_CLIENT_TICK.register(clientTick  -> {
					if (clientTick.getCurrentServerEntry() == null) {
						return;
					}

					if (!isLoggedIn && clientTick.currentScreen == null
							&& clientTick.player != null && ALLOWED_SERVER_IP.equals(ip)) {
						try {
							System.out.println(MinecraftClient.getInstance().player.getUuid().toString());
							MinecraftClient.getInstance().setScreen(new RegisterScreen(new RegisterGui()));
						} catch (IOException e) {
							throw new RuntimeException(e);
						}
					}
				});
			}
		});

		ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
			isLoggedIn = false;
		});
	}

	public static void setLoggedIn(boolean loggedIn) {
		isLoggedIn = loggedIn;
	}
	private static String extractIpFromFullAddress(String fullAddress) {
		if (fullAddress.contains("/")) {
			fullAddress = fullAddress.substring(fullAddress.indexOf("/") + 1);
		}
		if (fullAddress.contains(":")) {
			fullAddress = fullAddress.substring(0, fullAddress.indexOf(":"));
		}
		return fullAddress;
	}
}
