package waidesoper.someoneelses.init;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import waidesoper.someoneelses.SomeoneElses;
import waidesoper.someoneelses.item.SomeoneElsesDNACollector;
import waidesoper.someoneelses.item.SomeoneElsesEnderPearl;
import waidesoper.someoneelses.item.entity.SomeoneElsesDNACollectorEntity;

public class initItems {
    public static void register(){
        Registry.register(Registry.ITEM, new Identifier(SomeoneElses.MOD_ID,"someoneelsesenderpearl"), SEEP);
        Registry.register(Registry.ITEM, new Identifier(SomeoneElses.MOD_ID,"someoneelsesdnacollector"), SEDNAC);
        Registry.register(Registry.ENTITY_TYPE, new Identifier(SomeoneElses.MOD_ID,"someoneelsesdnacollectorentity"), SEDNACE);
    }

    public static final SomeoneElsesEnderPearl SEEP = new SomeoneElsesEnderPearl(new FabricItemSettings().group(ItemGroup.MISC).maxCount(16));
    public static final SomeoneElsesDNACollector SEDNAC = new SomeoneElsesDNACollector(new FabricItemSettings().group(ItemGroup.MISC).maxCount(1));
    public static final EntityType<SomeoneElsesDNACollectorEntity> SEDNACE =
            FabricEntityTypeBuilder.<SomeoneElsesDNACollectorEntity>create(SpawnGroup.MISC, SomeoneElsesDNACollectorEntity::new)
                .dimensions(EntityDimensions.fixed(0.25F, 0.25F))
                .trackRangeBlocks(4).trackedUpdateRate(10)
                .build();
}
