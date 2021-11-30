package waidesoper.someoneelses;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import waidesoper.someoneelses.item.SomeoneElsesEnderPearl;

public class SomeoneElses implements ModInitializer {
    public static String MOD_ID = "someone-elses";
    public static Logger LOGGER = LogManager.getLogger();
    public static final SomeoneElsesEnderPearl SEEP = new SomeoneElsesEnderPearl(new FabricItemSettings().group(ItemGroup.MISC).maxCount(16));

    @Override
    public void onInitialize() {
        Registry.register(Registry.ITEM, new Identifier(MOD_ID,"someoneelsesenderpearl"), SEEP);
    }
}
