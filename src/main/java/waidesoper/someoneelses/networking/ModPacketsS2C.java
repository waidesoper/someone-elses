package waidesoper.someoneelses.networking;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;

import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import waidesoper.someoneelses.client.EntitySpawnPacket;

import java.util.UUID;

public class ModPacketsS2C {
    public static void register(){

        ClientPlayNetworking.registerGlobalReceiver(ModPackets.SEEP_SET_OWNER,ModPacketsS2C::seepSetOwner);
        ClientPlayNetworking.registerGlobalReceiver(ModPackets.ENTITY_SPAWN_PACKET, ModPacketsS2C::receiveEntityPacket);
    }

    private static void seepSetOwner(MinecraftClient minecraftClient, ClientPlayNetworkHandler clientPlayNetworkHandler, PacketByteBuf packetByteBuf, PacketSender packetSender) {
        minecraftClient.execute(()->{
            PlayerEntity player = minecraftClient.player;
            Hand hand = player.getActiveHand();
            ItemStack itemStack = player.getStackInHand(hand);
            HitResult target = MinecraftClient.getInstance().crosshairTarget;
            if (target.getType() == HitResult.Type.ENTITY) {
                EntityHitResult entityHit = (EntityHitResult) target;
                Entity entity = entityHit.getEntity();
                if (entity instanceof PlayerEntity) {
                    itemStack.getOrCreateNbt().putUuid("owner", entity.getUuid());
                    itemStack.getOrCreateNbt().putString("ownerName", entity.getDisplayName().asString());
                }
            }   else if (target.getType() == HitResult.Type.MISS) {
                itemStack.getOrCreateNbt().putUuid("owner", player.getUuid());
                itemStack.getOrCreateNbt().putString("ownerName", player.getDisplayName().asString());
            }
        });
    }

    public static void receiveEntityPacket(MinecraftClient minecraftClient, ClientPlayNetworkHandler handler, PacketByteBuf byteBuf, PacketSender responseSender) {
        EntityType<?> et = Registry.ENTITY_TYPE.get(byteBuf.readVarInt());
        UUID uuid = byteBuf.readUuid();
        int entityId = byteBuf.readVarInt();
        Vec3d pos = EntitySpawnPacket.PacketBufUtil.readVec3d(byteBuf);
        float pitch = byteBuf.readFloat();  // EntitySpawnPacket.PacketBufUtil.readAngle(byteBuf);
        float yaw = byteBuf.readFloat();    //EntitySpawnPacket.PacketBufUtil.readAngle(byteBuf);

        minecraftClient.execute(() -> {
            if (minecraftClient.world == null)
                throw new IllegalStateException("Tried to spawn entity in a null world!");

            Entity e = et.create(minecraftClient.world);
            if (e == null)
                throw new IllegalStateException("Failed to create instance of entity \"" + Registry.ENTITY_TYPE.getId(et) + "\"!");

            e.updateTrackedPosition(pos);
            e.setPos(pos.x, pos.y, pos.z);
            e.setPitch(pitch);
            e.setYaw(yaw);
            e.setId(entityId);
            e.setUuid(uuid);

            minecraftClient.world.addEntity(entityId, e);
        });
    }
}
