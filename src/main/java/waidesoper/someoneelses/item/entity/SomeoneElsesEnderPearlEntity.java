package waidesoper.someoneelses.item.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.EndermiteEntity;
import net.minecraft.entity.projectile.thrown.EnderPearlEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;

public class SomeoneElsesEnderPearlEntity extends EnderPearlEntity {
    public SomeoneElsesEnderPearlEntity(World world, LivingEntity owner) {
        super(world, owner);
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
        for (int i = 0; i < 32; ++i) {
            this.world.addParticle(ParticleTypes.PORTAL, this.getX(), this.getY() + this.random.nextDouble() * 2.0, this.getZ(), this.random.nextGaussian(), 0.0, this.random.nextGaussian());
        }
        if (!this.world.isClient && !this.isRemoved()) {
            Entity i = this.getOwner();
            if (i instanceof ServerPlayerEntity) {
                ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity)i;
                if (serverPlayerEntity.networkHandler.getConnection().isOpen() && serverPlayerEntity.world == this.world && !serverPlayerEntity.isSleeping()) {
                    if (this.random.nextFloat() < 0.05f && this.world.getGameRules().getBoolean(GameRules.DO_MOB_SPAWNING)) {
                        EndermiteEntity endermiteEntity = EntityType.ENDERMITE.create(this.world);
                        endermiteEntity.refreshPositionAndAngles(i.getX(), i.getY(), i.getZ(), i.getYaw(), i.getPitch());
                        this.world.spawnEntity(endermiteEntity);
                    }
                    if (i.hasVehicle()) {
                        serverPlayerEntity.requestTeleportAndDismount(this.getX(), this.getY(), this.getZ());
                    } else {
                        i.requestTeleport(this.getX(), this.getY(), this.getZ());
                    }
                    i.fallDistance = 0.0f;
                    i.damage(DamageSource.FALL, 5.0f);
                }
            } else if (i != null) {
                i.requestTeleport(this.getX(), this.getY(), this.getZ());
                i.fallDistance = 0.0f;
            }
            this.discard();
        }
    }
}
