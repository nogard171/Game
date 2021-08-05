package ui;

import java.awt.Rectangle;

import org.lwjgl.opengl.GL11;

import core.Renderer;

public class UIPanel {
	public Rectangle bounds = new Rectangle(0, 0, 3, 3);

	public static void renderPanel(int x, int y, int w, int h) {
		
		//GL11.glBegin(GL11.GL_QUADS);
		for (int ix = 0; ix < w; ix++) {
			for (int iy = 0; iy < h; iy++) {
				UITextureType type = getUITextureType(ix, iy, w-1, h-1);
				Renderer.renderUITexture(type, x + (ix * 32), y + (iy * 32), 32, 32);
			}
		}
		//GL11.glEnd();
	}

	private static UITextureType getUITextureType(int ix, int iy, int w, int h) {
		UITextureType type = UITextureType.BLANK;

		if (iy == 0) {
			if (ix == 0) {
				type = UITextureType.PANEL_TL;
			} else if (ix > 0 && ix < w) {
				type = UITextureType.PANEL_TC;
			} else if (ix >= w) {
				type = UITextureType.PANEL_TR;
			}
		} else if (iy > 0 && iy < h) {
			if (ix == 0) {
				type = UITextureType.PANEL_ML;
			} else if (ix > 0 && ix < w) {
				type = UITextureType.PANEL_MC;
			} else if (ix >= w) {
				type = UITextureType.PANEL_MR;
			}
		} else if (iy >= h) {
			if (ix == 0) {
				type = UITextureType.PANEL_BL;
			} else if (ix > 0 && ix < w) {
				type = UITextureType.PANEL_BC;
			} else if (ix <= w) {
				type = UITextureType.PANEL_BR;
			}
		}

		return type;
	}
}
