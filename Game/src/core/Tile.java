package core;

import java.awt.Point;
import java.awt.Polygon;

import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Color;

public class Tile {
	protected TextureType type = TextureType.AIR;
	private Vector2f position;
	private Point index;

	public void setIndex(Point newIndex) {
		index = newIndex;
	}

	public Point getIndex() {
		return index;
	}

	public Tile(TextureType newType) {
		setType(newType);
	}

	public TextureType getType() {
		return type;
	}

	public void setType(TextureType type) {
		this.type = type;
	}

	public Vector2f getPosition() {
		return position;
	}

	public void setPosition(Vector2f position) {
		this.position = position;
	}

	public String toHoverString() {
		String str = "";
		switch (type) {
		case TREE:
			str = "Chop !%0,255,0%Tree";
			break;
		case ROCK:
			str = "Mine !%128,128,128%Rock";
			break;
		case COPPER_ORE:
			Resource res = (Resource) this;
			str = "Mine !%128,128,128%" + res.getType();
			break;
		case TIN_ORE:
			res = (Resource) this;
			str = "Mine !%128,128,128%" + res.getType();
			break;
		case BUSH:
			str = "Search !%0,224,0%Bush";
			break;
		case ITEM:
			GroundItem groundItem = (GroundItem) this;
			str = "Pickup !%66,135,245%" + groundItem.type;
			break;
		case FISHING_SPOT:
		case FISHING_SPOT1:
		case FISHING_SPOT2:
		case FISHING_SPOT3:
		case FISHING_SPOT4:

			str = "Fish !%0,224,0%Fishing Spot";
			break;
		default:
			break;
		}
		return str;
	}
}
