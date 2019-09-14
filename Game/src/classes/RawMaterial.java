package classes;

import org.lwjgl.util.vector.Vector2f;

public class RawMaterial {
	public byte[] indices = { 0, 1, 2, 2, 3, 0 };
	public Vector2f[] vectors = { new Vector2f(32, 0), new Vector2f(64, 0), new Vector2f(64, 32),
			new Vector2f(32, 32) };
}
