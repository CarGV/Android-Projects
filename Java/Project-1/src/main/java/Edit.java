package com.example.brandonmain.listviewexample1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Edit extends AppCompatActivity {

    /**We declare the EditTexts and the ListView */
    ListView listViewRecipes;
    EditText editTextName;
    EditText editTextURL;
    List<Ingrediente> listItem ;
    /** Declaring an ArrayAdapter to set items to ListView */
    private ArrayAdapter<String> adapter;
    private DataSource datasource = new DataSource(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /** Setting a custom layout for the activity */
        setContentView(R.layout.activity_edit);
        datasource.open();

        /** Reference to the EditTexts and the ListView of the layout activity_fridge */
        //casting to ListView
        listViewRecipes = (ListView) findViewById(R.id.listViewRecipes);
        //casting to edittext
        editTextName= (EditText) findViewById(R.id.editTextNameRecipes);
        editTextURL= (EditText) findViewById(R.id.editTextUrl);
        listItem = datasource.getAllIngredients();
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_checked, listItem);
        setTitle("Add new Recipe");

        //checked
        listViewRecipes.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);


        listViewRecipes.setAdapter(adapter);

    }

    // saveRecipe: This method save the values that we write in the activity_edit.xml in the database

    public void saveRecipe(View v){
        SparseBooleanArray idCheckIngredients= listViewRecipes.getCheckedItemPositions();
        ArrayList <String> nameIngredient= new ArrayList<>();

        for (int i=0; i<idCheckIngredients.size(); i++){
            if(idCheckIngredients.valueAt(i)){
                String item = listViewRecipes.getAdapter().getItem(idCheckIngredients.keyAt(i)).toString();
                nameIngredient.add(item);
            }

        }

        datasource.addRecipe(editTextName.getText().toString(), null,  editTextURL.getText().toString(), nameIngredient);
        finish();
    }

    }

