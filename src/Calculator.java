import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

public class Calculator {

	public static void main(String[] args) throws Exception 
	{
		System.out.println("Please enter the name of the file to be tested: ");
		Scanner s = new Scanner(System.in);
		String filename = s.nextLine();
		try
		{
			showResult(filename);
		}
		catch(FileNotFoundException e)
		{
			System.err.println("Cannot find the file: " + filename);
		}
	}

	public static void showResult(String s) throws Exception
	{
		System.out.println("The output for file '" + s + "' is: ");
		File file = new File(s);
		FileInputStream fin = new FileInputStream(file);
		ArrayList<String> codes = new ArrayList<String>(InputAnalysis.readFile(fin));
		Parser.sentenceCheck(codes);
	}
}
