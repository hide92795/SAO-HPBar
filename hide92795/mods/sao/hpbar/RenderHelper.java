package hide92795.mods.sao.hpbar;

import org.lwjgl.opengl.GL11;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.Tessellator;

public class RenderHelper {
	private static final int FONT_HEIGHT = 12;

	public static final void renderStringWithBackGround(FontRenderer fontRenderer, String string, int x, int y,
			int font_color, int bg_color) {
		// 文字幅を取得
		int string_w = fontRenderer.getStringWidth(string);
		// 背景描画
		Gui.drawRect(x, y, x + string_w, y + FONT_HEIGHT, bg_color);
		// 文字描画
		fontRenderer.drawString(string, x, y, font_color, false);
	}
}
