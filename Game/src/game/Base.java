package game;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;

import core.Chunk;
import core.PlayerController;
import core.Stage;
import core.Window;
import utils.FPS;
import utils.Loader;
import utils.Logger;
import utils.Renderer;

import core.Stage;

public class Base {
	public static Properties settings;
	public static Stage gameStage = Stage.TITLE;
	public static boolean isRunning = true;
	private boolean saveConfigOnExit = false;

	public Game game;
	public Title title;

	public void start() {
		Logger.writeLog("Initializing Game...");

		this.setup();
		FPS.setup();

		while (isRunning) {
			this.update();
			this.render();
			if (Window.isCloseRequested()) {
				isRunning = false;
			}
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

			gameStage = Stage.GAME;
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
				Renderer.renderSquare(0, 14, 51, 14, new Color(0, 0, 0, 0.9f));
				Renderer.renderText(0, 16, "Delta:" + FPS.delta, 12, Color.white);
			}
		}
		String boundariesString = settings.getProperty("telemetry.boundaries");
		if (boundariesString != null) {
			boolean boundaries = Boolean.parseBoolean(boundariesString);

			if (boundaries&&PlayerController.viewBounds!=null) {
				Renderer.renderRectangle(PlayerController.viewBounds, Color.red, GL11.GL_LINE_LOOP);
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
		if (saveConfigOnExit) {
			try (OutputStream output = new FileOutputStream("config.properties")) {
				settings.store(output, null);
			} catch (IOException io) {
				io.printStackTrace();
			}
		}

		// this needs to come last
		Window.destroy();
	}
}
