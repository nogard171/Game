package core;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.lwjgl.util.vector.Vector2f;

import classes.GLAction;
import classes.GLCharacter;
import classes.GLIndex;
import classes.GLItem;
import classes.GLObject;
import classes.GLResource;
import classes.GLTask;
import classes.GLType;

public class GLQueueManager {
	public static LinkedList<GLTask> tasks = new LinkedList<GLTask>();
	private ArrayList<GLTask> completedTasks = new ArrayList<GLTask>();
	public static LinkedList<GLIndex> allCharacters = new LinkedList<GLIndex>();
	public static LinkedList<GLIndex> waitingCharacters = new LinkedList<GLIndex>();
	private HashMap<GLIndex, GLTask> startedTasks = new HashMap<GLIndex, GLTask>();
	private int tickCount = 0;
	private int previousTick = 0;
	private boolean ticked = false;
	private long currentTime = 0;
	private long endTime = -1;
	private int tickTime = 200;
	APathFinder pathFinder;

	public void setup() {

		pathFinder = new APathFinder();

		GLChunk chunk = GLChunkManager.chunks.get(new GLIndex(0, 0, 0, 0, 0, 0));

		GLCharacter obj = new GLCharacter(GLType.CHARACTER);
		GLIndex index = new GLIndex(6, 0, 5, 0, 0, 0);
		chunk.setObject(index, obj);
		waitingCharacters.add(index);
		allCharacters.add(index);

		GLCharacter obj2 = new GLCharacter(GLType.CHARACTER);
		GLIndex index2 = new GLIndex(10, 0, 5, 0, 0, 0);
		chunk.setObject(index2, obj2);
		waitingCharacters.add(index2);
		allCharacters.add(index2);
	}

	public static void addTask(GLTask newTask) {
		if (!endExists(newTask)) {
			tasks.add(newTask);
		} else {
			if (waitingCharacters.size() > 0) {
				GLIndex waitingCharacterIndex = waitingCharacters.removeFirst();

				int posX = (waitingCharacterIndex.x - waitingCharacterIndex.z) * 32;
				int posY = (waitingCharacterIndex.y - 1) * 32;
				int posZ = ((waitingCharacterIndex.z + waitingCharacterIndex.x) * 16) + posY;

				GLUIManager.showMessage(new Vector2f(posX, posZ), "Position being serviced.");

				waitingCharacters.add(waitingCharacterIndex);
			}
		}
	}

	public static boolean endExists(GLTask checkTask) {
		boolean exists = false;
		for (GLTask task : tasks) {
			if (!task.complete) {
				if (task.endIndex.equals(checkTask.endIndex)) {
					exists = true;
					break;
				}
			}
		}
		return exists;
	}

	public void update() {
		currentTime = System.currentTimeMillis();
		if (tickCount > previousTick) {
			previousTick = tickCount;
			ticked = true;
		} else {
			ticked = false;
		}
		if (ticked) {
			for (GLTask task : tasks) {
				if (!task.complete) {
					boolean setup = setupTask(task);
					if (setup) {
						process(task);
					}
				} else {
					completedTasks.add(task);
				}
			}
			tasks.removeAll(completedTasks);
		}
		if (currentTime > endTime) {
			tickCount++;
			endTime = System.currentTimeMillis() + (tickTime);
		}
	}

	public boolean setupTask(GLTask task) {
		boolean ready = false;
		if (task.action == GLAction.MOVE) {
			if (!task.started) {
				if (waitingCharacters.size() > 0) {
					GLIndex waitingCharacterIndex = waitingCharacters.removeFirst();
					if (waitingCharacterIndex != null) {
						GLChunk chunk = GLChunkManager.chunks.get(new GLIndex(0, 0, 0, waitingCharacterIndex.chunkX,
								waitingCharacterIndex.chunkY, waitingCharacterIndex.chunkZ));
						if (chunk != null) {
							GLIndex objIndex = new GLIndex(waitingCharacterIndex.x, waitingCharacterIndex.y,
									waitingCharacterIndex.z, waitingCharacterIndex.chunkX, waitingCharacterIndex.chunkY,
									waitingCharacterIndex.chunkZ);
							GLObject obj = chunk.objects.get(objIndex);
							if (obj != null) {
								task.startIndex = objIndex;

								GLIndex index = new GLIndex(waitingCharacterIndex.x, waitingCharacterIndex.y,
										waitingCharacterIndex.z, waitingCharacterIndex.chunkX,
										waitingCharacterIndex.chunkY, waitingCharacterIndex.chunkZ);
								GLObject charaObj = chunk.objects.get(index);
								if (charaObj.getType() == GLType.CHARACTER) {
									task.character = (GLCharacter) chunk.objects.get(index);
								}
								
								ready = true;
								startedTasks.put(task.endIndex, task);
							}
						}
					}
				}
			} else {
				ready = task.started;
			}
		}

		if (task.action == GLAction.CHOP) {
			ready = true;
		}
		if (task.action == GLAction.MINE) {
			ready = true;
		}
		return ready;
	}

