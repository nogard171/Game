package threads;

import utils.Ticker;

public class BaseThread extends Thread {
	private Ticker ticker;

	private static boolean isRunning = true;

	public void run() {
		this.setup();
		while (this.isRunning) {
			this.update();
			this.render();
		}
		this.clean();
	}

	public void setup() {
		this.ticker = new Ticker();

	}

	public void update() {
		this.ticker.update();
		if (this.ticker.hasTicked()) {

		}
	}

	public void render() {

	}

	public void clean() {

	}

	public static void close() {
		isRunning = false;
	}
}
