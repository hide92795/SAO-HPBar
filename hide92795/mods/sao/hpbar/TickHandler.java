package hide92795.mods.sao.hpbar;

import java.util.EnumSet;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;

public class TickHandler implements ITickHandler {
	private SAOHPBar saoHPBar;
	private boolean noWorld;

	public TickHandler(SAOHPBar saoHPBar) {
		this.saoHPBar = saoHPBar;
	}

	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {
	}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {
		if (saoHPBar.minecraft.theWorld == null) {
			noWorld = true;
			return;
		}
		if (noWorld) {
			noWorld = false;
			String username = saoHPBar.minecraft.thePlayer.username;
			int nameOffset = saoHPBar.minecraft.fontRenderer.getStringWidth(username);
			saoHPBar.manager.setBar(new HPBarObject(nameOffset));
		}
		if (saoHPBar.minecraft.currentScreen != null) {
			return;
		}
		if (saoHPBar.minecraft.mcProfiler.profilingEnabled) {
			saoHPBar.minecraft.mcProfiler.startSection(SAOHPBar.MOD_NAME);
			update();
			saoHPBar.minecraft.mcProfiler.endSection();
		} else {
			update();
		}
	}

	private void update() {
		if (saoHPBar.minecraft.thePlayer != null) {
			saoHPBar.manager.checkUpdate(saoHPBar.minecraft);
			saoHPBar.manager.render(saoHPBar.minecraft);
		}
	}

	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.RENDER);
	}

	@Override
	public String getLabel() {
		return SAOHPBar.MOD_NAME;
	}
}
