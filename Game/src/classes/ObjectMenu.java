package classes;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Color;

import data.WorldData;
import threads.GameThread;
import utils.Renderer;
import utils.View;
import utils.Window;

public class ObjectMenu {
	Object obj;
	public boolean showObjectMenu = false;
	MouseIndex objectIndex;
	Rectangle menuBounds;

	ArrayList<MenuItem> menuItems = new ArrayList<MenuItem>();

	public void setup() {
		MenuItem mine = new MenuItem(new AFunction() {
			public void click() {
				System.out.println("Mine");

			}
		});

		mine.text = "Mine";
		menuItems.add(mine);

		MenuItem till = new MenuItem(new AFunction() {
			public void click() {
				System.out.println("till");
			}
		});
		till.text = "Till";
		menuItems.add(till);

		MenuItem chop = new MenuItem(new AFunction() {
			public void click() {
				System.out.println("chop");
				Event move = new Event();
				move.eventName = "MOVE";
				move.end = new Point(GameThread.hover.getX(), GameThread.hover.getY());

				Event chop = new Event();
				chop.eventName = "CHOP";
				chop.end = new Point(GameThread.hover.getX(), GameThread.hover.getY());
				move.followUpEvent = chop;
				EventManager.addEvent(move);
			}
		});
		chop.text = "Chop";
		menuItems.add(chop);

		MenuItem info = new MenuItem(new AFunction() {
			public void click() {
				System.out.println("info");
			}
		});
		info.text = "Info";
		menuItems.add(info);

		MenuItem cancel = new MenuItem(new AFunction() {
			public void click() {
				showObjectMenu = false;
			}
		});
		cancel.text = "Cancel";
		menuItems.add(cancel);
		menuBounds = new Rectangle(0, 0, 100, menuItems.size() * 13);
	}

	int menuIn = 0;

	public void update() {
		if (Mouse.isButtonDown(1) && GameThread.getHover() != null && !showObjectMenu) {
			showObjectMenu = true;
			objectIndex = GameThread.getHover();

			int hoverX = objectIndex.getX();
			int hoverY = objectIndex.getY();
			int chunkX = hoverX / 16;
			int chunkY = hoverY / 16;

			Chunk chunk = WorldData.chunks.get(chunkX + "," + chunkY);
			if (chunk != null) {
				int objX = objectIndex.getX() % 16;
				int objY = objectIndex.getY() % 16;

				Object ground = chunk.groundObjects[objX][objY];
				Object mask = chunk.maskObjects[objX][objY];
				if (ground != null) {
					if (mask != null && mask.getMaterial() != "AIR") {
						obj = mask;
					} else {
						obj = ground;
					}
				}
			}

			System.out.println("Object: " + obj.getMaterial());
		}
		if (showObjectMenu) {
			int menuCartX = objectIndex.getX() * 32;
			int menuCartZ = objectIndex.getY() * 32;

			int isoX = menuCartX - menuCartZ;
			int isoZ = (menuCartX + menuCartZ) / 2;
			menuBounds.x = isoX;
			menuBounds.y = isoZ;

			for (MenuItem item : menuItems) {
				int cartX = Window.getMouseX() - View.x;
				int cartY = Window.getMouseY() - View.y;
				if (item.bounds.contains(new Point(cartX, cartY))) {
					item.hovered = true;
				} else {
					item.hovered = false;
				}

				if (item.hovered && Mouse.isButtonDown(0)) {
					item.click();
				} else {
					item.unclick();
				}
			}

			int cartX = Window.getMouseX() - View.x;
			int cartY = Window.getMouseY() - View.y;
			if (menuBounds.contains(new Point(cartX, cartY))) {
				menuIn++;
			}
			if (!menuBounds.contains(new Point(cartX, cartY)) && menuIn > 0) {
				showObjectMenu = false;
				menuIn = 0;
			}
		}
	}

	public void render() {
		if (showObjectMenu) {
			int cartX = objectIndex.getX() * 32;
			int cartZ = objectIndex.getY() * 32;

			int isoX = cartX - cartZ;
			int isoZ = (cartX + cartZ) / 2;
			menuBounds.x = isoX;
			menuBounds.y = isoZ;
			Renderer.renderRectangle(menuBounds.x, menuBounds.y, menuBounds.width, menuBounds.height,
					new Color(0, 0, 0, 0.5f));
			int y = 0;
			for (MenuItem item : menuItems) {
				item.bounds = new Rectangle(isoX, (isoZ) + (y * 12) + 2, 100, 12);
				if (item.hovered) {
					Renderer.renderRectangle(item.bounds.x, item.bounds.y, item.bounds.width, item.bounds.height,
							new Color(1, 0, 0, 0.5f));
				}
				Renderer.renderText(new Vector2f(isoX + 3, isoZ + (y * 12)), item.text, 12, Color.white);

				y += 1;
			}
		}

	}
}
