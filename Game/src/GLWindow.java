import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JFrame;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Point;

public class GLWindow {
	// display things
	DisplayMode _initDisplay = null;
	int _defaultWidth = 800;
	int _defaultHeight = 600;
	int _width = 800;
	int _height = 600;
	int _fps = 9999;
	boolean Fullscreen = false;
	boolean resizable = true;
	boolean vsync;
	// fps things
	long lastFrame;
	int fps;
	int currentFPS = 0;
	long lastFPS;
	// inputs
	MouseInput mouse = null;
	KeyboardInput keyboard = null;

	public void start() {
		Init();
		keyboard = new KeyboardInput();
		mouse = new MouseInput();
		while (!Display.isCloseRequested()) {
			this.sync(getTime());
			if (Display.wasResized()) {
				this.Resized();
			}
			int delta = getDelta();
			this.Update(delta);
			this.Render();
			Display.update();
			Display.sync(this._fps);
		}
		this.Destroy();
	}

	public void Destroy() {
		Display.destroy();
		System.exit(0);
	}

	private void sync(double loopStartTime) {
		float loopSlot = 1f / 50;
		double endTime = loopStartTime + loopSlot;
		while (getTime() < endTime) {
			try {
				Thread.sleep(1);
			} catch (InterruptedException ie) {
			}
		}
	}

	public void Update(int delta) {
		System.out.println("FPS:" + this.currentFPS);
		this.keyboard.startPoll();
		this.mouse.poll(this._height);
		this.updateFPS(); // update FPS Counter
	}

	public void Resized() {
		this._height = Display.getHeight();
		this._width = Display.getWidth();

		GL11.glViewport(0, 0, this._width, this._height);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, this._width, this._height, 0, 1, -1);
	}

	public void setDisplayMode(int width, int height, boolean fullscreen) {
		// return if requested DisplayMode is already set
		if ((Display.getDisplayMode().getWidth() == width) && (Display.getDisplayMode().getHeight() == height)
				&& (Display.isFullscreen() == fullscreen)) {
			return;
		}

		try {
			DisplayMode targetDisplayMode = null;

			if (fullscreen) {
				DisplayMode[] modes = Display.getAvailableDisplayModes();
				int freq = 0;

				for (int i = 0; i < modes.length; i++) {
					DisplayMode current = modes[i];

					if ((current.getWidth() == width) && (current.getHeight() == height)) {
						if ((targetDisplayMode == null) || (current.getFrequency() >= freq)) {
							if ((targetDisplayMode == null)
									|| (current.getBitsPerPixel() > targetDisplayMode.getBitsPerPixel())) {
								targetDisplayMode = current;
								freq = targetDisplayMode.getFrequency();
							}
						}

						// if we've found a match for bpp and frequence against
						// the
						// original display mode then it's probably best to go
						// for this one
						// since it's most likely compatible with the monitor
						if ((current.getBitsPerPixel() == Display.getDesktopDisplayMode().getBitsPerPixel())
								&& (current.getFrequency() == Display.getDesktopDisplayMode().getFrequency())) {
							targetDisplayMode = current;
							break;
						}
					}
				}
			} else {
				targetDisplayMode = new DisplayMode(width, height);
			}

			if (targetDisplayMode == null) {
				System.out.println("Failed to find value mode: " + width + "x" + height + " fs=" + fullscreen);
				return;
			}

			Display.setDisplayMode(targetDisplayMode);
			Display.setFullscreen(fullscreen);

		} catch (LWJGLException e) {
			System.out.println("Unable to setup mode " + width + "x" + height + " fullscreen=" + fullscreen + e);
		}
	}

	/**
	 * Calculate how many milliseconds have passed since last frame.
	 * 
	 * @return milliseconds passed since last frame
	 */
	public int getDelta() {
		long time = this.getTime();
		int delta = (int) (time - this.lastFrame);
		this.lastFrame = time;
		return delta;
	}

	/**
	 * Get the accurate system time
	 * 
	 * @return The system time in milliseconds
	 */
	public long getTime() {
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}

	/**
	 * Calculate the FPS and set it in the title bar
	 */
	public void updateFPS() {
		if (this.getTime() - this.lastFPS > 1000) {
			// Display.setTitle("FPS: " + fps);
			this.currentFPS = this.fps;
			this.fps = 0;
			this.lastFPS += 1000;
		}
		this.fps++;
	}

	public void initGL() {
		GL11.glClearColor(1, 1, 1, 1);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, this._width, this._height, 0, 1, -1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

	}

	public void Render() {
		// Clear The Screen And The Depth Buffer
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

	}

	public void changeDispalyMode(int width, int height, boolean fullscreen) {

		this.Fullscreen = fullscreen;
		this._width = width;
		this._height = height;
		this.setDisplayMode(this._width, this._height, this.Fullscreen);
		this.Resized();
	}

	public void ToggleFullscreen() {

		this.Fullscreen = !this.Fullscreen;
		this._width = Display.getDesktopDisplayMode().getWidth();
		this._height = Display.getDesktopDisplayMode().getHeight();
		if (!this.Fullscreen) {
			this._width = _defaultWidth;
			this._height = _defaultHeight;
		}
		this.setDisplayMode(this._width, this._height, this.Fullscreen);
		this.Resized();
	}

	public void Init() {
		// set the initial display, so the game can revert back to original
		// display.
		this._initDisplay = Display.getDisplayMode();
		try {
			// set the display to the display width and display height
			this.setDisplayMode(this._width, this._height, this.Fullscreen);
			// set the display resizable if the resizable is true
			Display.setResizable(resizable);
			// create the display.
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(0);
		}
		// init opengl
		this.initGL();
		this.getDelta(); // call once before loop to initialise lastFrame
		lastFPS = getTime(); // call before loop to initialise fps timer
	}
}
