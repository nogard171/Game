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
import utils.Ticker;

public class ChunkManager {

	public static Vector3f size = new Vector3f(16, 16, 16);
	public static LinkedHashMap<String, Chunk> chunks = new LinkedHashMap<String, Chunk>();
	// public static LinkedList<ANode> characters = new LinkedList<ANode>();
	public static LinkedList<ANode> avaliableCharacters = new LinkedList<ANode>();
	public static LinkedList<UUID> avaliableCharacterUUIDs = new LinkedList<UUID>();

	public static ArrayList<Chunk> chunksInView = new ArrayList<Chunk>();

	int layer = 0;
	APathFinder pathFinder;
	Ticker ticker;

	public void setup() {
		ticker = new Ticker(100, new ActionHandler() {
			@Override
			public void tickUpdate() {
				ticked();
			}
		});

		pathFinder = new APathFinder();

		for (int x = 0; x < 10; x++) {
			for (int z = 0; z < 10; z++) {
				Chunk chunk = new Chunk(new Vector3f(x, 0, z));
				chunk.load();
				chunks.put(chunk.getIndex(), chunk);
			}
		}
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

	private void pollHover() {
		int cartX = (Mouse.getX() + Base.view.x) - (32);
		int cartY = ((Window.height - Mouse.getY()) + Base.view.y) - (32 * layer);
		int isoX = (int) ((float) cartX / (float) 2 + ((float) cartY));
		int isoZ = (int) ((float) cartY - (float) cartX / (float) 2);
		int indexX = (int) Math.floor((float) isoX / (float) 32);
		int indexZ = (int) Math.floor((float) isoZ / (float) 32);

		float chunkRawX = (float) indexX / (float) size.x;
		float chunkRawZ = (float) indexZ / (float) size.z;

		int chunkX = (int) Math.floor(chunkRawX);
		int chunkZ = (int) Math.floor(chunkRawZ);
		if (indexX < 0) {
			chunkX--;
		}
		if (indexZ < 0) {
			chunkZ--;
		}
		Chunk chunkTest = chunks.get(chunkX + ",0," + chunkZ);
		Object obj = null;
		if (chunkTest != null) {
			int tempLayer = 0;
			obj = getObjectAt(indexX, tempLayer, indexZ);
			int loopCount = 0;
			boolean isAir = true;
			while (isAir) {
				if (obj != null) {
					if (!obj.getSprite().equals("air")) {
						isAir = false;
					}
				}
				tempLayer++;
				obj = getObjectAt(indexX, tempLayer, indexZ);
				loopCount++;
				if (loopCount > size.y) {
					break;
				}
			}
			Vector3f tempIndex = obj.getIndex();
			float newY = tempIndex.y - (layer + 1);
		}
		if (obj != null) {
			hover = new MouseIndex(indexX, indexZ, chunkX, chunkZ, (int) obj.getIndex().getY());
		}
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

	public static UUID setObjectAt(int x, int y, int z, Object obj) {
		UUID newUUID = null;
		int chunkX = (int) (x / ChunkManager.size.x);
		int chunkZ = (int) (z / ChunkManager.size.z);
		Chunk chunk = chunks.get(chunkX + ",0," + chunkZ);
		if (chunk != null) {
			int objX = (int) (x % size.x);
			int objY = (int) (y % size.y);
			int objZ = (int) (z % size.z);

			Object tempObj = chunk.getData(objX, objY, objZ);
			if (tempObj != null) {
				obj.uuid = tempObj.uuid;
			}
			newUUID = obj.uuid;
			chunk.setData(objX, objY, objZ, obj);
		}
		return newUUID;
	}

	public static MouseIndex hover;
	int leftCount = 0;
	LinkedList<UUID> uuids = new LinkedList<UUID>();
	Task task;

	public void ticked() {
		System.out.println("Move Setup: " + ChunkManager.avaliableCharacterUUIDs);
		if (task != null) {
			if (task.data != null) {
				if (task.data.path != null) {
					if (task.data.path.size() > 0) {
						ANode node = task.data.path.removeFirst();
						if (node != null) {
							Object charaObj = getObjectByUUID(task.characteruuid);
							if (charaObj != null) {
								if (task.data.previousNode != null) {
									setObjectAt(task.data.previousNode.x, task.data.previousNode.y,
											task.data.previousNode.z, "air");
								} else if (task.data.previousNode == null) {
									setObjectAt(node.x, node.y, node.z, "air");
								}
								task.data.previousNode = node;
								charaObj.setIndex(node.toVector3f());
								task.characteruuid = setObjectAt(node.x, node.y, node.z, charaObj);
							}
						}
					}
					if (task.data.path.size() <= 0) {
						System.out.println("Complete");
						ChunkManager.avaliableCharacterUUIDs.add(task.characteruuid);
						task = null;
					}
				}
			}
		}
	}

	public void update() {
		ticker.update();
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
		pollHover();
		if (Base.viewChanged) {
			chunksInView.clear();
			for (Chunk chunk : chunks.values()) {
				if (chunk.chunkBounds.intersects(Base.view)) {
					chunksInView.add(chunk);
				}
			}
		}
		Base.hoveredObjects.clear();
		for (Chunk chunk : chunks.values()) {

			chunk.setLayer(layer);
			chunk.update();
			/*
			 * if (chunk.chunkBounds.contains(Base.mousePosition)) { // chunk.handleHover();
			 * }
			 */
		}

		// move task system to agent manager to handle queueing
		if (hover != null && Mouse.isButtonDown(0) && leftCount == 0) {
			Task moveTask = new Task("MOVE",
					new TaskData(new ANode(hover.getX(), hover.getObjectIndex() - 2, hover.getY())));
			uuids.add(moveTask.uuid);
			TaskManager.addTask(moveTask);
			leftCount++;
		} else if (!Mouse.isButtonDown(0)) {
			leftCount = 0;
		}

		if (uuids.size() > 0) {
			UUID uuid = uuids.removeFirst();
			task = TaskManager.removeTaskByUUID(uuid);
			if (task == null) {
				uuids.add(uuid);
			}
		}
	}

	public void render() {
		for (Chunk chunk : chunksInView) {
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
