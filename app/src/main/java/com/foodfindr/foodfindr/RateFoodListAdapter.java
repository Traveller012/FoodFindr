package com.foodfindr.foodfindr;

/**
 * Created by suhani on 12/5/15.
 */
import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class RateFoodListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] itemname;

    public RateFoodListAdapter(Activity context, String[] itemname) {
        super(context, R.layout.mylist_rate_food_items, itemname);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.itemname=itemname;
    }

    public View getView(final int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.mylist_rate_food_items, null, true);


        //sets food item details
        TextView foodItemOrderedTextView = (TextView) rowView.findViewById(R.id.foodItemOrderedTextView);
        foodItemOrderedTextView.setText(itemname[position]);

        //sadFace - onClickListener
        final ImageView sadImageTextView = (ImageView) rowView.findViewById(R.id.sadFace);


        //happyFace - onClickListener
        final ImageView happyImageTextView = (ImageView) rowView.findViewById(R.id.happyFace);

        sadImageTextView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                //v.getId() will give you the image id
                sadImageTextView.setColorFilter(Color.RED);
                happyImageTextView.clearColorFilter();
                UploadBill.sentiment.add(position,-1);

            }
        });


        happyImageTextView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                //v.getId() will give you the image id
                happyImageTextView.setColorFilter(Color.GREEN);
                sadImageTextView.clearColorFilter();
                UploadBill.sentiment.add(position, 1);

            }
        });


        return rowView;


    };
}
