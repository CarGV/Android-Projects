package com.example.brandonmain.listviewexample1;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
/**We create a new Activity class for Fridge */
public class Fridge extends AppCompatActivity  {
    /**We declare the EditTexts and the ListView */
    EditText editText;
    ListView listItems;
    EditText editDate;
    /** Declaring an ArrayAdapter to set items to ListView */
    private ArrayAdapter<String> adapter;
    ArrayList<String> listNames = new ArrayList<String>();
    ArrayList<String> listNames2 = new ArrayList<String>();

    DataSource dataSource;
    DialogFragment picker = new DatePickerFragment();
    static final int DATE_DIALOG_ID = 0;
    private int mYear,mMonth,mDay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /** Setting a custom layout for the activity */
        setContentView(R.layout.activity_fridge);
        /**Setting the image background and the action bar with title of the activity*/
        getWindow().setBackgroundDrawable(ContextCompat.getDrawable(this,R.drawable.nev));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        /**We "create" our DB for the activity to make the items in our list persistent(to store them in an "empty space"*/
        dataSource= new DataSource(this);
        dataSource.open();
        /** Reference to the EditTexts and the ListView of the layout activity_fridge */
        editDate=(EditText) findViewById(R.id.editDate);
        editText=(EditText) findViewById(R.id.item_editText);
        listItems=(ListView) findViewById(R.id.shopping_listView);
        /** Defining the ArrayAdapter to set items to ListView */
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listNames);
        listItems.setAdapter(adapter);
        updateList();
        registerForContextMenu(listItems);

    }
    /**ondateclick custom OnClick event to show the fragment containing the calendar*/
    public void ondateclick(View v){

        picker.show(getFragmentManager(), "datePicker");

    }


    /**OnClick event: when the user presses the button*/
    public void onClickButton(View v){
        /**The values of the 2 EditTexts will be concatenaded and added into the listview as well as in the DB in one String as an ingredient*/
        listNames.add(editText.getText().toString()+" - "+editDate.getText().toString() );
        Log.i("A",editText.getText().toString()+" - "+editDate.getText().toString());
        dataSource.addIngredient(editText.getText().toString(),-1,0,editDate.getText().toString(),true);
        updateList();
        adapter.notifyDataSetChanged();
    }
    /**function to updateList in the DB when called*/
    public void updateList(){
        dataSource= new DataSource(this);
        dataSource.open();
        listNames.clear(); listNames2.clear();
        for (String i:dataSource.getActualIngredients()){
            listNames.add(dataSource.getIngredient(i).toStringDate());
            listNames2.add(dataSource.getIngredient(i).toString());
        }

    }


    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    /** This will be invoked when an item in the listview is long pressed */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.menu_fridge , menu);

    }
    /** This will be invoked when a menu item is selected */
    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo(); /**  */
        Ingrediente ing;
        switch(item.getItemId()){
            /**This case remove with the arrayadapter(method remove) and the get method with info.position wich is an integer which contains the position of the item, so we use it as an ID */
            case R.id.delete_item:
                Toast.makeText(this, listNames.get(info.position) + " deleted " , Toast.LENGTH_SHORT).show();
                adapter.remove(listNames.get(info.position));
                dataSource.delIng(listNames2.get(info.position));
                break;
            /**This case allows us to set our selected item as "in refrigerator" so it will have a snowflake next to the expiration date*/
            case R.id.add_to_refridge:
                ing = dataSource.getIngredient(listNames2.get(info.position));
                dataSource.delIng(listNames2.get(info.position));
                dataSource.addIngredient(ing.getName(),0,0, "❅ "+ ing.getExpire(),true);
                Toast.makeText(this, listNames2.get(info.position)+" added to your refrigerator"  , Toast.LENGTH_SHORT).show();
                updateList();
                adapter.notifyDataSetChanged();
                break;
            /**This case allows us to set our selected item as "about to expire" so it will have an exclamation mark next to the date */
            case R.id.addtoExpire:
                ing = dataSource.getIngredient(listNames2.get(info.position));
                dataSource.delIng(listNames2.get(info.position));
                Toast.makeText(this, listNames.get(info.position)+"marked as 'about to expire'",Toast.LENGTH_SHORT).show();
                dataSource.addIngredient(ing.getName(),0,0, " ! "+ ing.getExpire(),true);
                updateList();
                adapter.notifyDataSetChanged();
                break;
        }
        return true;

    }




}