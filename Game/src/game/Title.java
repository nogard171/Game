package game;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Window;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;

import core.Stage;
import utils.Renderer;

public class Title {

	Rectangle exitBounds;
	Rectangle optionsBounds;
	Rectangle loadBounds;
	Rectangle newBounds;

	Rectangle forwardControlBounds;
	Rectangle backwardControlBounds;
	Rectangle leftControlBounds;
	Rectangle rightControlBounds;

	boolean exitHovered = false;
	boolean optionsHovered = false;
	boolean loadHovered = false;
	boolean newHovered = false;

	boolean forwardHovered = false;
	boolean backwardHovered = false;
	boolean leftHovered = false;
	boolean rightHovered = false;

	boolean forwardChange = false;
	boolean backwardChange = false;
	boolean leftChange = false;
	boolean rightChange = false;

	boolean showOptions = false;
	int leftMouseCount = 0;

	boolean showControls = true;

	public void setup() {
	}

	public void update() {
		exitBounds = new Rectangle(5, Integer.parseInt(Base.settings.getProperty("window.height")) - 47, 150, 30);
		optionsBounds = new Rectangle(5, Integer.parseInt(Base.settings.getProperty("window.height")) - 87, 150, 30);

		loadBounds = new Rectangle(5, Integer.parseInt(Base.settings.getProperty("window.height")) - 127, 150, 30);
		newBounds = new Rectangle(5, Integer.parseInt(Base.settings.getProperty("window.height")) - 167, 150, 30);

		forwardControlBounds = new Rectangle(160, 245, 200, 25);
		backwardControlBounds = new Rectangle(160, 275, 200, 25);
		leftControlBounds = new Rectangle(160, 305, 200, 25);
		rightControlBounds = new Rectangle(160, 335, 200, 25);

		if (exitBounds.contains(
				new Point(Mouse.getX(), Integer.parseInt(Base.settings.getProperty("window.height")) - Mouse.getY()))) {
			exitHovered = true;
		} else {
			exitHovered = false;
		}
		if (optionsBounds.contains(
				new Point(Mouse.getX(), Integer.parseInt(Base.settings.getProperty("window.height")) - Mouse.getY()))) {
			optionsHovered = true;
		} else {
			optionsHovered = false;
		}
		if (loadBounds.contains(
				new Point(Mouse.getX(), Integer.parseInt(Base.settings.getProperty("window.height")) - Mouse.getY()))) {

			loadHovered = true;
		} else {
			loadHovered = false;
		}
		if (newBounds.contains(
				new Point(Mouse.getX(), Integer.parseInt(Base.settings.getProperty("window.height")) - Mouse.getY()))) {
			newHovered = true;
		} else {
			newHovered = false;
		}

		if (forwardControlBounds.contains(
				new Point(Mouse.getX(), Integer.parseInt(Base.settings.getProperty("window.height")) - Mouse.getY()))) {
			forwardHovered = true;
		} else {
			forwardHovered = false;
		}

		if (backwardControlBounds.contains(
				new Point(Mouse.getX(), Integer.parseInt(Base.settings.getProperty("window.height")) - Mouse.getY()))) {
			backwardHovered = true;
		} else {
			backwardHovered = false;
		}

		if (leftControlBounds.contains(
				new Point(Mouse.getX(), Integer.parseInt(Base.settings.getProperty("window.height")) - Mouse.getY()))) {
			leftHovered = true;
		} else {
			leftHovered = false;
		}

		if (rightControlBounds.contains(
				new Point(Mouse.getX(), Integer.parseInt(Base.settings.getProperty("window.height")) - Mouse.getY()))) {
			rightHovered = true;
		} else {
			rightHovered = false;
		}

		if (Mouse.isButtonDown(0)) {
			leftMouseCount++;
		} else {
			leftMouseCount = 0;
		}
		if (leftMouseCount == 1) {

			if (optionsHovered && leftMouseCount == 1) {
				System.out.println("options");
				showOptions = !showOptions;
			}
			if (!showOptions) {
				if (loadHovered) {
					System.out.println("load game");
				}
				if (newHovered) {
					System.out.println("new game");
					Base.gameStage = Stage.LOADING;
				}
				if (exitHovered) {
					Base.isRunning = false;
				}
			}

			if (forwardHovered) {
				forwardChange = true;
			}
			if (backwardHovered) {
				backwardChange = true;
			}
			if (leftHovered) {
				leftChange= true;
			}
			if (rightHovered) {
				rightChange=true;
			}
		}

		if (showOptions) {
				while (Keyboard.next()&&(forwardChange||backwardChange||leftChange||rightChange)) {
					char key =  Keyboard.getEventCharacter();
					if(forwardChange)
					{
						Base.settings.setProperty("control.forward",key+"");
						forwardChange=false;
					}
					if(backwardChange)
					{
						Base.settings.setProperty("control.backward",key+"");
						backwardChange=false;
					}if(leftChange)
					{
						Base.settings.setProperty("control.left",key+"");
						leftChange=false;
					}if(rightChange)
					{
						Base.settings.setProperty("control.right",key+"");
						rightChange=false;
					}
				}
		}
	}

