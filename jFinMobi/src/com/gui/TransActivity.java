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

import com.curr.CurrContent;

import content.Transaction;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TableLayout;
/**
 * This class provides a basic demonstration of how to write an Android
 * activity. Inside of its window, it places a single view: an EditText that
 * displays and edits some internal text.
 */
public class TransActivity extends Activity {
        
	private static TransActivity _instance = null;
	private ScrollView scrollPane;
	private TableLayout table;
	
    public TransActivity() {
    	_instance = this;
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
        
        fillTrans();
        
    }

    
    private void fillTrans()
    {
    	if(CurrContent.get().getName() == null)
    		return;
    	
    	ArrayList<Transaction> entries = CurrContent.get().getTransactions();
    	
    	scrollPane.removeAllViews();
            		  		
        table = (TableLayout) PanelBuilder.createWidgetPanel(entries, this);
        table.addView(createAddButton());

        scrollPane.addView(table);

        scrollPane.post(new Runnable() { 
        	public void run() {             	
        		scrollPane.fullScroll(ScrollView.FOCUS_DOWN); 
        	} 
        });
    }
        
    private View createAddButton() {

  	  Button b = new Button(this);
  	  
  	  b.setText("Add entry");

  	  b.setOnClickListener(new View.OnClickListener() {
  	    public void onClick(View view) {
  	      Intent i = new Intent(TransActivity.this, AddActivity.class);
  	      startActivityForResult(i, 0);  	      
  	    }
  	  });

  	  return b;
  	}
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) 
    {    	
    	super.onActivityResult(requestCode, resultCode, data);
    
    	if(resultCode >= 0)
		{
    		switch(requestCode)
    		{
    		case 0:
    		{
    				//System.out.println("Add was pressed");
    				Transaction tran = CurrContent.get().getTmpTrans();
    				if(tran != null)
    				{
    					CurrContent.get().getTransactions().add(tran);

    		        	System.out.println("Saving");
    		        	if(CurrContent.get().saveContent())
    		        		System.out.println("Was saved");
    				}
    		}
    		break;
    		
    		case 1: 
    		{
    			//System.out.println("Accepted idx " + resultCode);
    			Transaction tran = CurrContent.get().getTmpTrans();
    			if(tran != null)
				{
    				//CurrContent.get().getTransactions().remove(resultCode);
    				CurrContent.get().getTransactions().set(resultCode, tran);
				}
    		}    	
    		break;
    			
    		default:
    			break;
    		}
    	}
    	
    	
    }
    
    public static TransActivity get()
    {
    	return _instance;
    }
     

}
