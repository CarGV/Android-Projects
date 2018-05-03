package com.example.brandonmain.listviewexample1;

import java.util.ArrayList;

/*
This class represent an object Recipe
 */

public class Recipe {
    private String name;
    private ArrayList<String> listIngredients = new ArrayList<String>();
    private String method;
    private String url;

    public ArrayList<String> getListIngredients() {
        return listIngredients;
    }

    public void setListIngredients(ArrayList<String> listIngredients) {
        this.listIngredients = listIngredients;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    // Will be used by the ArrayAdapter in the ListView
    @Override
    public String toString() {
        return name;
    }

    /*
    This function is for see all the ingredients that you need to do the recipe
     */

    public boolean canDoWith(ArrayList<String> ings){
        return  ings.containsAll(getListIngredients());
    }
}