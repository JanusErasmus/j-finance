package content;

import fileIO.ArrayList;


public class Category 
{
	
	public String name;
	public double amount;
	public double prevAmount;
	public ArrayList<Category> subCatagories;
	
	public Category()
	{
		subCatagories = new ArrayList<Category>();
		name = new String();
		amount = 0;
		prevAmount = 0;
	}
	
	public Category(String name, double amount, double preAmount)
	{
		subCatagories = new ArrayList<Category>();
		this.name = name;
		this.amount = amount;		
		this.prevAmount = preAmount;
	}
}
