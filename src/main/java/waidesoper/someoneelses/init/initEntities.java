package waidesoper.someoneelses.init;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import waidesoper.someoneelses.SomeoneElses;
import waidesoper.someoneelses.entity.SomeoneElsesDNACollectorEntity;

public class initEntities {
    public static final EntityType<SomeoneElsesDNACollectorEntity> SEDNACET = FabricEntityTypeBuilder.<SomeoneElsesDNACollectorEntity>create(SpawnGroup.MISC, SomeoneElsesDNACollectorEntity::new)
            .dimensions(EntityDimensions.fixed(0.25F,0.25F))
            .trackRangeBlocks(4).trackedUpdateRate(10)
            .build();
    public static void register(){
        Registry.register(Registry.ENTITY_TYPE, new Identifier(SomeoneElses.MOD_ID, "someoneelsesdnacollector"), SEDNACET);
    }
}
