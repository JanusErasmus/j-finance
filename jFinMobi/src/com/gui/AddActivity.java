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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import com.curr.CurrContent;

import content.Category;
import content.Transaction;

import android.app.Activity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

/**
 * This class provides a basic demonstration of how to write an Android
 * activity. Inside of its window, it places a single view: an EditText that
 * displays and edits some internal text.
 */
public class AddActivity extends Activity {    
    
	/**
     * Fields to contain the current position and display contents of the spinner
     */
	private ArrayAdapter<CharSequence> mAdapter;	 
    protected int mPos = 0;
    private int subIdx = 0;
    private int mIndex = 0;
    private EditText descTxt;
    private EditText amountTxt;
    private Spinner catSpinner;
    private Spinner descSpinner;
    private Spinner fromSpinner;
    private int tmpIndex;
    
	
    public AddActivity() 
    {
    	
    }

    /** Called with the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate our UI from its XML layout description.
        //setContentView(R.layout.add_activity);

        if(CurrContent.get().getName() != null)
        	setTitle("jFinance - " + CurrContent.get().getName());
        
        LinearLayout addLayout = new LinearLayout(this);
        addLayout.setOrientation(LinearLayout.VERTICAL);
                
        catSpinner = new Spinner(this);      
        String[] items = getItems(CurrContent.get().getCategories());   
        
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        catSpinner.setAdapter(adapter);
        catSpinner.setSelection(mPos);    
        OnItemSelectedListener spinnerListener = new categoryOnItemSelectedListener(this,this.mAdapter);
        catSpinner.setOnItemSelectedListener(spinnerListener);
        addLayout.addView(catSpinner);
        
        descTxt = new EditText(this);
        descTxt.setSelectAllOnFocus(true);
        descTxt.setHint("Description");
        descTxt.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        descTxt.requestFocus();        
        addLayout.addView(descTxt);
        
        descSpinner =  new Spinner(this);
        OnItemSelectedListener descSpinnerListener = new discriptionOnItemSelectedListener(this,this.mAdapter);
        descSpinner.setOnItemSelectedListener(descSpinnerListener);
        descSpinner.setVisibility(Spinner.GONE);
        
        addLayout.addView(descSpinner);
        
        amountTxt = new EditText(this);
        amountTxt.setHint("R 0.00");
        amountTxt.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);
        amountTxt.setSelectAllOnFocus(true);
         
        addLayout.addView(amountTxt);
        
        fromSpinner =  new Spinner(this);
        String[] fromItems = {"Bank", "Wallet"};        
        adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, fromItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fromSpinner.setAdapter(adapter);        
        addLayout.addView(fromSpinner);
               
        Button addBtn = new Button(this);
        addBtn.setText("Add");
        addBtn.setOnClickListener(mAddListener);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(250, 100);
        layoutParams.setMargins(0, 24, 0, 24);
        addLayout.addView(addBtn, layoutParams);
        
        
        //Transaction tran = CurrContent.get().getTmpTrans();
    	//if(tran != null)
        tmpIndex = CurrContent.get().getTmpIndex(); 
        if(tmpIndex >= 0)
    	{
        	mIndex = tmpIndex;
        	//System.out.println("Index is " + index);
    		Transaction tran = CurrContent.get().getTransactions().get(tmpIndex);
    		
    		ArrayList<String> catNames = new ArrayList<String>();    		
    		for (int i = 0; i < CurrContent.get().getCategories().size(); i++)
    		{
    			catNames.add(CurrContent.get().getCategories().get(i).name);
			}
    		//System.out.println("Cat is " + tran.category);
    		int idx = catNames.indexOf(tran.category);
    		//System.out.println("Cat idx: " + idx);    		
    		if(idx < 0)
    		{
//    			finish();    			
//    			return;
    			idx = CurrContent.get().getCategories().size();//catSpinner.getCount();
    			
    		}        
    		else
    		{   
    			if(CurrContent.get().getCategories().get(idx).subCatagories.size() > 0)
    			{
    				catNames.clear();
    				for (int i = 0; i < CurrContent.get().getCategories().get(idx).subCatagories.size(); i++)
    				{
    					catNames.add(CurrContent.get().getCategories().get(idx).subCatagories.get(i).name);
    				}
    				subIdx = catNames.indexOf(tran.description);
    				//System.out.println("subCat idx: " + subIdx);  
    				if(subIdx < 0)
    				{
    					finish();    			
    					return;
    				}          
    			}
    			else
    			{
    				descTxt.setText(tran.description);
    			}
    		}
    		catSpinner.setSelection(idx);
    		
    		if(tran.from.equals("Wallet"))
    		{
    			fromSpinner.setSelection(1);
    		}
    		
    		mPos = idx;
    		addBtn.setText("Accept");  		
    		
    		    		
    		amountTxt.setText(PanelBuilder.amount(tran.amount));
    		
    	}
        LayoutUtils.Layout.WidthFill_HeightWrap.applyViewGroupParams(addLayout);
        setContentView(addLayout);
    	
                
    }
    
    OnClickListener mAddListener = new OnClickListener() {
        public void onClick(View v) 
        {
        	Transaction tran = new Transaction();
        	
        	
        	//set transaction start date to today
    		Calendar cal = Calendar.getInstance();
    		java.util.Date now = cal.getTime();			
    		SimpleDateFormat fmt = new SimpleDateFormat("dd-MM-yyyy");
    			
    		tran.date = fmt.format(now);
    		
        	
        	if(mPos >= CurrContent.get().getCategories().size())
        	{
        		if(((String)fromSpinner.getSelectedItem()).equalsIgnoreCase("Bank"))
        			tran.description = "from Bank...";
        		else
        			tran.description = "from Wallet...";
        		tran.category = "Transfer";
        	}
        	else
        	{
        		if(CurrContent.get().getCategories().get(mPos).subCatagories.size() > 0)
        		{
        			tran.description = (String) descSpinner.getSelectedItem();
        		}
        		else
        		{
        			tran.description = descTxt.getText().toString();
        		}
        		tran.category = CurrContent.get().getCategories().get(mPos).name;
        	}
        	tran.amount = PanelBuilder.getAmount(amountTxt.getText().toString());        	
        	tran.from = (String)fromSpinner.getSelectedItem();
        	
        	
        	CurrContent.get().setTmpTrans(tran);
        	setResult(mIndex);        	        	
            finish();
        }
    };
    
    OnClickListener mDescListener = new OnClickListener() {
        public void onClick(View v) 
        {
        	
        }
    };

    private String[] getItems(ArrayList<Category> cats)
    {    	
		String[] items = new String[cats.size() + 1];
		
		for (int i = 0; i < cats.size(); i++) 
		{
			items[i] = cats.get(i).name;	
		}
		
		items[cats.size()] = "Transfer";
		
		return items;
	}

	/**
     * Called when the activity is about to start interacting with the user.
     */
    @Override
    protected void onResume() {
        super.onResume();
    }
    
