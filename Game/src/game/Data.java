package game;

import java.util.HashMap;
import java.util.Properties;

import org.newdawn.slick.opengl.Texture;

import classes.GLResourceData;
import classes.GLSpriteData;

public class Data {
	public static Properties settings;
	
	public static HashMap<String, GLSpriteData> sprites = new HashMap<String, GLSpriteData>();
	public static HashMap<String, GLResourceData> resources = new HashMap<String, GLResourceData>();

	public static Texture texture;
	
}
