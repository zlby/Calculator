import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class InputAnalysis {
	
	public static ArrayList<String> readFile(InputStream fileName)
	{
		ArrayList<String> strarr = new ArrayList<String>();
		BufferedReader br = new BufferedReader(new InputStreamReader(fileName));
		String temp = "";		
		try {
			for(;;)
			{
				temp = br.readLine();
				if (temp == null)
					break;
				strarr.add(temp);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return strarr;
	}
	
	public static ArrayList<String> check(ArrayList<String> code) throws Exception
	{
		ArrayList<String> arr = new ArrayList<String>();
		int line = 1;
		for (String str : code)
		{
			char[] chars_ = str.toCharArray();
			StringBuilder sp = new StringBuilder();
			for (int i = 0; i < chars_.length; i++)
			{
				if (chars_[i] != Operator.space)
					sp.append(chars_[i]);
			}
			str = sp.toString();
			if(str.indexOf(Operator.simic) == -1)
			{
				System.err.println("ERROR! line:" + line + "    " + "The sentence doesn't have a semicolon.");
				System.exit(0);
			}
			else if (str.indexOf(Operator.simic) != (str.length() - 1))
			{
				System.err.println("ERROR! line:" + line + "    " + "Semicolon can't be inside the sentence.");
				System.exit(0);
			}
			else
			{
				arr.add(str);
			}
			line++;
		}
		return arr;
	}
}
