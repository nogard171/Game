package core;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import classes.GLIndex;
import classes.GLObject;
import classes.GLTask;
import classes.GLType;

public class GLQueueManager {
	private static ArrayList<GLTask> tasks = new ArrayList<GLTask>();
	private ArrayList<GLIndex> waitingCharacters = new ArrayList<GLIndex>();
	private int tickCount = 0;
	private int previousTick = 0;
	private boolean ticked = false;
	private long currentTime = 0;
	private long endTime = -1;
	private int tickTime = 100;
	APathFinder pathFinder;

	public void setup() {
		pathFinder = new APathFinder();

		GLChunk chunk = GLChunkManager.chunks.get(new GLIndex(0, 0, 0, 0, 0, 0));

		GLObject obj = new GLObject(GLType.CHARACTER);
		GLIndex index = new GLIndex(6, 0, 5, 0, 0, 0);
		chunk.setObject(index, obj);
		waitingCharacters.add(index);
	}

	public static void addTask(GLTask newTask) {
		tasks.add(newTask);
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
			System.out.println("Ticked");
			for (GLTask task : tasks) {
				if (!task.complete) {
					boolean setup = setupTask(task);
					if (setup) {
						process(task);
					}
				}
			}
		}
		if (currentTime > endTime) {
			tickCount++;
			endTime = System.currentTimeMillis() + (tickTime);
		}
	}

	public boolean setupTask(GLTask task) {
		boolean ready = false;
		if (waitingCharacters.size() > 0) {
			GLIndex waitingCharacterIndex = waitingCharacters.remove(0);
			if (waitingCharacterIndex != null) {
				GLChunk chunk = GLChunkManager.chunks.get(new GLIndex(waitingCharacterIndex.chunkX,
						waitingCharacterIndex.chunkY, waitingCharacterIndex.chunkZ));
				if (chunk != null) {
					GLIndex objIndex = new GLIndex(waitingCharacterIndex.x, waitingCharacterIndex.y,
							waitingCharacterIndex.z);
					GLObject obj = chunk.objects.get(objIndex);
					if (obj != null) {
						task.startIndex = objIndex;
						ready = true;
					}
				}
			}
		}
		if (task.started) {
			ready = task.started;
		}
		return ready;
	}

	private void process(GLTask task) {
		if (task.startIndex != null && task.endIndex != null) {
			GLChunk chunk = GLChunkManager.chunks
					.get(new GLIndex(task.startIndex.chunkX, task.startIndex.chunkY, task.startIndex.chunkZ));
			if (chunk != null) {

				if (task.path == null) {
					task.path = pathFinder.find(chunk, new Point(task.startIndex.x, task.startIndex.z),
							new Point(task.endIndex.x, task.endIndex.z));
					task.step = 0;
					task.started = true;
				}
				if (task.path != null) {
					if (task.step > 0) {
						System.out.println("move: " + task.step);
						Point previousP = (Point) task.path.get(task.step - 1);
						GLObject obj = new GLObject(GLType.AIR);
						GLIndex index = new GLIndex(previousP.x, 0, previousP.y, 0, 0, 0);
						chunk.setObject(index, obj);

					}
					if (task.step == 0) {
						System.out.println("start: " + task.startIndex.x+","+task.startIndex.z);
						GLObject obj = new GLObject(GLType.AIR);
						GLIndex index = new GLIndex(task.startIndex.x, 0, task.startIndex.z, 0, 0, 0);
						chunk.setObject(index, obj);
					}

					Point p = (Point) task.path.get(task.step);
					GLObject obj = new GLObject(GLType.CHARACTER);
					GLIndex index = new GLIndex(p.x, 0, p.y, 0, 0, 0);
					chunk.setObject(index, obj);
					task.step++;

				}
			}
		}
		if (task.path != null) {
			if (task.step >= task.path.size()) {
				System.out.println("Complete");
				waitingCharacters.add(task.endIndex);
				task.complete = true;
				task.started = false;
			}
		} else {
			System.out.println("Complete");
			waitingCharacters.add(task.startIndex);
			task.complete = true;
			task.started = false;
		}
	}
}
