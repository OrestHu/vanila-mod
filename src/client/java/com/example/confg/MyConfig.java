package com.example.confg;

import com.example.RegistermodClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Environment(EnvType.CLIENT)
public class MyConfig {
    private static final Path CONFIG_FILE = FabricLoader.getInstance().getConfigDir().resolve(RegistermodClient.MOD_ID + "-config.json");

    private boolean enableAutoInput = false;
    private String password = "";

    public boolean getAutoInputEnabled() {
        loadConfig();
        return enableAutoInput;
    }

    public String getSavedPassword() {
        loadConfig();
        return password;
    }

    public void saveConfig() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try {
            String json = gson.toJson(this);
            Files.write(CONFIG_FILE, json.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadConfig() {
        try {
            if (Files.exists(CONFIG_FILE)) {
                Gson gson = new Gson();
                MyConfig config = gson.fromJson(new String(Files.readAllBytes(CONFIG_FILE)), MyConfig.class);
                this.enableAutoInput = config.enableAutoInput;
                this.password = config.password;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateConfig(String key, String value) {
        loadConfig();
        switch (key) {
            case "password":
                this.password = value;
                break;
            default:
                throw new IllegalArgumentException("Invalid key: " + key);
        }
        saveConfig();
    }

    public void updateConfig(String key, boolean value) {
        loadConfig();
        switch (key) {
            case "enableAutoInput":
                this.enableAutoInput = value;
                break;
            default:
                throw new IllegalArgumentException("Invalid key: " + key);
        }
        saveConfig();
    }
}
