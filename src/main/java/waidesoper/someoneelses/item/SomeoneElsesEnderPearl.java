package waidesoper.someoneelses.item;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.EnderPearlEntity;
import net.minecraft.item.EnderPearlItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import waidesoper.someoneelses.SomeoneElses;
import waidesoper.someoneelses.networking.ModPackets;

import java.util.List;
import java.util.UUID;

public class SomeoneElsesEnderPearl extends EnderPearlItem {


    public SomeoneElsesEnderPearl(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if (user.isSneaking()) {
            HitResult target = MinecraftClient.getInstance().crosshairTarget;
            if (target.getType() == HitResult.Type.ENTITY) {
                EntityHitResult entityHit = (EntityHitResult) target;
                Entity entity = entityHit.getEntity();
                if (entity instanceof PlayerEntity) {
                    setOwner(itemStack, (PlayerEntity) entity);
                }
                return TypedActionResult.success(itemStack, world.isClient);
            } else if (target.getType() == HitResult.Type.MISS) {
                setOwner(itemStack, user);
                return TypedActionResult.success(itemStack, world.isClient);
            }
            return TypedActionResult.fail(itemStack);
        } else {
            PacketByteBuf buffer = PacketByteBufs.create();
            buffer.writeItemStack(itemStack);
            ClientPlayNetworking.send(ModPackets.SEEP_THROW,buffer);
            return TypedActionResult.success(itemStack, world.isClient());
        }
    }

    @Override
    public boolean hasGlint(ItemStack itemStack){
        if(itemStack.hasNbt()){
            return itemStack.getNbt().getUuid("owner") != null;
        }
        return false;
    }

    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext){
        if (itemStack.hasNbt()) {
            tooltip.add(new TranslatableText("item.someone-elses.seepwithowner",
                    itemStack.getOrCreateNbt().getString("ownerName")));
        } else {
            tooltip.add(new TranslatableText("item.someone-elses.seepnoowner"));
        }
    }

    public PlayerEntity getOwner(World world, ItemStack itemStack){
        return world.getPlayerByUuid(itemStack.getOrCreateNbt().getUuid("owner"));
    }
    public void setOwner(ItemStack itemStack, PlayerEntity entity){
        itemStack.getOrCreateNbt().putUuid("owner", entity.getUuid());
        itemStack.getOrCreateNbt().putString("ownerName", entity.getDisplayName().asString());
        PacketByteBuf buffer = PacketByteBufs.create();
        buffer.writeUuid(entity.getUuid());
        buffer.writeItemStack(itemStack);
        buffer.writeString(entity.getDisplayName().asString());
        ClientPlayNetworking.send(ModPackets.SEEP_SET_OWNER,buffer);
    }

}
