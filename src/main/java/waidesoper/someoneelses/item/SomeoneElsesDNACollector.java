package waidesoper.someoneelses.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import waidesoper.someoneelses.init.initItems;
import waidesoper.someoneelses.item.entity.SomeoneElsesDNACollectorEntity;

public class SomeoneElsesDNACollector extends Item {

    public SomeoneElsesDNACollector(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand){
        ItemStack itemStack = user.getStackInHand(hand);
        if(!world.isClient) {
            SomeoneElsesDNACollectorEntity collectorEntity = new SomeoneElsesDNACollectorEntity(initItems.SEDNACE, world);
            collectorEntity.setItem(itemStack);
            collectorEntity.setVelocity(user, user.getPitch(), user.getYaw(), 0.0f, 1.5f, 1.0f);
            world.spawnEntity(collectorEntity);
        }

        if(!user.getAbilities().creativeMode){
            itemStack.decrement(1);
        }
        return TypedActionResult.success(itemStack,world.isClient());
    }

    @Override
    public boolean hasGlint(ItemStack itemStack){
        return itemStack.hasNbt();
    }

}
