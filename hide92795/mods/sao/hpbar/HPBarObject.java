package hide92795.mods.sao.hpbar;

import java.awt.Color;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Tessellator;
import cpw.mods.fml.common.network.Player;

public class HPBarObject {
	private static final int X_OFFSET = 10;
	private static final int Y_OFFSET = 10;
	private static final int MARGIN_BG_BAR_HEIGHT = 3;
	private static final int MARGIN_BG_BAR_LEFT = 1;
	private static final int MARGIN_BG_BAR_RIGHT = 13;


	private static final float BAR_WIDTH_UPPER = 150;
	private static final float BAR_WIDTH_LOWER = BAR_WIDTH_UPPER / 2;
	private static final int BAR_HEIGHT_HALF = 3;
	private static final int SLOPE_MOVEMENT_BAR_X = 3;
	private static final int SLOPE_MOVEMENT_BG_X = 6;
	private static final int MARGIN_NAME_BAR = 2;
	private static final int MARGIN_HP_LEVEL = 5;
	private static final int MARGIN_BAR_HP_X = 1;
	private static final int MARGIN_BAR_HP_Y = 2;

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
		if (max != player.getMaxHealth()) {
			updated = true;
			max = player.getMaxHealth();
		}
		if (current != player.getHealth()) {
			updated = true;
			current = player.getHealth();
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
		GL11.glPushMatrix();
		Tessellator tessellator = Tessellator.instance;
		minecraft.renderEngine.bindTexture("/hide92795/mods/sao/hpbar/texture/hp_bg.png");
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);


		tessellator.startDrawingQuads();
		tessellator.addVertexWithUV(X_OFFSET + nameOffset + MARGIN_NAME_BAR - MARGIN_BG_BAR_LEFT, Y_OFFSET
				+ BAR_HEIGHT_HALF * 2 + MARGIN_BG_BAR_HEIGHT, 0, 0.0d, 0.078125d);
		tessellator.addVertexWithUV(X_OFFSET + nameOffset + MARGIN_NAME_BAR + BAR_WIDTH_UPPER + MARGIN_BG_BAR_RIGHT
				- SLOPE_MOVEMENT_BG_X, Y_OFFSET + BAR_HEIGHT_HALF * 2 + MARGIN_BG_BAR_HEIGHT, 0, 1.0d, 0.078125d);
		tessellator.addVertexWithUV(X_OFFSET + nameOffset + MARGIN_NAME_BAR + BAR_WIDTH_UPPER + MARGIN_BG_BAR_RIGHT,
				Y_OFFSET - MARGIN_BG_BAR_HEIGHT, 0, 1.0d, 0.0d);
		tessellator.addVertexWithUV(X_OFFSET + nameOffset + MARGIN_NAME_BAR - MARGIN_BG_BAR_LEFT, Y_OFFSET
				- MARGIN_BG_BAR_HEIGHT, 0, 0.0d, 0.0d);
		tessellator.draw();



		minecraft.renderEngine.bindTexture("/hide92795/mods/sao/hpbar/texture/bar.png");
		GL11.glColor4f(colorType.r, colorType.g, colorType.b, 1.0f);
		float width = X_OFFSET + nameOffset + MARGIN_NAME_BAR + BAR_WIDTH_UPPER * ratio;

		// 上段
		tessellator.startDrawingQuads();
		tessellator.addVertexWithUV(X_OFFSET + nameOffset + MARGIN_NAME_BAR, Y_OFFSET + BAR_HEIGHT_HALF, 0, 0.0f, 1.0f);
		tessellator
				.addVertexWithUV(Math.max(width - SLOPE_MOVEMENT_BAR_X, 0.0f), Y_OFFSET + BAR_HEIGHT_HALF, 0, 1.0f, 1.0f);
		tessellator.addVertexWithUV(Math.max(width, 0.0f), Y_OFFSET, 0, 1.0f, 0.0f);
		tessellator.addVertexWithUV(X_OFFSET + nameOffset + MARGIN_NAME_BAR, Y_OFFSET, 0, 0.0f, 0.0f);
		tessellator.draw();
		// 下段
		tessellator.startDrawingQuads();
		if (ratio >= 0.5f) {
			// draw all
			tessellator.addVertexWithUV(X_OFFSET + nameOffset + MARGIN_NAME_BAR, Y_OFFSET + BAR_HEIGHT_HALF * 2, 0,
					0.0f, 1.0f);
			tessellator.addVertexWithUV(X_OFFSET + nameOffset + MARGIN_NAME_BAR + BAR_WIDTH_LOWER - SLOPE_MOVEMENT_BAR_X
					* 2, Y_OFFSET + BAR_HEIGHT_HALF * 2, 0, 1.0f, 1.0f);
			tessellator.addVertexWithUV(X_OFFSET + nameOffset + MARGIN_NAME_BAR + BAR_WIDTH_LOWER - SLOPE_MOVEMENT_BAR_X,
					Y_OFFSET + BAR_HEIGHT_HALF, 0, 1.0f, 0.0f);
			tessellator.addVertexWithUV(X_OFFSET + nameOffset + MARGIN_NAME_BAR, Y_OFFSET + BAR_HEIGHT_HALF, 0, 0.0f,
					0.0f);
		} else {
			// use ratio
			tessellator.addVertexWithUV(X_OFFSET + nameOffset + MARGIN_NAME_BAR, Y_OFFSET + BAR_HEIGHT_HALF * 2, 0,
					0.0f, 1.0f);
			tessellator.addVertexWithUV(Math.max(width - SLOPE_MOVEMENT_BAR_X * 2, 0.0f), Y_OFFSET + BAR_HEIGHT_HALF * 2,
					0, 1.0f, 1.0f);
			tessellator.addVertexWithUV(Math.max(width - SLOPE_MOVEMENT_BAR_X, 0.0f), Y_OFFSET + BAR_HEIGHT_HALF, 0, 1.0f,
					0.0f);
			tessellator.addVertexWithUV(X_OFFSET + nameOffset + MARGIN_NAME_BAR, Y_OFFSET + BAR_HEIGHT_HALF, 0, 0.0f,
					0.0f);
		}
		tessellator.draw();
		FontRenderer fontRenderer = minecraft.fontRenderer;
		fontRenderer.drawString(minecraft.thePlayer.username, X_OFFSET, Y_OFFSET, Color.WHITE.getRGB(), false);
		String hp = current + "/" + max;
		int hp_w = fontRenderer.getStringWidth(hp);
		fontRenderer.drawString(hp,
				Math.round(X_OFFSET + nameOffset + MARGIN_NAME_BAR + BAR_WIDTH_LOWER + MARGIN_BAR_HP_X), Y_OFFSET
						+ BAR_HEIGHT_HALF * 2 + MARGIN_BAR_HP_Y, Color.WHITE.getRGB(), false);

		String level = "LV:" + minecraft.thePlayer.experienceLevel;
		int level_w = fontRenderer.getStringWidth(level);
		fontRenderer.drawString(
				level,
				Math.round(X_OFFSET + nameOffset + MARGIN_NAME_BAR + BAR_WIDTH_LOWER + MARGIN_BAR_HP_X + hp_w
						+ MARGIN_HP_LEVEL), Y_OFFSET + BAR_HEIGHT_HALF * 2 + MARGIN_BAR_HP_Y, Color.WHITE.getRGB(),
				false);
		GL11.glPopMatrix();
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
