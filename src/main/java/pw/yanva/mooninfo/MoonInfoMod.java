package pw.yanva.mooninfo;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.world.World;
import pw.yanva.mooninfo.config.ModConfig;
import pw.yanva.mooninfo.events.KeyInputHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class MoonInfoMod implements ClientModInitializer {
    public static final String MOD_ID = "yanva_moonphase";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static ModConfig config;
    PhaseIcon icon;
    SendInfo sendInfo;
    long last_time;
    String address;
    boolean isConnect;

    @Override
    public void onInitializeClient(){
        config = new ModConfig();
        icon = new PhaseIcon();
        address = "";
        isConnect = false;
        sendInfo = new SendInfo("");
        last_time = System.currentTimeMillis();
        try {
            config.load();
        } catch (IOException ie) {
            return;
        }
        HudRenderCallback.EVENT.register((matrixStack, tickDelta) -> {
            icon.drawPhaseIcon(matrixStack, config.hudPosition);
            MinecraftClient minecraftClient = MinecraftClient.getInstance();
            if (minecraftClient.world.getRegistryKey() == World.OVERWORLD &&
                    System.currentTimeMillis()- last_time >= 20*1000 && isConnect && address.equals("146.59.104.153")) {
                sendInfo.sendMoonPhase(minecraftClient.world.getMoonPhase(), minecraftClient.world.getTimeOfDay());
                last_time = System.currentTimeMillis();
            }
        });
        ClientPlayConnectionEvents.JOIN.register(((handler, sender, client) -> {
            address = handler.getServerInfo().address;
            isConnect = true;
        }));
        ClientPlayConnectionEvents.DISCONNECT.register(((handler, client) -> {
            isConnect = false;
        }));
        KeyInputHandler.register();
    }
}
