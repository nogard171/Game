package gui;

import java.awt.Rectangle;
import java.util.LinkedList;

import core.Window;
import game.Database;
import utils.Input;

public class Settings {
	private boolean show = false;
	private Panel backgroundPanel;
	private LinkedList<Button> menuBtns = new LinkedList<Button>();

	public void setup() {
		backgroundPanel = new Panel(new Rectangle((Database.width / 2) - 200, (Database.height / 2) - 200, 400, 400));
		menuBtns.add(new Button("Back", new Rectangle((Database.width / 2) - 200, (Database.height / 2) - 200, 50, 25),
				new Action() {
					@Override
					public void click() {
						show = false;
					}

					@Override
					public void in(Button self) {
						self.setActive(true);
					}

					@Override
					public void out(Button self) {
						self.setActive(false);
					}
				}));
		menuBtns.add(new Button("Controls",
				new Rectangle((Database.width / 2) - 200 + 60, (Database.height / 2) - 200, 100, 25), new Action() {
					@Override
					public void click() {
					}

					@Override
					public void in(Button self) {
						self.setActive(true);
					}

					@Override
					public void out(Button self) {
						self.setActive(false);
					}
				}));
		menuBtns.add(new Button("Graphics",
				new Rectangle((Database.width / 2) - 200 + 60 + 110, (Database.height / 2) - 200, 100, 25),
				new Action() {
					@Override
					public void click() {
					}

					@Override
					public void in(Button self) {
						self.setActive(true);
					}

					@Override
					public void out(Button self) {
						self.setActive(false);
					}
				}));

		menuBtns.add(new Button("Audio",
				new Rectangle((Database.width / 2) - 200 + 60 + 110 + 110, (Database.height / 2) - 200, 65, 25),
				new Action() {
					@Override
					public void click() {
					}

					@Override
					public void in(Button self) {
						self.setActive(true);
					}

					@Override
					public void out(Button self) {
						self.setActive(false);
					}
				}));

		menuBtns.add(new Button("Etc",
				new Rectangle((Database.width / 2) - 200 + 60 + 110 + 110 + 75, (Database.height / 2) - 200, 40, 25),
				new Action() {
					@Override
					public void click() {
					}

					@Override
					public void in(Button self) {
						self.setActive(true);
					}

					@Override
					public void out(Button self) {
						self.setActive(false);
					}
				}));
	}

	public void setShow(boolean newShow) {
		this.show = newShow;
	}

	public void update() {
		if (Window.wasResized) {
			backgroundPanel.setBounds(new Rectangle((Database.width / 2) - 200, (Database.height / 2) - 200, 400, 400));
		}
		if (show) {
			updateWhenVisible();
		}
	}

	public void updateWhenVisible() {
		for (Button b : menuBtns) {
			b.update(Input.getMousePoint());
		}
	}

	public void updateControls() {

	}

	public void updateGraphics() {

	}

	public void updateAudio() {

	}

	public void updateEtc() {

	}

	public void render() {
		if (show) {
			renderWhenVisible();
		}
	}

	public void renderWhenVisible() {
		backgroundPanel.render();

		for (Button b : menuBtns) {
			b.render();
		}
	}

	public void show() {
		show = true;
	}

	public void renderControls() {

	}

	public void renderGraphics() {

	}

	public void renderAudio() {

	}

	public void renderEtc() {

	}
}
