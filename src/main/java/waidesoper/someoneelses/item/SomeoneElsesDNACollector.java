package waidesoper.someoneelses.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import waidesoper.someoneelses.entity.SomeoneElsesDNACollectorEntity;
import waidesoper.someoneelses.init.initEntities;

public class SomeoneElsesDNACollector extends Item {
    public SomeoneElsesDNACollector(Settings settings) {
        super(settings);
    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand){
        ItemStack itemStack = user.getStackInHand(hand);
        if(!world.isClient){
            SomeoneElsesDNACollectorEntity someoneElsesDNACollectorEntity = new SomeoneElsesDNACollectorEntity(initEntities.SEDNACET,user,world);
            someoneElsesDNACollectorEntity.setItem(itemStack);
            someoneElsesDNACollectorEntity.setVelocity(user,user.getPitch(),user.getYaw(),0.0F, 1.5F, 0F);
            world.spawnEntity(someoneElsesDNACollectorEntity);
        }
        user.incrementStat(Stats.USED.getOrCreateStat(this));
        if (!user.isCreative() ){
            itemStack.decrement(1); // decrements itemStack if user is not in creative mode
        }
        return TypedActionResult.success(itemStack,world.isClient);
    }

    @Override
    public boolean hasGlint(ItemStack itemStack){
        return itemStack.hasNbt() && itemStack.getNbt().contains("owner");
    }
}
