package package1;

import java.util.LinkedList;

/*
 * The RegEx class has all the fields and operations necessary for the Breaking down, optimizing and matching of any Regular expression
 */
public class RegEx {

	private String pattern; // The pattern in string form
	private LinkedList<String> splitPattern = new LinkedList<String>(); // The pattern broken up into pieces
	private int numOfStates; // The total number of states
	
	/*
	 * If the Regular Expression has nothing passed in, it will be automatically be set as empty with length 0, but not null
	 * It will try to split and will become an empty LinkedList of length 0, but not null
	 * It will get the size of the stateNums as the total number of states.
	 */
	public RegEx() {
		this.pattern = "";
		this.splitPattern = split(pattern);
		this.numOfStates = getStateNums(this.splitPattern).size();
	}

	/*
	 * If the Regular Expression has a string passed in, it will set the pattern to that string
	 * The expression is split up and the result is stored in the splitPattern field
	 * The total number of states will be the size of the stateNums() method return value (a linked List of integers)
	 */
	public RegEx(String pattern) {
		this.pattern = pattern;
		this.splitPattern = split(pattern);
		this.numOfStates = getStateNums(this.splitPattern).size();
	}
	
	/*
	 * Getters and Setters for the pattern, splitPattern and numOfStates fields
	 */
	public String getPattern() {
		return pattern;
	}
	
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}
	
	public int getNumOfStates() {
		return numOfStates;
	}
	
	public void setNumOfStates(int numOfStates) {
		this.numOfStates = numOfStates;
	}
	
	public LinkedList<String> getSplitPattern() {
		return splitPattern;
	}
	
	public void setSplitPattern(LinkedList<String> splitPattern) {
		this.splitPattern = splitPattern;
	}

	
	/*
	 * This method tries to match the expression to a string (This will be the line in the file that we pass in the main method)
	 * 
	 * Read inside comments to see how it works. I tried to not make it too detailed
	 * 
	 */
	public void match(String string, int index) {
		// I have a split method that splits the expression into parts (Helper Method)
		LinkedList<String> states = new LinkedList<String>();
		states = getSplitPattern();
		
		// This method calls another method which takes the split pattern and assigns each type of state an ID so that it's easier to manage
		// Those numbers are stored in a linked List for easier use
		LinkedList<Integer> stateNums = new LinkedList<Integer>();
		stateNums = getStateNums(states);

		String finalString = ""; // This is the final string we build

		int numOfStates = getNumOfStates();
		int curState = 0; // This is the current state
		int i = 0; // Letter Index in the line so we don't lose track from where we started
		int numsIndex = 0; // Index numstates
		int stateIndex = 0; // Index States
		int startIndex = -1; // Start index we have not filled yet
		int endIndex = -1; // Start index we have not filled yet
		int cur = 0; // current index while we are in the middle of an expression
		boolean success = false;
		
		// While i or cur (index we use for inside string) arent greater than its length and success is false
		while (i < string.length() && cur < string.length() && success == false) {

			if (curState < numOfStates) { // if the current State hasnt reached the total number of states, then we run this

				if (finalString.equals("") && curState == 0) { // If the state has not changed, then we set cur to i (starting point)
					cur = i;
				}

				if (stateNums.get(numsIndex) == 1) { // If the state type we are in right now is 1, then we are looking for just 1 literal character
					// if it is equal to the current character in the line, we add it to the final string, else, we set everything to the beginning values, and we increment i to start at the next index later
					if (states.get(stateIndex).charAt(0) == string.charAt(cur)) { 
						finalString += string.charAt(cur);
						curState++;
						numsIndex++;
						stateIndex++;
						cur++;
					} else {
						curState = 0;
						startIndex = -1;
						numsIndex = 0;
						stateIndex = 0;
						i++;
						finalString = "";
					}
				} else if (stateNums.get(numsIndex) == 2) { // If state is 2, we are looking for a letter that repeats 0 or more times (a*)
					// If the letters match, we add all that exist in a row, if not then we can just skip because it's 0 or more, so we increment states
					if (states.get(stateIndex).charAt(0) == string.charAt(cur)) {
						while (states.get(stateIndex).charAt(0) == string.charAt(cur)) {
							finalString += string.charAt(cur);
							cur++;
						}
					}
					curState++;
					numsIndex++;
					stateIndex++;
				} else if (stateNums.get(numsIndex) == 3) { // + operator
					// If the letters don't match we set everything to original values and increment i by 1, else if they do we add all that exist in a row because it's 1 or more, then we increment states
					if (states.get(stateIndex).charAt(0) != string.charAt(cur)) {
						curState = 0;
						startIndex = -1;
						numsIndex = 0;
						stateIndex = 0;
						i++;
						finalString = "";
					} else {
						while (states.get(stateIndex).charAt(0) == string.charAt(cur)) {
							finalString += string.charAt(cur);
							cur++;
						}
						curState++;
						numsIndex++;
						stateIndex++;
					}
				} else if (stateNums.get(numsIndex) == 4) { // State id = 4, [] brackets (if the character in the line is equal to any character inside the brackets, we add it to the finalString and move on)
					String charsString = "";
					if (states.get(stateIndex).length() == 2) {
						curState++;
						numsIndex++;
						stateIndex++;
					} else {
						for (int j = 1; j < states.get(stateIndex).length() - 1; j++) {
							charsString += states.get(stateIndex).charAt(j);
						}
						if (charsString.contains("" + string.charAt(cur))) {
							finalString += string.charAt(cur);
							cur++;
							curState++;
							numsIndex++;
							stateIndex++;
						} else {
							curState = 0;
							startIndex = -1;
							numsIndex = 0;
							stateIndex = 0;
							i++;
							finalString = "";
						}
					}

				} else if (stateNums.get(numsIndex) == 5) { // []* brackets with *, (Same as state id 4, except that we also apply the rule for 0 or more of that character to be added to finalString if they exist)
					String charsString = "";
					if (states.get(stateIndex).length() == 3) {
						curState++;
						numsIndex++;
						stateIndex += 2;

					} else {
						char c = string.charAt(cur);
						for (int j = 1; j < states.get(stateIndex).length() - 1; j++) {
							charsString += states.get(stateIndex).charAt(j);
						}
						if (charsString.contains("" + string.charAt(cur))) {
							while (c == string.charAt(cur)) {
								finalString += string.charAt(cur);
								cur++;

							}

						}
						curState++;
						numsIndex++;
						stateIndex += 2;

					}

				} else if (stateNums.get(numsIndex) == 6) { // []+ brackets with +, same as state id = 5, however instead of 0 or more, it's 1 or more, if its not 1 or more, then we simply reset to original values and increment i (starting point)
					String charsString = "";
					if (states.get(stateIndex).length() == 3) {
						curState = 0;
						startIndex = -1;
						numsIndex = 0;
						stateIndex = 0;
						i++;
						finalString = "";
					} else {
						char c = string.charAt(cur);
						for (int j = 1; j < states.get(stateIndex).length() - 1; j++) {
							charsString += states.get(stateIndex).charAt(j);
						}
						if (charsString.contains("" + c)) {
							while (c == string.charAt(cur)) {

								finalString += string.charAt(cur);
								cur++;
							}

							curState++;
							numsIndex++;
							stateIndex += 2;

						} else {
							curState = 0;
							startIndex = -1;
							numsIndex = 0;
							stateIndex = 0;
							i++;
							finalString = "";
						}
					}

				}
			}
			
			// If the current state is equal to the number of States, then we have successfully completed through the expression, the starting index is set to i, and the final one to cur (last index we were at)
			if (curState == numOfStates) {
				success = true;
				startIndex = i;
				endIndex = cur;
				break; // we break the loop
			}

		}
		
		//After the loop is broken, if success is true, we will print out this, otherwise we will print out nothing (as we were asked)
		if (success) {
			System.out.println("Match found on line " + (index + 1) + ", starting at position " + startIndex
					+ " and ending at position " + endIndex + ": " + finalString);
		}

	}

	/*
	 * This method takes in the expression in string format and breaks it down into smaller parts
	 * 
	 * We loop through everything. I am constantly checking if i + 1 is equal to the length of the pattern to make sure I don't go out of bounds
	 * basically if we have an expression like this "a*cd[ues]+l+"
	 * The LinkedList<String that will pop out will look like this |a*, c, d, [ues], +, l+|
	 * Now any sane person would have done this |a*, c, d, [ues]+, l+|, however I have a method that gives ID to each type of operation so I don't have
	 * to do a bunch of useless work later, so |a*, c, d, [ues], +, l+| is easier for me and it works, so it ain't stupid.
	 * The method that gives each type of state IDs is named getStateNums()
	 */
	public LinkedList<String> split(String pattern) {
		String temp = "";
		LinkedList<String> output = new LinkedList<String>();

		for (int i = 0; i < pattern.length(); i++) {
			if (!isOperator(pattern.charAt(i))) {
				if ((i + 1 != pattern.length())) {
					if (!isOperator(pattern.charAt(i + 1))) {
						output.add("" + pattern.charAt(i));
					} else {
						if (pattern.charAt(i + 1) == '*' || pattern.charAt(i + 1) == '+') {
							output.add("" + pattern.charAt(i) + pattern.charAt(i + 1));
							i++;
						} else if (pattern.charAt(i + 1) == '[') {
							output.add("" + pattern.charAt(i));

						}
					}
				} else {
					output.add("" + pattern.charAt(i));
				}
			} else {
				if (pattern.charAt(i) == '[') {
					while (pattern.charAt(i) != ']') {
						temp = temp + pattern.charAt(i);
						i++;
					}
				}
				temp = temp + pattern.charAt(i);
				output.add(temp);
				temp = "";
			}
		}
		return output;
	}
	
	/*
	 * This method takes the split up linked list and creates a new Linked List of Integers with IDs of each type of state in the expression
	 * For example:
	 * a single character with nothing else will receive id: 1
	 * a letter with an asterisk (a*) will receive an id of: 2
	 * a letter with a plus sign (a+) will receive an id of: 3
	 * Letters in brackets ([abcdefg...]) will receive an id of: 4
	 * Letters in brackets with an asterisk afterward ([abcdefg...]*) will receive an id of: 5 (they differ as they are 0 or more of one one of those letters (I don't know if it is required, but it will work either way))
	 * Letters in brackets with a plus sign afterward ([abcdefg...]+) will receive an id of: 6 (they differ as they are 1 or more of one one of those letters (I don't know if it is required, but it will work either way))
	 * 
	 * It literally searches through every part of the split up pattern. It checks by length first for the single letter, asterisk and plus sign, then assumes everything else is bracket stuff
	 * In the else statement, it checks bounds, and then checks the first character of the next part of the pattern, if it is an asterisk, this is id 5, if it is a plus sign, it is id 6, else it is id 4 (regular brackets)
	 * I know this is complicated but it works. Try an expression of your own in your head and work through the logic, I don't want to fill up this document with comments every other line. This should be enough.
	 */
	public LinkedList<Integer> getStateNums(LinkedList<String> list) {
		LinkedList<Integer> nums = new LinkedList<Integer>();

		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).length() == 1 && !isOperator(list.get(i).charAt(0))) {
				nums.add(1);
			} else if (list.get(i).length() == 2 && isOperator(list.get(i).charAt(1))) {
				if (list.get(i).charAt(1) == '*') {
					nums.add(2);
				} else {
					nums.add(3);
				}
			} else {
				if (i + 1 < list.size() && list.get(i + 1).charAt(0) == '*') {
					nums.add(5);
					i++;
				} else if (i + 1 < list.size() && list.get(i + 1).charAt(0) == '+') {
					nums.add(6);
					i++;
				} else {
					nums.add(4);
				}
			}
		}

		return nums;
	}

	// Checks if the character we enter is an operator or not
	public boolean isOperator(char c) {
		return (c == '*' || c == '+' || c == '[' || c == ']');
	}

}
