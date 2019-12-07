package utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

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

import classes.GLSize;
import classes.GLSpriteData;
import game.Main;
import game.Settings;

public class GLLoader {
	public static void loadSprites() {
		String filename = Settings.getSpriteDataFile();
		float textureWidth = Main.texture.getImageWidth();
		float textureHeight = Main.texture.getImageHeight();

		System.out.println("Width:" + textureHeight);

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
					System.out.println("Name: " + newName);

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
							System.out.println("x:" + texX);
							float texY = Float.parseFloat(eElement2.getAttribute("y")) / textureHeight;
							float texWidth = Float.parseFloat(eElement2.getAttribute("width")) / textureWidth;
							float texHeight = Float.parseFloat(eElement2.getAttribute("height")) / textureHeight;

							newSpriteData.textureData = new Vector4f(texX, texY, texWidth, texHeight);
						}
					}
					spriteData.add(newSpriteData);
				}
			}

		} catch (SAXException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (GLSpriteData singleSpriteData : spriteData) {
			Main.sprites.put(singleSpriteData.name.toUpperCase(), singleSpriteData);
		}
	}
}
