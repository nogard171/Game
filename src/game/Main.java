package game;

import javax.swing.SwingUtilities;

import utils.Debugger;
import utils.FileHandler;

public class Main {

	public static void main(String[] args) {
		Debugger.log("Application Stated");
		final Base app = new Base();
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				Debugger.log("Base Stated");
				app.start();
			}
		});
	}
}