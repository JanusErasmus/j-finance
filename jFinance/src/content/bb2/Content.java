package content;

import fileIO.ArrayList;


public class Content
{
	public ArrayList<Transaction> entries;
	public ArrayList<Category>	categories;
	public incomeList income;
	public incomeList currIncome;
	public incomeList prevIncome;
	
	
	
	public Content()
	{
		entries = new ArrayList<Transaction>();
		categories = new ArrayList<Category>();
		income = new incomeList();
		currIncome = new incomeList();
		prevIncome = new incomeList();
		
	}
	
	public class incomeList 
	{		
		public double wallet;
		public double bank;
		
		public incomeList()
		{
			wallet = 0;
			bank = 0;
		}
	}
	
	
	/**Calculates the sum of the categories in the list
	 * 
	 * @return The sum of the categories amount and previous amounts
	 */
	 public double sumOfCategory()
	 {
		 double sum = 0;
		 
		 if(categories.size() == 0)
			 return 0;
		 
		 for (int i = 0; i < categories.size(); i++)
		{
			 
			 if(categories.get(i).subCatagories.size() > 0)
				 sum += sumOfSubCategory(categories.get(i));
			 else
				 sum += categories.get(i).amount + categories.get(i).prevAmount;
		}
		 
		 return sum;
	 }
	 
	 /** Calculates the sum of subCategories in a Category
	  * 
	  * @param cat The category that should be calculated
	  * @return The sum of the sub categories amount and prevAmount
	  */
	 public double sumOfSubCategory(Category cat)
	 {
		 double sum = 0;
		 
		//when no categories to filter, or index to high return
		 if(cat == null || categories.size() == 0)
			 return 0;
		 
		 //is in list?
		 int index = categories.indexOf(cat);		 
		 if(index < 0 )
			 return 0;
		 
		 for (int i = 0; i < categories.get(index).subCatagories.size(); i++)
		{
			 sum += categories.get(index).subCatagories.get(i).amount + categories.get(index).subCatagories.get(i).prevAmount; 			
		}		 
		 
		 return sum;
	 }
	 
	 public double sumOfTransfers(String from)
	 {
		 double sum = 0;
		 
		// when no entries
		 if (entries.size() == 0)
			 return 0;

		 for (int i = 0; i < entries.size(); i++)
		 {
			 if("Transfer".equalsIgnoreCase(entries.get(i).category))
			 {
				 if(from.equalsIgnoreCase(entries.get(i).from))
					 sum += entries.get(i).amount;
			 }
		 }
		 
		 return sum;
	 }
	 
	 /** Calculates the sum of the transactions
	  * 
	  * @param from The place where the transaction is processed from
	  * @return the sum of the entries' with from
	  */
	 public double sumOfTransactions(String from)
	 {
		 double sum = 0;
		 
		 if(from == null)
			 return 0;
		 
		// when no entries
		 if (entries.size() == 0)
			 return 0;
		 
		 for (int i = 0; i < entries.size(); i++)
		 {
			 if(from.equalsIgnoreCase(entries.get(i).from))
			 	sum += entries.get(i).amount;
		 }
		 
		 return sum;
	 }
	 
	 /** Calculates the sum of the transactions
	  * 
	  * @param cat The category that should be calculated
	  * @param subCat The sub category
	  * @return The sum of the entries' with cat and subCat
	  */
	 public double sumOfTransaction(Category cat, Category subCat)
	{
		double sum = 0;

		// when no categories to filter, or index to high return
		if (cat == null || subCat == null || categories.size() == 0)
			return 0;

		// when no entries
		if (entries.size() == 0)
			return 0;
		
		String srcCat = cat.name;
		String srcSubCat = subCat.name;
		for (int i = 0; i < entries.size(); i++)
		{
			if(entries.get(i).category == srcCat)
			{
				if(entries.get(i).description == srcSubCat)
					sum += entries.get(i).amount;
			}
		}

		return sum;
	}
	
	/** Calculates the sum of the transactions 
	 * 
	 * @param cat The category that should be calculated
	 * @return The sum of the entries' amounts
	 */
	public double sumOfTransaction(Category cat)
	{
		double sum = 0;
		
		//when no categories to filter, or index to high return
		if(cat == null || categories.size() == 0)
			return 0;
		
		//when no entries
		if(entries.size() == 0)
			return 0;
		
		String srcCat = cat.name;
		for (int i = 0; i < entries.size(); i++)
		{
			if(entries.get(i).category == srcCat)
				sum += entries.get(i).amount;
		}		
		
		
		return sum;
	}
}
