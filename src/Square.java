import java.util.List;
import java.util.*;
import java.awt.Point;
import java.io.Serializable;



public class Square implements Serializable{
	private List<List<Point>> possibleSpots = new ArrayList<>();
	private int startHeight;
	private int startWidth;
	private int[] numbersInSquare = new int[9];
	private boolean correct = true;
	
	
	public void setupSquare(ArrayList<ArrayList<Number>> numbers, int startHeight, int startWidth){
		this.startHeight = startHeight;
		this.startWidth = startWidth;
		for(int i = 1; i < 10; i++ ) {
			possibleSpots.add(i - 1, new ArrayList<Point>());
		}
	
	}
	
	public List<List<Point>> getPossibleSpots() {
	    return possibleSpots;
	}
	
	public ArrayList<ArrayList<Number>> updateSquare(ArrayList<ArrayList<Number>> numbers) {
		for(int possibleNumber = 1; possibleNumber < 10; possibleNumber++) {
			//checks for each number if it is inside of the square
			boolean containsNumber = false;
			for(int height = startHeight; height < startHeight + 3; height++) {
				for(int width = startWidth; width < startWidth + 3; width++) {
					if(numbers.get(height).get(width).value == possibleNumber) {
						containsNumber = true;
					}
				}
			}
			//when the number has been used, this clears the array
			if(!containsNumber) {
				possibleSpots.get(possibleNumber -1).clear();
			}
				
				
				
			//if the number isn't inside the square, this will find its possible spots
			if(!containsNumber) {
				for(int height = startHeight; height < startHeight + 3; height++) {
					for(int width = startWidth; width < startWidth + 3; width++) {
						//if the place is not occupied, and inside the possible numbers of the place, we find this one we add it, and it can't already be in the array
						if(numbers.get(height).get(width).value == 0 && numbers.get(height).get(width).possibleNumbers.contains(possibleNumber) && 
								!possibleSpots.get(possibleNumber - 1).contains(new Point(height, width))) {
							possibleSpots.get(possibleNumber - 1).add(new Point(height, width));
						}
						//this removes the point from the array if the number can't be placed there anymore
						if(!numbers.get(height).get(width).possibleNumbers.contains(possibleNumber) && 
								possibleSpots.get(possibleNumber -1).contains(new Point(height, width))) {
							possibleSpots.get(possibleNumber - 1).remove(new Point(height, width));
						}
					}
				}
			
			//when there is only one possibility, this puts the number on the field
			if(possibleSpots.get(possibleNumber - 1).size() == 1) {
				numbers.get(possibleSpots.get(possibleNumber - 1).get(0).x).get(possibleSpots.get(possibleNumber - 1).get(0).y).value = possibleNumber;
			}
			
			//ads the numbers to an array, to check if they are correct
			for(int height = startHeight, index = 0; height < startHeight + 3; height++) {
				for(int width = startWidth; width < startWidth + 3; width++, index++) {
					numbersInSquare[index] = numbers.get(height).get(width).value;
					}
				}
			}
		}
		return numbers;
	}
	
	public boolean isCorrect() {
		for(int number = 1; number < 10; number++) {
			int contained = 0;
			for(int arrayIndex = 0; arrayIndex < 9; arrayIndex++) {
				if(numbersInSquare[arrayIndex] == number) {
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
