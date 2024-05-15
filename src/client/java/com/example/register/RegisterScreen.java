package com.example.register;

import io.github.cottonmc.cotton.gui.GuiDescription;
import io.github.cottonmc.cotton.gui.client.CottonClientScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class RegisterScreen extends CottonClientScreen {
    public RegisterScreen(GuiDescription description) {
        super(description);
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), GLFW.GLFW_KEY_ESCAPE)) {
            MinecraftClient.getInstance().setScreen(new GameMenuScreen(true));
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
}
