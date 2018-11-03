import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ContactInfo {
	private String name;
	private String phoneNumber;
	private String emailAddress;
	
	//creates dictionary files that will be used to check input for identifiers
	//also calls function that grabs contactInfo from doc
	public ContactInfo(String[] document) throws IOException {
		getContactInfo(document);
	}
	
	//Creates arrays that will store "point" values that determine which line of the
	//input doc is most likely a name/email address/phone number
	// then calls functions that populate the arrays with points
	//then determines which strings are most likely a name/email address/phone number
	private void getContactInfo(String[] document) throws IOException {
		int[] namePoints = new int[document.length];
		int[] phonePoints = new int[document.length];
		int[] emailPoints = new int[document.length];
		getNamePoints(document, namePoints);
		getPhonePoints(document, phonePoints);
		getEmailPoints(document, emailPoints);
		findContactInfo(namePoints, phonePoints, emailPoints, document);
		
	}
	
	//grabs contact info based on points in each array
	private void findContactInfo(int[] namePoints, int[] phonePoints, int[] emailPoints, String[] document) {
		int index = 0;
		int maxPoints = 0;
		//checks which index has the most email points
		for(int i = 0; i<emailPoints.length;i++) {
			if(emailPoints[i]>maxPoints) {
				maxPoints = emailPoints[i];
				index = i;
			}
		}
		//grabs the corresponding index in the input doc for which has the most points
		//sets that as the email address
		//sets that index to 0 in the other point columns
		//(as each index in the original doc should only be used once)
		if(maxPoints>0) {
			emailAddress = document[index];
			phonePoints[index] = 0;
			namePoints[index] = 0;
		}
		//if maxPoints == 0, that means no identifiers were found in the input String for emails
		//as a result, we return a "no matches found" string, rather than a random
		//string from the input
		else {
			emailAddress = "No Matches Found";
		}
		index = 0;
		maxPoints = 0;
		//does the same thing as above, except with phonePoints and 'phoneNumber'
		for(int i = 0; i<phonePoints.length;i++) {
			if(phonePoints[i]>maxPoints) {
				maxPoints = phonePoints[i];
				index = i;
			}
		}
		if(maxPoints>0) {
			phoneNumber = onlyDigits(document[index]);
			namePoints[index] = 0;
		}
		else {
			phoneNumber = "No Matches Found";
		}
		index = 0;
		maxPoints = 0;
		//does the same thing as above, except with namePoints and 'name'
		for(int i = 0; i<namePoints.length;i++) {
			if(namePoints[i]>maxPoints) {
				maxPoints = namePoints[i];
				index = i;
			}
		}
		if(maxPoints>0) {
			name = document[index];
		}
		else {
			name = "No Matches Found";
		}
		
	}
	
	//gets namePoints for each index of the input doc, storing it in the corresponding
	//index for namePoints
	private void getNamePoints(String[] document, int[] points) throws IOException {
		for(int i=0;i<document.length;i++) {
			points[i] = nameParameters(document[i], getFiles("nameDictionary.txt"));
		}
	}
	//checks the input String for various parameters that may identify it as a name
	// for each parameter it passes, its 'numPoints' value is incremented by 1
	// numPoints is returned
	private int nameParameters(String line, String[] nameDict) {
		int numPoints = 0;
		//Check if any of the words in the nameDict show up. If they DO NOT show up
		//increment 'numPoints' by 1
		if(nameDict!=null) {
			boolean match = false;
			for(int i = 0;i<nameDict.length;i++) {
				if(line.toLowerCase().contains(nameDict[i])) {
					match = true;
					break;
				}
			}
			if(!match){
				numPoints++;
			}
		}
		//if a space exists
		//increment 'numPoints'
		if(line.contains(" ")) {
			numPoints++;
		}
		return numPoints;
	}
	//gets phonePoints for each index of the input doc, storing it in the corresponding
	//index for phonePoints
	private void getPhonePoints(String[] document, int[] points) throws IOException {
		for(int i=0;i<document.length;i++) {
			points[i] = phoneParameters(document[i], getFiles("phoneDictionary.txt"));
		}
	}
	//checks the input String for various parameters that may identify it as a phone number
	// for each parameter it passes, its 'numPoints' value is incremented by 1
	// numPoints is returned
	private int phoneParameters(String line, String[] phoneDict) {
		int digitCheck = 10;
		int characterCheck = 8;
		int numPoints = 0;
		//checks if any of the phoneDictonary terms show up in the input string (tel, telephone, phone)
		//for each one that does, increment the 'numPoints' counter
		int numDigits = getDigits(line);
		if(phoneDict!=null) {
			for(int i = 0;i<phoneDict.length;i++) {
				if(line.toLowerCase().contains(phoneDict[i])) {
					numPoints++;
				}
			}
		}
		//check the number of digits in the String. If the String contains at least 10 digits
		//increment the 'numPoints' counter
		//also checks how many characters in the String aren't digits. 
		//If there are less than 8 (characterCheck) non-digit characters
		//increment 'numPoints' counter
		if(numDigits>=digitCheck) {
			numPoints++;
			if(line.length()-numDigits<characterCheck) {
				numPoints++;
			}
		}
		return numPoints;
	}

	private void getEmailPoints(String[] document, int[] points) throws IOException{
		for(int i=0;i<document.length;i++) {
			points[i] = emailParameters(document[i], getFiles("emailDictionary.txt"));
		}
	}
	
	//checks the input String for various parameters that may identify it as an email address
	// for each parameter it passes, its 'numPoints' value is incremented by 1
	// numPoints is returned
	private int emailParameters(String line, String[] emailDict) {
		int numPoints = 0;
		//checks if any of the emailDictonary terms show up in the input string (@,.com,.net, etc)
		//for each one that does, increment the 'numPoints' counter
		if(emailDict!=null) {
			for(int i = 0;i<emailDict.length;i++) {
				if(line.contains(emailDict[i])) {
					numPoints++;
				}
			}
		}
		
		//check if any whitespace exists in the string.
		//If so, increment the 'numPoints' counter
		if(!line.contains(" ")) {
			numPoints++;
		}
		
		//check if an '@' symbol exists. If so, check if it is followed by a '.'
		//If so, increment the 'numPoints' counter
		if(line.contains("@")) {
			char[] ln = line.toCharArray();
			int atIndex = 0;
			for(int i = 0;i<ln.length;i++) {
				if(ln[i]=='@') {
					atIndex=i;
					break;
				}
			}
			for(int i = atIndex;i<ln.length;i++) {
				if(ln[i]=='.') {
					numPoints++;
					break;
				}
			}
		}
		return numPoints;
	}	
	
	public String getName() {
		return name;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public String getEmailAddress() {
		return emailAddress;
	}
	//how the line by line files are stored as strings in a String[]
	private String[] getFiles(String fileName) throws IOException {
		File document = new File(fileName);
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(document));
			String line; 
			List<String> lines = new ArrayList<String>();
			while ((line = br.readLine()) != null) {
				lines.add(line);
			  } 
			br.close();
			String[] doc = lines.toArray(new String[0]);
			return doc;
		} catch (FileNotFoundException e) {	
			return null;
		} 
	
	}
	//get number of digits in a String
	//only used for phone number
	private int getDigits(String line) {
		int numDigits = 0;
		for (int i = 0; i < line.length(); i++) {
	        if (Character.isDigit(line.charAt(i))) {
	            numDigits++;
	        }
	    }
		return numDigits;
	}
	//only returns digits, removing all non-digits
	private String onlyDigits(String line) {
		String number = "";
		for (int i = 0; i < line.length(); i++) {
	        if (Character.isDigit(line.charAt(i))) {
	            number+=Character.toString(line.charAt(i));
	        }
	    }
		return number;
	}
}
