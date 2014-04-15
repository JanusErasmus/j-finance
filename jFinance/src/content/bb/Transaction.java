package content;

public class Transaction
{
	public String date;
	public String description;
	public String category;
	public double amount;
	public String from;
	
	public Transaction()
	{
		date = new String();
		description = new String();
		category = new String();
		amount = 0;
		from = new String();
	}
}
