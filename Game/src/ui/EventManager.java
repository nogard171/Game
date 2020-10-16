package ui;

import java.awt.Point;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.lwjgl.input.Mouse;

import classes.Chunk;
import classes.GroundItem;
import classes.ItemData;
import classes.ItemDrop;
import classes.Object;
import classes.Resource;
import classes.ResourceData;
import classes.Skill;
import classes.SkillData;
import data.WorldData;
import utils.APathFinder;
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
		if (!checkForCraft(newEvent)) {
			events.add(newEvent);
		}
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

	public static boolean playerWaiting = true;

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
			checkSkills(event.eventName);
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
			checkSkills(event.eventName);
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
		} else if (event.eventName.equals("CRAFT_RECIPE")) {
			event.setup = true;
			playerWaiting = true;
		} else if (UserInterface.crafting.showSystem) {
			UserInterface.crafting.showSystem = false;
		}

		if (event.eventName.equals("SMELT")) {
			event.setup = true;
			playerWaiting = false;
		} else if (event.eventName.equals("SMELT_RECIPE")) {
			event.stepTime = 500;
			String recipeName = event.followUpEvent.eventName;
			if (recipeName != "") {
				RecipeData data = UIData.recipeData.get(recipeName);
				if (data != null) {
					ItemData item = WorldData.itemData.get(data.name);
					if (item != null) {
						for (SmeltingSlot slot : SmeltingSystem.slots) {
							if (slot.slotItem != null) {
								ItemData recipeItemData = WorldData.itemData.get(slot.slotItem.name);
								if (recipeItemData != null) {
									slot.slotItem.smeltTime = recipeItemData.smeltTime;
								}
							}
						}
					}
				}
			}

			if (SmeltingSystem.fuelSlot.slotItem != null) {
				ItemData data = WorldData.itemData.get(SmeltingSystem.fuelSlot.slotItem.name);
				if (data != null) {
					SmeltingSystem.fuel = data.fuelAmount;
					SmeltingSystem.currentFuel = SmeltingSystem.fuel;
					if (SmeltingSystem.fuelSlot.slotItem.count > 1) {
						SmeltingSystem.fuelSlot.slotItem.count--;
					} else {
						SmeltingSystem.fuelSlot.slotItem = null;
					}
				}
			}

			event.setup = true;
			playerWaiting = true;
		} else if (UserInterface.smelting.showSystem) {
			UserInterface.smelting.showSystem = false;
		}
	}

	public void checkSkills(String action) {

		for (String key : WorldData.skillData.keySet()) {
			SkillData skill = WorldData.skillData.get(key);

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

	public void processSkill(String action) {

		for (String key : WorldData.skillData.keySet()) {

			SkillData skill = WorldData.skillData.get(key);

			if (skill.obtainingAction.equals(action)) {

				if (!CharacterData.obtainedSkills.contains(key) && CharacterData.skills.containsKey(key)) {
					// System.out.println("test: " + skill.obtainingAction + "/" + action);
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
		if (event.eventName.equals("CRAFT_RECIPE")) {
			if (getTime() >= event.startTime) {
				if (event.step > 0) {
					event.startTime = getTime() + event.stepTime;
					event.step--;
					CraftingSystem.currentCraftTime--;
				}
				if (event.step <= 0) {

					if (event.followUpEvent != null) {
						String recipeName = event.followUpEvent.eventName;
						if (recipeName != "") {
							RecipeData data = UIData.recipeData.get(recipeName);
							if (data != null) {
								ItemData item = WorldData.itemData.get(data.name);
								if (item != null) {
									int recipeItemCount = 0;
									for (CraftingSlot slot : CraftingSystem.slots) {
										if (slot.slotItem != null) {
											ItemData recipeItemData = WorldData.itemData.get(slot.slotItem.name);
											if (recipeItemData != null) {
												for (RecipeItem recipeItem : data.items) {
													if (recipeItemData.name.equals(recipeItem.itemName)) {
														recipeItemCount++;
														if (recipeItem.reuse) {
															InventoryItem reusedItem = slot.slotItem;
															reusedItem.durability -= 1;
															if (reusedItem.durability > 0) {
																InventorySystem.addItem(reusedItem);
															}
															slot.slotItem = null;
														} else if (slot.slotItem.count > 1) {
															InventoryItem reusedItem = slot.slotItem;
															reusedItem.count--;
															InventorySystem.addItem(reusedItem);
															slot.slotItem = null;
														} else {
															slot.slotItem = null;
														}
													}
												}
											}
										}
									}
									if (recipeItemCount == data.items.size()) {
										InventoryItem craftedItem = new InventoryItem();
										craftedItem.name = data.name;
										ItemData itemData = WorldData.itemData.get(craftedItem.name);
										if (itemData != null) {
											craftedItem.durability = itemData.durability;
											craftedItem.setMaterial(item.inventoryMaterial);
											// InventorySystem.addItem(craftedItem);

											//CraftingSystem.finalSlot.slotItem = craftedItem;
										}
									}

									CraftingSystem.craftTime = 0;
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
		if (event.eventName.equals("SMELT_RECIPE")) {
			if (getTime() >= event.startTime) {
				System.out.println("step: " + event.step);
				if (event.step > 0) {
					event.startTime = getTime() + event.stepTime;
					event.step--;

					String recipeName = event.followUpEvent.eventName;
					if (recipeName != "") {
						RecipeData data = UIData.recipeData.get(recipeName);
						if (data != null) {
							System.out.println("smelt time: " + data.name);
							ItemData item = WorldData.itemData.get(data.name);
							if (item != null) {
								// working on event smelting
								for (SmeltingSlot slot : SmeltingSystem.slots) {
									if (slot.slotItem != null) {
										ItemData recipeItemData = WorldData.itemData.get(slot.slotItem.name);
										if (recipeItemData != null) {
											slot.slotItem.smeltTime--;
											if (slot.slotItem.smeltTime <= 0) {
												if (SmeltingSystem.smeltedItem != null) {
													if (SmeltingSystem.smeltedItem.name.equals(slot.slotItem.name)) {
														if (slot.slotItem.count == 1) {
															slot.slotItem = null;
														} else {
															slot.slotItem.count--;
														}
														SmeltingSystem.smeltedItem.count++;
													}
												}
											}
										}
									}
								}
							}
						}
					}

					if (SmeltingSystem.currentFuel > 0) {
						SmeltingSystem.currentFuel--;
						if (SmeltingSystem.currentFuel <= 0) {
							if (SmeltingSystem.fuelSlot.slotItem != null) {
								ItemData data = WorldData.itemData.get(SmeltingSystem.fuelSlot.slotItem.name);
								if (data != null) {
									SmeltingSystem.fuel = data.fuelAmount;
									SmeltingSystem.currentFuel = SmeltingSystem.fuel;
									if (SmeltingSystem.fuelSlot.slotItem.count > 1) {
										SmeltingSystem.fuelSlot.slotItem.count--;
									} else {
										SmeltingSystem.fuelSlot.slotItem = null;
									}
								}
							}
						}
					}
				}
				/*
				 * if (event.step <= 0) { if (event.followUpEvent != null) { String recipeName =
				 * event.followUpEvent.eventName; if (recipeName != "") { RecipeData data =
				 * UIData.recipeData.get(recipeName); if (data != null) { ItemData item =
				 * WorldData.itemData.get(data.name); if (item != null) { int recipeItemCount =
				 * 0; for (SmeltingSlot slot : SmeltingSystem.slots) { if (slot.slotItem !=
				 * null) { ItemData recipeItemData = WorldData.itemData.get(slot.slotItem.name);
				 * if (recipeItemData != null) { for (RecipeItem recipeItem : data.items) { if
				 * (recipeItemData.name.equals(recipeItem.itemName)) { recipeItemCount++; if
				 * (recipeItem.reuse) { InventoryItem reusedItem = slot.slotItem;
				 * reusedItem.durability -= 1; if (reusedItem.durability > 0) {
				 * InventorySystem.addItem(reusedItem); } slot.slotItem = null; } else if
				 * (slot.slotItem.count > 1) { InventoryItem reusedItem = slot.slotItem;
				 * reusedItem.count--; InventorySystem.addItem(reusedItem); slot.slotItem =
				 * null; } else { slot.slotItem = null; } } } } } } // if (fuelSlot.slotItem !=
				 * null) { if (recipeItemCount == data.items.size()) { InventoryItem craftedItem
				 * = new InventoryItem(); craftedItem.name = data.name; ItemData itemData =
				 * WorldData.itemData.get(craftedItem.name); if (itemData != null) {
				 * craftedItem.durability = itemData.durability;
				 * craftedItem.setMaterial(item.inventoryMaterial);
				 * InventorySystem.addItem(craftedItem); } } event.followUpEvent.processed =
				 * true; event.processed = true; playerWaiting = true; }
				 * 
				 * } } } }
				 */
			}
		}
		if (event.eventName.equals("CRAFT")) {
			UserInterface.crafting.showSystem = true;

			event.processed = true;
			playerWaiting = true;
		}
		if (event.eventName.equals("SMELT")) {
			UserInterface.smelting.showSystem = true;

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
						if (obj.isItem) {
							GroundItem groundItem = (GroundItem) obj;

							if (groundItem.name != "") {
								chunk.groundItems[objX][objY] = null;
								chunk.needsUpdating();
								ItemData itemData = WorldData.itemData.get(groundItem.name);

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

							ItemData itemData = WorldData.itemData.get(inventoryItem.name);

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
				processSkill(event.eventName);
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
										ResourceData rawRes = WorldData.resourceData.get(res.name);

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

												ItemData itemData = WorldData.itemData.get(item.name);
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

				processSkill(event.eventName);

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
