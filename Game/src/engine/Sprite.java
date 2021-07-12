package engine;

import java.awt.Point;
import java.awt.Polygon;

import org.lwjgl.util.vector.Vector2f;

public class Sprite {

	public Polygon shape;
	public Polygon texture;
	public Polygon[][] textures;
	public Point offset = new Point(0,0);
}
