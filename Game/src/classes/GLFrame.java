package classes;

public class GLFrame {
	private int dl = -1;	

	public GLFrame(int newDL) {
		this.setDL(newDL);
	}

	public void setDL(int newDL) {
		this.dl = newDL;
	}

	public int getDL() {
		return this.dl;
	}
}
