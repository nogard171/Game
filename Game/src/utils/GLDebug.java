package utils;

import java.awt.Font;
import java.util.HashMap;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.opengl.TextureImpl;

public class GLDebug {
	static HashMap<Integer, TrueTypeFont> fonts = new HashMap<Integer, TrueTypeFont>();
	private static boolean isSetup = false;
	private static boolean enabled = true;

	public static void Setup() {

	}

	public static boolean IsEnabled() {
		return enabled;
	}

	public static void enabled(boolean newEnabled) {
		enabled = newEnabled;
	}

	public static void RenderBackground(float x, float y, float width, float height) {
		TextureImpl.bindNone();
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glColor4f(0, 0, 0, 0.5f);

		GL11.glVertex2f(x, y);
		GL11.glVertex2f(x + width, y);
		GL11.glVertex2f(x + width, y + height);
		GL11.glVertex2f(x, y + height);

		GL11.glEnd();
	}

	public static void RenderString(float x, float y, String text) {

		RenderString(x, y, text, 12, Color.black);
	}

	public static void RenderString(float x, float y, String text, int fontSize) {

		RenderString(x, y, text, fontSize, Color.black);
	}

	public static void RenderString(float x, float y, String text, int fontSize, Color color) {
		TrueTypeFont font = fonts.get(fontSize);
		if (font == null) {
			Font awtFont = new Font("Times New Roman", Font.PLAIN, fontSize);
			font = new TrueTypeFont(awtFont, false);
			fonts.put(fontSize, font);
		}
		font.drawString(x, y, text, color);
		TextureImpl.bindNone();
	}
}
