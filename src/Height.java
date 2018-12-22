import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Height implements Serializable{

	List<List<Integer>> possibleSpots = new ArrayList<>();
	int startWidth;
	boolean correct = true;
	int[] numbersInHeight = new int[9];
	
	public void setupWidth(ArrayList<ArrayList<Number>> numbers, int startWidth) {
		this.startWidth = startWidth;
		for(int i = 1; i < 10; i++) {
			possibleSpots.add(i - 1, new ArrayList<Integer>());
		}
	}
	
	public ArrayList<ArrayList<Number>> updateHeight(ArrayList<ArrayList<Number>> numbers) {
		for(int possibleNumber = 1; possibleNumber < 10; possibleNumber++) {
			//checks for each number if it is inside of the square
			boolean containsNumber = false;
				for(int height = 0; height < 9; height++) {
					if(numbers.get(height).get(startWidth).value == possibleNumber) {
						containsNumber = true;
					}
				}
			
			//when the number has been used, this clears the array
			if(!containsNumber) {
				possibleSpots.get(possibleNumber -1).clear();
			}
		
			//if the number isn't inside the square, this will find its possible spots
			if(!containsNumber) {
					for(int height = 0; height < 9; height++) {
						//if the place is not occupied, and inside the possible numbers of the place, we find this one we add it, and it can't already be in the array
						if(numbers.get(height).get(startWidth).value == 0 && numbers.get(height).get(startWidth).possibleNumbers.contains(possibleNumber) && 
								!possibleSpots.get(possibleNumber - 1).contains(new Point(height, startWidth))) {
							possibleSpots.get(possibleNumber - 1).add(height);
						}
						//this removes the point from the array if the number can't be placed there anymore
						if(!numbers.get(height).get(startWidth).possibleNumbers.contains(possibleNumber) && 
								possibleSpots.get(possibleNumber -1).contains(height)) {
						    Object removeNumber = new Integer(height);
							possibleSpots.get(possibleNumber - 1).remove(removeNumber);
						}
					}
				
			
			//when there is only one possibility, this puts the number on the field
			if(possibleSpots.get(possibleNumber - 1).size() == 1) {
				numbers.get(possibleSpots.get(possibleNumber - 1).get(0)).get(startWidth).value = possibleNumber;
				//System.out.println("the number: " + possibleNumber + " goes to: " + possibleSpots.get(possibleNumber - 1).get(0) + " and " + startWidth);
			}
			//System.out.println(possibleNumber + " "+ possibleSpots.get(possibleNumber - 1));
			
			for(int height = 0; height < 9; height++) {
				numbersInHeight[height] = numbers.get(height).get(startWidth).value;
				}
		
			}
		}
		return numbers;
	}
	
	public boolean isCorrect() {
		for(int number = 1; number < 10; number++) {
			int contained = 0;
			for(int arrayIndex = 0; arrayIndex < 9; arrayIndex++) {
				if(numbersInHeight[arrayIndex] == number) {
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
