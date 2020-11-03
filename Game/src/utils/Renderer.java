package utils;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

import classes.Face;

public class Renderer {
	public static void renderFace(Face face) {
		if (face.direction.equals(new Vector3f(0, 1, 0))) {
			float size = 1f;

			GL11.glBegin(GL11.GL_QUADS);
			GL11.glColor3f(0.5f, 0.75f, 0.5f);
			// top
			GL11.glVertex3f(face.position.x + size, face.position.y, face.position.z);
			GL11.glVertex3f(face.position.x, face.position.y, face.position.z);
			GL11.glVertex3f(face.position.x, face.position.y, face.position.z + size);
			GL11.glVertex3f(face.position.x + size, face.position.y, face.position.z + size);
			float differnce = -0.0005f;
			GL11.glColor3f(0.5f, 0.2f, 0f);
			// bottom
			GL11.glVertex3f(face.position.x + size, face.position.y + differnce, face.position.z + size);
			GL11.glVertex3f(face.position.x, face.position.y + differnce, face.position.z + size);
			GL11.glVertex3f(face.position.x, face.position.y + differnce, face.position.z);
			GL11.glVertex3f(face.position.x + size, face.position.y + differnce, face.position.z);

			GL11.glEnd();
		}
		if (face.direction.equals(new Vector3f(0, -1, 0))) {
			float size = 1f;

			GL11.glBegin(GL11.GL_QUADS);
			GL11.glColor3f(0.5f, 0.2f, 0f);
			// top
			GL11.glVertex3f(face.position.x + size, face.position.y, face.position.z);
			GL11.glVertex3f(face.position.x, face.position.y, face.position.z);
			GL11.glVertex3f(face.position.x, face.position.y, face.position.z + size);
			GL11.glVertex3f(face.position.x + size, face.position.y, face.position.z + size);
			float differnce = -0.0005f;
			GL11.glColor3f(0.5f, 0.75f, 0.5f);
			// bottom
			GL11.glVertex3f(face.position.x + size, face.position.y + differnce, face.position.z + size);
			GL11.glVertex3f(face.position.x, face.position.y + differnce, face.position.z + size);
			GL11.glVertex3f(face.position.x, face.position.y + differnce, face.position.z);
			GL11.glVertex3f(face.position.x + size, face.position.y + differnce, face.position.z);

			GL11.glEnd();
		}
	}
}
