package gui;

import java.awt.Rectangle;

import org.newdawn.slick.Color;

import core.World;
import utils.FPS;
import utils.Renderer;

public class Telemetry {
	private static int count = 0;
	private static boolean showFPS = true;
	private static boolean showRegsionCount = true;
	private static boolean showTextureCount = true;

	public static void render() {
		Renderer.renderQuad(new Rectangle(0, 0, 200, count * 16), new Color(0, 0, 0, 0.5f));
		count=0;
		if (showFPS) {
			Renderer.renderText(0, 0, "FPS: " + FPS.getFPS(), 12, Color.white);
			count++;
		}
		if (showRegsionCount) {
			Renderer.renderText(0, count * 16, "Region Count: " + World.getRegionCount(), 12, Color.white);
			count++;
		}
		if (showTextureCount) {
			Renderer.renderText(0, count * 16, "Texture Count: " + World.getTextureCount(), 12, Color.white);
			count++;
		}

	}
}
