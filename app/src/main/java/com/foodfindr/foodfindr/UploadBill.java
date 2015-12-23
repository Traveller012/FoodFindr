package com.foodfindr.foodfindr;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.foodfindr.foodfindr.com.foodfindr.foodfindr.model.BillDetails;
import com.foodfindr.foodfindr.com.foodfindr.foodfindr.model.DynamoDBManager;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

public class UploadBill extends AppCompatActivity {

    ListView list;
    String[] itemname ={
            "Dosa",
            "Pizza",
            "Idli"
    };
    public static ArrayList<String> JSONMatchedMenuItems = new ArrayList<String>();
    public static ArrayList<Integer> sentiment = new ArrayList<Integer>();
    public static BillDetails billDetails;
    private static final int TAKE_PICTURE = 1;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_bill);

        sentiment.clear();
        JSONMatchedMenuItems.clear();
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        File photo = new File(Environment.getExternalStorageDirectory(),  "Pic.jpg");
        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(photo));
        imageUri = Uri.fromFile(photo);
        startActivityForResult(intent, TAKE_PICTURE);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);



    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case TAKE_PICTURE:
                if (resultCode == Activity.RESULT_OK) {
                    Uri selectedImage = imageUri;
                    getContentResolver().notifyChange(selectedImage, null);
                    ImageView imageView = (ImageView) findViewById(R.id.capturedBillImageView);
                    ContentResolver cr = getContentResolver();
                    Bitmap bitmap;
                    try {
                        bitmap = android.provider.MediaStore.Images.Media
                                .getBitmap(cr, selectedImage);

                        imageView.setImageBitmap(bitmap);
                        Toast.makeText(this, selectedImage.toString(),
                                Toast.LENGTH_LONG).show();

                        UploadBill.JSONMatchedMenuItems = ScanReceipts.getJSONDataFromImage(bitmap);



                        itemname = JSONMatchedMenuItems.toArray(new String[JSONMatchedMenuItems.size()]);

                        RateFoodListAdapter adapter=new RateFoodListAdapter(this, itemname);
                        list=(ListView)findViewById(R.id.mylist_rate_food_items);
                        list.setAdapter(adapter);

                        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view,
                                                    int position, long id) {
                                // TODO Auto-generated method stub
                                String Selecteditem = itemname[+position];
                                //Toast.makeText(getApplicationContext(), Selecteditem, Toast.LENGTH_SHORT).show();


                            }
                        });


                        Button b=(Button)findViewById(R.id.saveRatingsButton);

                        b.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                billDetails = new BillDetails();
//                                billDetails.setID(new Date().getTime()+"");
                                billDetails.setUserID(MainActivity.userID);
                                billDetails.setFoodItem(UploadBill.JSONMatchedMenuItems);
                                billDetails.setFoodSentiment(UploadBill.sentiment);
                                billDetails.setBillID("" + new Date() + MainActivity.userID);
                                Log.d("UploadBill",billDetails.toString());
                                DynamoDBManager.insertBillData(billDetails);
                                finish();

                            }
                        });




                    } catch (Exception e) {
                        Toast.makeText(this, "Failed to load", Toast.LENGTH_SHORT)
                                .show();
                        Log.e("Camera", e.toString());
                    }
                }
                else
                {   //if image not taken
                    finish();
                }
        }
    }
}
