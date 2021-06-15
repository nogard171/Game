package core;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.Color;

import game.Base;

public class ChunkManager {

	public static Vector3f size = new Vector3f(16, 32, 16);
	public static LinkedHashMap<String, Chunk> chunks = new LinkedHashMap<String, Chunk>();
	// public static LinkedList<ANode> characters = new LinkedList<ANode>();
	public static LinkedList<ANode> avaliableCharacters = new LinkedList<ANode>();

	public static ArrayList<Chunk> chunksInView = new ArrayList<Chunk>();

	public static int layer = 0;
	APathFinder pathFinder;

	public void setup() {
		pathFinder = new APathFinder();

		for (int x = 0; x < 3; x++) {
			for (int z = 0; z < 3; z++) {
				Chunk chunk = new Chunk(new Vector3f(x, 0, z));
				chunk.load();
				chunks.put(chunk.getIndex(), chunk);
				chunksInView.add(chunk);
			}
		}
		Input.setup();
	}

	public static Object getObjectAt(int x, int y, int z) {
		Object data = null;
		int chunkX = (int) (x / ChunkManager.size.x);
		int chunkZ = (int) (z / ChunkManager.size.z);
		Chunk chunk = chunks.get(chunkX + ",0," + chunkZ);
		if (chunk != null) {
			int objX = (int) (x % size.x);
			int objY = (int) (y % size.y);
			int objZ = (int) (z % size.z);

			data = chunk.getData(objX, objY, objZ);

		}
		return data;
	}

	public static Object getObjectByUUID(UUID uuid) {
		Object obj = null;
		for (Chunk chunk : chunksInView) {
			for (Object object : chunk.renderedObjects) {
				if (object.uuid.equals(uuid)) {
					obj = object;
					break;
				}
			}
		}
		return obj;
	}

	public static void setObjectAt(int x, int y, int z, String type) {
		int chunkX = (int) (x / ChunkManager.size.x);
		int chunkZ = (int) (z / ChunkManager.size.z);
		Chunk chunk = chunks.get(chunkX + ",0," + chunkZ);
		if (chunk != null) {
			int objX = (int) (x % size.x);
			int objY = (int) (y % size.y);
			int objZ = (int) (z % size.z);

			chunk.setDataSprite(objX, objY, objZ, type);
		}
	}

	public static MouseIndex hover;
	int leftCount = 0;
	int startId = 0;
	int endId = 0;
	UUID test;

	public void update() {
		int mouseWheel = Mouse.getDWheel();
		if (mouseWheel > 0) {
			if (layer > 0) {
				layer--;
			}
		} else if (mouseWheel < 0) {
			if (layer <= size.y - 2) {
				layer++;
			}
		}
		Base.hoveredObjects.clear();
		for (Chunk chunk : chunks.values()) {
			chunk.setLayer(layer);
			chunk.update();
		}
		Task task = TaskManager.removeTaskByUUID(test);
		if (task != null) {

			if (task.data != null) {
				if (task.data.path != null) {
					for (int i = 0; i < task.data.path.size(); i++) {
						ANode node = (ANode) task.data.path.get(i);
						if (node != null) {
							setObjectAt(node.x, node.y + 1, node.z, "dirt");
						}
					}
				}
			}
		}
		if (hover != null && Input.isMousePressed(0)) {
			Task moveTask = new Task("MOVE",
					new TaskData(new ANode(hover.getX(), hover.getObjectIndex() - 2, hover.getY())));
			test = moveTask.uuid;
			TaskManager.addTask(moveTask);
		}

		if (hover != null && Input.isMousePressed(1)) {
			System.out.println("Mouse pressed");

		}
	}

	public void render() {
		for (Chunk chunk : chunksInView) {
			// chunk.setLayer(layer);
			// chunk.update();
			chunk.render();
		}
		if (hover != null) {
			int posX = ((hover.getX() - hover.getY()) * 32);
			int posY = 32;
			int posZ = (((hover.getY() + hover.getX()) * 16) + posY);

			for (int i = 0; i < (hover.getObjectIndex() - layer) - 1; i++) {
				GL11.glBegin(GL11.GL_QUADS);
				Renderer.renderSprite("hover3d", posX, (posZ + (i * 32)) + (32 * (layer - 1)));
				GL11.glEnd();
			}

			GL11.glBegin(GL11.GL_QUADS);
			Renderer.renderSprite("hover", posX, posZ + (32 * (layer - 1)));
			GL11.glEnd();

		}
	}

	public void destroy() {

	}

	public static int getRenderCount() {
		int count = 0;
		for (Chunk chunk : chunksInView) {
			count += chunk.renderedObjects.size();
		}
		return count;
	}
}
