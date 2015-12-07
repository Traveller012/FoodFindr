package com.foodfindr.foodfindr;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class FoodHistory extends AppCompatActivity {

    ListView list;
    String[] historicalFoodItems ={
            "Dosa",
            "Pizza",
            "Idli"
    };
    String[] historicalFoodRatings ={
            "High",
            "Medium",
            "High"
    };
    Integer[] historicalFoodFrequencies ={
            4, 6, 8
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_history);


        FoodHistoryListAdapter adapter=new FoodHistoryListAdapter(this, historicalFoodItems, historicalFoodRatings, historicalFoodFrequencies);
        list=(ListView)findViewById(R.id.mylist_food_history);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                String Slecteditem = historicalFoodItems[+position];
                //Toast.makeText(getApplicationContext(), Slecteditem, Toast.LENGTH_SHORT).show();

            }
        });

    }
}