	private void process(GLTask task) {
		if (task.action == GLAction.MOVE) {
			if (task.startIndex != null && task.endIndex != null && task.character != null) {

				if (task.path == null) {
					task.path = pathFinder.find(task.startIndex, task.endIndex);

					task.step = 0;
					task.started = true;
				}
				if (task.path != null) {
					if (task.step > 0) {
						Point previousP = (Point) task.path.get(task.step - 1);

						GLObject obj = new GLObject(GLType.AIR);

						int cx = (int) Math.floor(previousP.x / (int) GLChunkManager.chunkSize.x);
						int cy = (int) Math.floor(previousP.y / (int) GLChunkManager.chunkSize.z);

						int nx = (int) (previousP.x % GLChunkManager.chunkSize.x);
						int ny = (int) (previousP.y % GLChunkManager.chunkSize.z);

						GLIndex index = new GLIndex(nx, 0, ny, cx, 0, cy);

						GLChunk chunk = GLChunkManager.chunks.get(new GLIndex(0, 0, 0, cx, 0, cy));

						if (chunk != null) {

							chunk.setObject(index, obj);
						}
					}
					if (task.step == 0) {

						GLObject obj = new GLObject(GLType.AIR);

						int cx = (int) Math.floor(task.startIndex.x / (int) GLChunkManager.chunkSize.x);
						int cy = (int) Math.floor(task.startIndex.y / (int) GLChunkManager.chunkSize.z);

						int nx = (int) (task.startIndex.x % GLChunkManager.chunkSize.x);
						int ny = (int) (task.startIndex.y % GLChunkManager.chunkSize.z);

						GLIndex index = new GLIndex(nx, 0, ny, cx, 0, cy);

						GLChunk chunk = GLChunkManager.chunks.get(new GLIndex(0, 0, 0, cx, 0, cy));
						if (chunk != null) {

							chunk.setObject(index, obj);
						}
					}
					if (task.path.size() > task.step) {
						Point p = (Point) task.path.get(task.step);

						int cx = (int) Math.floor(p.x / (int) GLChunkManager.chunkSize.x);
						int cy = (int) Math.floor(p.y / (int) GLChunkManager.chunkSize.z);

						int nx = (int) (p.x % GLChunkManager.chunkSize.x);
						int ny = (int) (p.y % GLChunkManager.chunkSize.z);
						
						System.out.println("test123: " + cx + "," + cy);

						if (task.character != null) {
							GLCharacter obj = task.character;
							GLIndex index = new GLIndex(nx, 0, ny, cx, 0, cy);
							GLChunk chunk = GLChunkManager.chunks.get(new GLIndex(0, 0, 0, cx, 0, cy));

							if (chunk != null) {

								chunk.setObject(index, obj);
							}
						}
						task.step++;
					}
				}
			}
		}
		if (task.path != null) {
			if (task.step >= task.path.size()) {
				System.out.println("test: " + task.isLead);
				if (!task.isLead) {
					startedTasks.remove(task.endIndex);
					waitingCharacters.add(task.endIndex);
				}
				task.complete = true;
				task.started = false;
			}
		}

		if (task.action == GLAction.CHOP) {
			GLTask leadTask = startedTasks.get(task.leadIndex);
			if (leadTask != null) {
				if (leadTask.complete) {
					GLChunk chunk = GLChunkManager.chunks.get(
							new GLIndex(0, 0, 0, task.endIndex.chunkX, task.endIndex.chunkY, task.endIndex.chunkZ));
					if (chunk != null) {
						GLResource obj = (GLResource) chunk.objects.get(task.endIndex);
						if (obj != null) {
							if (task.step >= 10) {
								obj.setType(obj.getGrowType());

								GLObject charaObj = chunk.objects.get(task.leadIndex);
								if (charaObj.getType() == GLType.CHARACTER) {
									GLCharacter chara = (GLCharacter) charaObj;
									chara.addItem(new GLItem(obj.getItemType()));
								}

								startedTasks.remove(task.leadIndex);
								waitingCharacters.add(task.leadIndex);
								task.complete = true;
							}
							task.step++;
						}
					}
				}
			}
		}

		if (task.action == GLAction.MINE) {
			GLTask leadTask = startedTasks.get(task.leadIndex);
			if (leadTask != null) {
				if (leadTask.complete) {
					GLChunk chunk = GLChunkManager.chunks.get(
							new GLIndex(0, 0, 0, task.endIndex.chunkX, task.endIndex.chunkY, task.endIndex.chunkZ));
					if (chunk != null) {
						GLResource obj = (GLResource) chunk.objects.get(task.endIndex);
						if (obj != null) {
							System.out.println("step: " + task.step);
							if (task.step >= 10) {
								obj.setType(obj.getGrowType());
								GLObject charaObj = chunk.objects.get(task.leadIndex);
								if (charaObj.getType() == GLType.CHARACTER) {
									GLCharacter chara = (GLCharacter) charaObj;
									chara.addItem(new GLItem(obj.getItemType()));
								}
								startedTasks.remove(task.leadIndex);

								waitingCharacters.add(task.leadIndex);
								task.complete = true;
							}
							task.step++;
						}

					}
				}
			}
		}

	}
}
