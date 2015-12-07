package com.foodfindr.foodfindr;

/**
 * Created by suhani on 12/5/15.
 */
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FoodHistoryListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] historicalFoodItems;
    private final String[] historicalFoodRatings;
    private final Integer[] historicalFoodFrequencies;

    public FoodHistoryListAdapter(Activity context, String[] historicalFoodItems, String[] historicalFoodRatings, Integer[] historicalFoodFrequencies) {
        super(context, R.layout.mylist_rate_food_items, historicalFoodItems);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.historicalFoodItems=historicalFoodItems;
        this.historicalFoodRatings=historicalFoodRatings;
        this.historicalFoodFrequencies=historicalFoodFrequencies;
    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.mylist_food_history, null, true);

        TextView historicalFoodItemTextView = (TextView) rowView.findViewById(R.id.historicalFoodItemTextView);
        TextView historicalFoodRatingTextView = (TextView) rowView.findViewById(R.id.historicalFoodRatingTextView);
        TextView historicalFoodFrequencyTextView = (TextView) rowView.findViewById(R.id.historicalFoodFrequencyTextView);

        historicalFoodItemTextView.setText(historicalFoodItems[position]);
        historicalFoodRatingTextView.setText("Liking: " + historicalFoodRatings[position]);
        historicalFoodFrequencyTextView.setText("Ordered "+ historicalFoodFrequencies[position] + " times");

        return rowView;

    };
}
