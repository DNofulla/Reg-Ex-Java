package package1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

// Driver class, only has main method
public class Driver {

	public static void main(String[] args) throws IOException {

		RegEx pattern = new RegEx(args[0]); // Take in args[0] as the expression
		String file = args[1]; // Take in args[1] for the file name
		// Create the actual file, passing in the same
		FileReader fr = new FileReader(file);
		BufferedReader br = new BufferedReader(fr);
		String currLine; // Current line in the file
		// This arrayList is gonna hold all the lines
		ArrayList<String> lines = new ArrayList<String>();
		
		// We fill in the arrayList
		while ((currLine = br.readLine()) != null) {
			lines.add(currLine);
		}
		
		// We try to match the pattern to each lines we collected from the file
		for (int i = 0; i < lines.size(); i++) {
			pattern.match(lines.get(i), i);
		}
		br.close();
	}

	

}
