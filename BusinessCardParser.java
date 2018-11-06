import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BusinessCardParser {
	//gets the contact info
	//prints out each element of the ContactInfo
	public static void main(String[] args) throws IOException {
		String inputDoc = null;
		try {
			inputDoc = args[0];
		}
		catch(ArrayIndexOutOfBoundsException e){
			System.out.println("No input file, ending process.");
			System.exit(0);
		}
		ContactInfo contactInfo = getContactInfo(inputDoc);
		System.out.println("Name: "+contactInfo.getName());
		System.out.println("Phone: "+contactInfo.getPhoneNumber());
		System.out.println("Email: "+contactInfo.getEmailAddress());
	}
	
	//gets each line of the input doc, turning each into a string
	//and storing it in a String[]
	//then returns a ContactInfo object after 
	public static ContactInfo getContactInfo(String document) throws IOException {
		File fileDocument = new File(document);
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(fileDocument));
		} catch (FileNotFoundException e) {
			System.out.println("Input file does not exist, ending process.");
			System.exit(0);
		} 
		String line; 
		List<String> lines = new ArrayList<String>();
		while ((line = br.readLine()) != null) {
			lines.add(line);
		  } 
		br.close();
		String[] doc = lines.toArray(new String[0]);
		return new ContactInfo(doc);
	}

}
