package classes;

import java.util.UUID;

import core.GLChunkManager;

public class GLIndex {
	public int x = 0;
	public int y = 0;
	public int z = 0;

	public int chunkX = 0;
	public int chunkY = 0;
	public int chunkZ = 0;

	public int itemSlotX = 0;
	public int itemSlotY = 0;

	public GLIndex(int i, int j, int k) {
		x = i;
		y = j;
		z = k;
	}

	public GLIndex(int i, int j, int k, int l, int m, int n) {
		x = i;
		y = j;
		z = k;

		chunkX = l;
		chunkY = m;
		chunkZ = n;

	}

	public GLIndex(int i, int j) {
		itemSlotX = i;
		itemSlotY = j;
	}

	@Override
	public int hashCode() {
		int hash = 1;
		hash = hash * 31 + (x * 2) + y + z + (chunkX * 2) + chunkY + chunkZ + itemSlotX + itemSlotY;
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		// Not strictly necessary, but often a good optimization
		if (this == obj)
			return true;
		if (!(obj instanceof GLIndex))
			return false;
		GLIndex other = (GLIndex) obj;
		if (x == other.x && y == other.y && z == other.z) {
			return true;
		}
		return false;
	}

	public boolean above(Object obj) {
		GLIndex other = (GLIndex) obj;
		if (x == other.x && y + 1 == other.y && z == other.z) {
			return true;
		}
		return false;
	}

	public boolean beside(Object obj) {
		GLIndex other = (GLIndex) obj;
		if ((x >= other.x - 1 && x <= other.x + 1) && (y >= other.y - 1 && y <= other.y + 1)
				&& (z >= other.z - 1 && z <= other.z + 1)) {
			return true;
		}
		return false;
	}
}
