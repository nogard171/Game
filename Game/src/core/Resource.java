package core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

public class Resource extends Object {
	private int health;
	private int animationIndex = 0;
	private TextureType baseType = TextureType.AIR;
	private boolean animated = false;
	public int[] animatedID;

	public boolean isAnimated() {
		return animated;
	}

	public Resource(TextureType newType) {
		super(newType);
		health = 1;
		baseType = newType;
	}

	public Resource(TextureType newType, int newHealth) {
		super(newType);
		health = newHealth;
		baseType = newType;
	}

	@Override
	public void setType(TextureType newType) {
		this.type = newType;
		if (baseType == TextureType.AIR) {
			baseType = newType;
		}
	}

	public void setRawType(TextureType newType) {
		this.type = newType;
	}

	public void setType(TextureType newType, int newHealth) {

		setType(newType);
		health = newHealth;
	}

	public int getHealth() {
		return health;
	}

	public LinkedList<ANode> getANode(ANode node) {

		LinkedList<ANode> nodePath = new LinkedList<ANode>();

		for (int h = 0; h < health; h++) {
			nodePath.add(node);
		}

		return nodePath;
	}

	public int getAnimationIndex() {
		return animationIndex;
	}

	public void setAnimationIndex(int animationIndex) {
		this.animationIndex = animationIndex;
	}

	public void cycleAnimation() {
		ResourceData data = GameDatabase.resources.get(baseType);
		int index = animationIndex;
		if (data != null) {
			if (index < data.animationTypes.length - 1) {
				index++;
			} else {
				index = 0;
			}
		}
		animationIndex = index;

	}

	public TextureType getBaseType() {
		return baseType;
	}

	public void setAnimated(boolean b) {
		this.animated = b;

		ResourceData data = GameDatabase.resources.get(baseType);
		if (data != null) {
			animatedID = new int[data.animationTypes.length];
			for (int i = 0; i < animatedID.length; i++) {
				animatedID[i] = -1;
				System.out.println("test: " + i);
			}

			int r = 0 + (int) (Math.random() * (((data.animationTypes.length - 1) - 0) + 1));
			animationIndex = r;
		}
	}

	public int getAnimatedID() {
		return animatedID[animationIndex];
	}

	public void setHealth(int newHealth) {
		this.health = newHealth;
	}
}
