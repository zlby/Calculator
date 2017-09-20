import java.nio.Buffer;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.Stack;

public class Parser {
	
	private static ArrayList<String> variableName = new ArrayList<String>();
	private static ArrayList<Object> variableValue = new ArrayList<Object>();
	private static int line = 0;
	
	public static void sentenceCheck(ArrayList<String> sentences) throws Exception
	{
		sentences = InputAnalysis.check(sentences);
		for (String sentence : sentences)
		{
			line++;
			char[] chars = sentence.toCharArray();
			if (sentence.indexOf("print(") == 0)
			{
				String vari = sentence.substring(sentence.indexOf("(") + 1, sentence.indexOf(")"));
				if (variableName.contains(vari))
				{
					System.out.println(variableValue.get(variableName.indexOf(vari)));
				}
				else
				{
					try
					{
						System.out.println(calculate(vari));
					}
					catch(Exception e)
					{
						System.err.println("");
					}
				}
			}
			else if (chars[0] < 65 || chars[0] > 122 || (chars[0] > 90) && (chars[0] < 97))
			{
				System.err.println("ERROR! line:" + line + "    " + "Variable name should begin with an English character.");
				System.exit(0);
			}
			else if (sentence.indexOf(Operator.equal) == -1)
			{
				System.err.println("ERROR! line:" + line + "    " + "The equation must either have a equal '=' sign or begin with 'print('.");
				System.exit(0);
			}
			else
			{
				StringBuilder var = new StringBuilder();
				int i = 0;
				for (;;)
				{
					if (chars[i] == Operator.equal)
						break;
					var.append(chars[i]);
					i++;
				}
				if (var.toString().contains("(") || var.toString().contains(")") || var.toString().contains("+") || var.toString().contains("-") || var.toString().contains("*") || var.toString().contains("/") || var.toString().contains("."))
				{
					System.err.println("ERROR! line:" + line + "    " + "Variable name contains illegal character: '.' or '(' or ')' or '+' or '-' or '*' or '/'.");
					System.exit(0);
				}
				if (!variableName.contains(var.toString()))
				{
					variableName.add(var.toString());
					variableValue.add(null);
				}
				
				StringBuilder sen = new StringBuilder();
				for (; i < chars.length - 1; i++)
					sen.append(chars[i]);
				variableValue.set(variableName.indexOf(var.toString()), wordAnalysis(sen));
			}
		}
	}
	
	public static Object wordAnalysis(StringBuilder sen) throws Exception
	{
		Stack<String> st = new Stack<String>();
		StringBuilder s = new StringBuilder();
		char[] chars = sen.toString().toCharArray();
		if (chars[chars.length - 1] == Operator.add || chars[chars.length - 1] == Operator.div || chars[chars.length - 1] == Operator.equal || chars[chars.length - 1] == Operator.minus || chars[chars.length - 1] == Operator.mul)
		{
			System.err.println("ERROR! line:" + line + "    " + "Invalid formula.");
			System.exit(0);
		}
		for (int i = 1; i < sen.length(); i++)
		{
			if (chars[i] == Operator.equal)
			{
				System.err.println("ERROR! line:" + line + "    " + "Invalid formula.");
				System.exit(0);
			}
			if (chars[i] == Operator.add || chars[i] == Operator.div || chars[i] == Operator.minus || chars[i] == Operator.mul)
			{
				if (chars[i + 1] == Operator.add || chars[i + 1] == Operator.div || chars[i + 1] == Operator.minus || chars[i + 1] == Operator.mul)
				{
					System.err.println("ERROR! line:" + line + "    " + "Invalid formula.");
					System.exit(0);
				}
			}
		}
		for (int i = 0; i < sen.length(); i++)
		{
			st.push(sen.substring(i, i + 1));
			if(chars[i] == Operator.rbr)
			{
				StringBuilder sb = new StringBuilder();
				st.pop();
				try
				{
					while(!st.peek().equals("("))
					{
						sb.insert(0, st.pop());
					}
				}
				catch(EmptyStackException e)
				{
					System.err.println("ERROR! line:" + line + "    " + "Brackets cannot match. (loss of left bracket)");
					System.exit(0);
				}
				st.pop();
				String stemp = calculate(sb.toString());
				for(int k = 0; k < stemp.length(); k++)
					st.push(stemp.substring(k, k + 1));
			}
		}
		while(!st.peek().equals("="))
		{
			s.insert(0, st.pop());
		}
		if (s.toString().contains("("))
		{
			System.err.println("ERROR! line:" + line + "    " + "Brackets cannot match. (loss of right bracket)");
			System.exit(0);
		}
		try
		{
			return Integer.parseInt(calculate(s.toString()));
		}
		catch(NumberFormatException e)
		{
			return Double.parseDouble(calculate(s.toString()));
		}
	}
	
