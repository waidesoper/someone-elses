package waidesoper.someoneelses.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import waidesoper.someoneelses.networking.ModPacketsS2C;

@Environment(EnvType.CLIENT)
public class SomeoneElsesClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModPacketsS2C.register();
    }
}
