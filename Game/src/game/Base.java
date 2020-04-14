package game;

import java.io.IOException;
import java.util.Properties;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;

import core.Chunk;
import core.Stage;
import core.Window;
import utils.FPS;
import utils.Loader;
import utils.Logger;
import utils.Renderer;

import core.Stage;

public class Base {
	public static Properties settings;
	private Stage gameStage = Stage.TITLE;

	public Game game;
	public Title title;

	public void start() {
		Logger.writeLog("Initializing Game...");

		this.setup();
		FPS.setup();

		while (!Window.isCloseRequested()) {
			this.update();
			this.render();
		}
		this.destroy();
	}

	public void setup() {
		Window.setup();

		Renderer.loadSprites();
		if (gameStage == Stage.TITLE) {
			title = new Title();
			title.setup();
		}
	}

	public void update() {
		Window.update();
		FPS.updateFPS();

		if (gameStage == Stage.LOADING) {
			game = new Game();
			game.setup();
		}

		if (gameStage == Stage.GAME && game != null) {
			game.update();
		}

		if (gameStage == Stage.TITLE && title != null) {
			title.update();
		}
	}

	public void render() {
		Window.clear();

		if (gameStage == Stage.GAME && game != null) {
			game.render();
		}

		if (gameStage == Stage.TITLE && title != null) {
			title.render();
		}

		String telemetryFPSString = settings.getProperty("telemetry.fps");
		if (telemetryFPSString != null) {
			boolean telemetryFPS = Boolean.parseBoolean(telemetryFPSString);
			if (telemetryFPS) {
				// FPS Telemetry
				Renderer.renderSquare(0, 0, 51, 14, new Color(0, 0, 0, 0.9f));
				Renderer.renderText(0, 2, "FPS:" + FPS.getFPS(), 12, Color.white);
			}
		}
	}

	public void destroy() {
		if (title != null) {
			title.destroy();
		}
		if (game != null) {
			game.destroy();
		}

		// this needs to come last
		Window.destroy();
	}
}
