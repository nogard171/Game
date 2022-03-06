package utils;

import core.ActionHandler;

public class Ticker {
	int tickCount = 0;
	long time = 0;
	long tickTime = 0;
	long tickLength = 1000;
	boolean ticked = false;
	public ActionHandler action;

	public Ticker(long newTickLength, ActionHandler newAction) {
		this.action = newAction;
		this.tickLength = newTickLength;
	}

	public void update() {
		ticked = false;
		time = System.currentTimeMillis();
		if (time > tickTime) {
			tickTime = time + this.tickLength;
			tickCount++;
			ticked = true;
		}
		if (ticked) {
			if (this.action != null) {
				this.action.tickUpdate();
			}
		}
	}
}
