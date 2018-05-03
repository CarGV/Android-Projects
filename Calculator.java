package com.example.brandonmain.listviewexample1;

import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Calculator extends AppCompatActivity {

    private ArrayAdapter<String> adapter;
    private ListView listItems;
    private float total = 0;
    private DataSource datasource;
    ArrayList<String> listNames = new ArrayList<String>();
    ArrayList<String> listRows = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Call to creator of super class
        super.onCreate(savedInstanceState);

        // Initialize and open datasource to interact with the database

        datasource = new DataSource(this);
        datasource.open();

        // Read layout from xml data and setting design parameters

        setContentView(R.layout.activity_calculator);
        setTitle("Shopping List");
        getWindow().setBackgroundDrawable(ContextCompat.getDrawable(this,R.drawable.carro2));

        // Initialize the ListView listItems
        listItems = (ListView) findViewById(R.id.listItems);
        registerForContextMenu(listItems);			// Enabling context menu on listitems

        // Set the adapter of the list for showing the data of listRows in the list
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_activated_1, listRows);
        listItems.setAdapter(adapter);
        init_list();


        // Set Total to 0
        updateTotal(0);
    }

    // This will be invoked when an item in the listview is long pressed
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.menu_calculator , menu);
    }

    // This will be invoked when a menu item is selected
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        // Get info of the selected item
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch(item.getItemId()){
            case R.id.delete_item:
                Toast.makeText(this, listNames.get(info.position) + " deleted " , Toast.LENGTH_SHORT).show();
                delItemFromList(info.position);         //Delete ingredient from list
                break;
            case R.id.add_to_fridge:
                datasource.markAsHave(listNames.get(info.position));
                String name_aux = listNames.get(info.position);
                delItemFromList(info.position);         //Delete from the list
                datasource.addIngredient(name_aux,0,0,"",true);         //Add again, but with the value of the last parameter to true to mark it as actual ingredient
                Toast.makeText(this, name_aux+" added to your fridge"  , Toast.LENGTH_SHORT).show();
                break;

        }
        return true;

    }

    // This add a new ingredient to the list and the database
    public void addItemToList(View v) {
        String newLine = "";
        EditText quantity_i = (EditText) findViewById(R.id.inputQuantity);
        int quantity = Integer.valueOf(quantity_i.getText().toString());
        EditText name_i = (EditText) findViewById(R.id.inputText);
        String name = name_i.getText().toString();
        EditText price_i = (EditText) findViewById(R.id.inputPrice);
        float price = Float.valueOf(price_i.getText().toString());
        datasource.addIngredient(name, quantity, price, "", false);
        addItemToList(name, quantity, price, "");
    }

    // Add a new row in the list and update the total
    public void addItemToList(String name, int quantity, float price, String expiration) {
        String newLine = quantity + "x" + price + "=" + price * quantity + "kr  : " + name;
        listRows.add(newLine);
        listNames.add(name);
        adapter.notifyDataSetChanged();
        updateTotal(price * quantity);
    }

    // List initialize
    public void init_list() {
        listRows.clear();
        listNames.clear();
        total=0;
        for (Ingrediente ing : datasource.getWantedIngredients()) {
            addItemToList(ing.getName(), ing.getQuantity(), ing.getPrice(), ing.getExpire());
        }
    }

    // List clear. This will be invoked when the "clear" button is pressed
    public void clearList(View v) {
        listRows.clear();
        listNames.clear();
        datasource.delWantedIngredients();
        total = 0;
        updateTotal(0);
    }

    // Update the textview to show the correct sum
    public void updateTotal(float diff) {
        TextView total_t = (TextView) findViewById(R.id.textView_total);
        total += diff;
        total_t.setText("TOTAL:  " + total + " kr");
    }

    // Delete a item from the database and refresh the list
    public void delItemFromList(int pos){
        String name = listNames.get(pos);
        datasource.delIng(name);
        listRows.remove(pos);
        listNames.remove(pos);
        adapter.notifyDataSetChanged();
        init_list();
    }


}
