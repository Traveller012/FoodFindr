package com.foodfindr.foodfindr;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class UploadBill extends AppCompatActivity {

    ListView list;
    String[] itemname ={
            "Dosa",
            "Pizza",
            "Idli"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_bill);


        RateFoodListAdapter adapter=new RateFoodListAdapter(this, itemname);
        list=(ListView)findViewById(R.id.mylist_rate_food_items);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                String Slecteditem = itemname[+position];
                //Toast.makeText(getApplicationContext(), Slecteditem, Toast.LENGTH_SHORT).show();

            }
        });

    }
}
