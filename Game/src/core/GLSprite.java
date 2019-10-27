package core;

import org.lwjgl.opengl.GL11;

public class GLSprite {
	private String name = "";
	private GLFrame frame = null;

	public GLSprite(String newName) {
		this.name = newName;
	}

	public void setFrame(GLFrame newFrame) {
		this.frame = newFrame;
	}

	public void renderSprite() {
		GL11.glCallList(frame.getDL());
	}
}
