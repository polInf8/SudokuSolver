import java.util.ArrayList;
import java.util.Iterator;
import java.io.Serializable;
import java.util.*;

public class Number implements Serializable {
	int value;
	private int x;
	private int y;
	private boolean isTwin = false;
	private boolean isValid = true;
	private final int squareNumber; 
	private int stopX, stopY;
	private int startX, startY;
	private ArrayList<ArrayList<Number>> numbers = new ArrayList<ArrayList<Number>>();
	private ArrayList<Integer> squarePossibleNumbers = new ArrayList<Integer>();
	private ArrayList<Integer> widthPossibleNumbers = new ArrayList<Integer>();
	private ArrayList<Integer> heightPossibleNumbers = new ArrayList<Integer>();
	ArrayList<Integer> possibleNumbers = new ArrayList<Integer>();
	
	
	
	public Number(int value, int x, int y) {
		this.value = value;
		this.x = x;
		this.y = y;
		
		final int widthSquare;
		final int heightSquare;
		
		//calculates to which of the 9 squares this number belongs to
		if(x < 3) {
			heightSquare = 0;
		}else if(x < 6) {
			heightSquare = 1;
		}else {
			heightSquare = 2;
		}
		
		if(y < 3) {
			widthSquare = 0;
		}else if(y < 6) {
			widthSquare = 1;
		}else {
			widthSquare = 2;
		}
		
		squareNumber =  widthSquare + 3 * heightSquare;		
	}
	
	
	public int getX() {
	    return x;
	}
	
	public int getY() {
	    return y;
	}
	
	public void setupNumber(ArrayList<ArrayList<Number>> numbers) {
		//check possible numbers in square
				this.numbers = numbers;
			if(value == 0) {
				for(int i = 1; i < 10; i++) {
				squarePossibleNumbers.add(i);
				widthPossibleNumbers.add(i);
				heightPossibleNumbers.add(i);
				}
					if(x < 3) {
						stopX = 3;
						startX = 0;
					}else if(x < 6) {
						stopX = 6;
						startX = 3;
					}else {
						stopX = 9;
						startX = 6;
					}
					
					if(y < 3) {
						stopY = 3;
						startY = 0;
					}else if(y < 6) {
						stopY = 6;
						startY = 3;
					}else {
						stopY = 9;
						startY = 6;
					}
					
					for(int height = startX; height < stopX; height++) {
						for(int width = startY; width < stopY; width++) {
							for(int number = 0; number < squarePossibleNumbers.size(); number++) {	
							if(squarePossibleNumbers.get(number).equals(numbers.get(height).get(width).value)) {
								squarePossibleNumbers.remove(number);
							}
						}	
					}
				}
				
				//checks possible numbers in width
				for(int width  = 0; width < 9; width++) {
					for(int number = 0; number < widthPossibleNumbers.size(); number++) {
						if(widthPossibleNumbers.get(number).equals(numbers.get(x).get(width).value)) {
							widthPossibleNumbers.remove(number);
						}
					}
				}
				//checks possible numbers in height
				int maxH = 9;
				
				for(int i = 0; i < 9; i++) {
					for(int j = 0; j < maxH; j++ ) {
						if(heightPossibleNumbers.get(j).equals(numbers.get(i).get(y).value)) {
							heightPossibleNumbers.remove(j);
							maxH --;
						}
					}
				}
					//comparing the arrays to add to possibleNumbers:
				
				for(int squareIndex = 0; squareIndex < squarePossibleNumbers.size(); squareIndex++) {
					for( int widthIndex = 0; widthIndex < widthPossibleNumbers.size(); widthIndex++) {
						for(int heightIndex = 0; heightIndex < heightPossibleNumbers.size(); heightIndex++) {
							if(squarePossibleNumbers.get(squareIndex).equals(widthPossibleNumbers.get(widthIndex)) && widthPossibleNumbers.get(widthIndex).equals(heightPossibleNumbers.get(heightIndex))) {
								if(!possibleNumbers.contains(squarePossibleNumbers.get(squareIndex))) {
									possibleNumbers.add(squarePossibleNumbers.get(squareIndex));
								}
							}
							
						}
					}
				}
			}

	}
	
	
	public int solveNumber(ArrayList<ArrayList<Number>> numbers) {			
		//checking the possibleNumbers, for the possibility to remove numbers
		ArrayList <Integer> removeIndexes = new ArrayList<Integer>();
		
		for(int number: possibleNumbers) {
			for(int squareIndex = 0; squareIndex < squarePossibleNumbers.size(); squareIndex++) {
				for( int widthIndex = 0; widthIndex < widthPossibleNumbers.size(); widthIndex++) {
					for(int heightIndex = 0; heightIndex < heightPossibleNumbers.size(); heightIndex++) {
						if(!squarePossibleNumbers.get(squareIndex).equals(widthPossibleNumbers.get(widthIndex)) || !widthPossibleNumbers.get(widthIndex).equals(heightPossibleNumbers.get(heightIndex))) {
							if(possibleNumbers.contains(squarePossibleNumbers.get(squareIndex))) {
								removeIndexes.add(squarePossibleNumbers.get(squareIndex));
							}
						}
					}
				}
			}
		}
		
		Iterator<Integer>removeIterator = removeIndexes.iterator();
		while(removeIterator.hasNext()) {
			removeIterator.next();
			removeIterator.remove();
		}
		removeIndexes.clear();
		
		if(possibleNumbers.size() == 1) {
		value = possibleNumbers.get(0);
		}
		return value;
		
	}
	
	
	
	public void makeTwin(ArrayList<ArrayList<Number>> numbers, List<Integer> remainingNumbers) {
		if(!isTwin) {
			squarePossibleNumbers.clear();
			widthPossibleNumbers.clear();
			heightPossibleNumbers.clear();
			possibleNumbers.clear();
			
			for(int remainingIndex = 0; remainingIndex < remainingNumbers.size(); remainingIndex++) {
				squarePossibleNumbers.add(remainingNumbers.get(remainingIndex));
				widthPossibleNumbers.add(remainingNumbers.get(remainingIndex));
				heightPossibleNumbers.add(remainingNumbers.get(remainingIndex));
				possibleNumbers.add(remainingNumbers.get(remainingIndex));
			}
			
		}
		isTwin = true;
		
		
	}
	
	public void removePossibleNumber(int number) {
		Object n = (Integer) number;
		if(possibleNumbers.contains(n)) {
		squarePossibleNumbers.remove(n);
		widthPossibleNumbers.remove(n);
		heightPossibleNumbers.remove(n);
		possibleNumbers.remove(n);
		}
	}
	
	public boolean getValid() {
		if(value == 0 && possibleNumbers.size() == 0) {
			isValid = false;
		}
		return isValid;
	}
}
