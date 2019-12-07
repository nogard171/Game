package classes;

import org.lwjgl.util.vector.Vector2f;

public class GLView {
	private Vector2f position = new Vector2f(0, 0);
	private GLSize size = new GLSize(0, 0);

	public Vector2f getPosition() {
		return position;
	}

	public void setPosition(Vector2f position) {
		this.position = position;
	}

	public GLSize getSize() {
		return size;
	}

	public void setSize(GLSize size) {
		this.size = size;
	}

	public void move(Vector2f direction) {
		position.x += direction.x;
		position.y += direction.y;
	}
}
