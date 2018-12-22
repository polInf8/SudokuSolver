
public class Launcher {
	public static void main(String[] args) {
		Game g = new Game();
		int missingNumbers;
		missingNumbers = g.setupGame();
		Solver s = new Solver();
		s.solveGame(g);

	}
	
}
