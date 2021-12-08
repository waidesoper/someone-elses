package waidesoper.someoneelses.networking;

import net.minecraft.util.Identifier;
import org.lwjgl.system.CallbackI;
import waidesoper.someoneelses.SomeoneElses;

public class ModPackets {
    public static final Identifier SEEP_THROW = new Identifier(SomeoneElses.MOD_ID,"seep_throw");
    public static final Identifier SEEP_SET_OWNER = new Identifier(SomeoneElses.MOD_ID, "seep_set_owner");
    public static final Identifier SEDNAC_THROW = new Identifier(SomeoneElses.MOD_ID, "sednac_throw");
}
