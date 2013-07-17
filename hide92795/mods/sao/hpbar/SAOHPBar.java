package hide92795.mods.sao.hpbar;

import net.minecraft.client.Minecraft;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

@Mod(modid = SAOHPBar.MOD_ID, name = SAOHPBar.MOD_NAME, version = SAOHPBar.MOD_VERSION)
public class SAOHPBar {
	public static final String MOD_ID = "sao-hpbar";
	public static final String MOD_NAME = "SAO HPBar";
	public static final String MOD_VERSION = "1.5.2_1";
	protected final Minecraft minecraft = Minecraft.getMinecraft();
	protected SAOHPBarManager manager;

	@EventHandler
	public void init(FMLInitializationEvent event) {
		this.manager = new SAOHPBarManager();
		TickRegistry.registerTickHandler(new TickHandler(this), Side.CLIENT);
	}
}
