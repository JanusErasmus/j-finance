package content;

import java.io.Serializable;

/** Transaction fields in each entry
 * 
 * @author Janus
 *
 */
public class transaction implements Serializable
{
	private static final long serialVersionUID = 1L;
	public String date;
	public String description;
	public String catagory;
	public double amount;
	public String from;
	
	public transaction()
	{
		date = new String();
		description = new String();
		catagory = new String();
		amount = 0;
		from = new String();
	}
}
