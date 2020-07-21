package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import classes.ItemDrop;
import classes.RawMaterial;
import classes.RawModel;
import classes.RawResource;
import classes.TextureData;
import classes.TextureType;
import data.Settings;
import data.WorldData;

public class Loader {
	public static void loadMaterials() {
		try {
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
					RawMaterial mat = null;
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
						WorldData.materialData.put(name, mat);
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static RawMaterial loadMaterial(String filename) {
		RawMaterial mat = new RawMaterial();

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

	public static void loadTextures() {
		try {
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
						RawModel raw = loadModel(modelFile);

						WorldData.modelData.put(name, raw);
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static RawModel loadModel(String filename) {
		RawModel raw = new RawModel();

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
					RawResource raw = new RawResource();
					Element resourceElement = (Element) resourceNode;
					String name = resourceElement.getAttribute("name");
					String model = resourceElement.getAttribute("model");
					raw.model = model;

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
					WorldData.resourceData.put(name, raw);
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
