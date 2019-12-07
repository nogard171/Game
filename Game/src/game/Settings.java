package game;

public class Settings {
	public static String dataFolder = "data/";
	public static String textureFolder = "textures/";
	
	public static String textureFile = "tileset.png";
	public static String spriteDataFile = "Sprites.xml";

	public static String getDataFolder() {
		return dataFolder;
	}

	public static String getTextureFolder() {
		return dataFolder + textureFolder;
	}

	public static String getSpriteDataFile() {
		return dataFolder + spriteDataFile;
	}
	
	public static String getTextureFile()
	{
		return getTextureFolder() + textureFile;
	}
}
