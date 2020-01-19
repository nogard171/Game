package core;

import org.lwjgl.util.vector.Vector2f;

import classes.GLIndex;

public class GLMessage {
	private Vector2f position = new Vector2f(0,0);
	private boolean visible = false;
	private String message = "";
	private long timeout = 0;
	private long showTime = 0;

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public long getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = (timeout*1000) + this.showTime;
	}

	public long getShowTime() {
		return showTime;
	}

	public void setShowTime(long showTime) {
		this.showTime = showTime;
	}

	public Vector2f getPosition() {
		return position;
	}

	public void setPosition(Vector2f position) {
		this.position = position;
	}

}
