package waidesoper.someoneelses.item;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.EnderPearlEntity;
import net.minecraft.item.EnderPearlItem;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import waidesoper.someoneelses.SomeoneElses;

import java.util.List;
import java.util.UUID;

public class SomeoneElsesEnderPearl extends EnderPearlItem {


    public SomeoneElsesEnderPearl(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if(!world.isClient) {
            if (user.isSneaking()) {
                HitResult target = MinecraftClient.getInstance().crosshairTarget;
                SomeoneElses.LOGGER.info(target.getType().name());
                if(target.getType()==HitResult.Type.ENTITY){
                    EntityHitResult entityHit = (EntityHitResult) target;
                    Entity entity = entityHit.getEntity();
                    if(entity instanceof PlayerEntity){
                        setOwner(itemStack, (PlayerEntity) entity);
                    }
                    return TypedActionResult.success(itemStack,world.isClient());
                }
            } else {
                ServerPlayerEntity owner = null;
                if (itemStack.hasNbt()) {
                    owner = (ServerPlayerEntity) world.getPlayerByUuid(itemStack.getOrCreateNbt().getUuid("owner"));
                }
                if (owner != null) {
                    world.playSound(null, owner.getX(), owner.getY(), owner.getZ(), SoundEvents.ENTITY_ENDER_PEARL_THROW, SoundCategory.NEUTRAL, 0.5f, 0.4f / (world.getRandom().nextFloat() * 0.4f + 0.8f));
                    user.getItemCooldownManager().set(this, 20);
                    if (!world.isClient) {
                        SomeoneElses.LOGGER.info(owner.getDisplayName());
                        EnderPearlEntity enderPearlEntity = new EnderPearlEntity(world, (ServerPlayerEntity) owner);
                        enderPearlEntity.setItem(itemStack);
                        enderPearlEntity.setProperties(user, user.getPitch(), user.getYaw(), 0.0f, 1.5f, 1.0f);
                        world.spawnEntity(enderPearlEntity);
                    }
                    user.incrementStat(Stats.USED.getOrCreateStat(this));
                if (!user.getAbilities().creativeMode) {
                    itemStack.decrement(1);
                }
                    return TypedActionResult.success(itemStack, world.isClient());
                }
            }
        }
        return TypedActionResult.fail(itemStack);
    }

    public void setOwner(ItemStack itemStack, PlayerEntity entity){
        itemStack.getOrCreateNbt().putUuid("owner", entity.getUuid());
    }

}
