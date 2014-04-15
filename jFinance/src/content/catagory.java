package content;

import java.io.Serializable;
import java.util.ArrayList;

public class catagory implements Serializable//, Cloneable
{

	private static final long serialVersionUID = 1L;
	
	public String name;
	public double amount;
	public double prevAmount;
	public ArrayList<catagory> subCatagories;
	
	public catagory()
	{
		subCatagories = new ArrayList<catagory>();
		name = new String();
		amount = 0;
		prevAmount = 0;
	}
	
	public catagory(String name, double amount, double preAmount)
	{
		subCatagories = new ArrayList<catagory>();
		this.name = name;
		this.amount = amount;		
		this.prevAmount = preAmount;
	}
}