	public static String calculate(String s) throws Exception
	{
		int state = 0;   //plus: 1	minus:2		mul:3	div:4
		char[] chars_ = s.toCharArray();
		StringBuilder resb = new StringBuilder();
		for (int i = 0; i < chars_.length; i++)
		{
			if ((chars_[i] >= 65 &&chars_[i] <= 90) || (chars_[i] >= 97 &&chars_[i] <= 122))
			{
				StringBuilder varsb = new StringBuilder();
				while((i != chars_.length) && chars_[i] != Operator.add && chars_[i] != Operator.minus && chars_[i] != Operator.mul && chars_[i] != Operator.div && chars_[i] != Operator.rbr)
				{
					varsb.append(chars_[i]);
					i++;
				}
				if (!variableName.contains(varsb.toString()))
					{
						System.err.println("ERROR! line:" + line + "    " + "Use of undefined variable '" + varsb.toString() + "'");
						System.exit(0);
					}
				resb.append(variableValue.get(variableName.indexOf(varsb.toString())));
				i--;
			}
			else
			{
				resb.append(chars_[i]);
			}
		}
		s = resb.toString();
		char[] chars_2 = s.toCharArray();
		
		if (s.contains("*") || s.contains("/"))
		{
			StringBuilder codes = new StringBuilder();
			for (int i = 0; i < chars_2.length; i++)
			{
				StringBuilder numbers = new StringBuilder();
				while ((i != chars_2.length) && chars_2[i] != Operator.mul && chars_2[i] != Operator.div)
				{
					codes.append(chars_2[i]);
					i++;
				}
				int begin = i - 1;
				while ((begin != -1) && (chars_2[begin] != Operator.add) && chars_2[begin] != Operator.minus && chars_2[begin] != Operator.lbr)
				{
					codes.deleteCharAt(codes.length() - 1);
					numbers.insert(0, chars_2[begin--]);
				}
				while((i != chars_2.length) && chars_2[i] != Operator.add && chars_2[i] != Operator.minus && chars_2[i] != Operator.rbr)
				{
					numbers.append(chars_2[i++]);
				}
				
				char[] chars_3 = numbers.toString().toCharArray();
				if (numbers.toString().contains("."))
				{
					double tempRes = 0.0;
					int j = 0;
					for(; j < chars_3.length; j++)
					{
						int beg = j;
						if (chars_3[j] >= '0' && chars_3[j] <= '9')
						{
							while(j < chars_3.length &&  ((chars_3[j] >= 48 && chars_3[j] <= 57) || chars_3[j] == 46)) //chars[i] != Operator.add && chars[i] != Operator.div && chars[i] != Operator.minus && chars[i] != Operator.mul
								j++;
							if (state == 0)
							{
								tempRes = Double.parseDouble(numbers.substring(beg, j));
								j--;
							}
							else if (state == 3)
							{
								tempRes *= Double.parseDouble(numbers.substring(beg, j));
								j--;
								state = 0;
							}
							else if(state == 4)
							{
								if (Double.parseDouble(numbers.substring(beg, j)) == 0)
								{
									System.err.println("ERROR! line:" + line + "    " + "Cannot divided by zero.");
									System.exit(0);
								}
								tempRes /= Double.parseDouble(numbers.substring(beg, j));
								j--;
								state = 0;
							}
						}
						else if (chars_3[j] == Operator.mul)
							state = 3;
						else if (chars_3[j] == Operator.div)
							state = 4;
					}
					codes.append(Double.toString(tempRes));
					i--;
				}
				else if (!numbers.toString().contains("."))
				{
					int tempRes = 0;
					int j = 0;
					for(; j < chars_3.length; j++)
					{
						int beg = j;
						if (chars_3[j] >= '0' && chars_3[j] <= '9')
						{
							while(j < chars_3.length &&  ((chars_3[j] >= 48 && chars_3[j] <= 57) || chars_3[j] == 46)) //chars[i] != Operator.add && chars[i] != Operator.div && chars[i] != Operator.minus && chars[i] != Operator.mul
								j++;
							if (state == 0)
							{
								tempRes = Integer.parseInt(numbers.substring(beg, j));
								j--;
							}
							else if (state == 3)
							{
								tempRes *= Integer.parseInt(numbers.substring(beg, j));
								j--;
								state = 0;
							}
							else if(state == 4)
							{
								if (Integer.parseInt(numbers.substring(beg, j)) == 0)
								{
									System.err.println("ERROR! line:" + line + "    " + "Cannot divided by zero.");
									System.exit(0);
								}
								tempRes /= Integer.parseInt(numbers.substring(beg, j));
								j--;
								state = 0;
							}
						}
						else if (chars_3[j] == Operator.mul)
							state = 3;
						else if (chars_3[j] == Operator.div)
							state = 4;
					}
					codes.append(Integer.toString(tempRes));
					i--;
				}
			}
			s = codes.toString();
		}
		char[] chars = s.toCharArray();
		
		if (s.contains("."))
		{
			double result = 0.0;
			int i = 0;
			for (; i < chars.length; i++)
			{
				int begin = i;
				if (chars[i] >= '0' && chars[i] <= '9')
				{
					while(i < chars.length &&  ((chars[i] >= 48 && chars[i] <= 57) || chars[i] == 46)) //chars[i] != Operator.add && chars[i] != Operator.div && chars[i] != Operator.minus && chars[i] != Operator.mul
						i++;
					if (state == 0)
					{
						result = Double.parseDouble(s.substring(begin, i));
						i--;
					}
					else if (state == 1)
					{
						result += Double.parseDouble(s.substring(begin, i));
						i--;
						state = 0;
					}
					else if(state == 2)
					{
						result -= Double.parseDouble(s.substring(begin, i));
						i--;
						state = 0;
					}
				}
				else if (chars[i] == Operator.add)
					state = 1;
				else if (chars[i] == Operator.minus)
					state = 2;
				else if (chars[i] == Operator.mul)
					state = 3;
				else if (chars[i] == Operator.div)
					state = 4;
			}
			return Double.toString(result);
		}
		if (!s.contains("."))
		{
			int result = 0;
			int i = 0;
			for (; i < chars.length; i++)
			{
				int begin = i;
				if (chars[i] >= '0' && chars[i] <= '9')
				{
					while(i < chars.length &&  ((chars[i] >= 48 && chars[i] <= 57) || chars[i] == 46)) //chars[i] != Operator.add && chars[i] != Operator.div && chars[i] != Operator.minus && chars[i] != Operator.mul
						i++;
					if (state == 0)
					{
						result = Integer.parseInt(s.substring(begin, i));
						i--;
					}
					else if (state == 1)
					{
						result += Integer.parseInt(s.substring(begin, i));
						i--;
						state = 0;
					}
					else if(state == 2)
					{
						result -= Integer.parseInt(s.substring(begin, i));
						i--;
						state = 0;
					}
				}
				else if (chars[i] == Operator.add)
					state = 1;
				else if (chars[i] == Operator.minus)
					state = 2;
			}
			return Integer.toString(result);
		}
		
		return null;
	}
}
