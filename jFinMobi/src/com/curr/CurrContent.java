package com.curr;

import java.io.File;
import java.util.ArrayList;

import content.Category;
import content.Content;
import content.Transaction;

import com.fileIO.NoFileName;
import com.fileIO.openObj;
import com.fileIO.saveObj;

public class CurrContent 
{
	private static final String workDir = "/sdcard/jFinance/";
	private Content currBudget = null;
	private String fileName = null;
	private static CurrContent _instance  = null;
	private Transaction tmpTrans = null;
	private int tmpIndex = -1;
	
	private CurrContent()
	{
		System.out.println("Creating CurrContent");
	}
	
	public static void init()
	{
		_instance = new CurrContent();
	}
	
	public static CurrContent get()
	{
		return _instance;
	}
	
	public ArrayList<Category> getCategories()
	{
		if(currBudget == null)
			return null;
		
		return currBudget.categories;
	}
	
	public ArrayList<Transaction> getTransactions()
	{
		if(currBudget == null)
			return null;
		
		return currBudget.entries;
	}
	
	public String getName()
	{
		return fileName;
	}
	
	public void setFileName(String name)
	{
		System.out.println("Setting file name " + name);
		fileName = name;
		
		if(!openContent())
			System.out.println("Object not opened");		
	}
	
	private boolean openContent()
	{				
		if(fileName == null)
			return false;
		
		File f = new File(workDir + fileName);
		
		openObj open;
		
		try 
		{
			open = new openObj(f);
		}
		catch (NoFileName e) 
		{
			e.printStackTrace();
			return false;
		}
		
		//System.out.println("Opened file " + workDir + fileName);
		
		currBudget = (Content)open.read();
		
		if(currBudget == null)
			return false;
		
		return true;
	}
	
	public boolean saveContent()
	{				
		if(fileName == null)
			return false;
		
				
		File f = new File(workDir + fileName);
		
		saveObj save;
		
		try
		{
			save = new saveObj(f);
		} 
		catch (NoFileName e) 
		{
			e.printStackTrace();
			return false;
		}
		
		if(currBudget == null)
			currBudget = new Content();
		
					
		save.write(currBudget);
				
		return true;
	}
	
	public String getWorkDir()
	{
		return workDir;
	}

	public Content getBudget()
	{
		return currBudget;
		
	}
	
	public void setTmpTrans(Transaction tmp)
	{
		tmpTrans = tmp;
	}
	
	public Transaction getTmpTrans()
	{
		Transaction trans = tmpTrans;
		tmpTrans = null; //read only when it was set;
		
		return trans;		
	}
	
	public void setTmpIndex(int idx)
	{
		tmpIndex = idx;
	}
	
	public int getTmpIndex()
	{
		int idx = tmpIndex;
		tmpIndex = -1;
		return idx;
	}

}
