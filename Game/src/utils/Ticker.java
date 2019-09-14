package utils;

public class Ticker {
	public long count = 0;
	public long previousCount = 0;
	public int tickCount = 1;
	public int ticks = 0;
	public int previousTick = 0;

	public void update() {
		if (count > previousCount) {
			count = System.currentTimeMillis();
			previousCount = System.currentTimeMillis() + (1000 * tickCount);

			ticks++;
		}
		count = System.currentTimeMillis();
	}

	public boolean hasTicked() {
		boolean ticked = false;
		if (previousTick != ticks) {
			ticked = true;
			previousTick = ticks;
		}
		return ticked;
	}
}
