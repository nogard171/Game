package ui;

import java.awt.Point;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.lwjgl.input.Mouse;

import classes.Building;
import classes.BuildingData;
import classes.Chunk;
import classes.CraftingTable;
import classes.Furnace;
import classes.GroundItem;
import classes.ItemData;
import classes.ItemDrop;
import classes.Object;
import classes.ObjectType;
import classes.Resource;
import classes.ResourceData;
import classes.Skill;
import classes.SkillData;
import data.WorldData;
import utils.APathFinder;
import utils.Tools;
import data.ActionData;
import data.CharacterData;
import data.UIData;

public class EventManager {

	public static LinkedList<Event> events = new LinkedList<Event>();
	public static LinkedList<Event> eventsToRemove = new LinkedList<Event>();

	public static Point start = new Point(0, 0);

	APathFinder pathFinder;
	public Point previous = null;

	public long getTime() {
		return System.currentTimeMillis();
	}

	public void setup() {

		pathFinder = new APathFinder();
	}

	public static void addEvent(Event newEvent) {
		// if (!checkForCraft(newEvent)) {
		events.add(newEvent);
		// }
	}

	public static boolean checkForCraft(Event recipeEvent) {
		boolean isQueued = false;

		if (recipeEvent.eventName.equals("CRAFT_RECIPE")) {
			if (recipeEvent.followUpEvent != null) {
				String recipe = recipeEvent.followUpEvent.eventName;
				for (Event event : events) {
					if (event.eventName.equals("CRAFT_RECIPE")) {
						if (event.followUpEvent != null) {
							if (event.followUpEvent.eventName.equals(recipe)) {
								isQueued = true;
								break;
							}
						}
					}
				}
			}
		}

		return isQueued;

	}

	public static boolean checkForSmelt(Event recipeEvent) {
		boolean isQueued = false;

		if (recipeEvent.eventName.equals("SMELT_RECIPE")) {
			if (recipeEvent.followUpEvent != null) {
				String recipe = recipeEvent.followUpEvent.eventName;
				for (Event event : events) {
					if (event.eventName.equals("SMELT_RECIPE")) {
						if (event.followUpEvent != null) {
							if (event.followUpEvent.eventName.equals(recipe)) {
								isQueued = true;
								break;
							}
						}
					}
				}
			}
		}

		return isQueued;

	}

	public static boolean playerWaiting = true;
	private boolean craftingWaiting = true;
	private boolean smeltingWaiting = true;

	public void update() {
		for (Event event : events) {

			if (!event.setup && playerWaiting) {
				// System.out.println("test: " + event.eventName);
				setupEvent(event);
			}
			if (!event.processed && event.setup) {
				processEvent(event);
			}
			if ((!event.processed && !event.failed) && event.followUpEvent != null && event.setup) {

				if (!event.followUpEvent.processed) {
					processEvent(event);
				} else {
					event.processed = true;
					playerWaiting = true;
				}
			}
			if (event.processed || event.failed) {
				eventsToRemove.add(event);
			}

		}
		events.removeAll(eventsToRemove);
	}

