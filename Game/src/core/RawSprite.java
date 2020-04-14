package core;

import java.awt.Polygon;
import java.util.LinkedList;

import org.lwjgl.util.vector.Vector2f;

public class RawSprite {
	public String name = "";
	public String inheritShape = "";
	public LinkedList<Vector2f> shape = new LinkedList<Vector2f>();
	public LinkedList<Vector2f> texture = new LinkedList<Vector2f>();
}
