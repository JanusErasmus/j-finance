package content;

import java.io.Serializable;

/** Transaction fields in each entry
 * 
 * @author Janus
 *
 */
public class Transaction implements Serializable
{
	private static final long serialVersionUID = 1L;
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
