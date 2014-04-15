package com.fileIO;


public class NoFileName extends Exception
{
	private static final long serialVersionUID = 1L;
	private int num;

	NoFileName(int num)
	{
		this.num = num;
	}

	public String toString()
	{
		return "No file name given" + num;
	}
}	

