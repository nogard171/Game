package game;

import java.io.IOException;

import javax.swing.SwingUtilities;

import utils.Loader;
import utils.Logger;

public class Main {

	public static void main(String[] args) {
		final Base game = new Base();
		try {
			Loader.loadSettings("config.properties");
		} catch (IOException e) {
		}
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				game.start();
			}
		});
	}
}