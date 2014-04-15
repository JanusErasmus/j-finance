package content;

public class TransactionList
{
	private Transaction[] trans;
	private int cnt;
	
	public TransactionList()
	{
		trans = new Transaction[100];
	}
	
	public int size()
	{
		return cnt;
	}
	
	public Transaction get(int index)
	{
		if(index > cnt)
			return null;
		
		return trans[index];
	}
	
	public int indexOf(Transaction tran)
	{
		for(int k = 0 ; k < cnt; k++)
		{
			if(trans[k] == tran)
				return k;
		}
		
		return -1;
	}
}
