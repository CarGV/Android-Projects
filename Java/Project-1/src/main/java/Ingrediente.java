package com.example.brandonmain.listviewexample1;

/*
This class represent the object Ingredient that we use in all the application and we save in the DB
 */

public class Ingrediente {
    private float price;
    private String name;
    private int quantity;
    private boolean have;

    public String getExpire() {
        return expire;
    }

    public void setExpire(String expire) {
        this.expire = expire;
    }

    private String expire;
    private long id;

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isHave() {
        return have;
    }

    public void setHave(boolean have) {
        this.have = have;
    }

    /*
    This method is for know if the ingredient is in the DB or not
     */

    public void setHave(int have) {
        if (have>0) {
            this.have = true;
        }else{
            this.have = false;
        }
    }

    // Will be used by the ArrayAdapter in the ListView
    @Override
    public String toString() {
        return name;
    }

    public String toStringDate(){
        return expire+" - "+name;
    }

}