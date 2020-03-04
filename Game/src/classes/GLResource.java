package classes;

import utils.GLTicker;

public class GLResource extends GLObject {

	private long tickEnd = -1;
	private long tickLength = 10;
	private boolean triggered = false;
	private GLType growType = GLType.EMPTY_ORE;
	private GLType growthType = GLType.IRON_ORE;

	public GLResource(GLType newType) {
		super(newType);
	}

	public GLResource(GLType newType, GLType newGrowType) {
		super(newType);
		growType = newGrowType;
		growthType = newType;
	}

	public GLType getGrowType() {
		return growType;
	}

	public GLType getGrowthType() {
		return growthType;
	}

	public void update() {
		if (this.getType() == growType && tickEnd <= -1) {
			tickEnd = GLTicker.tickCount + tickLength;
			triggered = false;
		}

		if (GLTicker.tickCount > tickEnd && !triggered) {
			if (this.getType() == growType) {
				this.setType(growthType);
				triggered = true;
				tickEnd = -1;
			}
		}
	}

	public GLType getItemType() {

		return growthType;
	}

}