	public void setupEvent(Event event) {

		if (event.eventName.equals("MOVE")) {
			// checkSkills(event.eventName);
			if (start.x == event.end.x && start.y == event.end.y) {
				event.setup = true;
				playerWaiting = true;
				if (event.setup) {
					Event child = event.followUpEvent;
					if (child != null) {
						if (!child.setup) {
							setupEvent(child);
						}
					} else {
						event.processed = true;
					}
				}
			} else {
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
		}
		if (event.eventName.equals("CHOP") || event.eventName.equals("MINE") || event.eventName.equals("HARVEST")) {
			// checkSkills(event.eventName);
			if (event.eventName == "CHOP") {

			}

			event.step = 10;
			event.stepTime = 500;
			event.setup = true;
			playerWaiting = false;
		}
		if (event.eventName.equals("DROP_ITEM")) {
			event.setup = true;
			playerWaiting = false;
		}

		if (event.eventName.equals("PICKUP")) {
			event.setup = true;
			playerWaiting = false;
		}
		if (event.eventName.equals("CRAFT")) {
			event.setup = true;
			playerWaiting = false;
		}
		if (event.eventName.equals("CRAFT_RECIPE") && craftingWaiting) {
			event.setup = true;
			playerWaiting = true;
			craftingWaiting = false;
		}
		if (event.eventName.equals("SMELT")) {
			event.setup = true;
			playerWaiting = false;
		} else if (event.eventName.equals("SMELT_RECIPE") && smeltingWaiting) {
			if (event.followUpEvent != null) {
				event.stepTime = 500;
				event.setup = true;
				playerWaiting = true;
				smeltingWaiting = false;
			}
		}

		if (event.eventName.equals("BUILD") || event.eventName.equals("ERASE")) {
			event.setup = true;
			playerWaiting = false;
		}
	}

	public void checkSkills(String action) {

		for (String key : UIData.skillData.keySet()) {
			SkillData skill = UIData.skillData.get(key);

			if (skill.obtainingAction.equals(action)) {
				if (!CharacterData.obtainedSkills.contains(key) && !CharacterData.skills.containsKey(key)) {
					Skill newSkill = new Skill();
					CharacterData.skills.put(key, newSkill);
				}

			}
		}
		/*
		 * if (!CharacterData.obtainedSkills.contains("WOODCUTTING") &&
		 * !CharacterData.skills.containsKey("WALKING")) {
		 * 
		 * }
		 */
	}

	public void handleEXP(String action) {

		ActionData data = UIData.actionData.get(action);
		if (data != null) {
			SkillData skill = UIData.skillData.get(data.skill);
			if (skill != null) {
				if (!CharacterData.obtainedSkills.contains(data.skill)
						&& !CharacterData.skills.containsKey(data.skill)) {
					Skill newSkill = new Skill();
					CharacterData.skills.put(data.skill, newSkill);
				}
				if (!CharacterData.obtainedSkills.contains(data.skill)
						&& CharacterData.skills.containsKey(data.skill)) {
					Skill newSkill = CharacterData.skills.get(data.skill);
					newSkill.learnCount++;
					if (newSkill.learnCount >= skill.count) {
						newSkill.learned = true;
						CharacterData.obtainedSkills.add(data.skill);
					}
				}

			}
		}
	}

	public void processSkill(String action) {

		for (String key : UIData.skillData.keySet()) {

			SkillData skill = UIData.skillData.get(key);

			if (skill.obtainingAction.equals(action)) {

				if (!CharacterData.obtainedSkills.contains(key) && CharacterData.skills.containsKey(key)) {
					Skill newSkill = CharacterData.skills.get(key);

					newSkill.learnCount++;
					if (newSkill.learnCount >= 10) {
						newSkill.learned = true;
						CharacterData.obtainedSkills.add(key);
					}
				}
			}
		}

	}

	Random r = new Random();

	public void processEvent(Event event) {
		if (event.eventName.equals("DECONSTRUCT")) {
			if (getTime() >= event.startTime) {
				if (event.step >= 0) {
					handleEXP(event.eventName);
					event.startTime = getTime() + event.stepTime;
					event.step++;
					BuildingSystem.deconstruct();
				}
				if (event.step > 100) {

					int hoverX = event.end.x;
					int hoverY = event.end.y;
					int chunkX = hoverX / 16;
					int chunkY = hoverY / 16;
					Chunk chunk = WorldData.chunks.get(chunkX + "," + chunkY);
					if (chunk != null) {
						int objX = event.end.x % 16;
						int objY = event.end.y % 16;

						chunk.maskObjects[objX][objY] = null;

						chunk.needsUpdating();
						
						BuildingSystem.complete();
						event.processed = true;
						event.followUpEvent.processed = true;
						playerWaiting = true;
					}
				}
			}
		}
		if (event.eventName.equals("BUILD")) {
			if (getTime() >= event.startTime) {
				if (event.step >= 0) {
					handleEXP(event.eventName);
					event.startTime = getTime() + event.stepTime;
					event.step++;
					BuildingSystem.construct();
				}
				if (event.step > 100) {

					BuildingSystem.complete();
					if (event.followUpEvent != null) {
						String buildingName = event.followUpEvent.eventName;
						if (buildingName != "") {
							BuildingData data = UIData.buildingData.get(buildingName);
							if (data != null) {
								int hoverX = event.end.x;
								int hoverY = event.end.y;
								int chunkX = hoverX / 16;
								int chunkY = hoverY / 16;

								Chunk chunk = WorldData.chunks.get(chunkX + "," + chunkY);
								if (chunk != null) {
									int objX = event.end.x % 16;
									int objY = event.end.y % 16;

									Building building = new Building();
									int carX = objX * 32;
									int carY = objY * 32;
									int isoX = carX - carY;
									int isoY = (carY + carX) / 2;

									building.setX(isoX);
									building.setY(isoY);

									building.name = BuildingSystem.selectedBuilding;

									chunk.maskObjects[objX][objY] = building;

									chunk.needsUpdating();
									event.followUpEvent.processed = true;
									event.processed = true;
									playerWaiting = true;
								}
							}
						}
					}
				}
			}
		}

		if (event.eventName.equals("CRAFT_RECIPE")) {
			if (getTime() >= event.startTime) {
				if (event.step > 0) {
					handleEXP(event.eventName);
					event.startTime = getTime() + event.stepTime;
					event.step--;
					CraftingSystem.currentCraftTime--;
					CraftingSystem.updateQueuedTime(event.followUpEvent.hash, event.step);
				}
				if (event.step <= 0) {
					CraftingSystem.currentCraftTime = 100;
					if (event.followUpEvent != null) {
						String recipeName = event.followUpEvent.eventName;
						if (recipeName != "") {
							RecipeData data = UIData.recipeData.get(recipeName);
							if (data != null) {
								ItemData item = UIData.itemData.get(data.name);
								if (item != null) {
									// System.out.println("item name: " + item.commonName);
									InventoryItem invItem = new InventoryItem();
									invItem.name = item.name;
									invItem.count = data.minCount;
									invItem.setMaterial(item.inventoryMaterial);
									invItem.setModel("SQUARE");
									boolean hasAdded = CraftingSystem.addToTable(event.followUpEvent.end, invItem);

									if (hasAdded) {
										CraftingSystem.removeQueuedRecipe(event.followUpEvent.hash);
										event.followUpEvent.processed = true;
										event.processed = true;
										craftingWaiting = true;
									}
								}
							}
						}
					}
				}
			}
		}
		if (event.eventName.equals("SMELT_RECIPE")) {
			if (getTime() >= event.startTime) {
				if (event.step > 0) {
					handleEXP(event.eventName);
					event.startTime = getTime() + event.stepTime;
					if (SmeltingSystem.totalFuel > 0) {
						event.step--;

						System.out.println("Fuel: " + SmeltingSystem.totalFuel);
						System.out.println("step: " + event.step);
						SmeltingSystem.totalSmelting++;
						SmeltingSystem.totalFuel--;
						SmeltingSystem.updateQueuedTime(event.followUpEvent.hash, event.step);
					} else {
						SmeltingSystem.addFuel();
					}
				}
				if (event.step <= 0) {
					if (event.followUpEvent != null) {
						String recipeName = event.followUpEvent.eventName;
						if (recipeName != "") {
							RecipeData data = UIData.recipeData.get(recipeName);
							if (data != null) {
								ItemData item = UIData.itemData.get(data.name);
								if (item != null) {
									InventoryItem invItem = new InventoryItem();
									invItem.name = item.name;
									invItem.count = data.minCount;
									invItem.setMaterial(item.inventoryMaterial);
									invItem.setModel("SQUARE");
									boolean hasAdded = SmeltingSystem.addToFurnace(event.followUpEvent.end, invItem);

									if (hasAdded) {
										SmeltingSystem.removeQueuedRecipe(event.followUpEvent.hash);
										SmeltingSystem.smelting = false;
										event.processed = true;
										smeltingWaiting = true;
									}
								}
							}
						}
					}
				}
			}
		}
		if (event.eventName.equals("CRAFT")) {
			CraftingSystem.selectedTable = event.end;
			UserInterface.crafting.showSystem = true;

			System.out.println("test: " + UserInterface.crafting.showSystem);
			int chunkX = event.end.x / 16;
			int chunkY = event.end.y / 16;

			Chunk chunk = WorldData.chunks.get(chunkX + "," + chunkY);
			if (chunk != null) {
				int objX = EventManager.start.x % 16;
				int objY = EventManager.start.y % 16;

				Object obj = chunk.maskObjects[objX][objY];
				if (obj != null) {
					CraftingSystem.table = (CraftingTable) obj;
				}
			}

			event.processed = true;
			playerWaiting = true;
		}
		if (event.eventName.equals("SMELT")) {
			SmeltingSystem.selectedFurnace = event.end;
			UserInterface.smelting.showSystem = true;

			int chunkX = event.end.x / 16;
			int chunkY = event.end.y / 16;

			Chunk chunk = WorldData.chunks.get(chunkX + "," + chunkY);
			if (chunk != null) {
				int objX = EventManager.start.x % 16;
				int objY = EventManager.start.y % 16;

				Object obj = chunk.maskObjects[objX][objY];
				if (obj != null) {
					SmeltingSystem.furnace = (Furnace) obj;
				}
			}
			event.processed = true;
			playerWaiting = true;
		}
		if (event.eventName.equals("PICKUP")) {
			Point objectIndex = event.end;
			if (objectIndex != null) {
				int hoverX = objectIndex.x;
				int hoverY = objectIndex.y;
				int chunkX = hoverX / 16;
				int chunkY = hoverY / 16;

				Chunk chunk = WorldData.chunks.get(chunkX + "," + chunkY);
				if (chunk != null) {
					int objX = EventManager.start.x % 16;
					int objY = EventManager.start.y % 16;

					Object obj = chunk.groundItems[objX][objY];

					if (obj != null) {
						if (obj.type.equals(ObjectType.ITEM)) {
							GroundItem groundItem = (GroundItem) obj;

							if (groundItem.name != "") {
								chunk.groundItems[objX][objY] = null;
								chunk.needsUpdating();
								ItemData itemData = UIData.itemData.get(groundItem.name);

								if (itemData != null) {
									InventoryItem item = new InventoryItem();
									item.setMaterial(itemData.inventoryMaterial);
									item.name = groundItem.name;
									item.count = groundItem.count;
									InventorySystem.addItem(item);
								}

								event.processed = true;
								playerWaiting = true;
							} else {
								event.failed = true;
								playerWaiting = true;
							}
						}
					}
				}
			}
		}
		if (event.eventName.equals("DROP_ITEM")) {
			MouseIndex objectIndex = UserInterface.getHover();
			if (objectIndex != null) {
				int hoverX = objectIndex.getX();
				int hoverY = objectIndex.getY();
				int chunkX = hoverX / 16;
				int chunkY = hoverY / 16;

				Chunk chunk = WorldData.chunks.get(chunkX + "," + chunkY);
				if (chunk != null) {
					int objX = EventManager.start.x % 16;
					int objY = EventManager.start.y % 16;

					Object obj = chunk.groundItems[objX][objY];

					if (obj == null) {
						int index = ((int) event.end.getX()
								+ ((int) event.end.getY() * InventorySystem.size.getWidth()));

						InventoryItem inventoryItem = InventorySystem.items.remove(index);
						if (inventoryItem != null) {

							GroundItem groundItem = new GroundItem();

							ItemData itemData = UIData.itemData.get(inventoryItem.name);

							if (itemData != null) {
								groundItem.name = inventoryItem.name;
								groundItem.count = inventoryItem.count;
								groundItem.setMaterial(itemData.material);
								chunk.groundItems[objX][objY] = groundItem;

								chunk.needsUpdating();
								UserInterface.chat.sendMessage(" has dropped " + inventoryItem.name + ".");
							}

							event.processed = true;
							playerWaiting = true;
						}

					}
				}
			}
		}
		if (event.eventName.equals("CHOP") || event.eventName.equals("MINE") || event.eventName.equals("HARVEST")) {

			if (getTime() >= event.startTime) {
				// processSkill(event.eventName);
				if (event.step > 0) {
					handleEXP(event.eventName);
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

								if (obj.type.equals(ObjectType.RESOURCE)) {
									Resource res = (Resource) obj;
									if (res != null) {
										ResourceData rawRes = UIData.resourceData.get(res.name);
										System.out.println("test");

										if (rawRes != null) {
											if (rawRes.harvestedMaterial == "" && rawRes.harvestedModel == "") {
												rawRes.harvestedMaterial = "AIR";
											}
											if (rawRes.harvestedModel == "") {
												rawRes.harvestedModel = res.getModel();
											}

											for (ItemDrop drop : rawRes.itemDrops) {

												InventoryItem item = new InventoryItem();
												item.name = drop.name;
												if (drop.maxDropCount > 0) {
													item.count = r.nextInt(drop.maxDropCount - drop.minDropCount)
															+ drop.minDropCount;

												} else {
													item.count = drop.minDropCount;
												}

												ItemData itemData = UIData.itemData.get(item.name);
												if (itemData != null) {

													item.setMaterial(itemData.inventoryMaterial);

													InventorySystem.addItem(item);

												}
											}

											obj.setMaterial(rawRes.harvestedMaterial);
											obj.setModel(rawRes.harvestedModel);

											chunk.needsUpdating();
											UserInterface.chat.sendMessage(" has " + event.eventName);
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

		if (event.eventName.equals("MOVE")) {

			if (event.path == null) {

				Event child = event.followUpEvent;
				if (child != null) {
					event.childNeedsProcessed = true;
				} else {
					event.processed = true;
					playerWaiting = true;
				}
				if (event.childNeedsProcessed) {

					if (child != null) {
						if (!child.processed) {

							// System.out.println("test: " + event.childNeedsProcessed);
							processEvent(child);
						}
					}
				}
			}
			if (event.path != null && getTime() >= event.startTime) {
				handleEXP(event.eventName);

				// processSkill(event.eventName);

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
							CharacterData.index = point;

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
