package gui;

import java.awt.Rectangle;

import org.newdawn.slick.Color;

import core.World;
import game.Database;
import utils.FPS;
import utils.Renderer;

public class Telemetry {
	private static int count = 0;

	public static void render() {
		Renderer.renderQuad(new Rectangle(0, 0, 200, count * 16), new Color(0, 0, 0, 0.5f));
		count = 0;
		if (Database.showFPS) {
			Renderer.renderText(0, 0, "FPS: " + FPS.getFPS(), 12, Color.white);
			count++;
		}
		if (Database.showRegsionCount) {
			Renderer.renderText(0, count * 16, "Region Count: " + World.getRegionCount(), 12, Color.white);
			count++;
		}
		if (Database.showTextureCount) {
			Renderer.renderText(0, count * 16, "Texture Count: " + World.getTextureCount(), 12, Color.white);
			count++;
		}
		Renderer.renderText(0, (count + 1) * 16, "Queued Task Count: " + Database.agentMgr.queuedSize(), 12,
				Color.white);
		Renderer.renderText(0, (count + 2) * 16, "Agents Available Count: " + Database.agentMgr.agentSize(), 12,
				Color.white);
		Renderer.renderText(0, (count + 3) * 16, "Agents Active Count: " + Database.agentMgr.activeAgentSize(), 12,
				Color.white);
		Renderer.renderText(0, (count + 4) * 16, "Chunk Count: " + Database.regions.size(), 12,
				Color.white);

	}
}