    /**
     *  A callback listener that implements the
     *  {@link android.widget.AdapterView.OnItemSelectedListener} interface
     *  For views based on adapters, this interface defines the methods available
     *  when the user selects an item from the View.
     *
     */
    public class categoryOnItemSelectedListener implements OnItemSelectedListener {

        /*
         * provide local instances of the mLocalAdapter and the mLocalContext
         */

        ArrayAdapter<CharSequence> mLocalAdapter;
        Activity mLocalContext;

        /**
         *  Constructor
         *  @param c - The activity that displays the Spinner.
         *  @param ad - The Adapter view that
         *    controls the Spinner.
         *  Instantiate a new listener object.
         */
        public categoryOnItemSelectedListener(Activity c, ArrayAdapter<CharSequence> ad) {

          this.mLocalContext = c;
          this.mLocalAdapter = ad;

        }

        /**
         * When the user selects an item in the spinner, this method is invoked by the callback
         * chain. Android calls the item selected listener for the spinner, which invokes the
         * onItemSelected method.
         *
         * @see android.widget.AdapterView.OnItemSelectedListener#onItemSelected(
         *  android.widget.AdapterView, android.view.View, int, long)
         * @param parent - the AdapterView for this listener
         * @param v - the View for this listener
         * @param pos - the 0-based position of the selection in the mLocalAdapter
         * @param row - the 0-based row number of the selection in the View
         */
        public void onItemSelected(AdapterView<?> parent, View v, int pos, long row) {
 
        	if(pos >= CurrContent.get().getCategories().size())
        	{
        		 AddActivity.this.mPos = pos;
        		 descTxt.setVisibility(EditText.GONE);
         		 descSpinner.setVisibility(Spinner.GONE);
        		return;
        	}
        	
        	if(CurrContent.get().getCategories().get(pos).subCatagories.size() > 0)
        	{
//        		Toast msg =	Toast.makeText(AddActivity.this,"Can not select categories with sub categories", Toast.LENGTH_SHORT);
//        		msg.show();
//        		spinner.setSelection(AddActivity.this.mPos);
//        		return;
        		        		
        		String[] items = getItems(CurrContent.get().getCategories().get(pos).subCatagories);        
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddActivity.this,
                            android.R.layout.simple_spinner_item, items);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                descSpinner.setAdapter(adapter);
                
                //descSpinner.setSelection(mPos); 
        		
        		descTxt.setVisibility(EditText.GONE);
        		descSpinner.setVisibility(Spinner.VISIBLE);
        		descSpinner.setSelection(subIdx);
        	    
        		Category cat = CurrContent.get().getCategories().get(pos);
        		Category subCat = cat.subCatagories.get(subIdx);
        		double amount = subCat.amount + subCat.prevAmount - CurrContent.get().getBudget().sumOfTransaction(cat, subCat);        		
        		amountTxt.setHint(PanelBuilder.amount(amount));
        		
        	}
        	else
        	{
        		descTxt.setVisibility(EditText.VISIBLE);
    			descTxt.requestFocus();  
        		descSpinner.setVisibility(Spinner.GONE);
        		
        		Category cat = CurrContent.get().getCategories().get(pos);
        		double amount = cat.amount + cat.prevAmount - CurrContent.get().getBudget().sumOfTransaction(cat); 
        		amountTxt.setHint(PanelBuilder.amount(amount));
        	}
        	
