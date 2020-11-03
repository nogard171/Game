package data;

import classes.Size;
import classes.SkillData;
import utils.WorldGenerator;

import java.util.HashMap;

import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.opengl.Texture;

import classes.BuildingData;
import classes.Chunk;
import classes.Index;
import classes.ItemData;
import classes.Object;
import classes.MaterialData;
import classes.ModelData;
import classes.ResourceData;

public class WorldData {
	public static Texture texture;

	public static HashMap<String, Chunk> chunks = new HashMap<String, Chunk>();

	public static Chunk getChunk(int x, int z) {
		Chunk chunk = null;

		if (x > WorldGenerator.chunkIndex.getX() - (WorldGenerator.chunkRenderSize.getWidth() * 2)
				&& x < WorldGenerator.chunkIndex.getX() + (WorldGenerator.chunkRenderSize.getWidth() * 2)
				&& z > WorldGenerator.chunkIndex.getY() - (WorldGenerator.chunkRenderSize.getHeight() * 2)
				&& z < WorldGenerator.chunkIndex.getY() + (WorldGenerator.chunkRenderSize.getHeight() * 2)) {
			chunk = WorldData.chunks.get(x + "," + z);
		}

		return chunk;
	}
}
