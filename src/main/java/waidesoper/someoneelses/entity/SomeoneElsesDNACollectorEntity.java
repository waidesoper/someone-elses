package waidesoper.someoneelses.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;
import waidesoper.someoneelses.client.EntitySpawnPacket;
import waidesoper.someoneelses.init.initEntities;
import waidesoper.someoneelses.init.initItems;
import waidesoper.someoneelses.networking.ModPackets;

public class SomeoneElsesDNACollectorEntity extends ThrownItemEntity {

    public SomeoneElsesDNACollectorEntity(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
    }

    public SomeoneElsesDNACollectorEntity(EntityType<? extends ThrownItemEntity> entityType, double d, double e, double f, World world) {
        super(initEntities.SEDNACET, d, e, f, world);
    }

    public SomeoneElsesDNACollectorEntity(EntityType<? extends ThrownItemEntity> entityType, LivingEntity livingEntity, World world) {
        super(initEntities.SEDNACET, livingEntity, world);
    }

    @Override
    protected Item getDefaultItem() {
        return initItems.SEDNAC;
    }

    protected void onEntityHit(EntityHitResult entityHitResult){
        super.onEntityHit(entityHitResult);
        Entity entity = entityHitResult.getEntity();
        ItemStack itemStack = initItems.SEDNAC.getDefaultStack();
        if(entity instanceof PlayerEntity playerEntity){
            itemStack.getOrCreateNbt().putUuid("owner",playerEntity.getUuid());
            itemStack.getOrCreateNbt().putString("ownerName", playerEntity.getDisplayName().asString());
        }
        this.kill();
        if(this.getOwner() != null) {
            ServerPlayerEntity user = (ServerPlayerEntity) this.getOwner();
            user.giveItemStack(itemStack);
        } else {
            dropStack(itemStack);
        }
    }

    protected void onCollision(HitResult hitResult){
        super.onCollision(hitResult);
    }

    protected void onBlockHit(BlockHitResult hitResult){
        super.onBlockHit(hitResult);
        if(!this.world.isClient){
            this.kill();
            dropItem(initItems.SEDNAC);
        }
    }

    @Override
    public Packet<?> createSpawnPacket() {
        return EntitySpawnPacket.create(this, ModPackets.ENTITY_SPAWN_PACKET);
    }
}
