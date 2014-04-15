/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gui;

import java.util.ArrayList;

import content.Category;
import com.curr.CurrContent;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ScrollView;
/**
 * This class provides a basic demonstration of how to write an Android
 * activity. Inside of its window, it places a single view: an EditText that
 * displays and edits some internal text.
 */
public class SummaryActivity extends Activity {    
    
	private static SummaryActivity _instance = null;
	private ScrollView scrollPane;
	
    public SummaryActivity() 
    {
    	_instance  = this;
    }

    /** Called with the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       
        // wrap the table in a scrollpane
        scrollPane = new ScrollView(this);
        LayoutUtils.Layout.WidthFill_HeightWrap.applyViewGroupParams(scrollPane);
        
        setContentView(scrollPane);
    }

    

	/**
     * Called when the activity is about to start interacting with the user.
     */
    @Override
    protected void onResume() {
        super.onResume();
        
        //System.out.println("Summary resume, filling...");
        fillSummary();
        
    }
    
    private void fillSummary()
    {
    	if(CurrContent.get().getName() == null)
    		return;
    	
    	ArrayList<Category> cats = CurrContent.get().getCategories();
    	
    	scrollPane.removeAllViews();
         if(cats != null)
         {
//        	 for (int i = 0; i < cats.size(); i++)
//         	{
//                 System.out.println(cats.get(i).name);
//     		} 
         	 
         	ArrayList<String> nameList = generateNameList(cats);
         	ArrayList<Double> valueList = generateValueList(cats); 
         	scrollPane.addView(PanelBuilder.createWidgetPanel(nameList, valueList, this));
         }
    	
    }
    
    private ArrayList<String> generateNameList(ArrayList<Category> categories)
    {
    	ArrayList<String> list = new ArrayList<String>();
    	
    	//add category names
    	for (Category cat : categories)
    	{
    		list.add(cat.name);
    	}
    	
    	//addBudget names
    	list.add("");
    	list.add("");
    	list.add("Bank");
    	list.add("Wallet");
    	list.add("Total");
    	
		return list;
	}
    

	private ArrayList<Double> generateValueList(ArrayList<Category> categories) 
	{	
		ArrayList<Double> list = new ArrayList<Double>();
				
    	//do all category totals
    	for (int i = 0; i < categories.size(); i++)
    	{
    		double total = 0;
    		Category cat = categories.get(i);
    		if(cat.subCatagories.size() > 0)
    		{    			
    			for(int j = 0; j < cat.subCatagories.size(); j++ )
    			{
    				total += cat.subCatagories.get(j).amount + cat.subCatagories.get(j).prevAmount;
    			}
    		}
    		else
    		{
    			total = cat.amount + cat.prevAmount;
    		}
    		
    		
    		double trans = CurrContent.get().getBudget().sumOfTransaction(categories.get(i));
    		
    		list.add(total - trans);
    	}
    	
    	list.add((double)-9999999);//openSpace
    	list.add((double)-9999999);//openSpace
    	
    	//do bank
    	double bankSumm = CurrContent.get().getBudget().prevIncome.bank + CurrContent.get().getBudget().income.bank + CurrContent.get().getBudget().sumOfTransfers("Wallet") - CurrContent.get().getBudget().sumOfTransactions("Bank"); 
    	list.add(bankSumm);
    	
    	//do wallet
    	double walletSumm = CurrContent.get().getBudget().prevIncome.wallet + CurrContent.get().getBudget().income.wallet + CurrContent.get().getBudget().sumOfTransfers("Bank") - CurrContent.get().getBudget().sumOfTransactions("Wallet");
    	list.add(walletSumm);
    	
    	
    	//do total
    	list.add(bankSumm + walletSumm);
    	
		return list;
	}
	
	 public static SummaryActivity get()
	    {
	    	return _instance;
	    }
}

