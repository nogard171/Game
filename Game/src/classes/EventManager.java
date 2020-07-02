package classes;

import java.awt.Point;
import java.util.LinkedList;
import java.util.List;

import org.lwjgl.input.Mouse;

import data.WorldData;
import utils.APathFinder;

public class EventManager {

	public static LinkedList<Event> events = new LinkedList<Event>();

	public Point start = new Point(0, 0);

	APathFinder pathFinder;
	public Point previous = null;

	public long getTime() {
		return System.currentTimeMillis();
	}

	public void setup() {

		pathFinder = new APathFinder();
	}

	public static void addEvent(Event newEvent) {
		events.add(newEvent);
	}

	boolean playerWaiting = true;

	public void update() {
		for (Event event : events) {
			if (!event.setup && playerWaiting) {
				setupEvent(event);
			}
			if (!event.processed && event.setup) {
				processEvent(event);
			}
			if (event.followUpEvent != null) {
				if (!event.followUpEvent.processed) {
					processEvent(event);
				} else {
					event.processed = true;
				}
			}
		}
		for (Event event : events) {
			if (event.processed) {
				events.remove(event);
			}
		}
	}

	public void setupEvent(Event event) {
		if (event.eventName == "MOVE") {
			event.path = pathFinder.find(start, event.end);
			event.setup = true;
			playerWaiting = false;

			if (event.setup) {
				Event child = event.followUpEvent;
				if (child != null) {

					if (!child.setup) {
						System.out.println("setup Child");
						setupEvent(child);
					}
				}
			}
		}
		if (event.eventName == "CHOP") {
			event.step = 10;
			event.stepTime = 2000;
			event.setup = true;
			playerWaiting = false;
		}
	}

	public void processEvent(Event event) {
		if (event.eventName == "CHOP") {
			if (getTime() >= event.startTime) {
				if (event.step > 0) {

					System.out.println("Processing Child: " + event.step);
					event.startTime = getTime() + event.stepTime;
					event.step--;

					if (event.step <= 0) {
						System.out.println("complete chop");
						event.processed = true;
						playerWaiting = true;
					}
				}
			}
		}
		if (event.eventName == "MOVE") {
			if (event.path != null && getTime() >= event.startTime) {
				if (event.path.size() > 0) {
					if (previous != null) {

						int chunkX = previous.x / 16;
						int chunkY = previous.y / 16;
						Chunk chunk = WorldData.chunks.get(chunkX + "," + chunkY);

						if (chunk != null) {

							int previousX = previous.x % 16;
							int previousY = previous.y % 16;

							System.out.println("Point: " + chunkX + "," + chunkY + "/" + previousX + "," + previousY);

							chunk.entityObjects[previousX][previousY].setMaterial("AIR");
							chunk.needsUpdating();
						}
					}

					Point point = (Point) event.path.get(event.step);
					if (point != null) {

						int chunkX = point.x / 16;
						int chunkY = point.y / 16;
						Chunk chunk = WorldData.chunks.get(chunkX + "," + chunkY);
						if (chunk != null) {

							int objX = point.x % 16;
							int objY = point.y % 16;

							chunk.entityObjects[objX][objY].setMaterial("PLAYER");
							chunk.needsUpdating();
							previous = point;
						}

						event.startTime = getTime() + event.stepTime;
						event.step++;
					}
					int max = event.path.size() - 1;
					Event child = event.followUpEvent;
					if (child != null) {
						max = event.path.size() - 2;
					}
					if (event.step > max) {
						event.path.clear();
						event.step = 0;
						event.startTime = getTime() + event.stepTime;
						start = event.end;
						System.out.println("complete");
						if (child != null) {
							event.childNeedsProcessed = true;
						} else {
							event.processed = true;
							playerWaiting = true;
						}
					}
				}
			}
			if (event.childNeedsProcessed) {
				Event child = event.followUpEvent;
				if (child != null) {
					if (!child.processed) {
						processEvent(child);
					}
				}
			}
		}

	}
}
