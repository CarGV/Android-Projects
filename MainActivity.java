package com.example.brandonmain.listviewexample1;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/*
This is our MainActivity that control our MainLayout that contains only three buttons that bring you
to the next activity that you choose
 */
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }


    /*
    Here there are the three methods that bring you to the next activity
     */

    public void clickFridge(View view) {
        Intent i = new Intent(MainActivity.this, Fridge.class);
        startActivity(i);
    }
    public void clickRecipes(View view){
        Intent i = new Intent(MainActivity.this, Recipes.class);
        startActivity(i);
    }
    public void clickList(View view){
        Intent i = new Intent(MainActivity.this, Calculator.class);
        startActivity(i);
    }

}