package core;

public class ANode {
	public int x = 0;
	public int y = 0;
	public int z = 0;

	public ANode(int newX, int newY, int newZ) {
		x = newX;
		y = newY;
		z = newZ;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) x;
		result = prime * result + (int) y;
		result = prime * result + (int) z;
		return result;
	}

	@Override
	public boolean equals(java.lang.Object obj) {
		if (this == obj)
		{
			//return true;
		}
		if (obj == null)
		{
			return false;
		}
		if (getClass() != obj.getClass())
		{
			return false;
		}
		ANode other = (ANode) obj;
		if (x == other.x && y == other.y && z == other.z)
		{
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		return "ANode(" + x + "," + y + "," + z + ")";
	}
}
