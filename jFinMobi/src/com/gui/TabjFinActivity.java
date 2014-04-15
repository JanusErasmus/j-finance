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

import com.curr.CurrContent;

import android.app.TabActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;

/**
 * This class provides a basic demonstration of how to write an Android
 * activity. Inside of its window, it places a single view: an EditText that
 * displays and edits some internal text.
 */
public class TabjFinActivity extends TabActivity {
	
	public static final String PREFS_NAME = "MyPrefsFile";
	public static final String PREF_USERNAME = "username";
    
    static final private int BACK_ID = Menu.FIRST;
    static final private int SAVE_ID = Menu.FIRST + 1;
    static final private int OPEN_ID = Menu.FIRST + 2;

    
    public TabjFinActivity() {
    }

    /** Called with the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jfin_activity);
        
        CurrContent.init();
       
        Resources res = getResources(); // Resource object to get Drawables
        TabHost tabHost = getTabHost();  // The activity TabHost
        TabHost.TabSpec spec;  // Resusable TabSpec for each tab
        Intent intent;  // Reusable Intent for each tab

     // Create an Intent to launch an Activity for the tab (to be reused)
        intent = new Intent().setClass(TabjFinActivity.this, SummaryActivity.class);        
        // Initialize a TabSpec for each tab and add it to the TabHost
        spec = tabHost.newTabSpec("summary").setIndicator("Summary",
        		res.getDrawable(R.drawable.summary)).setContent(intent);
       	tabHost.addTab(spec);

        // Create an Intent to launch an Activity for the tab (to be reused)
        intent = new Intent().setClass(TabjFinActivity.this, TransActivity.class);        
        // Initialize a TabSpec for each tab and add it to the TabHost
        spec = tabHost.newTabSpec("trans").setIndicator("Transactions",
        		res.getDrawable(R.drawable.trans)).setContent(intent);
       	tabHost.addTab(spec);
       	
       	tabHost.setCurrentTab(1);
       	
       	
       	SharedPreferences pref = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);   
       	String username = pref.getString(PREF_USERNAME, null);
       	
       	if(username != null)
       	{
       		//System.out.print("Stored fileName is ");
           	//System.out.println(username);
       		CurrContent.get().setFileName(username);
       	}
       	else
       	{
       		getSharedPreferences(PREFS_NAME,MODE_PRIVATE)
            .edit()
            .putString(PREF_USERNAME, "NotStored")
            .commit();
       	}
       	

        
    }

    /**
     * Called when the activity is about to start interacting with the user.
     */
    @Override
    protected void onResume() {
        super.onResume();    
        
        if(CurrContent.get().getName() != null)
        	setTitle("jFinance - " + CurrContent.get().getName());
    }

    /**
     * Called when your activity's options menu needs to be created.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        // We are going to create two menus. Note that we assign them
        // unique integer IDs, labels from our string resources, and
        // given them shortcuts.
        menu.add(0, BACK_ID, 0, R.string.back).setShortcut('0', 'b');
        menu.add(0, SAVE_ID, 0, R.string.save).setShortcut('1', 's');
        menu.add(0, OPEN_ID, 0, R.string.open).setShortcut('2', 'o');

        return true;
    }

    /**
     * Called right before your activity's option menu is displayed.
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        
        return true;
    }

    /**
     * Called when a menu item is selected.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case BACK_ID:
            finish();
            return true;
        case OPEN_ID:
        	Intent i = new Intent().setClass(TabjFinActivity.this, OpenActivity.class);
        	startActivity(i);        	 
            return true;
            
        case SAVE_ID:
        	System.out.println("Saving");
        	if(CurrContent.get().saveContent())
        		System.out.println("Was saved");
        	 
            return true;
        }

        return super.onOptionsItemSelected(item);
    }    
}
