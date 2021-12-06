package waidesoper.someoneelses.networking;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.projectile.thrown.EnderPearlEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.world.World;
import waidesoper.someoneelses.SomeoneElses;
import waidesoper.someoneelses.item.SomeoneElsesEnderPearl;

import java.util.UUID;

public class ModPacketsC2S {
    public static void register(){
        ServerPlayNetworking.registerGlobalReceiver(ModPackets.SEEP_THROW,ModPacketsC2S::seepThrow);
        ServerPlayNetworking.registerGlobalReceiver(ModPackets.SEEP_SET_OWNER,ModPacketsC2S::seepSetOwner);
    }

    public static void seepThrow(MinecraftServer minecraftServer, ServerPlayerEntity playerEntity, ServerPlayNetworkHandler handler, PacketByteBuf buffer, PacketSender sender){
        ItemStack itemStack = buffer.readItemStack();
        minecraftServer.execute(()->{
            SomeoneElses.LOGGER.info("throw detected");
                if(!itemStack.isEmpty()) {
                    World world = playerEntity.world;
                    ServerPlayerEntity owner = null;
                    if (itemStack.hasNbt() && itemStack.getNbt().contains("owner")) {
                        owner = (ServerPlayerEntity) world.getPlayerByUuid(itemStack.getOrCreateNbt().getUuid("owner"));
                    }
                    if (owner != null && !owner.isDisconnected()) {

                        if (!world.isClient) {
                            EnderPearlEntity enderPearlEntity = new EnderPearlEntity(world, (ServerPlayerEntity) owner);
                            enderPearlEntity.setItem(itemStack);
                            enderPearlEntity.setProperties(playerEntity, playerEntity.getPitch(), playerEntity.getYaw(), 0.0f, 1.5f, 1.0f);
                            world.spawnEntity(enderPearlEntity);
                        }
                        world.playSound(null, owner.getX(), owner.getY(), owner.getZ(), SoundEvents.ENTITY_ENDER_PEARL_THROW, SoundCategory.NEUTRAL, 0.5f, 0.4f / (world.getRandom().nextFloat() * 0.4f + 0.8f));
                        playerEntity.getItemCooldownManager().set(itemStack.getItem(), 20);
                        playerEntity.incrementStat(Stats.USED.getOrCreateStat(itemStack.getItem()));
                        if (!playerEntity.getAbilities().creativeMode) {
                            itemStack.decrement(1);
                        }
                    }
                }
        });
    }

    public static void seepSetOwner(MinecraftServer minecraftServer, ServerPlayerEntity playerEntity, ServerPlayNetworkHandler handler, PacketByteBuf buffer, PacketSender sender){
        UUID uuid = buffer.readUuid();
        ItemStack itemStack = buffer.readItemStack();
        String ownerName = buffer.readString();
        minecraftServer.execute(()->{
            itemStack.getOrCreateNbt().putUuid("owner", uuid);
            itemStack.getOrCreateNbt().putString("ownerName", ownerName);
            SomeoneElses.LOGGER.info("owner set to " + ownerName);
        });
    }
}
