package fileIO;

public class ArrayList<T>
{
	private T list[];
	private int count = 0;
	
	public ArrayList()
	{		
		
	}
	
	public T get(int index)
	{
		return list[index];
	}
	
	public int size()
	{
		return count;
	}
	
	public int indexOf(T t)
	{		
		//look for this object
		for(int k = 0; k < count; k++)
		{
			if(list[k] == t)
				return k;
		}
		
		return -1;
	}
}
