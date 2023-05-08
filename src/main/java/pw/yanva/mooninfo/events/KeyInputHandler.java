package pw.yanva.mooninfo.events;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import pw.yanva.mooninfo.MoonInfoMod;

import java.io.IOException;

public class KeyInputHandler {
    public static final String KEY_CATEGORY_MOONPHASE = "Moon Phase Info";
    public static final String KEY_CHANGE_POSITION = "Change HUD position";

    public static KeyBinding changePositionKey;

    public static void registerKeyInputs() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if(changePositionKey.wasPressed()) {
                try {
                    MoonInfoMod.config.load();
                    int currentPosition = MoonInfoMod.config.hudPosition;
                    if (currentPosition + 1 > 4) { currentPosition = 1; }
                    else { currentPosition++; }
                    MoonInfoMod.config.save(currentPosition);
                } catch (IOException ei) {
                    return;
                }
            }
        });
    }

    public static void register() {
        changePositionKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                KEY_CHANGE_POSITION,
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_F6,
                KEY_CATEGORY_MOONPHASE
        ));

        registerKeyInputs();
    }
}