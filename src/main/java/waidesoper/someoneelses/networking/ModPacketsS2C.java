package waidesoper.someoneelses.networking;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import waidesoper.someoneelses.SomeoneElses;

public class ModPacketsS2C {
    public static void register(){
        ClientPlayNetworking.registerGlobalReceiver(ModPackets.SEEP_SET_OWNER,ModPacketsS2C::seepSetOwner);
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

}
