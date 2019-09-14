package data;

import java.util.HashMap;

import org.newdawn.slick.opengl.Texture;

import classes.RawMaterial;
import classes.RawModel;

public class MaterialData {
	public static Texture texture;
	public static HashMap<String, RawMaterial> materialData = new HashMap<String, RawMaterial>();
}
