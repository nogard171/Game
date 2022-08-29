package utils;

public class View {
	public static int x = 0;
	public static int y = 0;
	public static int previousX = 0;
	public static int previousY = 0;
	public static boolean moved = false;

	public static void update() {
		if (previousX != x || previousY != y) {
			moved = true;
			previousX = x;
			previousY = y;
		} else {
			moved = false;
		}
	}
}
