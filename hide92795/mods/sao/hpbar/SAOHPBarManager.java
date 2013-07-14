package hide92795.mods.sao.hpbar;

import net.minecraft.client.Minecraft;

public class SAOHPBarManager {
	private HPBarObject barobj;

	public void checkUpdate(Minecraft minecraft) {
		barobj.update(minecraft);
	}

	public void render(Minecraft minecraft) {
		barobj.render(minecraft);
	}

	public void setBar(HPBarObject barobj) {
		this.barobj = barobj;
	}

}
