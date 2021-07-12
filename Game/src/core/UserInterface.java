package core;

import java.awt.Rectangle;
import java.util.UUID;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.Color;

import game.Base;

public class UserInterface {
	UIMenu menu;

	public void setup() {
		menu = new UIMenu("tesT", new Rectangle(200, 200, 100, 0), new UIAction() {
			@Override
			public void onClick(UIMenu menu) {
				Object obj = ChunkManager.getObjectAt(hover.getX(), hover.getObjectIndex() - 1, hover.getY());
				if (obj != null) {
					System.out.println("object: " + obj.getSprite());
					if (obj.getSprite() == "tree") {
						menu.setItemVisiblility("chop", true);
					}
				}
			}
		});
		UIMenuItem item = new UIMenuItem("Chop", false);
		item.onClickAction = new UIAction() {
			@Override
			public void onClick(UIMenuItem item) {
				System.out.println("chop:" + (hover.getObjectIndex() - 1));

				TaskData dat = new TaskData(new ANode(hover.getX()-1, hover.getObjectIndex() - 1, hover.getY()));
				Task task = new Task("MOVE", dat);
				TaskManager.addTask(task);
			}
		};
		menu.addItem(item);
		UIMenuItem citem = new UIMenuItem("cancel");
		citem.onClickAction = new UIAction() {
			@Override
			public void onClick(UIMenu menu, UIMenuItem item) {
				System.out.println("cancel");
				menu.isVisible = false;
			}
		};
		menu.addItem(citem);
	}

	private void pollHover() {
		int cartX = (Mouse.getX() + Base.viewTest.x) - (32);
		int cartY = ((Window.height - Mouse.getY()) + Base.viewTest.y) - (32 * ChunkManager.layer);
		int isoX = (int) ((float) cartX / (float) 2 + ((float) cartY));
		int isoZ = (int) ((float) cartY - (float) cartX / (float) 2);
		int indexX = (int) Math.floor((float) isoX / (float) 32);
		int indexZ = (int) Math.floor((float) isoZ / (float) 32);

		float chunkRawX = (float) indexX / (float) ChunkManager.size.x;
		float chunkRawZ = (float) indexZ / (float) ChunkManager.size.z;

		int chunkX = (int) Math.floor(chunkRawX);
		int chunkZ = (int) Math.floor(chunkRawZ);
		if (indexX < 0) {
			chunkX--;
		}
		if (indexZ < 0) {
			chunkZ--;
		}
		Chunk chunkTest = ChunkManager.chunks.get(chunkX + ",0," + chunkZ);
		Object obj = null;
		if (chunkTest != null) {
			int tempLayer = 0;
			obj = ChunkManager.getObjectAt(indexX, tempLayer, indexZ);
			int loopCount = 0;
			boolean isAir = true;
			while (isAir) {
				if (obj != null) {
					if (!obj.getSprite().equals("air")) {
						isAir = false;
					}
				}
				tempLayer++;
				obj = ChunkManager.getObjectAt(indexX, tempLayer, indexZ);
				loopCount++;
				if (loopCount > ChunkManager.size.y) {
					break;
				}
			}
		}
		if (obj != null) {
			hover = new MouseIndex(indexX, indexZ, chunkX, chunkZ, (int) obj.getIndex().getY());
		}
	}

	public static MouseIndex hover;

	public void update() {
		menu.update();
		pollHover();

		if (hover != null && Input.isMousePressed(0)) {
			/*
			 * System.out.println("Move: " + (hover.getObjectIndex() - 2)); Task moveTask =
			 * new Task("MOVE", new TaskData(new ANode(hover.getX(), hover.getObjectIndex()
			 * - 2, hover.getY()))); UUID test = moveTask.uuid;
			 * TaskManager.addTask(moveTask);
			 */

		}

		if (Input.isMousePressed(1)) {

			menu.show();
		}
	}

	public void render() {
		menu.render();

		if (hover != null) {
			int posX = ((hover.getX() - hover.getY()) * 32) - Base.viewTest.x;
			int posY = 32;
			int posZ = (((hover.getY() + hover.getX()) * 16) + posY) - Base.viewTest.y;

			for (int i = 0; i < (hover.getObjectIndex() - ChunkManager.layer) - 1; i++) {
				GL11.glBegin(GL11.GL_QUADS);
				Renderer.renderSprite("hover3d", posX, (posZ + (i * 32)) + (32 * (ChunkManager.layer - 1)));
				GL11.glEnd();
			}

			GL11.glBegin(GL11.GL_QUADS);
			Renderer.renderSprite("hover", posX, posZ + (32 * (ChunkManager.layer - 1)));
			GL11.glEnd();

			Object hoverObj = ChunkManager.getObjectAt(hover.getX(), ChunkManager.layer, hover.getY());
			if (hoverObj != null) {
				Renderer.renderText(posX, posZ + (32 * (ChunkManager.layer - 1)) - 16, hoverObj.getName(), 12,
						Color.white);
			}

		}
	}

	public void destroy() {

	}
}
