package com.fileIO;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

public class openObj
{
	ObjectInputStream file_in;
	
	public openObj(File name)  throws NoFileName
	{	
		if (name == null)
		{
			throw new NoFileName(45);
		}

		try
		{
			FileInputStream input = new FileInputStream(name);
			file_in = new ObjectInputStream(input);

		} 
		catch (FileNotFoundException e)
		{
			System.out.println("File not found");
			throw new NoFileName(5);

		}
		catch (IOException e)
		{
			System.out.println("tableFile error: " + e);
		}		
		
	}
	
	public Object read()
	{
		Object obj = null;
		try
		{
			obj = file_in.readObject();
		}
		catch(EOFException e)
		{			
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		
		
		return obj;
	}
}
