import threads.GameThread;

public class Appication {
	public static void main(String[] args) {
		GameThread game = new GameThread();
		game.start();
	}
}
