package com.example.brandonmain.listviewexample1;

import android.database.Cursor;
        import java.util.ArrayList;
        import android.content.ContentValues;
        import android.content.Context;
        import android.database.Cursor;
        import android.database.SQLException;
        import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

public class DataSource {

    // Database fields
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumnsI = { MySQLiteHelper.COLUMN_NAME, MySQLiteHelper.COLUMN_QUANTITY, MySQLiteHelper.COLUMN_PRICE, MySQLiteHelper.COLUMN_EXPIRE };
    private String[] allColumnsR = { MySQLiteHelper.COLUMN_URL, MySQLiteHelper.COLUMN_METHOD, MySQLiteHelper.COLUMN_NAME };
    private String[] allColumnsN = { MySQLiteHelper.COLUMN_INGREDIENT, MySQLiteHelper.COLUMN_RECIPE };

    public DataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void addIngredient(String name, int quantity, float price, String expiration, boolean have) {
        int have_aux = 0;
        if(have)
            have_aux = 1;

        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_NAME, name);
        values.put(MySQLiteHelper.COLUMN_HAVE, have_aux);
        values.put(MySQLiteHelper.COLUMN_QUANTITY, quantity);
        values.put(MySQLiteHelper.COLUMN_PRICE, price);
        values.put(MySQLiteHelper.COLUMN_EXPIRE, expiration);

