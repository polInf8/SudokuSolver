
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;


public class Input {
	private ArrayList<ArrayList<Number>> numbers = new ArrayList<ArrayList<Number>>();


	public ArrayList<ArrayList<Number>> getInput(ArrayList<ArrayList<Number>> numbers) {
		this.numbers = numbers;
		try {
			Scanner scanner = new Scanner(Paths.get("sudoku2.txt"));
			for(int height = 0; height < 9; height++) {
				for(int width = 0; width < 9; width++) {
					int i = scanner.nextInt();
					
					numbers.get(height).add(new Number(i, height, width));
				}
			}
		
			scanner.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return numbers;
	}
	
	}
	
	

