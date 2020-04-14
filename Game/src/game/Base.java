package game;

import core.Window;

public class Base {

	boolean isRunning = true;

	public void start() {
		this.setup();

		while (this.isRunning) {
			this.update();

			this.render();
		}
		this.destroy();

	}

	public void setup() {
		Window.start();
		Window.setup();

	}

	public void update() {
		Window.update();
		if (Window.close()) {
			this.isRunning = false;
		}
	}

	public void render() {
		Window.render();
	}

	public void destroy() {
		Window.destroy();
	}
}