        database.insert(MySQLiteHelper.TABLE_INGREDIENTS, null, values);
    }

    public Ingrediente getIngredient(String name){
        Cursor cursor = database.query(MySQLiteHelper.TABLE_INGREDIENTS,
                allColumnsI, MySQLiteHelper.COLUMN_NAME + " = '" + name+"'", null,
                null, null, null);
        cursor.moveToFirst();
        Ingrediente newIng = cursorToIngredient(cursor);
        cursor.close();
        return newIng;
    }

    public void addRecipe(String name, String method, String url, ArrayList<String> ingredients){
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_NAME, name);
        values.put(MySQLiteHelper.COLUMN_METHOD,method);
        values.put(MySQLiteHelper.COLUMN_URL,url);

        for(String ing : ingredients){
            setNeed(ing,name);
        }

        database.insert(MySQLiteHelper.TABLE_RECIPES, null, values);
    }

    public Recipe getRecipe(String name){
        Cursor cursor = database.query(MySQLiteHelper.TABLE_RECIPES,
                allColumnsR, MySQLiteHelper.COLUMN_NAME + " = '" + name+"'", null,
                null, null, null);
        cursor.moveToFirst();
        Recipe newRec = cursorToRecipe(cursor);
        cursor.close();
        return newRec;
    }

    public void setNeed(String ing, String rec){
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_INGREDIENT, ing);
        values.put(MySQLiteHelper.COLUMN_RECIPE, rec);
        database.insert(MySQLiteHelper.TABLE_NEEDS,null,values);
    }

    public ArrayList<String> getNeeds(Recipe r){
        ArrayList<String> res = new ArrayList<>();
        Cursor cursor = database.query(MySQLiteHelper.TABLE_NEEDS,
                allColumnsN, MySQLiteHelper.COLUMN_RECIPE + " = '" + r.getName() +"'", null,
                null, null, null);
        try {
            while (cursor.moveToNext()) {
                res.add(cursorToNeed(cursor));
            }
        } finally {
            cursor.close();
        }
        return res;
    }

    public ArrayList<Recipe> getAllRecipes(){
        ArrayList<Recipe> res = new ArrayList<Recipe>();

        Cursor cursor = database.rawQuery("SELECT * FROM "+ MySQLiteHelper.TABLE_RECIPES +";",null);
        Log.i("A","AaAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
        try {
            while (cursor.moveToNext()) {
                Log.i("A","AaAAAAAAAAAAAAAAAAAAAAAAAAAAAAn");
                res.add(cursorToRecipe(cursor));
            }
        } finally {
            cursor.close();
        }

        return res;
    }

    public void delRecipe(String name){
        database.delete(MySQLiteHelper.TABLE_RECIPES, MySQLiteHelper.COLUMN_NAME+" = '"+name+"'", null);
    }

    public void delIng(String name) {
        database.delete(MySQLiteHelper.TABLE_INGREDIENTS, MySQLiteHelper.COLUMN_NAME + " = '" + name+"'", null);
    }

    public ArrayList<Ingrediente> getAllIngredients() {
        ArrayList<Ingrediente> res = new ArrayList<Ingrediente>();

        Cursor cursor = database.rawQuery("SELECT * FROM "+MySQLiteHelper.TABLE_INGREDIENTS+";",null);

        try {
            while (cursor.moveToNext()) {
                res.add(cursorToIngredient(cursor));
            }
        } finally {
            cursor.close();
        }
        return res;
    }

    public void delAllIngredients(){
        for(Ingrediente ing : getAllIngredients()){
            delIng(ing.getName());
        }
    }

    private Ingrediente cursorToIngredient(Cursor cursor) {
        Ingrediente i = new Ingrediente();
        i.setName(cursor.getString(0));
        i.setQuantity(cursor.getInt(1));
        i.setPrice(cursor.getFloat(2));
        i.setExpire(cursor.getString(3));
        return i;
    }

    private Recipe cursorToRecipe(Cursor cursor) {
        Recipe r = new Recipe();
        r.setName(cursor.getString(0));
        r.setMethod(cursor.getString(1));
        r.setUrl(cursor.getString(2));
        ArrayList<String> needs = new ArrayList<>();
        Cursor cursor_n = database.rawQuery("select * from "+MySQLiteHelper.TABLE_NEEDS+" where "+MySQLiteHelper.COLUMN_RECIPE+"='"+r.getName()+"';",null);


        try {
            while (cursor_n.moveToNext()) {
                needs.add(cursorToNeed(cursor_n));
            }
        } finally {
            cursor_n.close();
        }
        r.setListIngredients(needs);
        return r;
    }

    private String cursorToNeed(Cursor cursor) {
        return cursor.getString(0);
    }

    public ArrayList<Ingrediente> getWantedIngredients() {
        ArrayList<Ingrediente> res = new ArrayList<Ingrediente>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_INGREDIENTS,
                allColumnsI, MySQLiteHelper.COLUMN_QUANTITY + " > 0", null, null, null, null);

        try {
            while (cursor.moveToNext()) {
                res.add(cursorToIngredient(cursor));
            }
        } finally {
            cursor.close();
        }
        return res;
    }

    public ArrayList<String> getActualIngredients() {
        ArrayList<String> res = new ArrayList<>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_INGREDIENTS,
                allColumnsI, MySQLiteHelper.COLUMN_HAVE + " > 0", null, null, null, null);

        try {
            while (cursor.moveToNext()) {
                res.add(cursorToIngredient(cursor).toString());
            }
        } finally {
            cursor.close();
        }
        return res;
    }

    public void delWantedIngredients(){
        for(Ingrediente ing : getWantedIngredients()){
            delIng(ing.getName());
        }
    }



    public void markAsHave(String ing){
        database.rawQuery("UPDATE "+MySQLiteHelper.TABLE_INGREDIENTS+" SET "+MySQLiteHelper.COLUMN_HAVE+"='1' WHERE "+MySQLiteHelper.COLUMN_NAME+"='"+ing+"';",null);
    }

    public void markAsNotHave(String ing){
        database.rawQuery("UPDATE "+MySQLiteHelper.TABLE_INGREDIENTS+" SET "+MySQLiteHelper.COLUMN_HAVE+"='0' WHERE "+MySQLiteHelper.COLUMN_NAME+"='"+ing+"';",null);
    }

    public void markAsWanted(String ing){
        database.rawQuery("UPDATE "+MySQLiteHelper.TABLE_INGREDIENTS+" SET "+MySQLiteHelper.COLUMN_QUANTITY+"='1' WHERE "+MySQLiteHelper.COLUMN_NAME+"='"+ing+"';",null);
    }

    public void markAsNotWanted(String ing){
        database.rawQuery("UPDATE "+MySQLiteHelper.TABLE_INGREDIENTS+" SET "+MySQLiteHelper.COLUMN_QUANTITY+"='0' WHERE "+MySQLiteHelper.COLUMN_NAME+"='"+ing+"';",null);
    }

    public String getUrl(String s) {

        return getRecipe(s).getName();
    }
}