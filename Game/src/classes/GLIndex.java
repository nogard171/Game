package classes;

public class GLIndex {

	public int x = 0;
	public int y = 0;
	public int z = 0;

	public int chunkX = 0;
	public int chunkY = 0;
	public int chunkZ = 0;

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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x + y + z;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GLIndex other = (GLIndex) obj;
		if (x != other.x || y != other.y || z != other.z)
			return false;
		return true;
	}
}
