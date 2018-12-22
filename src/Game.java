import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Game implements Serializable {
	private int possibleNumbersTotal = 0;
	ArrayList<ArrayList<Number>> numbers = new ArrayList<ArrayList<Number>>();
	private List<Square> squares = new ArrayList<>();
	private List<Width> widths = new ArrayList<>();
	private List<Height> heights = new ArrayList<>();
	private boolean isValid = true;
	private int missingNumbers;
	
	
	List<List<List<Point>>> possibleFields = new ArrayList<>();
	
	
	public int getPossibleNumbersTotal() {
	    return possibleNumbersTotal;
	}
	
	
	public int setupGame() {
		Input input = new Input();
		for(int index = 0; index < 9; index++) {
			numbers.add(index, new ArrayList<Number>());
		}
		numbers = input.getInput(numbers);
		printGame();
		
		for(int height = 0; height < 9; height++) {
			for(int width = 0; width < 9; width++) {
				
				if(numbers.get(height).get(width).value == 0) {
					missingNumbers++;	
				}
				numbers.get(height).get(width).setupNumber(numbers);
			}
		} 
		//sets up the class to find twins
		for(int index = 0; index < 9; index++) {
			possibleFields.add(index, new ArrayList<List<Point>>());
			for(int innerIndex = 0; innerIndex < 9; innerIndex++) {
				possibleFields.get(index).add(innerIndex, new ArrayList<Point>());
			}
		}
		
		//sets up the objects for each square, width and height
		for(int index = 0; index < 9; index++) {
			squares.add(new Square());
			widths.add(new Width());
			heights.add(new Height());
			widths.get(index).setupWidth(numbers, index);
			heights.get(index).setupWidth(numbers, index);
		}
		
		//gives each square its coordinates
		int index = 0;
		for(int startX = 0; startX < 3; startX++) {
			for(int startY = 0; startY < 3; startY++) {
				squares.get(index).setupSquare(numbers, startX * 3 , startY * 3);
				index++;
			}
		}
		
		return missingNumbers;
	}
	


	public int solvingRound() {
	possibleNumbersTotal = 0;
	Number currentNumber;
	missingNumbers = 0;

	for(int height = 0; height < 9; height++) {
		for(int width = 0; width < 9; width++) {
			int changedNumber = 0;
			currentNumber = numbers.get(height).get(width);
			if(currentNumber.value == 0) {
				missingNumbers++;
				changedNumber = currentNumber.solveNumber(numbers);
				for(int possibilities : currentNumber.possibleNumbers) {
				possibleNumbersTotal += possibilities;
				}
			}
			if(changedNumber != 0) {
				missingNumbers--;
				currentNumber.value = changedNumber;
			}
		}
	}
	
	for(int index = 0; index < 9; index++) {
		boolean valid = true;
			numbers = squares.get(index).updateSquare(numbers);
			numbers = widths.get(index).updateWidth(numbers);
			numbers = heights.get(index).updateHeight(numbers);
			searchTwins(index);
			findTwins();
			inLineCheck(index);
			
			if(!squares.get(index).isCorrect() || !widths.get(index).isCorrect()  || !heights.get(index).isCorrect() ) {
				//System.out.println("ERROR!!!");
				isValid = false;
			}
			for(int width = 0; width < 9; width++) {
				if(!numbers.get(index).get(width).getValid()) {
					//System.out.println("ERROR!!! inside Number");
					isValid = false;
				}
			}
	}
	xWingMethodHeight();
	xWingMethodWidth();
	scanPerNumber();
	//printGame();
	return missingNumbers;
	}
	
	public boolean getValid() {
		return isValid;
	}
		
		
		
private void scanPerNumber() {	
	for(int index = 0; index < 9; index++) {
		int startX = 0;
		int startY = 0;
	
		for(int squareNumberWidth = 0; squareNumberWidth < 3; squareNumberWidth++) {
			for(int squareNumberHeight = 0; squareNumberHeight < 3; squareNumberHeight++) {
				searchEachSquare(index, startX, startY);
				startY += 3;
			}
		startY = 0;
		startX += 3;
		}		
	}
	
	for(int number = 1; number <= 9; number++) {
		for(int heightWidth = 0; heightWidth < 9; heightWidth++) {
			searchPerLineWidth(heightWidth, number);
			searchPerLineHeight(heightWidth, number);
		}
	}
	}
	

private void searchEachSquare(int number, int startX, int startY) {
	int stopX = startX + 3;
	int stopY = startY + 3;
	int[][] squareForNumber = new int[3][3];
	boolean isInSquare = false;
	
	for( int height = startX; height < stopX; height++){
		for(int width = startY; width < stopY; width++) {
			if(numbers.get(height).get(width).value == number) {
				isInSquare = true;
			}
			if(numbers.get(height).get(width).value != 0) {
				squareForNumber[height - startX][width- startY] = 1;
			}
		}
	}
	
	//establishes for each number in the square, an array of ones and zeros, with zeros marking the spots where the number can go
	if(!isInSquare) {
		for(int height = startX; height < stopX; height++ ) {
			for(int width = 0; width < 9; width++) {
				if(numbers.get(height).get(width).value == number) {
					squareForNumber[height - startX][0] = 1;
					squareForNumber[height - startX][1] = 1;
					squareForNumber[height - startX][2] = 1;
				}
			}
		}
			for(int height = 0; height < 9; height++) {
				for(int width = startY; width < stopY; width++) {
					if(numbers.get(height).get(width).value == number) {
						squareForNumber[0][width - startY] = 1;
						squareForNumber[1][width - startY] = 1;
						squareForNumber[2][width - startY] = 1;
					}
				}
			}		
		
		int squareSum = 0;
		int toX = 0, toY = 0;
		for(int height = 0; height < 3; height++) {
			for(int width = 0; width < 3; width++) {
				if(squareForNumber[height][width] == 1) {
				squareSum++;
				}else {
				toX = height + startX;
				toY = width + startY;
				}
			}
		}
			if(squareSum == 8) {
				numbers.get(toX).get(toY).value = number;
			}
	}
}

private void searchPerLineWidth(int height, int number) {
	boolean isInLineWidth = false;
	int[] lineForNumberWidth = new int[9];
	int startX = 0;
	int stopX;

		
	for(int width = 0; width < 9; width++) {
		if(numbers.get(height).get(width).value == number) {
			isInLineWidth = true;
		}else if(numbers.get(height).get(width).value == 0) {
			lineForNumberWidth[width] = 0;
		}else {
			lineForNumberWidth[width] = 1;
		}
	}
	if(!isInLineWidth) {

        	if(height > 2 && height <= 5) {
        		startX = 3;
        	}
        	if(height > 5) {
        		startX = 6;
        	}
        	if(height <= 2){
        		startX = 0;
        	}
        	stopX = startX + 3;
        	
        	for(int squareNumber = 0; squareNumber < 3; squareNumber++) {
        		for(int x = startX; x < stopX; x++){
        			for(int y = 0 + squareNumber * 3; y < 3 + squareNumber * 3; y++) {
        				if(numbers.get(x).get(y).value == number) {
        					lineForNumberWidth[squareNumber * 3] = 1;
        					lineForNumberWidth[squareNumber * 3 + 1] = 1;
        					lineForNumberWidth[squareNumber * 3 + 2] = 1;
        				}	
        			}
        		}
        }	
        	
        	for(int x = 0; x < 9; x++) {
        		for(int y = 0; y < 9; y++) {
        			if(numbers.get(x).get(y).value== number) {
        				lineForNumberWidth[y] = 1;
        			}
        		}
        	}
        	
        	int widthSum = 0;
        	int toY = 0;
        	
        	for(int h = 0; h < 9; h++) {
        		if(lineForNumberWidth[h] == 1) {
        			widthSum++;
        		}else {
        			toY = h;
        		}
        	}
        	
        	if(widthSum == 8) {
        		numbers.get(height).get(toY).value = number;
        	}

	}	
}

public void searchPerLineHeight(int width, int number) {
	boolean isInLineHeight = false;
	int[] lineForNumberHeight = new int[9];
	int startY = 0;
	int stopY;
	
	for(int height = 0; height < 9; height++) {
		if(numbers.get(height).get(width).value == number) {
			isInLineHeight = true;
		}else if(numbers.get(height).get(width).value == 0) {
			lineForNumberHeight[height] = 0;
		}else {
			lineForNumberHeight[height] = 1;
		}
	}
	
	if(!isInLineHeight) {
        	if(width > 2 && width <= 5) {
        		startY = 3;
        	}
        	if(width > 5) {
        		startY = 6;
        	}
        	if(width <= 2) {
        		startY = 0;
        	}
        	stopY = startY + 3;
        
        	for(int squareNumber = 0; squareNumber < 3; squareNumber++) {
        		for(int x = 0 + squareNumber * 3; x < 3 + squareNumber * 3; x++) {
        			for(int y = startY; y < stopY; y++){
        				if(numbers.get(x).get(y).value == number) {
        					lineForNumberHeight[squareNumber * 3] = 1;
        					lineForNumberHeight[squareNumber * 3 + 1] = 1;
        					lineForNumberHeight[squareNumber* 3 + 2] = 1;
        				}	
        			}
        	}
        	
        }	
        
        	for(int x = 0; x < 9; x++) {
        		for(int y = 0; y < 9; y++) {
        			if(numbers.get(x).get(y).value == number) {
        				lineForNumberHeight[x] = 1;
        			}
        		}
        		
        		
        	}
        	
        	
        	int heightSum = 0;
        	int toX = 0;
        	
        	for(int h = 0; h < 9; h++) {
        		if(lineForNumberHeight[h] == 1) {
        			heightSum++;
        		}else {
        			toX = h;
        		}
        	}
        	
        	if(heightSum == 8) {
        		numbers.get(toX).get(width).value = number;
        	}

	}
}

public void printGame() {
		for(int i = 0; i < 9; i++) {
			for(int j = 0; j < 9; j++) {
				System.out.print(numbers.get(i).get(j).value + "|");
				if((j + 1) % 3 == 0) {
					System.out.print("  ");
				}
			}
			System.out.println();
			if((i + 1) % 3 == 0) {
				System.out.println();
			}
		}
		System.out.println("________________________________");
	}
	
	
	private void findTwins() {

	int possibleFieldsIndex = 0;
	
	for(int outerIndex = 0; outerIndex < 3; outerIndex++) {
		for(int innerIndex = 0; innerIndex < 3; innerIndex++, possibleFieldsIndex++) {
			for(int possibleNumbers = 0; possibleNumbers < 9; possibleNumbers++) {
				for(int height = 0 + outerIndex * 3; height < 3 + outerIndex * 3; height++) {
					for(int width = 0 + innerIndex * 3; width < 3 + innerIndex * 3; width++) {
						if(numbers.get(height).get(width).possibleNumbers.contains(possibleNumbers +1)) {
							possibleFields.get(possibleNumbers).get(possibleFieldsIndex).add(new Point(height, width));
						}
					}
				}
			}
	
			for(int i = 0; i < 9; i++) {
				for(int j = 0; j < 9; j++) {
					if( possibleFields.get(i).size() == 2 && possibleFields.get(i).equals(possibleFields.get(j)) && i != j) {
						int x1 = (int) possibleFields.get(possibleFieldsIndex).get(i).get(0).getX();
						int y1 = (int) possibleFields.get(possibleFieldsIndex).get(i).get(0).getY();
						int x2 = (int) possibleFields.get(possibleFieldsIndex).get(i).get(1).getX();
						int y2 = (int) possibleFields.get(possibleFieldsIndex).get(i).get(1).getY();
				
						numbers.get(x1).get(y1).possibleNumbers.clear();
						numbers.get(x1).get(y1).possibleNumbers.add(i + 1);
						numbers.get(x1).get(y1).possibleNumbers.add(j + 1);
						numbers.get(x2).get(y2).possibleNumbers.clear();
						numbers.get(x2).get(y2).possibleNumbers.add(i + 1);
						numbers.get(x2).get(y2).possibleNumbers.add(j + 1);
					}
				}
				if(possibleFields.get(i).size() == 1) {
					int x = (int) possibleFields.get(possibleFieldsIndex).get(i).get(0).getX();
					int y = (int) possibleFields.get(possibleFieldsIndex).get(i).get(0).getY();
					numbers.get(x).get(y).value = i + 1;
					possibleFields.get(possibleFieldsIndex).get(i).clear();
				}
			}
		}
	}		
	}
	
	private void searchTwins(int squareNumber) {
		List<Integer> twinNumbers = new ArrayList<>();
		List<Point> twinNumbersPoints = new ArrayList<>();
		
		for(int outerIndex = 0; outerIndex < 9; outerIndex++) {
			for(int innerIndex = 0; innerIndex < 9; innerIndex++) {
				if(outerIndex != innerIndex && squares.get(squareNumber).getPossibleSpots().get(outerIndex).size() == 2 && 
				squares.get(squareNumber).getPossibleSpots().get(outerIndex).equals(squares.get(squareNumber).getPossibleSpots().get(innerIndex)) && twinNumbers.size() < 2) {
					twinNumbers.add(outerIndex + 1);
					twinNumbers.add(innerIndex + 1);
					twinNumbersPoints.add(squares.get(squareNumber).getPossibleSpots().get(outerIndex).get(0));
					twinNumbersPoints.add(squares.get(squareNumber).getPossibleSpots().get(outerIndex).get(1));
				}
			}
		}
		
		if(twinNumbersPoints.size() > 1) {
			for(Point p: twinNumbersPoints) {
				numbers.get(p.x).get(p.y).makeTwin(numbers, twinNumbers);
			}
		}		
	}
	

	private void inLineCheck(int squareNumber) {
		int x1, x2, x3, y1, y2, y3;
		for(int number = 1; number < 10; number++) {
				List<Point>squarePossibleSpots = squares.get(squareNumber).getPossibleSpots().get(number -1);
				x1 = 0;
				x2 = 0;
				x3 = 0;
				y1 = 0;
				y2 = 0;
				y3 = 0;
				if(squarePossibleSpots.size() == 2) {
					x1 = squarePossibleSpots.get(0).x;
					x2 = squarePossibleSpots.get(1).x;
					y1 = squarePossibleSpots.get(0).y;
					y2 = squarePossibleSpots.get(1).y;
					
					if(x1 == x2) {
						for(int width = 0; width < 9; width++) {
							if(width != y1 && width != y2) {
							numbers.get(x1).get(width).removePossibleNumber(number);
							}
						}
					}
					if(y1 == y2) {
						for(int height = 0; height < 9; height++) {
							if(height != x1 && height != x2) {
							numbers.get(height).get(y1).removePossibleNumber(number);
							}
						}
					}
					
				}
			
				if(squarePossibleSpots.size() == 3) {
					x1 = squarePossibleSpots.get(0).x;
					x2 = squarePossibleSpots.get(1).x;
					x3 = squarePossibleSpots.get(2).x;
					y1 = squarePossibleSpots.get(0).y;
					y2 = squarePossibleSpots.get(1).y;
					y3 = squarePossibleSpots.get(2).y;
					
					if(x1 == x2 && x2 == x3) {
						for(int width = 0; width < 9; width++) {
							if(width != y1 && width != y2 && width != y3) {
								numbers.get(x1).get(width).removePossibleNumber(number);
							}
						}
						
					}
					if(y1 == y2 && y2 == y3) {
						for(int height = 0; height < 9; height++) {
							if(height!= x1 && height != x2 && height != x3) {
								numbers.get(height).get(y1).removePossibleNumber(number);
							}
						}
						
					}
						
				}
			
		}
	}
	
	
	public void xWingMethodHeight() {
		for(int number = 0; number < 9; number++) {
				List<Integer> possibleWidths = new ArrayList<>();
			for(int width = 0; width < 9; width++) {
				List<Integer> insidePossibleNumbers = heights.get(width).possibleSpots.get(number);
				if(insidePossibleNumbers.size() == 2) {
					possibleWidths.add(width);
				}
			}
			
			if(possibleWidths.size() > 2) {
				for(int outerIndex = 0; outerIndex < possibleWidths.size(); outerIndex++) {
					for(int innerIndex = 0; innerIndex < possibleWidths.size(); innerIndex++) {
						if(heights.get(possibleWidths.get(outerIndex)).possibleSpots.get(number).get(0) == heights.get(possibleWidths.get(innerIndex)).possibleSpots.get(number).get(0) && 
								heights.get(possibleWidths.get(outerIndex)).possibleSpots.get(number).get(1) == heights.get(possibleWidths.get(innerIndex)).possibleSpots.get(number).get(1) && outerIndex != innerIndex)	{
							int width1 = possibleWidths.get(outerIndex);
							int width2 = possibleWidths.get(innerIndex);
							possibleWidths.clear();
							possibleWidths.add(width1);
							possibleWidths.add(width2);
						}
					}
				}	
			}
			if(possibleWidths.size() == 2) {
			boolean compareArrays = heights.get(possibleWidths.get(0)).possibleSpots.get(number).get(0) == heights.get(possibleWidths.get(1)).possibleSpots.get(number).get(0) &&
					heights.get(possibleWidths.get(0)).possibleSpots.get(number).get(1) == heights.get(possibleWidths.get(1)).possibleSpots.get(number).get(1);
			
			 if(compareArrays) {
				for(int width = 0; width < 9; width++) {
					if(possibleWidths.get(0) != width && possibleWidths.get(1) != width) {
					
					//for the height, we go inside the height array where the possibilities for number are and get the one at width
					numbers.get(heights.get(possibleWidths.get(0)).possibleSpots.get(number).get(0)).get(width).removePossibleNumber(number + 1);
					numbers.get(heights.get(possibleWidths.get(0)).possibleSpots.get(number).get(1)).get(width).removePossibleNumber(number + 1);
					}
				}		
			 }
			}
		}	
	}
	
	public void xWingMethodWidth() {
		for(int number = 0; number < 9; number++) {
			List<Integer> possibleHeights = new ArrayList<>();
			for(int height = 0; height < 9; height++) {
				List<Integer> insidePossibleNumbers = widths.get(height).possibleSpots.get(number);
				if(insidePossibleNumbers.size() == 2) {
					possibleHeights.add(height);
				}
			}
			if(possibleHeights.size() > 2) {
				for(int outerIndex = 0; outerIndex < possibleHeights.size(); outerIndex++) {
					for(int innerIndex = 0; innerIndex < possibleHeights.size(); innerIndex++) {
						if(widths.get(possibleHeights.get(outerIndex)).possibleSpots.get(number).get(0) == widths.get(possibleHeights.get(innerIndex)).possibleSpots.get(number).get(0) && 
								widths.get(possibleHeights.get(outerIndex)).possibleSpots.get(number).get(1) == widths.get(possibleHeights.get(innerIndex)).possibleSpots.get(number).get(1) && outerIndex != innerIndex)	{
							int height1 = possibleHeights.get(outerIndex);
							int height2 = possibleHeights.get(innerIndex);
							possibleHeights.clear();
							possibleHeights.add(height1);
							possibleHeights.add(height2);

						}
					}
				}	
			}
			
			if(possibleHeights.size() == 2) {
				boolean compareArrays = widths.get(possibleHeights.get(0)).possibleSpots.get(number).get(0) == widths.get(possibleHeights.get(1)).possibleSpots.get(number).get(0) &&
						widths.get(possibleHeights.get(0)).possibleSpots.get(number).get(1) == widths.get(possibleHeights.get(1)).possibleSpots.get(number).get(1);
				if(compareArrays) {
					for(int height = 0; height < 9; height++) {
						if(possibleHeights.get(0) != height && possibleHeights.get(1) != height) {
						numbers.get(height).get(widths.get(possibleHeights.get(0)).possibleSpots.get(number).get(0)).removePossibleNumber(number + 1);
						numbers.get(height).get(widths.get(possibleHeights.get(0)).possibleSpots.get(number).get(1)).removePossibleNumber(number + 1);
						}
					}	
				}
			}
			
		
			
			
		}
		
		
		
		
	}
	

}
