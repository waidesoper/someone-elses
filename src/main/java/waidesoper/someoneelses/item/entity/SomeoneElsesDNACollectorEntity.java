package waidesoper.someoneelses.item.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;
import waidesoper.someoneelses.init.initItems;
import waidesoper.someoneelses.networking.EntitySpawnPacket;
import waidesoper.someoneelses.networking.ModPackets;

public class SomeoneElsesDNACollectorEntity extends ThrownItemEntity {


    public SomeoneElsesDNACollectorEntity(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected Item getDefaultItem() {
        return initItems.SEDNAC;
    }

    protected void onEntityHit(EntityHitResult entityHitResult){
        super.onEntityHit(entityHitResult);
        Entity entity = entityHitResult.getEntity();
        ItemStack itemToDrop = new ItemStack(initItems.SEDNAC);
        if(entity instanceof PlayerEntity){
            itemToDrop.getOrCreateNbt().putUuid("owner", entity.getUuid());
            itemToDrop.getOrCreateNbt().putString("ownerName", entity.getDisplayName().asString());
        }
        dropStack(itemToDrop);
    }

    protected void onCollision(HitResult hitResult){
        super.onCollision(hitResult);
        if(!this.world.isClient){
            this.world.sendEntityStatus(this, (byte) 3);
            this.kill();
            dropStack(new ItemStack(initItems.SEDNAC));
        }
    }

    @Override
    public Packet createSpawnPacket(){
        return EntitySpawnPacket.create(this, ModPackets.SEDNAC_THROW);
    }
}
