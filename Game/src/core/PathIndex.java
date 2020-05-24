package core;

public class PathIndex {
	public int x = 0;
	public int y = 0;
	public int z = 0;

	public PathIndex(int i, int j, int k) {
		x = i;
		y = j;
		z = k;
	}

	public boolean equals(PathIndex obj) {
		if (obj == null) {
			return false;
		}

		if (x == obj.x && y == obj.y && z == obj.z) {
			return true;
		}

		return true;
	}
}
