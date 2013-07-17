package hide92795.mods.sao.hpbar;

import java.awt.Color;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import cpw.mods.fml.common.network.Player;

public class HPBarObject {
	private static final int X_OFFSET = 15;
	private static final int Y_OFFSET = 10;

	private static final int MARGIN_NAME_UPPER = 1;

	private static final int MARGIN_BG_BAR_HEIGHT = 2;
	private static final int MARGIN_BG_BAR_LEFT = 1;
	private static final int MARGIN_BG_BAR_RIGHT = 13;

	private static final float BAR_WIDTH_UPPER = 150;
	private static final float BAR_WIDTH_LOWER = BAR_WIDTH_UPPER / 2;
	private static final int BAR_HEIGHT_HALF = 3;
	private static final int SLOPE_MOVEMENT_BAR_X = 3;
	private static final int MARGIN_NAME_BAR = 2;
	private static final int MARGIN_HP_LEVEL = 6;
	private static final int MARGIN_BAR_HP_X = 2;
	private static final int MARGIN_BAR_HP_Y = 2;

	private static final int MARGIN_INFO_HEIGHT = 1;
	private static final int MARGIN_INFO_WIDTH = 2;
	private static final int STRING_HEIGHT = 9;
	private static final int INFO_BG_COLOR = 0X40FFFFFF;
	private static final int INFO_NAME_COLOR = 0X76FFFFFF;
	private static final int MARGIN_NAME_LEFT = 2;
	private static final int MARGIN_NAME_RIGHT = 1;

	private static final ResourceLocation bar = new ResourceLocation(SAOHPBar.MOD_ID, "textures/hp_bg.png");

	private final int nameOffset;
	private int max;
	private int current;
	private float ratio;

	private HPType colorType;

	public HPBarObject(int nameOffset) {
		this.nameOffset = nameOffset;
	}

	public void update(Minecraft minecraft) {
		EntityClientPlayerMP player = minecraft.thePlayer;
		boolean updated = false;
		if (max != MathHelper.ceiling_float_int(player.func_110138_aP())) {
			updated = true;
			max = MathHelper.ceiling_float_int(player.func_110138_aP());
		}
		if (current != MathHelper.ceiling_float_int(player.func_110143_aJ())) {
			updated = true;
			current = MathHelper.ceiling_float_int(player.func_110143_aJ());
		}
		if (updated) {
			ratio = (float) current / max;
			if (ratio <= HPType.GREEN.border) {
				colorType = HPType.GREEN;
			}
			if (ratio <= HPType.YELLOW.border) {
				colorType = HPType.YELLOW;
			}
			if (ratio <= HPType.RED.border) {
				colorType = HPType.RED;
			}
		}
	}

	public void render(Minecraft minecraft) {
		Tessellator tessellator = Tessellator.instance;
		GL11.glPushMatrix();
		// bar background
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		minecraft.renderEngine.func_110577_a(bar);
		renderBarBackGround();
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glPopMatrix();
		GL11.glPushMatrix();

		// bar
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		renderBar();

		// info
		renderInfo(minecraft.fontRenderer, minecraft.thePlayer.username, minecraft.thePlayer.experienceLevel);
		GL11.glPopMatrix();
	}

	private void renderInfo(FontRenderer fontRenderer, String name, int level) {
		// init info data
		String hp = current + "/" + max;
		int hp_w = fontRenderer.getStringWidth(hp);
		int hp_x = MathHelper.ceiling_float_int(X_OFFSET + nameOffset + MARGIN_NAME_BAR + BAR_WIDTH_LOWER
				+ MARGIN_BAR_HP_X);
		int hp_y = Y_OFFSET + BAR_HEIGHT_HALF * 2 + MARGIN_BAR_HP_Y;

		int lv_x = MathHelper.ceiling_float_int(X_OFFSET + nameOffset + MARGIN_NAME_BAR + BAR_WIDTH_LOWER
				+ MARGIN_BAR_HP_X + hp_w + MARGIN_HP_LEVEL);
		int lv_y = Y_OFFSET + BAR_HEIGHT_HALF * 2 + MARGIN_BAR_HP_Y;
		String level_s = "LV:" + level;
		int lv_w = fontRenderer.getStringWidth(level_s);

		// render info back ground
		Gui.drawRect(hp_x - MARGIN_INFO_WIDTH, hp_y - MARGIN_INFO_HEIGHT, hp_x + hp_w + MARGIN_INFO_WIDTH, hp_y
				+ STRING_HEIGHT - MARGIN_INFO_HEIGHT, INFO_BG_COLOR);
		fontRenderer.drawString(hp, hp_x, hp_y, Color.WHITE.getRGB(), false);

		Gui.drawRect(lv_x - MARGIN_INFO_WIDTH, lv_y - MARGIN_INFO_HEIGHT, lv_x + lv_w + MARGIN_INFO_WIDTH, lv_y
				+ STRING_HEIGHT - MARGIN_INFO_HEIGHT, INFO_BG_COLOR);
		fontRenderer.drawString(level_s, lv_x, lv_y, Color.WHITE.getRGB(), false);

		Gui.drawRect(X_OFFSET - MARGIN_NAME_LEFT, Y_OFFSET - MARGIN_BG_BAR_HEIGHT, X_OFFSET + nameOffset
				+ MARGIN_NAME_RIGHT, Y_OFFSET + BAR_HEIGHT_HALF * 2 + MARGIN_BG_BAR_HEIGHT, INFO_NAME_COLOR);
		fontRenderer.drawString(name, X_OFFSET, Y_OFFSET - MARGIN_NAME_UPPER, Color.WHITE.getRGB(), false);
	}


