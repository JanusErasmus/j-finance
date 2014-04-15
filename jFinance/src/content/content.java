package content;

import java.io.Serializable;
import java.util.ArrayList;



public class content implements Serializable
{
	private static final long serialVersionUID = 1L;
	public ArrayList<transaction> Entries;
	public ArrayList<catagory>	Catagories;
	public incomeList income;
	public incomeList currIncome;
	public incomeList prevIncome;
	
	
	
	public content()
	{
		Entries = new ArrayList<transaction>();
		Catagories = new ArrayList<catagory>();
		income = new incomeList();
		currIncome = new incomeList();
		prevIncome = new incomeList();
		
	}
	
	public class incomeList implements Serializable
	{		
		private static final long serialVersionUID = 1L;
		public double wallet;
		public double bank;
		
		public incomeList()
		{
			wallet = 0;
			bank = 0;
		}
	}
}
