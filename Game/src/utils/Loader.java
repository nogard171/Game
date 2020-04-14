package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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

import core.RawSprite;
import game.Base;
import game.Main;

public class Loader {

	public static void loadSettings(String file) throws IOException {
		Properties newSettings = new Properties();
		InputStream is = null;
		is = new FileInputStream(file);
		newSettings.load(is);

		Base.settings = newSettings;
	}

	public static HashMap<String, RawSprite> loadSprites() {
		if (Base.settings != null) {
			String tilesetFile = Base.settings.getProperty("assets.sprites");
			HashMap<String, RawSprite> rawSprites = new HashMap<String, RawSprite>();

			try {
				File inputFile = new File(tilesetFile);
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(inputFile);
				doc.getDocumentElement().normalize();
				NodeList tilesetList = doc.getElementsByTagName("sprites");

				Node tilesetNode = tilesetList.item(0);

				if (tilesetNode.getNodeType() == Node.ELEMENT_NODE) {
					Element tilesetElement = (Element) tilesetNode;

					NodeList tileList = tilesetElement.getElementsByTagName("sprite");
					for (int tileTemp = 0; tileTemp < tileList.getLength(); tileTemp++) {
						Node tileNode = tileList.item(tileTemp);

						RawSprite raw = new RawSprite();

						if (tileNode.getNodeType() == Node.ELEMENT_NODE) {
							Element tileElement = (Element) tileNode;
							String name = tileElement.getAttribute("name");
							raw.name = name;

							System.out.println("Name: " + name);

							NodeList shapeList = tileElement.getElementsByTagName("shape");
							Node shapeNode = shapeList.item(0);

							if (shapeNode.getNodeType() == Node.ELEMENT_NODE) {
								Element shapeElement = (Element) shapeNode;
								String inherit = shapeElement.getAttribute("inherit");

								if (inherit == "") {
									System.out.println("No Inherit");
									NodeList vectorList = shapeElement.getElementsByTagName("vector");
									for (int vectorTemp = 0; vectorTemp < vectorList.getLength(); vectorTemp++) {
										Node vectorNode = vectorList.item(vectorTemp);

										if (vectorNode.getNodeType() == Node.ELEMENT_NODE) {
											Element vectorElement = (Element) vectorNode;

											int x = Integer.parseInt(vectorElement.getAttribute("x"));
											int y = Integer.parseInt(vectorElement.getAttribute("y"));

											raw.shape.add(new Vector2f(x, y));
											System.out.println("Shape: " + x + "," + y);
										}
									}
								} else {
									System.out.println("Inherit: " + inherit);
									raw.inheritShape = inherit;
								}

							}
							NodeList textureList = tileElement.getElementsByTagName("texture");
							Node textureNode = textureList.item(0);

							if (textureNode.getNodeType() == Node.ELEMENT_NODE) {
								Element textureElement = (Element) textureNode;

								NodeList vectorList = textureElement.getElementsByTagName("vector");
								for (int vectorTemp = 0; vectorTemp < vectorList.getLength(); vectorTemp++) {
									Node vectorNode = vectorList.item(vectorTemp);

									if (vectorNode.getNodeType() == Node.ELEMENT_NODE) {
										Element vectorElement = (Element) vectorNode;

										int x = Integer.parseInt(vectorElement.getAttribute("x"));
										int y = Integer.parseInt(vectorElement.getAttribute("y"));

										raw.texture.add(new Vector2f(x, y));
										System.out.println("Texture: " + x + "," + y);
									}
								}

							}
						}
						rawSprites.put(raw.name, raw);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return rawSprites;
		}
		return null;
	}
}
