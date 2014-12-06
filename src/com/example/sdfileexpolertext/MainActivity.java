package com.example.sdfileexpolertext;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {

	private final String TAG_LOG = "MainActivity";
	private final String ROOT_PATH = "/mnt";
	
	private ListView listView = null;
	private TextView textView = null;
	private Button button = null;
	
	//记录当前的父路径
	File currentParentPath = null;
	//记录所有的当前父路径下的子路径
	File currentFiles[] = null;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        listView = (ListView)findViewById(R.id.listView1);
        textView = (TextView)findViewById(R.id.textView1);
        button = (Button)findViewById(R.id.button1);
        
        File root = new File(ROOT_PATH);
        if( root.exists() )
        {
        	currentParentPath = root;
        	
        	currentFiles = root.listFiles();
        	
        	updateFiles(currentFiles);
        }
        
        button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				try {
					if( !currentParentPath.getCanonicalPath().equals(ROOT_PATH) ) 
					{
						currentParentPath = currentParentPath.getParentFile();
						currentFiles = currentParentPath.listFiles();
						updateFiles(currentFiles);
					}
				} catch (Exception e) {
					// TODO: handle exception
					Log.e(TAG_LOG, e.getMessage().toString());
				}
			}
		});
        
        listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				
				//点击文件直接无视
				if( currentFiles[position].isFile() ) return ;
				
				else if( currentFiles[position].isDirectory() )
				{
					File tmp[] = currentFiles[position].listFiles();
					
					//这里犯2了，要先判断非空，在来判断是否为长度为0
					if( tmp != null && tmp.length > 0  )
					{
						currentParentPath = currentFiles[position];
						
						currentFiles = tmp;
						
						updateFiles(currentFiles);
					}
				}
			}
        	
		});
    }
    
    private void updateFiles(File files[])
    {
    	if( files == null || files.length <=0   ) return ;
    	
    	//List 嵌套 Map
    	List< Map<String, Object> > listItems = new ArrayList< Map<String, Object> >();
    	
    	for( int i = 0; i < files.length; ++ i )
    	{
    		Map<String, Object> itemMap = new HashMap<String, Object>();
    		
    		if( files[i].isDirectory() )
    		{
    			itemMap.put("icon", R.drawable.folder);
    		}
    		else if( files[i].isFile() )
    		{
    			itemMap.put("icon", R.drawable.file);
    		}
    		itemMap.put("fileName", files[i].getName());
    		listItems.add(itemMap);
    	}
    	
    	SimpleAdapter simpleAdapter = new SimpleAdapter(this, listItems, R.layout.line, 
    			     new String[]{"icon", "fileName"}, new int[]{R.id.icon, R.id.file_name});
    	
    	listView.setAdapter(simpleAdapter);
    	
    	try {
			textView.setText("当前路径为：" + currentParentPath.getCanonicalPath());
		} catch (Exception e) {
			// TODO: handle exception
			Log.e(TAG_LOG, e.getMessage().toString());
		}
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
