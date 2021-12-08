package waidesoper.someoneelses.networking;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import waidesoper.someoneelses.SomeoneElses;

import java.util.UUID;

public class ModPacketsS2C {
    public static void register(){
        ClientPlayNetworking.registerGlobalReceiver(ModPackets.SEDNAC_THROW, ModPacketsS2C::receiveEntityPacket);
    }

    public static void receiveEntityPacket(MinecraftClient minecraftClient, ClientPlayNetworkHandler clientPlayNetworkHandler, PacketByteBuf packetByteBuf, PacketSender packetSender) {
            EntityType<?> et = Registry.ENTITY_TYPE.get(packetByteBuf.readVarInt());
            UUID uuid = packetByteBuf.readUuid();
            int entityId = packetByteBuf.readVarInt();
            Vec3d pos = EntitySpawnPacket.PacketBufUtil.readVec3d(packetByteBuf);
            float pitch = EntitySpawnPacket.PacketBufUtil.readAngle(packetByteBuf);
            float yaw = EntitySpawnPacket.PacketBufUtil.readAngle(packetByteBuf);
            minecraftClient.execute(() -> {
                if (MinecraftClient.getInstance().world == null)
                    throw new IllegalStateException("Tried to spawn entity in a null world!");
                Entity e = et.create(MinecraftClient.getInstance().world);
                if (e == null)
                    throw new IllegalStateException("Failed to create instance of entity \"" + Registry.ENTITY_TYPE.getId(et) + "\"!");
                e.updateTrackedPosition(pos);
                e.setPos(pos.x, pos.y, pos.z);
                e.setPitch(pitch);
                e.setYaw(yaw);
                e.setId(entityId);
                e.setUuid(uuid);
                MinecraftClient.getInstance().world.addEntity(entityId, e);
            });
    };

}
