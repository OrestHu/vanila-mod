package com.example.register;

import com.example.confg.MyConfig;
import com.example.register.service.AuthService;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.*;
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment;
import io.github.cottonmc.cotton.gui.widget.data.VerticalAlignment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

import java.io.*;

public class RegisterGui extends LightweightGuiDescription {
    MyConfig config = new MyConfig();
    public RegisterGui() throws IOException {
        assert MinecraftClient.getInstance().player != null;
        String uuid = MinecraftClient.getInstance().player.getUuid().toString();

        boolean check = AuthService.check(uuid);

        if(check){
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
            assert MinecraftClient.getInstance().player != null;

            String password = textField.getText();
            var uuid = MinecraftClient.getInstance().player.getUuid().toString();
            String name = MinecraftClient.getInstance().player.getGameProfile().getName();

            if(password.isEmpty()){
                root.add(badPassword, 2,2);
                badPassword.setColor(0xFF0000);
            }else {
                if (config.getAutoInputEnabled()) {
                    config.updateConfig("password", password);
                }

                String error = AuthService.signUpLogic(name, uuid, password);

                if(!error.isEmpty()) {
                    root.add(badPassword, 2, 2);
                    badPassword.setText(Text.literal(error));
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
            assert MinecraftClient.getInstance().player != null;

            String password = textField.getText();
            var uuid = MinecraftClient.getInstance().player.getUuid().toString();
            String name = MinecraftClient.getInstance().player.getGameProfile().getName();

            if(password.isEmpty()){
                root.add(badPassword, 2,2);
                badPassword.setColor(0xFF0000);
            }else {
                if (config.getAutoInputEnabled()) {
                    config.updateConfig("password", password);
                }

                String error = AuthService.signInLogic(name, uuid, password);

                if(!error.isEmpty()) {
                    root.add(badPassword, 2, 2);
                    badPassword.setText(Text.literal(error));
                    badPassword.setColor(0xFF0000);
                }
            }
        });
    }
}
