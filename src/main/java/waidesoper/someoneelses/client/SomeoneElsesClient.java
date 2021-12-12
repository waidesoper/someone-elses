package waidesoper.someoneelses.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import waidesoper.someoneelses.SomeoneElses;
import waidesoper.someoneelses.init.initEntities;
import waidesoper.someoneelses.networking.ModPackets;
import waidesoper.someoneelses.networking.ModPacketsS2C;

import java.util.UUID;

@Environment(EnvType.CLIENT)
public class SomeoneElsesClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModPacketsS2C.register();
        EntityRendererRegistry.register(initEntities.SEDNACET, FlyingItemEntityRenderer::new);

    }

}
