import java.awt.Point;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;

public class Solver {
	private final ArrayList<String> fileNames = new ArrayList<>();
	private List<Map <Point, ArrayList<Integer>>> possibleNumbers = new ArrayList<>();	
	private Point[] spotsToTry = new Point[15];
	boolean hasThreePossibleNumbers = false;
	
	public void solveGame(Game game) {
		int missingNumbers;
		int safeGameIndex = 0;
		
		for(int index = 0; index <= 15; index++) {
		    fileNames.add(index, String.format("sudokuSafe%d.bin", index));
		}
		
		for(int index = 0; index < fileNames.size(); index++) {
			possibleNumbers.add(index, new HashMap<Point, ArrayList<Integer>>());
		}
		missingNumbers = game.setupGame();
		missingNumbers = solveTry(game, missingNumbers);
		
		
		//when normal formals don't get it done, this tries a number in a field
		while(missingNumbers > 0 && safeGameIndex < fileNames.size()) {
			saveGame(game, safeGameIndex);
			findTryNumber(game, safeGameIndex);
			Point p = spotsToTry[safeGameIndex];
			
			ArrayList<Integer> numbers = possibleNumbers.get(safeGameIndex).get(p);
			
			setInNumber(game, numbers.get(0), p);
			possibleNumbers.get(safeGameIndex).get(p).remove(0);
			missingNumbers = solveTry(game, missingNumbers);

		
			//when the try was bad, this tries the alternative number
			while(missingNumbers == 100 && possibleNumbers.get(safeGameIndex).get(p).size() > 0) {
				Game game2 = restoreGame(game, safeGameIndex);
				numbers = possibleNumbers.get(safeGameIndex).get(p);
				setInNumber(game2, numbers.get(0), p);
				possibleNumbers.get(safeGameIndex).get(p).remove(0);
				missingNumbers = solveTry(game2, missingNumbers);
				game= game2;
			}
			
			if(missingNumbers == 100) {
					safeGameIndex--;
					game = restoreGame(game, safeGameIndex);
				}else {
					safeGameIndex++;
				}
		}
		game.printGame();
		System.out.println("Solved it!");
		
	}

	
	private void setInNumber(Game game, int number, Point p) {
		game.numbers.get(p.x).get(p.y).value = number;
	}
	
	private void findTryNumber(Game game, int index){
			ArrayList<Integer> possibleN = new ArrayList<>();
			boolean addToPossibleNumbers = false;
			int alternativeNumber;
			int alternativeNumber2;
			Point spotToTry = new Point();

			spotToTry = searchSpotToTry(game);
			int number= game.numbers.get(spotToTry.x).get(spotToTry.y).possibleNumbers.get(0);
			alternativeNumber = game.numbers.get(spotToTry.x).get(spotToTry.y).possibleNumbers.get(1);
			possibleN.add(number);
			possibleN.add(alternativeNumber);

			if(hasThreePossibleNumbers) {
				alternativeNumber2 = game.numbers.get(spotToTry.x).get(spotToTry.y).possibleNumbers.get(2);
				possibleN.add(alternativeNumber2);
				hasThreePossibleNumbers = false;
			}
			
			if(possibleNumbers.get(index).containsKey(spotToTry)){
			    if(possibleNumbers.get(index).get(spotToTry).size() == 0) {
			        addToPossibleNumbers = true;
			    }
			}
			
			if(!possibleNumbers.get(index).containsKey(spotToTry) || addToPossibleNumbers) {
			possibleNumbers.get(index).put(spotToTry, possibleN);
			spotsToTry[index] = spotToTry;
			}
			
	}
	
	
	private Point searchSpotToTry(Game game) {
		Point spot = new Point();
		boolean selected = false;
			for(int height = 0; height < 9; height++) {
				for(int width = 0; width < 9; width++) {
					if(game.numbers.get(height).get(width).value == 0 &&  game.numbers.get(height).get(width).possibleNumbers.size() == 2 && !selected) {
					spot.setLocation(height, width);
						selected = true;
					}	
				}
			}
			if(!selected) {
				for(int height = 0; height < 9; height++) {
					for(int width = 0; width < 9; width++) {
						if(game.numbers.get(height).get(width).value == 0 &&  game.numbers.get(height).get(width).possibleNumbers.size() == 3 && !selected) {
						spot.setLocation(height, width);
							selected = true;
							hasThreePossibleNumbers = true;
						}	
					}
				}
			}
		
		return spot;
	}
	
	
	
	
	private void saveGame(Game game, int index) {
		try {
			ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(fileNames.get(index)));
			os.writeObject(game);
			os.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	private Game restoreGame(Game game, int index) {
			try {
			ObjectInputStream is = new ObjectInputStream(new FileInputStream(fileNames.get(index)));
			try {
				game = (Game) is.readObject();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
			return game;
	}
	
	
	private int solveTry(Game game, int missingNumbers) {
		
		boolean keepsSolving = true;
		boolean validGame = true;
		 
		while(validGame && keepsSolving) {
			int possibleNumbers1 = game.getPossibleNumbersTotal();
			 missingNumbers = game.solvingRound();
			 validGame = game.getValid();

			
			if(possibleNumbers1 == game.getPossibleNumbersTotal() || missingNumbers == 0) {
				keepsSolving = false;
				
			}
			if(!validGame) {
				missingNumbers = 100;
			}
	}
		
		return missingNumbers;
	}
	

}