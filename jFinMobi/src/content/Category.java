package content;

import java.io.Serializable;
import java.util.ArrayList;

public class Category implements Serializable//, Cloneable
{

	private static final long serialVersionUID = 1L;
	
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
