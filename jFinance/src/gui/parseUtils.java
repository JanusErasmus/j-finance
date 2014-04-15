package gui;

import java.text.ParseException;

import com.ibm.icu.text.DecimalFormat;
import com.ibm.icu.text.DecimalFormatSymbols;

public class parseUtils
{
	public static String amount(String val)
	{	
		double number = getAmount(val);

		return amount(number);
	}
	
	public static String amount(double num)
	{
		DecimalFormatSymbols otherSymbols = DecimalFormatSymbols.getInstance();//new DecimalFormatSymbols(Locale.GERMAN);//.getInstance();
		otherSymbols.setDecimalSeparator('.');
		otherSymbols.setGroupingSeparator(',');
		DecimalFormat df = new DecimalFormat("R #,###,##0.00", otherSymbols);
				
		return df.format(num).toString();
	}

	public static double getAmount(String val)
	{			
		double number = 0;
		Number parseNum = null;
		
		DecimalFormatSymbols otherSymbols = DecimalFormatSymbols.getInstance();//new DecimalFormatSymbols(Locale.GERMAN);//.getInstance();
		otherSymbols.setDecimalSeparator('.');
		otherSymbols.setGroupingSeparator(',');
		DecimalFormat df = new DecimalFormat("R #,###,##0.00", otherSymbols);
		
		try
		{
			parseNum = df.parse(val);
		}
		catch (ParseException e)
		{
			//System.out.println("Parse Err");
			try
			{
				number = Double.valueOf(val);
			}
			catch(NumberFormatException ex)
			{
				//System.out.println("Number format err");
				
				//is already formatted?
				if(val.startsWith("R"))
				{
					//System.out.println("Already formatted " + val.substring(1));
					try
					{
						number = Double.valueOf(val.substring(1));
					}
					catch(NumberFormatException err)
					{
						number = 0;
					}
				}
				else
				{
					number = 0;
				}
			}
		}
		
		if(parseNum != null)
		{
			//System.out.println(parseNum.doubleValue());
			number = parseNum.doubleValue();
		}
		
		return number;
	}
}