	private void renderBar() {
		Tessellator tessellator = Tessellator.instance;
		// upper
		float width = X_OFFSET + nameOffset + MARGIN_NAME_BAR + BAR_WIDTH_UPPER * ratio;
		GL11.glColor4f(colorType.r, colorType.g, colorType.b, 1.0f);
		tessellator.startDrawingQuads();
		tessellator.addVertex(X_OFFSET + nameOffset + MARGIN_NAME_BAR, Y_OFFSET + BAR_HEIGHT_HALF, 0);
		tessellator.addVertex(Math.max(width - SLOPE_MOVEMENT_BAR_X, 0.0f), Y_OFFSET + BAR_HEIGHT_HALF, 0);
		tessellator.addVertex(Math.max(width, 0.0f), Y_OFFSET, 0);
		tessellator.addVertex(X_OFFSET + nameOffset + MARGIN_NAME_BAR, Y_OFFSET, 0);
		tessellator.draw();
		// lower
		tessellator.startDrawingQuads();
		if (ratio >= 0.5f) {
			// draw all
			tessellator.addVertex(X_OFFSET + nameOffset + MARGIN_NAME_BAR, Y_OFFSET + BAR_HEIGHT_HALF * 2, 0);
			tessellator.addVertex(X_OFFSET + nameOffset + MARGIN_NAME_BAR + BAR_WIDTH_LOWER - SLOPE_MOVEMENT_BAR_X * 2,
					Y_OFFSET + BAR_HEIGHT_HALF * 2, 0);
			tessellator.addVertex(X_OFFSET + nameOffset + MARGIN_NAME_BAR + BAR_WIDTH_LOWER - SLOPE_MOVEMENT_BAR_X,
					Y_OFFSET + BAR_HEIGHT_HALF, 0);
			tessellator.addVertex(X_OFFSET + nameOffset + MARGIN_NAME_BAR, Y_OFFSET + BAR_HEIGHT_HALF, 0);
		} else {
			// use ratio
			tessellator.addVertex(X_OFFSET + nameOffset + MARGIN_NAME_BAR, Y_OFFSET + BAR_HEIGHT_HALF * 2, 0);
			tessellator.addVertex(Math.max(width - SLOPE_MOVEMENT_BAR_X * 2, 0.0f), Y_OFFSET + BAR_HEIGHT_HALF * 2, 0);
			tessellator.addVertex(Math.max(width - SLOPE_MOVEMENT_BAR_X, 0.0f), Y_OFFSET + BAR_HEIGHT_HALF, 0);
			tessellator.addVertex(X_OFFSET + nameOffset + MARGIN_NAME_BAR, Y_OFFSET + BAR_HEIGHT_HALF, 0);
		}
		tessellator.draw();
	}

	private void renderBarBackGround() {
		Tessellator tessellator = Tessellator.instance;
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		// 41
		// 32
		tessellator.startDrawingQuads();
		tessellator.addVertexWithUV(X_OFFSET + nameOffset + MARGIN_NAME_BAR - MARGIN_BG_BAR_LEFT, Y_OFFSET
				+ BAR_HEIGHT_HALF * 2 + MARGIN_BG_BAR_HEIGHT, 0, 0.0d, 0.0390625d);
		tessellator.addVertexWithUV(X_OFFSET + nameOffset + MARGIN_NAME_BAR + BAR_WIDTH_UPPER + MARGIN_BG_BAR_RIGHT,
				Y_OFFSET + BAR_HEIGHT_HALF * 2 + MARGIN_BG_BAR_HEIGHT, 0, 0.640625d, 0.0390625d);
		tessellator.addVertexWithUV(X_OFFSET + nameOffset + MARGIN_NAME_BAR + BAR_WIDTH_UPPER + MARGIN_BG_BAR_RIGHT,
				Y_OFFSET - MARGIN_BG_BAR_HEIGHT, 0, 0.640625d, 0.0d);
		tessellator.addVertexWithUV(X_OFFSET + nameOffset + MARGIN_NAME_BAR - MARGIN_BG_BAR_LEFT, Y_OFFSET
				- MARGIN_BG_BAR_HEIGHT, 0, 0.0d, 0.0d);
		tessellator.draw();

		tessellator.startDrawingQuads();
		tessellator.addVertexWithUV(X_OFFSET - 7.5f, Y_OFFSET + BAR_HEIGHT_HALF * 2 + MARGIN_BG_BAR_HEIGHT, 0, 0.0d,
				0.125d);
		tessellator.addVertexWithUV(X_OFFSET - 3, Y_OFFSET + BAR_HEIGHT_HALF * 2 + MARGIN_BG_BAR_HEIGHT, 0, 0.03125d,
				0.125d);
		tessellator.addVertexWithUV(X_OFFSET - 3, Y_OFFSET - MARGIN_BG_BAR_HEIGHT, 0, 0.03125d, 0.0625d);
		tessellator.addVertexWithUV(X_OFFSET - 7.5f, Y_OFFSET - MARGIN_BG_BAR_HEIGHT, 0, 0.0d, 0.0625d);
		tessellator.draw();
	}


	private enum HPType {
		GREEN(0.0f, 1.0f, 0.0f, 1.0f), YELLOW(1.0f, 1.0f, 0.0f, 0.5f), RED(1.0f, 0.0f, 0.0f, 0.2f);

		private final float r;
		private final float g;
		private final float b;
		private final float border;

		private HPType(float r, float g, float b, float border) {
			this.r = r;
			this.g = g;
			this.b = b;
			this.border = border;
		}
	}
}
