package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector4f;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import classes.GLResourceData;
import classes.GLSize;
import classes.GLSpriteData;
import game.Data;
import game.Main;

public class GLLoader {

	public static void loadSettings(String file) throws IOException {
		Properties newSettings = new Properties();
		InputStream is = null;
		is = new FileInputStream(file);
		newSettings.load(is);
		Data.settings = newSettings;
	}

	public static void loadSprites() {
		String filename = Data.settings.getProperty("assets.sprites");
		float textureWidth = Data.texture.getImageWidth();
		float textureHeight = Data.texture.getImageHeight();
		ArrayList<GLSpriteData> spriteData = new ArrayList<GLSpriteData>();
		try {
			File fXmlFile = new File(filename);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();
			NodeList nList = doc.getElementsByTagName("sprite");
			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					GLSpriteData newSpriteData = new GLSpriteData();
					Element eElement = (Element) nNode;
					String newName = eElement.getAttribute("name");
					newSpriteData.name = newName;
					NodeList nList2 = eElement.getElementsByTagName("size");
					if (nList2 != null) {
						Node nNode2 = nList2.item(0);
						if (nNode2.getNodeType() == Node.ELEMENT_NODE) {
							Element eElement2 = (Element) nNode2;
							int sizeWidth = Integer.parseInt(eElement2.getAttribute("width"));
							int sizeHeight = Integer.parseInt(eElement2.getAttribute("height"));
							newSpriteData.size = new GLSize(sizeWidth, sizeHeight);
						}
					}
					NodeList nList3 = eElement.getElementsByTagName("offset");
					if (nList3 != null) {
						Node nNode3 = nList3.item(0);
						if (nNode3 != null) {
							if (nNode3.getNodeType() == Node.ELEMENT_NODE) {
								Element eElement2 = (Element) nNode3;
								float texX = Float.parseFloat(eElement2.getAttribute("x"));
								float texY = Float.parseFloat(eElement2.getAttribute("y"));
								newSpriteData.offset = new Vector2f(texX, texY);
							}
						}
					}
					NodeList nList4 = eElement.getElementsByTagName("texture");
					if (nList4 != null) {
						Node nNode4 = nList4.item(0);
						if (nNode4.getNodeType() == Node.ELEMENT_NODE) {
							Element eElement2 = (Element) nNode4;
							float texX = Float.parseFloat(eElement2.getAttribute("x")) / textureWidth;
							float texY = Float.parseFloat(eElement2.getAttribute("y")) / textureHeight;
							float texWidth = Float.parseFloat(eElement2.getAttribute("width")) / textureWidth;
							float texHeight = Float.parseFloat(eElement2.getAttribute("height")) / textureHeight;
							newSpriteData.textureData = new Vector4f(texX, texY, texWidth, texHeight);
						}
					}
					// spriteData.add(newSpriteData);
					Data.sprites.put(newSpriteData.name.toUpperCase(), newSpriteData);
				}
			}
		} catch (SAXException | IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		/*
		 * for (GLSpriteData singleSpriteData : spriteData) {
		 * Data.sprites.put(singleSpriteData.name.toUpperCase(), singleSpriteData); }
		 */
	}

	public static void loadResources() {
		String filename = Data.settings.getProperty("assets.resources");
		ArrayList<GLResourceData> resourceData = new ArrayList<GLResourceData>();

		try {
			File fXmlFile = new File(filename);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();
			NodeList nList = doc.getElementsByTagName("resource");
			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					GLResourceData newResourceData = new GLResourceData();
					Element eElement = (Element) nNode;
					newResourceData.name = eElement.getAttribute("name").toUpperCase();
					newResourceData.empty = eElement.getAttribute("empty").toUpperCase();
					String actions = eElement.getAttribute("actions");
					if (actions.contains(",")) {
						String[] actionsList = actions.split(",");
						for (String action : actionsList) {
							newResourceData.actions.add(action);
						}
					}
					else
					{
						newResourceData.actions.add(actions);
					}

					newResourceData.tickLength = Integer.parseInt(eElement.getAttribute("tickLength"));
					System.out.println("name:" + newResourceData.name);

					Data.resources.put(newResourceData.name, newResourceData);
				}
			}
		} catch (SAXException | IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}

	}
}
