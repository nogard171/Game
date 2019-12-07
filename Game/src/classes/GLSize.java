package classes;

public class GLSize {
	private float width = 0;
	private float height = 0;

	public GLSize(float newWidth, float newHeight) {
		this.setSize(newWidth, newHeight);
	}

	public void setSize(float newWidth, float newHeight) {
		this.width = newWidth;
		this.height = newHeight;
	}

	public float getWidth() {
		return this.width;
	}

	public float getHeight() {
		return this.height;
	}
}
