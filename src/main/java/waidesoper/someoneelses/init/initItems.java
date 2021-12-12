package waidesoper.someoneelses.init;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import waidesoper.someoneelses.SomeoneElses;
import waidesoper.someoneelses.item.SomeoneElsesDNACollector;
import waidesoper.someoneelses.item.SomeoneElsesEnderPearl;

public class initItems {
    public static final SomeoneElsesEnderPearl SEEP = new SomeoneElsesEnderPearl(new FabricItemSettings().group(ItemGroup.MISC).maxCount(16));
    public static final SomeoneElsesDNACollector SEDNAC = new SomeoneElsesDNACollector(new FabricItemSettings().group(ItemGroup.MISC).maxCount(1));
    public static void register(){
        Registry.register(Registry.ITEM, new Identifier(SomeoneElses.MOD_ID,"someoneelsesenderpearl"), SEEP);
        Registry.register(Registry.ITEM, new Identifier(SomeoneElses.MOD_ID, "someoneelsesdnacollector"), SEDNAC);
    }
}
