package waidesoper.someoneelses.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.entity.EntityType;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import waidesoper.someoneelses.init.initItems;
import waidesoper.someoneelses.networking.EntitySpawnPacket;
import waidesoper.someoneelses.networking.ModPackets;
import waidesoper.someoneelses.networking.ModPacketsS2C;

import java.util.UUID;

@Environment(EnvType.CLIENT)
public class SomeoneElsesClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(initItems.SEDNACE, (context) ->
                new FlyingItemEntityRenderer(context));

    }

}
