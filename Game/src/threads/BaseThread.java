package threads;

import utils.Input;
import utils.Logger;
import utils.Window;

public class BaseThread {

	private static boolean isRunning = true;

	public void run() {
		Logger.log("tesT");
		this.setup();
		while (this.isRunning) {
			this.update();
			this.render();
		}
		this.clean();
	}

	public void setup() {
		Window.setup();
		Input.setup();
	}

	public void update() {
		Window.update();
		if (Window.initilizedClosing()) {
			this.isRunning = false;
		}
	}

	public void render() {
		Window.render();

	}

	public void clean() {
		Input.clean();
		Window.clean();
	}

	public static void close() {
		isRunning = false;
	}
}
