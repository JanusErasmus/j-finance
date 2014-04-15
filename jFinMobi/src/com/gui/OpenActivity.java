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

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import com.curr.CurrContent;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;



/**
 * This class provides a basic demonstration of how to write an Android
 * activity. Inside of its window, it places a single view: an EditText that
 * displays and edits some internal text.
 */
public class OpenActivity extends ListActivity {    
    
    public OpenActivity() {
    }

    /** Called with the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate our UI from its XML layout description.
        //setContentView(R.layout.open_activity);

        List<String> lst = getFiles();
//        for (int i = 0; i < lst.size(); i++) 
//        {
//        	System.out.println(lst.get(i));	
//		}
          
        setListAdapter(new ArrayAdapter<String>(this, R.layout.open_activity, lst));

        ListView lv = getListView();
        lv.setTextFilterEnabled(true);

        lv.setOnItemClickListener(new OnItemClickListener() 
        {

		public void onItemClick(AdapterView<?> arg0,
								View view,
								int arg2,
								long arg3)
		{
    		openFile(((TextView) view).getText().toString());
    		finish();
		}
        });        
    }

    /**
     * Called when the activity is about to start interacting with the user.
     */
    @Override
    protected void onResume() {
        super.onResume();
    }
    
    private void openFile(String fName)
    {
    	//System.out.println("Opening file " + fName);
    	CurrContent.get().setFileName(fName);
    	getSharedPreferences(TabjFinActivity.PREFS_NAME,MODE_PRIVATE)
        .edit()
        .putString(TabjFinActivity.PREF_USERNAME, fName)
        .commit();
    	
    }
    
    private List<String> getFiles()
    {
    	//ArrayList<String> lst = new ArrayList<String>();
    	List<String> lst = new ArrayList<String>();
    	
    	
    	File dir = new File(CurrContent.get().getWorkDir());
        FilenameFilter jFilter = new FilenameFilter()
        {
            public boolean accept(File dir, String filename)
            {
                return filename.endsWith(".jfin");
            }
        };            
        File[] fList = dir.listFiles(jFilter);            
        if(fList != null && fList.length > 0)
        {
        	//System.out.println("Files in jFinance");
        	
        	for (int i = 0; i < fList.length; i++) 
        	{        		
        		lst.add(fList[i].getName());
			}
        }        
        
    	
    	return lst;
    }
}