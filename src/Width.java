import java.awt.Point;
import java.io.Serializable;
import java.util.*;


public class Width implements Serializable {
	List<List<Integer>> possibleSpots = new ArrayList<>();
	int startHeight;

	int[] numbersInWidth = new int[9];
	
	public void setupWidth(ArrayList<ArrayList<Number>> numbers, int startHeight) {
		this.startHeight = startHeight;
		for(int i = 1; i < 10; i++) {
			possibleSpots.add(i - 1, new ArrayList<Integer>());
		}
	}
	
	public ArrayList<ArrayList<Number>> updateWidth(ArrayList<ArrayList<Number>> numbers) {
		for(int possibleNumber = 1; possibleNumber < 10; possibleNumber++) {
			//checks for each number if it is inside of the square
			boolean containsNumber = false;
				for(int width = 0; width < 9; width++) {
					if(numbers.get(startHeight).get(width).value == possibleNumber) {
						containsNumber = true;
					}
				}
			
			//when the number has been used, this clears the array
			if(!containsNumber) {
				possibleSpots.get(possibleNumber -1).clear();
			}
		
			//if the number isn't inside the square, this will find its possible spots
			if(!containsNumber) {
					for(int width = 0; width < 9; width++) {
						//if the place is not occupied, and inside the possible numbers of the place, we find this one we add it, and it can't already be in the array
						if(numbers.get(startHeight).get(width).value == 0 && numbers.get(startHeight).get(width).possibleNumbers.contains(possibleNumber) && 
								!possibleSpots.get(possibleNumber - 1).contains(width)) {
							possibleSpots.get(possibleNumber - 1).add(width);
						}
						//this removes the point from the array if the number can't be placed there anymore
						if(!numbers.get(startHeight).get(width).possibleNumbers.contains(possibleNumber) && 
						        possibleSpots.get(possibleNumber -1).contains(width)) {
						    Object removeObject = new Integer(width);
							possibleSpots.get(possibleNumber - 1).remove(removeObject);
						}
					}
				
			
			//when there is only one possibility, this puts the number on the field
			if(possibleSpots.get(possibleNumber - 1).size() == 1) {
				numbers.get(startHeight).get(possibleSpots.get(possibleNumber - 1).get(0)).value = possibleNumber;
			}
			
			for(int width = 0; width < 9; width++) {
					numbersInWidth[width] = numbers.get(startHeight).get(width).value;
					}
			}
		}
		return numbers;
	}
	
	public boolean isCorrect() {
	    boolean correct = true;
		for(int number = 1; number < 10; number++) {
			int contained = 0;
			for(int arrayIndex = 0; arrayIndex < 9; arrayIndex++) {
				if(numbersInWidth[arrayIndex] == number) {
					contained++;
				}
				if(contained > 1) {
					correct = false;
				}
			}
		}
		return correct;
	}
	
	
	
	
}
