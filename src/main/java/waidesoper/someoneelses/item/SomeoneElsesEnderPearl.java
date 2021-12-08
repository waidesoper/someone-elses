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
        if(world.isClient) return TypedActionResult.fail(itemStack);
            if (user.isSneaking()) {
                HitResult target = user.raycast(5.0D,1,true);
                user.sendMessage(new LiteralText("Hello: " + target.getType()), false);
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
                /*PacketByteBuf buffer = PacketByteBufs.create();
                buffer.writeItemStack(itemStack);

                ClientPlayNetworking.send(ModPackets.SEEP_THROW, buffer);*/
                if (!user.getAbilities().creativeMode) {
                    itemStack.decrement(1);
                }
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
