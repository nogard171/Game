package classes;

import org.lwjgl.util.vector.Vector3f;

public class Face {
	public Vector3f direction = new Vector3f(0, 0, 0);
	public Vector3f position = new Vector3f(0, 0, 0);

	public Face(Vector3f newDir, Vector3f pos) {
		direction = newDir;
		position = pos;
	}

}
