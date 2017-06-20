package com.codepath.simpletodo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // declare stateful objects
    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
    ListView lvItems;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //obtain reference to ListView created with layout
        lvItems = (ListView) findViewById(R.id.lvItems);
        //initialize items list
        readItems();
        //initialize the adapter using items list
        itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        //wire adapter to the view
        lvItems.setAdapter(itemsAdapter);

        //setup the listener on creation
        setupListViewListener();
    }

    private void setupListViewListener() {
        Log.i("MainActivity", "Setting up listener on list view");
        //set ListView's itemLongClickListener
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //remove item in the list at the index given by position
                items.remove(position);
                //notify the adapter that the underlying dataset changed
                itemsAdapter.notifyDataSetChanged();
                writeItems();
                Log.i("MainActivity", "Removed item " + position);
                //return true to tell framework that the long click was consumed
                return true;
            }
        });
    }

    private File getDataFile() {
        return new File(getFilesDir(), "todo.txt");
    }

    private void readItems() {
        try {
            items = new ArrayList<String>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            Log.e("MainActivity", "Error reading file", e);
            items = new ArrayList<>();
        }
    }

    private void writeItems() {
        try {
            FileUtils.writeLines(getDataFile(), items);
        } catch (IOException e) {
            Log.e("MainActivity", "Error writing file", e);
        }
    }

    public void onAddItem(View v) {
        //obtain reference to the EditText created with the layout
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        //grab EditText's content as a string
        String itemText = etNewItem.getText().toString();
        //add item to list via adapter
        itemsAdapter.add(itemText);
        //clear EditText by setting it to empty string
        etNewItem.setText("");
        writeItems();
        //display notification to user
        Toast.makeText(getApplicationContext(), "Item added to list", Toast.LENGTH_SHORT).show();
    }
}
