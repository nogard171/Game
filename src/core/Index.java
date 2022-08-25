package core;

import java.util.Objects;

public class Index {
	public int y = 0;
	public int x = 0;
	public int z = 0;
	private int hashCode;

	public Index(int ny, int nx, int nz) {
		y = ny;
		x = nx;
		z = nz;
		this.hashCode = Objects.hash(x, y);
	}

	public ANode toANode() {
		return new ANode(y, x, z);
	}

	@Override
	public String toString() {
		return y + "," + x + "," + z;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Index that = (Index) o;
		return x == that.x && y == that.y && z == that.z ;
	}

	@Override
	public int hashCode() {
		return this.hashCode;
	}
}
