package waidesoper.someoneelses;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.ProjectileDispenserBehavior;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.thrown.EnderPearlEntity;
import net.minecraft.entity.projectile.thrown.SnowballEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.Position;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import waidesoper.someoneelses.init.initEntities;

import waidesoper.someoneelses.init.initItems;
import waidesoper.someoneelses.item.SomeoneElsesEnderPearl;
import waidesoper.someoneelses.networking.ModPacketsC2S;
import waidesoper.someoneelses.networking.ModPacketsS2C;

public class SomeoneElses implements ModInitializer {
    public static String MOD_ID = "someone-elses";
    public static Logger LOGGER = LogManager.getLogger();


    @Override
    public void onInitialize() {
        initItems.register();
        initEntities.register();
        ModPacketsC2S.register();
        DispenserBlock.registerBehavior(initItems.SEEP,  new ProjectileDispenserBehavior(){

            @Override
            protected ProjectileEntity createProjectile(World world, Position position, ItemStack stack) {
                if (stack.hasNbt() && stack.getNbt().contains("owner") && !world.isClient) {
                    PlayerEntity owner = world.getPlayerByUuid(stack.getOrCreateNbt().getUuid("owner"));
                    EnderPearlEntity enderPearlEntity = new EnderPearlEntity(world, owner);
                    return enderPearlEntity;
                }
                return Util.make(new SnowballEntity(world, position.getX(), position.getY(), position.getZ()), snowballEntity -> snowballEntity.setItem(stack));
            }
        });
    }
}
