package game;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.SwingUtilities;

import org.lwjgl.LWJGLException;

public class Main {

	public static void main(String[] args) {
		final Base app = new Base();
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				app.start();
			}
		});
	}
}