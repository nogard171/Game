package utils;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import classes.BuildingData;
import classes.BuildingItem;
import classes.BuildingMaterial;
import classes.ItemData;
import classes.ItemDrop;
import classes.MaterialData;
import classes.ModelData;
import ui.RecipeData;
import ui.RecipeItem;
import classes.ResourceData;
import classes.SkillData;
import classes.TextureData;
import classes.TextureType;
import data.ActionData;
import data.Settings;
import data.UIData;
import data.WorldData;

public class Loader {
	public static void loadMaterials() {
		try {
			UIData.materialData.clear();
			File fXmlFile = new File(Settings.materialsFile);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);

			doc.getDocumentElement().normalize();

			Node texturesNode = doc.getElementsByTagName("materials").item(0);

			if (texturesNode.getNodeType() == Node.ELEMENT_NODE) {

				Element texturesElement = (Element) texturesNode;
				String textureFile = texturesElement.getAttribute("texture_file");
				if (textureFile != null && textureFile != "") {
					WorldData.texture = TextureLoader.getTexture("PNG",
							ResourceLoader.getResourceAsStream(textureFile));
				}
			}
			NodeList textureNodes = doc.getElementsByTagName("material");

			for (int temp = 0; temp < textureNodes.getLength(); temp++) {

				Node textureNode = textureNodes.item(temp);

				if (textureNode.getNodeType() == Node.ELEMENT_NODE) {
					MaterialData mat = null;
					Element textureElement = (Element) textureNode;
					String name = textureElement.getAttribute("name");

					Node dataNodes = textureElement.getElementsByTagName("data").item(0);

					if (dataNodes.getNodeType() == Node.ELEMENT_NODE) {

						Element dataNode = (Element) dataNodes;

						String materialName = dataNode.getAttribute("file");

						mat = loadMaterial(materialName);

					}
					Node offsetNodes = textureElement.getElementsByTagName("offset").item(0);
					if (offsetNodes != null) {
						if (offsetNodes.getNodeType() == Node.ELEMENT_NODE) {

							Element offsetNode = (Element) offsetNodes;

							int offset_X = Integer.parseInt(offsetNode.getAttribute("x"));
							int offset_Y = Integer.parseInt(offsetNode.getAttribute("y"));

							mat.offset = new Vector2f(offset_X, offset_Y);
						}
					}
					if (mat != null) {
						UIData.materialData.put(name, mat);
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static MaterialData loadMaterial(String filename) {
		MaterialData mat = new MaterialData();

		ArrayList<Vector2f> vecs = new ArrayList<Vector2f>();
		ArrayList<Byte> inds = new ArrayList<Byte>();

		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(filename));
			String line = reader.readLine();
			while (line != null) {
				String[] data = line.split(" ");
				if (data[0].contains("tv")) {
					vecs.add(new Vector2f(Float.parseFloat(data[1]), Float.parseFloat(data[2])));

				}
				if (data[0].contains("ti")) {
					inds.add(Byte.parseByte(data[1]));
					inds.add(Byte.parseByte(data[2]));
					inds.add(Byte.parseByte(data[3]));
				}
				line = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		mat.vectors = new Vector2f[vecs.size()];
		for (int i = 0; i < vecs.size(); i++) {
			mat.vectors[i] = vecs.get(i);
		}
		mat.indices = new byte[inds.size()];
		for (int i = 0; i < inds.size(); i++) {
			mat.indices[i] = inds.get(i);
		}

		return mat;
	}

	public static void loadModels() {
		try {
			UIData.modelData.clear();
			File fXmlFile = new File(Settings.textureFile);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);

			doc.getDocumentElement().normalize();

			Node texturesNode = doc.getElementsByTagName("models").item(0);
			NodeList textureNodes = doc.getElementsByTagName("model");

			for (int temp = 0; temp < textureNodes.getLength(); temp++) {

				Node textureNode = textureNodes.item(temp);

				if (textureNode.getNodeType() == Node.ELEMENT_NODE) {

					Element textureElement = (Element) textureNode;
					String name = textureElement.getAttribute("name");

					Node dataNodes = textureElement.getElementsByTagName("data").item(0);

					if (dataNodes.getNodeType() == Node.ELEMENT_NODE) {

						Element dataNode = (Element) dataNodes;

						String modelFile = dataNode.getAttribute("file");
						ModelData raw = loadModel(modelFile);

						UIData.modelData.put(name, raw);
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static ModelData loadModel(String filename) {
		ModelData raw = new ModelData();

		ArrayList<Vector2f> vecs = new ArrayList<Vector2f>();
		ArrayList<Byte> ind = new ArrayList<Byte>();

		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(filename));
			String line = reader.readLine();
			while (line != null) {
				String[] data = line.split(" ");
				if (data[0].contains("v")) {
					vecs.add(new Vector2f(Float.parseFloat(data[1]), Float.parseFloat(data[2])));

				}
				if (data[0].contains("i")) {
					ind.add(Byte.parseByte(data[1]));
					ind.add(Byte.parseByte(data[2]));
					ind.add(Byte.parseByte(data[3]));
				}
				line = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		raw.vectors = new Vector2f[vecs.size()];
		for (int i = 0; i < vecs.size(); i++) {
			raw.vectors[i] = vecs.get(i);
		}
		raw.indices = new byte[ind.size()];
		for (int i = 0; i < ind.size(); i++) {
			raw.indices[i] = ind.get(i);
		}

		return raw;
	}

	public static void loadResources() {
		try {
			UIData.resourceData.clear();
			File fXmlFile = new File(Settings.resourceFile);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);

			doc.getDocumentElement().normalize();

			Node resourcesNode = doc.getElementsByTagName("resources").item(0);
			NodeList resourceNodes = doc.getElementsByTagName("resource");

			for (int temp = 0; temp < resourceNodes.getLength(); temp++) {

				Node resourceNode = resourceNodes.item(temp);

				if (resourceNode.getNodeType() == Node.ELEMENT_NODE) {
					ResourceData raw = new ResourceData();
					Element resourceElement = (Element) resourceNode;
					String name = resourceElement.getAttribute("name");
					if (resourceElement.hasAttribute("action")) {
						String action = resourceElement.getAttribute("action");
						raw.action = action;
					}
					String model = resourceElement.getAttribute("model");
					raw.model = model;
					if (resourceElement.hasChildNodes()) {
						Node dataNodes = resourceElement.getElementsByTagName("data").item(0);

						if (dataNodes.getNodeType() == Node.ELEMENT_NODE) {

							Element dataNode = (Element) dataNodes;

							if (dataNode.hasAttribute("regenerate")) {
								boolean regrow = Boolean.parseBoolean(dataNode.getAttribute("regenerate"));
								raw.regrow = regrow;
							}

							String harvestedMaterial = dataNode.getAttribute("harvestedMaterial");
							String harvestedModel = dataNode.getAttribute("harvestedModel");

							raw.harvestedMaterial = harvestedMaterial;
							raw.harvestedModel = harvestedModel;

							NodeList itemNodes = dataNode.getElementsByTagName("item");

							for (int tempI = 0; tempI < itemNodes.getLength(); tempI++) {
								ItemDrop drop = new ItemDrop();
								Node itemNode = itemNodes.item(tempI);

								if (itemNode.getNodeType() == Node.ELEMENT_NODE) {

									Element itemDataNode = (Element) itemNode;
									String itemName = itemDataNode.getAttribute("name");
									String itemCount = itemDataNode.getAttribute("count");
									if (itemCount != "") {
										if (itemCount.contains("-")) {
											String[] itemCounts = itemCount.split("-");
											drop.minDropCount = Integer.parseInt(itemCounts[0]);
											drop.maxDropCount = Integer.parseInt(itemCounts[1]);
										} else {
											drop.minDropCount = Integer.parseInt(itemCount);
										}
									}
									drop.name = itemName;

								}
								raw.itemDrops.add(drop);
							}
						}
					}
					UIData.resourceData.put(name, raw);
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void loadItems() {
		try {
			UIData.itemData.clear();
			File fXmlFile = new File(Settings.itemFile);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);

			doc.getDocumentElement().normalize();

			Node resourcesNode = doc.getElementsByTagName("items").item(0);
			NodeList resourceNodes = doc.getElementsByTagName("item");

			for (int temp = 0; temp < resourceNodes.getLength(); temp++) {

				Node resourceNode = resourceNodes.item(temp);

				if (resourceNode.getNodeType() == Node.ELEMENT_NODE) {
					ItemData raw = new ItemData();
					Element resourceElement = (Element) resourceNode;
					String name = resourceElement.getAttribute("name");
					raw.name = name;
					if (resourceElement.hasAttribute("commonName")) {
						String commonName = resourceElement.getAttribute("commonName");
						raw.commonName = commonName;
					}
					if (resourceElement.hasAttribute("durability")) {
						int durability = Integer.parseInt(resourceElement.getAttribute("durability"));
						raw.durability = durability;
					}

					Node dataNodes = resourceElement.getElementsByTagName("data").item(0);

					if (dataNodes.getNodeType() == Node.ELEMENT_NODE) {

						Element dataNode = (Element) dataNodes;

						String material = dataNode.getAttribute("material");
						raw.material = material;

						String inventoryMaterial = dataNode.getAttribute("inventory_material");
						raw.inventoryMaterial = inventoryMaterial;

						int stackSize = 1;
						if (dataNode.hasAttribute("stack_size")) {
							stackSize = Integer.parseInt(dataNode.getAttribute("stack_size"));
						}
						raw.stackSize = stackSize;

						int value = Integer.parseInt(dataNode.getAttribute("value"));
						raw.value = value;

						if (dataNode.hasAttribute("fuel_amount")) {
							int fuelAmount = Integer.parseInt(dataNode.getAttribute("fuel_amount"));
							raw.fuelAmount = fuelAmount;
						}
						if (dataNode.hasAttribute("smelt_time")) {
							int smeltTime = Integer.parseInt(dataNode.getAttribute("smelt_time"));
							raw.smeltTime = smeltTime;
						}
					}
					UIData.itemData.put(name, raw);
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void loadSkills() {
		try {
			UIData.skillData.clear();
			File fXmlFile = new File(Settings.skillFile);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);

			doc.getDocumentElement().normalize();

			// Node resourcesNode = doc.getElementsByTagName("skills").item(0);
			NodeList resourceNodes = doc.getElementsByTagName("skill");

			for (int temp = 0; temp < resourceNodes.getLength(); temp++) {

				Node resourceNode = resourceNodes.item(temp);

				if (resourceNode.getNodeType() == Node.ELEMENT_NODE) {
					SkillData raw = new SkillData();
					Element resourceElement = (Element) resourceNode;
					String name = resourceElement.getAttribute("name");
					if (resourceElement.hasAttribute("description")) {
						String description = resourceElement.getAttribute("description");
						raw.description = description;
					}
					if (resourceElement.hasAttribute("material")) {
						String material = resourceElement.getAttribute("material");
						raw.material = material;
					}

					Node dataNodes = resourceElement.getElementsByTagName("obtain").item(0);

					if (dataNodes.getNodeType() == Node.ELEMENT_NODE) {

						Element dataNode = (Element) dataNodes;

						String action = dataNode.getAttribute("action");

						raw.obtainingAction = action;

						if (dataNode.hasAttribute("count")) {
							int count = Integer.parseInt(dataNode.getAttribute("count"));
							raw.count = count;
						}

					}
					if (resourceElement.getElementsByTagName("xp").getLength() > 0) {
						dataNodes = resourceElement.getElementsByTagName("xp").item(0);

						if (dataNodes.getNodeType() == Node.ELEMENT_NODE) {

							Element dataNode = (Element) dataNodes;

							String formula = dataNode.getAttribute("formula");

							raw.formula = formula;

						}
					}
					UIData.skillData.put(name, raw);
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void loadRecipes() {
		try {

			UIData.recipeData.clear();
			File fXmlFile = new File(Settings.recipesFile);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);

			doc.getDocumentElement().normalize();

			// Node resourcesNode = doc.getElementsByTagName("skills").item(0);
			NodeList recipeNodes = doc.getElementsByTagName("recipe");

			for (int temp = 0; temp < recipeNodes.getLength(); temp++) {

				Node recipeNode = recipeNodes.item(temp);

				if (recipeNode.getNodeType() == Node.ELEMENT_NODE) {
					RecipeData data = new RecipeData();

					Element recipeElement = (Element) recipeNode;
					String name = recipeElement.getAttribute("name");
					data.name = name;
					if (recipeElement.hasAttribute("count")) {

						String itemCount = recipeElement.getAttribute("count");
						if (itemCount != "") {
							if (itemCount.contains("-")) {
								String[] itemCounts = itemCount.split("-");
								data.minCount = Integer.parseInt(itemCounts[0]);
								data.maxCount = Integer.parseInt(itemCounts[1]);
							} else {
								data.minCount = Integer.parseInt(itemCount);
							}
						}
					} else {
						data.minCount = 1;
					}

					if (recipeElement.hasAttribute("craft_time")) {

						int craftTime = Integer.parseInt(recipeElement.getAttribute("craft_time"));
						data.craftTime = craftTime;
					}
					if (recipeElement.hasAttribute("level")) {

						int level = Integer.parseInt(recipeElement.getAttribute("level"));
						data.requiredLevel = level;
					}

					if (recipeElement.hasAttribute("skill")) {

						String skill = recipeElement.getAttribute("skill");
						data.requiredSkill = skill;
					}
					NodeList itemNodes = recipeElement.getElementsByTagName("item");
					String comboItems = "";
					for (int itemTemp = 0; itemTemp < itemNodes.getLength(); itemTemp++) {

						Node itemNode = itemNodes.item(itemTemp);

						if (itemNode.getNodeType() == Node.ELEMENT_NODE) {
							RecipeItem item = new RecipeItem();

							Element resourceElement = (Element) itemNode;
							String itemName = resourceElement.getAttribute("name");

							if (resourceElement.hasAttribute("reuse")) {
								Boolean itemReuse = Boolean.parseBoolean(resourceElement.getAttribute("reuse"));
								item.reuse = itemReuse;
							}
							item.itemName = itemName;

							if (comboItems == "") {
								comboItems = itemName;
							} else {
								comboItems += "+" + itemName;
							}

							if (resourceElement.hasAttribute("count")) {

								int itemCount2 = Integer.parseInt(resourceElement.getAttribute("count"));
								item.itemCount = itemCount2;
							} else {
								item.itemCount = 1;
							}
							data.items.add(item);
						}
					}
					UIData.recipeData.put(name, data);
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void loadBuildings() {
		try {
			UIData.buildingData.clear();
			File fXmlFile = new File(Settings.buildingsFile);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);

			doc.getDocumentElement().normalize();

			// Node resourcesNode = doc.getElementsByTagName("skills").item(0);
			NodeList itemNodes = doc.getElementsByTagName("item");

			for (int temp = 0; temp < itemNodes.getLength(); temp++) {

				Node itemNode = itemNodes.item(temp);

				if (itemNode.getNodeType() == Node.ELEMENT_NODE) {
					BuildingData data = new BuildingData();

					Element itemElement = (Element) itemNode;
					String name = itemElement.getAttribute("name");
					data.name = name;

					if (itemElement.hasAttribute("model")) {
						String model = itemElement.getAttribute("model");
						data.model = model;
					}
					if (itemElement.hasAttribute("type")) {
						String type = itemElement.getAttribute("type");
						data.type = type;
					}

					NodeList materialsNodes = itemElement.getElementsByTagName("material");
					for (int materialTemp = 0; materialTemp < materialsNodes.getLength(); materialTemp++) {

						Node materialNode = materialsNodes.item(materialTemp);

						if (materialNode.getNodeType() == Node.ELEMENT_NODE) {
							BuildingMaterial mat = new BuildingMaterial();

							Element materialElement = (Element) materialNode;
							String matName = materialElement.getAttribute("name");
							mat.name = matName;

							if (materialElement.hasChildNodes()) {
								NodeList offsetNodes = materialElement.getElementsByTagName("offset");
								if (offsetNodes.getLength() > 0) {
									Node offsetNode = offsetNodes.item(0);

									if (offsetNode.getNodeType() == Node.ELEMENT_NODE) {
										Element offsetElement = (Element) offsetNode;
										int offsetX = Integer.parseInt(offsetElement.getAttribute("x"));
										int offsetY = Integer.parseInt(offsetElement.getAttribute("y"));
										mat.offset = new Point(offsetX, offsetY);
									}
								}

								NodeList itemsNodes = materialElement.getElementsByTagName("item");
								for (int itemTemp = 0; itemTemp < itemsNodes.getLength(); itemTemp++) {

									Node itemCountNode = itemsNodes.item(0);

									if (itemCountNode.getNodeType() == Node.ELEMENT_NODE) {
										Element itemCountElement = (Element) itemCountNode;

										String itemName = itemCountElement.getAttribute("name");
										if (itemCountElement.hasAttribute("inherit")) {
											String inheritBuildingName = itemCountElement.getAttribute("inherit");
											BuildingData inheritData = UIData.buildingData.get(inheritBuildingName);
											if (inheritData != null) {
												for (BuildingItem buildingItem : inheritData.items) {
													data.items.add(buildingItem);
												}
											}
										} else {
											System.out.println("building: " + itemName);
											int itemCount = Integer.parseInt(itemCountElement.getAttribute("count"));

											BuildingItem item = new BuildingItem();
											item.itemName = itemName;
											item.itemCount = itemCount;

											data.items.add(item);
										}
									}
								}
							}

							data.materials.add(mat);
						}
					}
					System.out.println("test: " + name);
					UIData.buildingData.put(name, data);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void loadActions() {
		try {
			UIData.actionData.clear();
			File fXmlFile = new File(Settings.actionsFile);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);

			doc.getDocumentElement().normalize();

			// Node resourcesNode = doc.getElementsByTagName("skills").item(0);
			NodeList itemNodes = doc.getElementsByTagName("action");

			for (int temp = 0; temp < itemNodes.getLength(); temp++) {

				Node itemNode = itemNodes.item(temp);

				if (itemNode.getNodeType() == Node.ELEMENT_NODE) {
					ActionData data = new ActionData();

					Element itemElement = (Element) itemNode;
					String name = itemElement.getAttribute("name");

					if (itemElement.hasAttribute("xp")) {
						int xp = Integer.parseInt(itemElement.getAttribute("xp"));
						data.xp = xp;
					}

					if (itemElement.hasAttribute("skill")) {
						String skill = itemElement.getAttribute("skill");
						data.skill = skill;
					}

					UIData.actionData.put(name, data);
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
