package waidesoper.someoneelses.item;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.EnderPearlItem;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;
import waidesoper.someoneelses.SomeoneElses;
import waidesoper.someoneelses.networking.ModPackets;

import java.util.List;

public class SomeoneElsesEnderPearl extends EnderPearlItem {


    public SomeoneElsesEnderPearl(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if (user.isSneaking()) {
            if(!world.isClient) {
                PacketByteBuf buffer = PacketByteBufs.create();
                buffer.writeItemStack(itemStack);
                ServerPlayNetworking.send((ServerPlayerEntity) user, ModPackets.SEEP_SET_OWNER, buffer);
            }
            return TypedActionResult.success(itemStack, world.isClient());
        } else {
            PacketByteBuf buffer = PacketByteBufs.create();
            buffer.writeItemStack(itemStack);
            ClientPlayNetworking.send(ModPackets.SEEP_THROW,buffer);
            return TypedActionResult.success(itemStack, world.isClient());
        }
    }

    @Override
    public boolean hasGlint(ItemStack itemStack){
        return itemStack.hasNbt() && itemStack.getNbt().contains("owner");
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
}