	public void render() {
		// Renderer.bindTexture(true);
		Renderer.renderText(100, 100, "Title", 50, Color.black, "Times New Roman");

		if (exitHovered && !showOptions) {
			Renderer.renderSquare(exitBounds.x, exitBounds.y, exitBounds.width, exitBounds.height,
					new Color(0, 0, 0, 0.5f));
		}
		Renderer.renderText(10, Integer.parseInt(Base.settings.getProperty("window.height")) - 50, "Exit", 30,
				Color.white);
		if (optionsHovered || showOptions) {
			Renderer.renderSquare(optionsBounds.x, optionsBounds.y, optionsBounds.width, optionsBounds.height,
					new Color(0, 0, 0, 0.5f));
		}
		Renderer.renderText(10, Integer.parseInt(Base.settings.getProperty("window.height")) - 90, "Options", 30,
				Color.white);
		if (loadHovered && !showOptions) {
			Renderer.renderSquare(loadBounds.x, loadBounds.y, loadBounds.width, loadBounds.height,
					new Color(0, 0, 0, 0.5f));
		}
		Renderer.renderText(10, Integer.parseInt(Base.settings.getProperty("window.height")) - 130, "Load", 30,
				Color.white);
		if (newHovered && !showOptions) {
			Renderer.renderSquare(newBounds.x, newBounds.y, newBounds.width, newBounds.height,
					new Color(0, 0, 0, 0.5f));
		}
		Renderer.renderText(10, Integer.parseInt(Base.settings.getProperty("window.height")) - 170, "New", 30,
				Color.white);

		if (showOptions) {
			Renderer.renderSquare(155, 240, 625, Integer.parseInt(Base.settings.getProperty("window.height")) - 250,
					new Color(0, 0, 0, 0.5f));
			// vsync
			// target fps
			// fullscreen
			// max chunk rendering

			// controls
			// movement
			// combat
			// etc

			// sound

			// telemtry
			// fps
			// render count
			Renderer.renderSquare(160, 200, 150, 40, new Color(0.25f, 0.25f, 0.25f, 0.5f));
			Renderer.renderText(160, 200, "Controls", 30, Color.white);

			if (showControls) {
				if (forwardHovered) {
					Renderer.renderSquare(forwardControlBounds.x, forwardControlBounds.y + 5,
							forwardControlBounds.width, forwardControlBounds.height, new Color(1f, 1f, 1f, 0.5f));
				}

				Renderer.renderText(forwardControlBounds.x, forwardControlBounds.y, "Forward:", 30, Color.white);

				String forwardKey = Base.settings.getProperty("control.forward");
				if (forwardKey == null) {
					Base.settings.setProperty("control.forward", "w");
					forwardKey = Base.settings.getProperty("control.forward");
				}
				Renderer.renderText(forwardControlBounds.x + forwardControlBounds.width - 40, forwardControlBounds.y,
						forwardKey, 30, Color.white);

				if (backwardHovered) {
					Renderer.renderSquare(backwardControlBounds.x, backwardControlBounds.y, backwardControlBounds.width,
							backwardControlBounds.height, new Color(1f, 1f, 1f, 0.5f));
				}

				Renderer.renderText(backwardControlBounds.x, backwardControlBounds.y - 5, "Backward:", 30, Color.white);

				String backwardKey = Base.settings.getProperty("control.backward");
				if (backwardKey == null) {
					Base.settings.setProperty("control.backward", "w");
					backwardKey = Base.settings.getProperty("control.backward");
				}
				Renderer.renderText(backwardControlBounds.x + backwardControlBounds.width - 40,
						backwardControlBounds.y - 5, backwardKey, 30, Color.white);

				if (leftHovered) {
					Renderer.renderSquare(leftControlBounds.x, leftControlBounds.y, leftControlBounds.width,
							leftControlBounds.height, new Color(1f, 1f, 1f, 0.5f));
				}

				Renderer.renderText(leftControlBounds.x, leftControlBounds.y - 5, "Left:", 30, Color.white);

				String leftKey = Base.settings.getProperty("control.left");
				if (leftKey == null) {
					Base.settings.setProperty("control.left", "w");
					leftKey = Base.settings.getProperty("control.left");
				}
				Renderer.renderText(leftControlBounds.x + leftControlBounds.width - 40, leftControlBounds.y - 5,
						leftKey, 30, Color.white);

				if (rightHovered) {
					Renderer.renderSquare(rightControlBounds.x, rightControlBounds.y, rightControlBounds.width,
							rightControlBounds.height, new Color(1f, 1f, 1f, 0.5f));
				}

				Renderer.renderText(rightControlBounds.x, rightControlBounds.y - 5, "Right:", 30, Color.white);

				String rightKey = Base.settings.getProperty("control.right");
				if (rightKey == null) {
					Base.settings.setProperty("control.right", "w");
					rightKey = Base.settings.getProperty("control.right");
				}
				Renderer.renderText(rightControlBounds.x + rightControlBounds.width - 40, rightControlBounds.y - 5,
						rightKey, 30, Color.white);
			}

			Renderer.renderSquare(315, 200, 150, 40, new Color(0.1f, 0.1f, 0.1f, 0.5f));
			Renderer.renderText(345, 203, "Video", 30, Color.white);

			Renderer.renderSquare(470, 200, 150, 40, new Color(0.1f, 0.1f, 0.1f, 0.5f));
			Renderer.renderText(505, 203, "Audio", 30, Color.white);

			Renderer.renderSquare(625, 200, 150, 40, new Color(0.1f, 0.1f, 0.1f, 0.5f));
			Renderer.renderText(670, 203, "Etc", 30, Color.white);

			
			
		}
		String versionString = "Ver:" + Base.settings.getProperty("game.version");
		Renderer.renderText(Integer.parseInt(Base.settings.getProperty("window.width")) - (versionString.length() * 7),
				Integer.parseInt(Base.settings.getProperty("window.height")) - 20, versionString, 12, Color.white);
	}

	public void destroy() {

	}
}