            AddActivity.this.mPos = pos;
            //AddActivity.this.mSelection = parent.getItemAtPosition(pos).toString();
            
           
        }

        /**
         * The definition of OnItemSelectedListener requires an override
         * of onNothingSelected(), even though this implementation does not use it.
         * @param parent - The View for this Listener
         */
        public void onNothingSelected(AdapterView<?> parent) {

            // do nothing

        }
    }
    
    /**
     *  A callback listener that implements the
     *  {@link android.widget.AdapterView.OnItemSelectedListener} interface
     *  For views based on adapters, this interface defines the methods available
     *  when the user selects an item from the View.
     *
     */
    public class discriptionOnItemSelectedListener implements OnItemSelectedListener {

        /*
         * provide local instances of the mLocalAdapter and the mLocalContext
         */

        ArrayAdapter<CharSequence> mLocalAdapter;
        Activity mLocalContext;

        /**
         *  Constructor
         *  @param c - The activity that displays the Spinner.
         *  @param ad - The Adapter view that
         *    controls the Spinner.
         *  Instantiate a new listener object.
         */
        public discriptionOnItemSelectedListener(Activity c, ArrayAdapter<CharSequence> ad) {

          this.mLocalContext = c;
          this.mLocalAdapter = ad;

        }

        /**
         * When the user selects an item in the spinner, this method is invoked by the callback
         * chain. Android calls the item selected listener for the spinner, which invokes the
         * onItemSelected method.
         *
         * @see android.widget.AdapterView.OnItemSelectedListener#onItemSelected(
         *  android.widget.AdapterView, android.view.View, int, long)
         * @param parent - the AdapterView for this listener
         * @param v - the View for this listener
         * @param pos - the 0-based position of the selection in the mLocalAdapter
         * @param row - the 0-based row number of the selection in the View
         */
        public void onItemSelected(AdapterView<?> parent, View v, int pos, long row) {
        	
        	Category cat = CurrContent.get().getCategories().get(mPos);
    		Category subCat = cat.subCatagories.get(pos);
    		double amount = subCat.amount + subCat.prevAmount - CurrContent.get().getBudget().sumOfTransaction(cat, subCat);        	 
    		amountTxt.setHint(PanelBuilder.amount(amount));
        }

        /**
         * The definition of OnItemSelectedListener requires an override
         * of onNothingSelected(), even though this implementation does not use it.
         * @param parent - The View for this Listener
         */
        public void onNothingSelected(AdapterView<?> parent) {

            // do nothing

        }
    }
}
