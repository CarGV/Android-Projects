package com.example.brandonmain.listviewexample1;

import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Recipes extends AppCompatActivity {

    /* Here we have the Recipes.java:

    First of all we have the variables that we are going to use in all the class.
     */

    public Button addrecipe;
    SimpleAdapter adapter;
    private DataSource dataSource;
    ListView listView;
    ArrayList<String> listNames = new ArrayList<>();

    /* The method buttonOnClickRecipes is the method that control the button Add Recipe and bring you since activity_recipes.xml to
    activity_edit.xml
    For that we us setOnClickListener for know what is the button that we press
     */

    public void buttonOnClickRecipes (){
        addrecipe= (Button)findViewById(R.id.addrecipe);
        addrecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent mintent = new Intent (Recipes.this, Edit.class);
                startActivity (mintent);
            }
        });
    }


    /* Method onCreate starts the activity
     */

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);
        buttonOnClickRecipes();
        listView = (ListView) findViewById(R.id.results_listview);

        setTitle("Recipes");

        updateList();

        //This piece of code is that bring you to the url associated with the recipe in the COLUMN_URL of TABLE_RECIPES


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view, int position,
                                                                    long id) {

                                                String url = dataSource.getUrl(listNames.get(position));
                                                if (!url.startsWith("http://") && !url.startsWith("https://"))
                                                    url = "http://" + url;
                                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                                startActivity(intent);

                                            }
                                        });

        registerForContextMenu(listView);


    }




    /* The methods OnCreateContextMenu and OnContextItemSelected will be used for the context menu
    that we can deploy for select the item delete and remove the selected item.

    The first deploy the context menu and the second recognize what is the item selected and the
    things that you can do with the context menu, in this case we can only deleted the item selected.
     */


    /** This will be invoked when an item in the listview is long pressed */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.menu_recipes , menu);

    }


    /** This will be invoked when a menu item is selected */
    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        /**This case remove with the arrayadapter(method remove) and the get method with info.position wich is an integer which contains the position of the item, so we use it as an ID */
        switch(item.getItemId()){
            case R.id.delete_item:
                Toast.makeText(this, listNames.get(info.position) + " deleted " , Toast.LENGTH_SHORT).show();
                dataSource.delRecipe(listNames.get(info.position));
                listNames.remove(info.position);
                updateList();
                break;

        }
        return true;

    }

    /* The next method updateList, is when we have created the listView, how we have a listView that
    contains item (when we can see the name of the recipe) and subitem (when we can see the ingredients
    necessary to make the recipe) we need an ArrayList when we have the names of the recipes, and
    we put the recipes with its names and ingredients with a Map

     */

    /**function to updateList in the DB when called*/
    public void updateList(){
        dataSource= new DataSource(this);
        dataSource.open();
        listNames=new ArrayList<>();
        ListView resultsListView = (ListView) findViewById(R.id.results_listview);
        int p=0;
        HashMap<String, String> nameAddresses = new HashMap<>();
        for (Recipe r:dataSource.getAllRecipes()){
            String ingredients = "";
            for (String i:dataSource.getNeeds(r)){
                ingredients=ingredients+i+ " ";

            }
            ingredients=ingredients;
            nameAddresses.put(r.getName(), ingredients);
        }


        List<HashMap<String, String>> listItems = new ArrayList<>();
        adapter = new SimpleAdapter(this, listItems, R.layout.list_item,
                new String[]{"First Line", "Second Line"},
                new int[]{R.id.text1, R.id.text2});


        Iterator it = nameAddresses.entrySet().iterator();
        while (it.hasNext())
        {
            HashMap<String, String> resultsMap = new HashMap<>();
            Map.Entry pair = (Map.Entry)it.next();

            String star = "   ";
            if(dataSource.getRecipe(pair.getKey().toString()).canDoWith(dataSource.getActualIngredients())){
                star="★  ";
            }


            resultsMap.put("First Line", star+pair.getKey().toString());
            resultsMap.put("Second Line", pair.getValue().toString());
            listItems.add(resultsMap);
            listNames.add( pair.getKey().toString());
        }

        resultsListView.setAdapter(adapter);


    }


    //onResume method: User returns to the activity after another activity comes into foreground
    @Override
    protected void onResume() {
        super.onResume();
        updateList();
    }


    //onRestart method: User navigates to the activity after it's no longer visible
    @Override
    protected void onRestart() {
        super.onRestart();
        updateList();
    }
}