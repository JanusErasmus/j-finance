package com.fileIO;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public class saveObj
{	
	ObjectOutputStream file_out;
	
	public saveObj(File name) throws NoFileName
	{
		System.out.println("Opening file " + name);
		file_out = null;
		
		if (name == null)
		{
			throw new NoFileName(45);
		}

		try
		{
			FileInputStream input = new FileInputStream(name);
			ObjectInputStream file_in = new ObjectInputStream(input);
			file_in.close();

		} 
		catch (FileNotFoundException e)
		{			
			try
			{
				FileOutputStream file_output = new FileOutputStream(name);
				file_out = new ObjectOutputStream(file_output);				
				
			}
			catch (IOException ex)
			{
				System.out.println("File creation error");				
			}
			

		}
		catch (IOException e)
		{
			System.out.println("tableFile error: " + e);
		}
		
		if (file_out == null)
		{
			try
			{
				FileOutputStream file_output = new FileOutputStream(name);
				file_out = new ObjectOutputStream(file_output);

			} catch (IOException ex)
			{
				System.out.println("File creation error");
				throw new NoFileName(6);				
			}
		}		
	}

	public void write(Object obj)
	{
		try
		{
			file_out.writeObject(obj);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
}
