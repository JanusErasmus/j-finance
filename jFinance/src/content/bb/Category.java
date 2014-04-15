package content;

public class Category 
{	
	public String name;
	public double amount;
	public double prevAmount;
	public Category subCatagories[];
	
	public Category()
	{
		subCatagories = new Category[20];
		name = new String();
		amount = 0;
		prevAmount = 0;
	}
	
	public Category(String name, double amount, double preAmount)
	{
		subCatagories = new Category[20];
		this.name = name;
		this.amount = amount;		
		this.prevAmount = preAmount;
	}
}