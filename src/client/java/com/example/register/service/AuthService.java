package com.example.register.service;

import com.example.RegistermodClient;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
@Environment(EnvType.CLIENT)
public class AuthService {
    public static final String MOD_ID = "register-mod";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static boolean check(String uuid) {
        try {
            URL url = new URL("http://ec2-16-171-4-211.eu-north-1.compute.amazonaws.com:8090/api/v1/account/check/" + uuid);
            HttpURLConnection checkConn  = (HttpURLConnection) url.openConnection();
            checkConn.setRequestMethod("GET");
            checkConn.setRequestProperty("Accept", "application/json");

            BufferedReader checkReader = new BufferedReader(new InputStreamReader(checkConn.getInputStream()));
            String checkInputLine;
            StringBuilder checkResponse = new StringBuilder();

            while ((checkInputLine = checkReader.readLine()) != null) {
                checkResponse.append(checkInputLine);
            }
            checkReader.close();

            if (checkResponse.toString().equals("true")) {
                return true;
            }
        }catch (Exception e) {
            LOGGER.error(String.format("Player with uuid: %s is not allowed to connect the server", uuid));
        }
        return false;
    }
    public static String signUpLogic(String name, String uuid, String password){
        String errorMessage = "";

        try {
            URL url = new URL("http://ec2-16-171-4-211.eu-north-1.compute.amazonaws.com:8090/api/v1/account/signUp");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);

            String jsonInputString = String.format("{\"name\": \"%s\", \"uuid\": \"%s\", \"password\": \"%s\"}", name, uuid, password);

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int code = conn.getResponseCode();

            if (code == 200) {
                try (var socket = new Socket("176.31.135.122", 8061)) {
                    var writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                    assert MinecraftClient.getInstance().player != null;
                    writer.write(MinecraftClient.getInstance().player.getUuid().toString());
                    writer.flush();

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                RegistermodClient.setLoggedIn(true);
                MinecraftClient.getInstance().player.closeScreen();
            } else {
                try (InputStream errorStream = conn.getErrorStream()) {
                    if (errorStream != null) {
                        try (BufferedReader reader = new BufferedReader(new InputStreamReader(errorStream, "utf-8"))) {
                            StringBuilder responseBuilder = new StringBuilder();
                            String responseLine;
                            while ((responseLine = reader.readLine()) != null) {
                                responseBuilder.append(responseLine.trim());
                            }
                            Gson gson = new Gson();
                            JsonObject jsonResponse = gson.fromJson(responseBuilder.toString(), JsonObject.class);
                            if (jsonResponse.has("error")) {
                                errorMessage = jsonResponse.get("error").getAsString();
                            }
                        }
                    }
                } catch (IOException | com.google.gson.JsonSyntaxException ex) {
                    errorMessage = "Error reading error message from server";
                }
            }
        } catch (Exception e) {
            LOGGER.error("Connection to sign up is lost");
        }

        return errorMessage;
    }
    public static String signInLogic(String name, String uuid, String password){
        String errorMessage = "";

        try {
            URL url = new URL("http://ec2-16-171-4-211.eu-north-1.compute.amazonaws.com:8090/api/v1/account/signIn");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);

            String jsonInputString = String.format("{\"name\": \"%s\", \"uuid\": \"%s\", \"password\": \"%s\"}", name, uuid, password);

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int code = conn.getResponseCode();

            if (code == 200) {
                try (var socket = new Socket("176.31.135.122", 8061)) {
                    var writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                    assert MinecraftClient.getInstance().player != null;
                    writer.write(MinecraftClient.getInstance().player.getUuid().toString());
                    writer.flush();

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                RegistermodClient.setLoggedIn(true);
                MinecraftClient.getInstance().player.closeScreen();
            } else {
                try (InputStream errorStream = conn.getErrorStream()) {
                    if (errorStream != null) {
                        try (BufferedReader reader = new BufferedReader(new InputStreamReader(errorStream, "utf-8"))) {
                            StringBuilder responseBuilder = new StringBuilder();
                            String responseLine;
                            while ((responseLine = reader.readLine()) != null) {
                                responseBuilder.append(responseLine.trim());
                            }
                            Gson gson = new Gson();
                            JsonObject jsonResponse = gson.fromJson(responseBuilder.toString(), JsonObject.class);
                            if (jsonResponse.has("error")) {
                                errorMessage = jsonResponse.get("error").getAsString();
                            }
                        }
                    }
                } catch (IOException | com.google.gson.JsonSyntaxException ex) {
                    errorMessage = "Error reading error message from server";
                }

            }
        } catch (Exception e) {
            LOGGER.error("Connection to sign in is lost");
        }
        return errorMessage;
    }
}
