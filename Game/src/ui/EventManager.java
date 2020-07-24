package ui;

import java.awt.Point;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.lwjgl.input.Mouse;

import classes.Chunk;
import classes.Item;
import classes.ItemDrop;
import classes.Object;
import classes.RawResource;
import classes.Resource;
import data.WorldData;
import utils.APathFinder;

public class EventManager {

	public static LinkedList<Event> events = new LinkedList<Event>();
	public static LinkedList<Event> eventsToRemove = new LinkedList<Event>();

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
			if (event.processed) {
				eventsToRemove.add(event);
			}

		}
		events.removeAll(eventsToRemove);
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
						setupEvent(child);
					}
				}
			}
		}
		if (event.eventName == "CHOP" || event.eventName == "MINE" || event.eventName == "HARVEST") {
			event.step = 10;
			event.stepTime = 500;
			event.setup = true;
			playerWaiting = false;
		}
	}

	Random r = new Random();

	public void processEvent(Event event) {
		if (event.eventName == "CHOP" || event.eventName == "MINE" || event.eventName == "HARVEST") {
			if (getTime() >= event.startTime) {
				if (event.step > 0) {

					event.startTime = getTime() + event.stepTime;
					event.step--;

					if (event.step <= 0) {

						int chunkX = event.end.x / 16;
						int chunkY = event.end.y / 16;
						Chunk chunk = WorldData.chunks.get(chunkX + "," + chunkY);
						if (chunk != null) {

							int objX = event.end.x % 16;
							int objY = event.end.y % 16;

							Object obj = chunk.maskObjects[objX][objY];

							if (obj != null) {

								if (obj.isResource) {
									Resource res = (Resource) obj;
									if (res != null) {
										RawResource rawRes = WorldData.resourceData.get(res.name);

										if (rawRes != null) {
											if (rawRes.harvestedMaterial == "" && rawRes.harvestedModel == "") {
												rawRes.harvestedMaterial = "AIR";
											}
											if (rawRes.harvestedModel == "") {
												rawRes.harvestedModel = res.getModel();
											}

											for (ItemDrop drop : rawRes.itemDrops) {
												Item item = new Item();
												item.name = drop.name;
												if (drop.maxDropCount > 0) {
													item.count = r.nextInt(drop.maxDropCount - drop.minDropCount)
															+ drop.minDropCount;

												} else {
													item.count = drop.minDropCount;
												}
												InventorySystem.addItem(item);
											}

											obj.setMaterial(rawRes.harvestedMaterial);
											obj.setModel(rawRes.harvestedModel);

											chunk.needsUpdating();

										}
									}
								}
							}
						}

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
