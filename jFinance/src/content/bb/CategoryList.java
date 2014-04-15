package content;

public class CategoryList 
{
	private Category[] cats;
	private int cnt = 0;
	
	public CategoryList()
	{		
		cats = new Category[20];
	}
	
	public void add(Category cat)
	{
		cats[cnt] = cat;
		cnt++;
	}
	
	public int size()
	{
		return cnt;
	}
	
	public Category get(int index)
	{
		if(index > cnt)
			return null;
		
		return cats[index];
	}
	
	public int indexOf(Category cat)
	{
		for(int k = 0 ; k < cnt; k++)
		{
			if(cats[k] == cat)
				return k;
		}
		
		return -1;
	}
	
}
