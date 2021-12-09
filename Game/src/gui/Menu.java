package gui;

import java.awt.Rectangle;
import java.util.LinkedList;

import org.lwjgl.input.Keyboard;

import core.Window;
import utils.Input;

public class Menu {
	private Settings settings;
	private boolean showMenu = false;
	private Panel backgroundPanel;

	private LinkedList<Button> menuBtns = new LinkedList<Button>();

	public void setup() {
		backgroundPanel = new Panel(new Rectangle(0, 0, Window.width, Window.height));
		int width = 100;
		menuBtns.add(new Button("Resume", new Rectangle(300, 300, width, 25), new Action() {
			@Override
			public void click() {
				showMenu = false;
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
		menuBtns.add(new Button("Save", new Rectangle(300, 300, width, 25), new Action() {
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
		menuBtns.add(new Button("Load", new Rectangle(300, 300, width, 25), new Action() {
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
		menuBtns.add(new Button("Settings", new Rectangle(300, 300, width, 25), new Action() {
			@Override
			public void click() {
				settings.show();
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
		menuBtns.add(new Button("Quit", new Rectangle(300, 300, width, 25), new Action() {
			@Override
			public void click() {
				Window.close();
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
		updateBounds();

		settings = new Settings();
		settings.setup();
	}

	private void updateBounds() {
		int y = 0;
		for (Button b : menuBtns) {
			b.setPosition((Window.width / 2) - (b.getSize().width / 2),
					(Window.height / 2) - (b.getSize().height / 2) + (28 * y));
			y++;
		}
		/*
		 * 
		 * resumeBtn.setPosition((Window.width / 2) - (resumeBtn.getSize().width / 2),
		 * (Window.height / 2) - (resumeBtn.getSize().height / 2));
		 * saveBtn.setPosition((Window.width / 2) - (resumeBtn.getSize().width / 2),
		 * (Window.height / 2) - (resumeBtn.getSize().height / 2) + 20);
		 * loadBtn.setPosition((Window.width / 2) - (resumeBtn.getSize().width / 2),
		 * (Window.height / 2) - (resumeBtn.getSize().height / 2) + (20 * 2));
		 * settingsBtn.setPosition((Window.width / 2) - (resumeBtn.getSize().width / 2),
		 * (Window.height / 2) - (resumeBtn.getSize().height / 2) + (20 * 3));
		 * quitBtn.setPosition((Window.width / 2) - (resumeBtn.getSize().width / 2),
		 * (Window.height / 2) - (resumeBtn.getSize().height / 2) + (20 * 4));
		 */
	}

	public void updateWhenVisible() {
		if (Window.wasResized) {
			updateBounds();
		}
		settings.update();
		for (Button b : menuBtns) {
			b.update();
		}
	}

	public void update() {
		if (Window.wasResized) {
			backgroundPanel.setBounds(new Rectangle(0, 0, Window.width, Window.height));
		}

		if (Input.isKeyPressed(Keyboard.KEY_ESCAPE)) {
			showMenu = !showMenu;
			settings.setShow(false);
		}
		if (showMenu) {
			updateWhenVisible();
		}
	}

	private void renderWhenVisible() {
		backgroundPanel.render();

		for (Button b : menuBtns) {
			b.render();
		}
		settings.render();
	}

	public void render() {
		if (showMenu) {
			renderWhenVisible();
		}
	}

	public boolean isVisible() {
		return showMenu;
	}
}
