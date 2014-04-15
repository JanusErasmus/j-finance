package com.gui;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.ArrayList;

import com.curr.CurrContent;

import content.Category;
import content.Transaction;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class PanelBuilder{

	private static final int RowPadding = 5;


	public static View createWidgetPanel(ArrayList<String> lst, ArrayList<Double> values, Activity activity)
	{
	
	  TableLayout table = new TableLayout(activity);
	  LayoutUtils.Layout.WidthFill_HeightFill.applyViewGroupParams(table);
	
	  // set which column is expandable/can grow
	  table.setColumnStretchable(1, true);
	
	  // apply layout animation
	  //AnimUtils.setLayoutAnim_slideupfrombottom(table, activity);
	
	  ArrayList<View> rowList = new ArrayList<View>();	  
	
	  for(int i = 0; i < lst.size(); i++)
	  {
		  rowList.add(createRow(i, lst.get(i), values.get(i), activity));
	  }
	
	
	
	  // add all the rows to the table
	  {
	    for (View row : rowList) {
	      LayoutUtils.Layout.WidthWrap_HeightWrap.applyTableLayoutParams(row);
	      row.setPadding(1, 1, 1, 1);
	      table.addView(row);
	    }
	  }
	
	  return table;
	
	}
	
	public static View createWidgetPanel(ArrayList<Transaction> entries, Activity activity)
	{
	
	  TableLayout table = new TableLayout(activity);
	  LayoutUtils.Layout.WidthFill_HeightFill.applyViewGroupParams(table);
	
	  // set which column is expandable/can grow
	  table.setColumnStretchable(1, true);
	
	  // apply layout animation
	  //AnimUtils.setLayoutAnim_slideupfrombottom(table, activity);
	
	  ArrayList<View> rowList = new ArrayList<View>();	  
	
	  for(int i = 0; i < entries.size(); i++)
	  {
		  rowList.add(createRow(i, entries.get(i), activity));
	  }
	
	
	
	  // add all the rows to the table
	  {
	    for (View row : rowList) {
	      LayoutUtils.Layout.WidthWrap_HeightWrap.applyTableLayoutParams(row);
	      row.setPadding(RowPadding, RowPadding, RowPadding, RowPadding);
	      table.addView(row);
	    }
	  }
	
	  return table;
	
	}
	

	
	public static TableRow createRow(int idx, String txt, double val, Context activity) {
		  
		TableRow row = new TableRow(activity);
		
		OnLongClickListener clickLstn;
		clickLstn = new OnLongClickListener() {
			public boolean onLongClick(View v) {

				TableRow rView = (TableRow)v;
				if(rView != null)
				{	
					int index;
					TextView tView = (TextView)rView.getChildAt(1);					
					if(tView != null)
					{	
						index = Integer.parseInt(tView.getText().toString());
						//System.out.println("Summary Index is " + index);
						
						Category catSel = CurrContent.get().getCategories().get(index);						
						if(!catSel.subCatagories.isEmpty())
						{
							String txt = catSel.name + "\n\n";
							
							for(int k = 0; k < catSel.subCatagories.size(); k++)
							{
								Category subSel = catSel.subCatagories.get(k);
								txt += subSel.name + " " + amount( subSel.prevAmount + subSel.amount - CurrContent.get().getBudget().sumOfTransaction(catSel, subSel) ) + "\n";
							}
							Toast msg =	Toast.makeText(SummaryActivity.get(),txt, Toast.LENGTH_LONG);
			        		msg.show();
						}
												
					}					
				}
				return false;
			}
		};

		row.setOnLongClickListener(clickLstn);
						
		TextView text = new TextView(activity);
		text.setText(txt);
		text.setPadding(0, 0, 0, 0);
		LayoutUtils.Layout.WidthWrap_HeightWrap.applyTableRowParams(text);
		
		row.addView(text);
		
		text = new TextView(activity);
		text.setText(String.valueOf(idx));
		text.setPadding(0, 0, 0, 0);
		text.setWillNotDraw(true);
		LayoutUtils.Layout.WidthWrap_HeightWrap.applyTableRowParams(text);
		
		row.addView(text);

		text = new TextView(activity);
		if(val == -9999999)//openSpace
		{
			text.setText("");
		}
		else
		{
			text.setText(amount(val));
			if(val < 0)
			{
				text.setTextColor(Color.RED);
			}
			if(val == 0)
			{
				text.setTextColor(Color.DKGRAY);
			}
		}
		
		text.setPadding(0, 0, 0 , 0);
		text.setGravity(android.view.Gravity.RIGHT);
		LayoutUtils.Layout.WidthWrap_HeightWrap.applyTableRowParams(text);
		
		row.addView(text);


		return row;
	}	
	
	public static TableRow createRow(int idx, Transaction tran, Context activity) {
		  
		TableRow row = new TableRow(activity);
		
		OnLongClickListener clickLstn;
		
		
		clickLstn = new OnLongClickListener() {
			
			

			public boolean onLongClick(View v) {

				TableRow rView = (TableRow)v;
				if(rView != null)
				{	
					int index;
					TextView tView = (TextView)rView.getChildAt(1);					
					if(tView != null)
					{	
						index = Integer.parseInt(tView.getText().toString());
						//System.out.println("Index is " + index);
						
						CurrContent.get().setTmpIndex(index);
						Intent i = new Intent(TransActivity.get(), AddActivity.class);				
						TransActivity.get().startActivityForResult(i, 1);
					}
					
				}
				return false;
			}
		};

		row.setOnLongClickListener(clickLstn);


		TextView text = new TextView(activity);
		text.setText(tran.category + "    ");
		text.setPadding(RowPadding, 0, 0, 0);		
		LayoutUtils.Layout.WidthWrap_HeightWrap.applyTableRowParams(text);		
		row.addView(text);
		
		text = new TextView(activity);
		text.setText(String.valueOf(idx));
		text.setPadding(0, 0, 0, 0);
		text.setWillNotDraw(true);
		LayoutUtils.Layout.WidthWrap_HeightWrap.applyTableRowParams(text);		
		row.addView(text);
		
		text = new TextView(activity);		
		//if decription to long, limit to 10 characters
		String desc = tran.description;
		if(desc.length() > 10)
		{
			desc = desc.substring(0, 10) + "...";			
		}
		text.setText(desc);
		text.setPadding(0, 0, RowPadding , 0);
		LayoutUtils.Layout.WidthWrap_HeightWrap.applyTableRowParams(text);
		row.addView(text);

		text = new TextView(activity);
		text.setText(amount(tran.amount));
		text.setPadding(RowPadding, 0, RowPadding, 0) ;
		text.setGravity(android.view.Gravity.RIGHT);
		LayoutUtils.Layout.WidthWrap_HeightWrap.applyTableRowParams(text);
		row.addView(text);


		return row;
	}	
	
	
	public static String amount(double num)
	{
		DecimalFormatSymbols otherSymbols = DecimalFormatSymbols.getInstance();//new DecimalFormatSymbols(Locale.GERMAN);//.getInstance();
		otherSymbols.setDecimalSeparator('.');
		otherSymbols.setGroupingSeparator(',');
		DecimalFormat df = new DecimalFormat("R #,###,##0.00     ", otherSymbols);
				
		return df.format(num).toString();
	}
	
	public static double getAmount(String val)
	{			
		double number = 0;
		Number parseNum = null;
		
		DecimalFormatSymbols otherSymbols = DecimalFormatSymbols.getInstance();//new DecimalFormatSymbols(Locale.GERMAN);//.getInstance();
		otherSymbols.setDecimalSeparator('.');
		otherSymbols.setGroupingSeparator(',');
		DecimalFormat df = new DecimalFormat("R #,###,##0.00    ", otherSymbols);
		
		try
		{
			parseNum = df.parse(val);
		}
		catch (ParseException e)
		{
			//System.out.println("Parse Err");
			try
			{
				number = Double.valueOf(val);
			}
			catch(NumberFormatException ex)
			{
				//System.out.println("Number format err");
				
				//is already formatted?
				if(val.startsWith("R"))
				{
					//System.out.println("Already formatted " + val.substring(1));
					try
					{
						number = Double.valueOf(val.substring(1));
					}
					catch(NumberFormatException err)
					{
						number = 0;
					}
				}
				else
				{
					number = 0;
				}
			}
		}
		
		if(parseNum != null)
		{
			//System.out.println(parseNum.doubleValue());
			number = parseNum.doubleValue();
		}
		
		return number;
	}
};