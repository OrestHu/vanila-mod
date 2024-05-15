package com.example.register;

import com.example.RegistermodClient;
import com.example.confg.MyConfig;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.*;
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment;
import io.github.cottonmc.cotton.gui.widget.data.VerticalAlignment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

public class RegisterGui extends LightweightGuiDescription {
    MyConfig config = new MyConfig();
    public RegisterGui() throws IOException {

        if(check()){
            signIn();
        }else {
            signUp();
        }
    }
    public void signUp()  {
        WGridPanel root = new WGridPanel();
        WButton button = new WButton();
        WLabel label = new WLabel(Text.literal("Sign Up"));
        WLabel badPassword = new WLabel(Text.literal("Please type correct password!"));
        WTextField textField = new WTextField(Text.literal("Print password"));
        WToggleButton savePasswordMenu = new WToggleButton(Text.literal("Save password"));

        setRootPanel(root);
        root.setSize(240, 150);
        button.setLabel(Text.literal("Sign Up"));
        textField.setMaxLength(32);
        label.setVerticalAlignment(VerticalAlignment.TOP);
        label.setHorizontalAlignment(HorizontalAlignment.CENTER);

        root.add(button, 5, 7, 3, 1);
        root.add(textField,3,3,7,1);
        root.add(label,5,1, 3,3);
        root.add(savePasswordMenu, 4,5);

        savePasswordMenu.setToggle(config.getAutoInputEnabled());

        if (config.getAutoInputEnabled()) {
            textField.setText(config.getSavedPassword());
        }

        savePasswordMenu.setOnToggle(on -> {
            boolean Click = on.booleanValue();
            if (Click) {
                System.out.println("save");
                config.updateConfig("enableAutoInput", true);
            } else {
                System.out.println("delete");
                config.updateConfig("enableAutoInput", false);
            }
        });

        button.setOnClick(() -> {
            String password = textField.getText();
            UUID uuid = MinecraftClient.getInstance().player.getUuid();
            String name = MinecraftClient.getInstance().player.getName().toString();
            String userUuid = uuid.toString();

            if(password.isEmpty()){
                root.add(badPassword, 2,2);
                badPassword.setColor(0xFF0000);
            }else {
                if (config.getAutoInputEnabled()) {
                    config.updateConfig("password", password);
                }
                try {
                    URL url = new URL("http://ec2-16-171-4-211.eu-north-1.compute.amazonaws.com:8090/api/v1/account/signUp");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json; utf-8");
                    conn.setRequestProperty("Accept", "application/json");
                    conn.setDoOutput(true);

                    String jsonInputString = String.format("{\"name\": \"%s\", \"uuid\": \"%s\", \"password\": \"%s\"}", name, userUuid, password);

                    try (OutputStream os = conn.getOutputStream()) {
                        byte[] input = jsonInputString.getBytes("utf-8");
                        os.write(input, 0, input.length);
                    }

                    int code = conn.getResponseCode();
                    System.out.println("Response Code: " + code);

                    if (code == 200) {
                        RegistermodClient.setLoggedIn(true);
                        MinecraftClient.getInstance().player.closeScreen();
                    } else {
                        root.add(badPassword, 2, 2);
                        badPassword.setText(Text.literal("Error during sign up!"));
                        badPassword.setColor(0xFF0000);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    root.add(badPassword, 2, 2);
                    badPassword.setText(Text.literal("Error during sign up!"));
                    badPassword.setColor(0xFF0000);
                }
            }
        });
    }
    public void signIn()  {
        WGridPanel root = new WGridPanel();
        WButton button = new WButton();
        WLabel label = new WLabel(Text.literal("Sign In"));
        WLabel badPassword = new WLabel(Text.literal("Please type correct password!"));
        WTextField textField = new WTextField(Text.literal("Print password"));
        WToggleButton savePasswordMenu = new WToggleButton(Text.literal("Save password"));

        setRootPanel(root);
        root.setSize(240, 150);
        button.setLabel(Text.literal("Sign In"));
        textField.setMaxLength(32);
        label.setVerticalAlignment(VerticalAlignment.TOP);
        label.setHorizontalAlignment(HorizontalAlignment.CENTER);

        root.add(button, 5, 7, 3, 1);
        root.add(textField,3,3,7,1);
        root.add(label,5,1, 3,3);
        root.add(savePasswordMenu, 4,5);


        savePasswordMenu.setToggle(config.getAutoInputEnabled());

        if (config.getAutoInputEnabled()) {
            textField.setText(config.getSavedPassword());
        }

        savePasswordMenu.setOnToggle(on -> {
            boolean Click = on.booleanValue();
            if (Click) {
                System.out.println("save");
                config.updateConfig("enableAutoInput", true);
            } else {
                System.out.println("delete");
                config.updateConfig("enableAutoInput", false);
            }
        });

        button.setOnClick(() -> {
            String password = textField.getText();
            UUID uuid = MinecraftClient.getInstance().player.getUuid();
            String name = MinecraftClient.getInstance().player.getName().toString();
            String userUuid = uuid.toString();

            if(password.isEmpty()){
                root.add(badPassword, 2,2);
                badPassword.setColor(0xFF0000);
            }else {
                if (config.getAutoInputEnabled()) {
                    config.updateConfig("password", password);
                }
                try {
                    URL url = new URL("http://ec2-16-171-4-211.eu-north-1.compute.amazonaws.com:8090/api/v1/account/signIn");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json; utf-8");
                    conn.setRequestProperty("Accept", "application/json");
                    conn.setDoOutput(true);

                    String jsonInputString = String.format("{\"name\": \"%s\", \"uuid\": \"%s\", \"password\": \"%s\"}", name, userUuid, password);

                    try (OutputStream os = conn.getOutputStream()) {
                        byte[] input = jsonInputString.getBytes("utf-8");
                        os.write(input, 0, input.length);
                    }

                    int code = conn.getResponseCode();
                    System.out.println("Response Code: " + code);

                    if (code == 200) {
                        RegistermodClient.setLoggedIn(true);
                        MinecraftClient.getInstance().player.closeScreen();
                    } else {
                        root.add(badPassword, 2, 2);
                        badPassword.setText(Text.literal("Error during sign up!"));
                        badPassword.setColor(0xFF0000);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    root.add(badPassword, 2, 2);
                    badPassword.setText(Text.literal("Cannot connect to the server"));
                    badPassword.setColor(0xFF0000);
                }
            }
        });
    }
    public boolean check() {
        try {
            UUID uuid = MinecraftClient.getInstance().player.getUuid();
            String userUuid = uuid.toString();

            URL url = new URL("http://ec2-16-171-4-211.eu-north-1.compute.amazonaws.com:8090/api/v1/account/check/" + userUuid);
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
            System.out.println("Cannot connect to the server");
        }
        return false;
    }
}
