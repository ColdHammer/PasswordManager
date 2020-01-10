package com.coldhammer.teg.passwordkeeper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import com.coldhammer.teg.passwordkeeper.CustomRecyclerAdapter.RecyclerViewAdapter;
import com.coldhammer.teg.passwordkeeper.Serializer.GObjectSerializer;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    FileManager converter;
    Context context;
    RecyclerViewAdapter adaptor;
    private static final String TAG = "MainActivity";


    RecyclerView passwordList;
    RecyclerView.LayoutManager manager;
    SearchView searchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        context = this;

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchItemPage(true, null, adaptor.getItemCount());
            }
        });
        converter = new FileManager(this);
        adaptor = new RecyclerViewAdapter(context, converter.read());
        manager = new LinearLayoutManager(this);

        passwordList = findViewById(R.id.password_list);
        passwordList.setLayoutManager(manager);
        passwordList.setAdapter(adaptor);
        searchView = findViewById(R.id.password_search);

//        passwordList.setAdapter(adaptor);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adaptor.getFilter().filter(s);
                return false;
            }
        });
    }

    public void launchItemPage(boolean onlyEdit, String item, int i)
    {
        Intent myIntent = new Intent(MainActivity.this, ItemPage.class);
        myIntent.putExtra("onlyEdit", onlyEdit); //Optional parameters
        myIntent.putExtra("position", i);
        if(item != null) {
            myIntent.putExtra("data", item); //Optional parameters
        }
        startActivityForResult(myIntent, 1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if(searchView.getQuery().length() > 0)
        {
            searchView.setQuery("", true);
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1)
        {
            if(resultCode == Activity.RESULT_OK)
            {
                //TODO: I have to manage the data coming from the intent
                String dataString = data.getStringExtra("data");
                HashMap<String, String> keySet = GObjectSerializer.createHashMap(dataString);
                Log.d(TAG, "dataString: " + dataString);
                Log.d(TAG, "hashMap: " + GObjectSerializer.keySetToStringBuffer(keySet, null));
                int position = data.getIntExtra("position", -1);
                if(position == adaptor.getItemCount())
                {
                    adaptor.add(keySet);
                    Log.d(TAG, "onActivityResult: " + adaptor.keySet.toString());
                    converter.write(adaptor.keySet);
                    return;
                }
                adaptor.update(keySet, position);
                converter.write(adaptor.keySet);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        converter.write(adaptor.keySet);

    }
}
