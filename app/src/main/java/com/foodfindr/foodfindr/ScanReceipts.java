package com.foodfindr.foodfindr;

/**
 * Created by suhani on 12/19/15.
 */
import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;

import com.foodfindr.foodfindr.com.foodfindr.foodfindr.model.RestaurantData;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import static com.foodfindr.foodfindr.com.foodfindr.foodfindr.model.DynamoDBManager.getRestaurantDataFromRestaurantName;

public class ScanReceipts {

    /**
     * @param args
     */
    public static ArrayList<String> getJSONDataFromImage(Bitmap image)
    {
        String license_code = "7E5C0FAB-2F70-48C3-BC07-29756AF14DFA";
        String user_name =  "KUSHWANTH";

        //Supporting only English Language Receipts
        String ocrURL = "http://www.ocrwebservice.com/restservices/processDocument?gettext=true";
        String filePath = "C:\\Users\\ASHASHANTHARAM\\Desktop\\IMAG0681.jpg";
        ArrayList<String> matchedItems = new ArrayList<>();
        try
        {

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] fileContent = stream.toByteArray();

//            byte[] fileContent = bitmap.bytes
            URL url;
            HttpURLConnection connection;
            url = new URL(ocrURL);
            connection = (HttpURLConnection)url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("POST");

                    connection.setRequestProperty("Authorization", "Basic " + Base64.encodeToString((user_name + ":" + license_code).getBytes(), Base64.DEFAULT));
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Content-Length", Integer.toString(fileContent.length));
            OutputStream outputStream = connection.getOutputStream();

            //Making the POST Request
            outputStream.write(fileContent);
            outputStream.close();
            int httpCode = connection.getResponseCode();
            System.out.println("HTTP Response code: " + httpCode);
            if (httpCode == HttpURLConnection.HTTP_OK)
            {
                System.out.println("The HTTP response is OK");
                // Get response stream
                String jsonResponse = GetResponseToString(connection.getInputStream());

                // Parse and print response from OCR server
                String receiptData = PrintOCRResponse(jsonResponse);
                System.out.println("The receipt data obtained is ");
                Log.d("d", receiptData);
                matchedItems = NamedEntityExtractor.extractFoodData(receiptData);

                return matchedItems;
            }
            else if (httpCode == HttpURLConnection.HTTP_UNAUTHORIZED)
            {
                System.out.println("OCR Error Message: Unauthorized request");
            }
            else
            {
                // Error occurred
                String jsonResponse = GetResponseToString(connection.getErrorStream());

                JSONParser parser = new JSONParser();
                JSONObject jsonObj = (JSONObject)parser.parse(jsonResponse);

                // Error message
                System.out.println("Error Message: " + jsonObj.get("ErrorMessage"));
            }

            connection.disconnect();

        }
        catch (Exception e1)
        {
            e1.printStackTrace();
        }
        return matchedItems;
    }

    private static String GetResponseToString(InputStream inputStream) throws IOException
    {
        InputStreamReader responseStream  = new InputStreamReader(inputStream);

        BufferedReader br = new BufferedReader(responseStream);
        StringBuffer strBuff = new StringBuffer();
        String s;
        while ( ( s = br.readLine() ) != null )
        {
            strBuff.append(s);
        }

        return strBuff.toString();
    }


    private static String PrintOCRResponse(String jsonResponse) throws ParseException, IOException
    {
        // Parse JSON data
        JSONParser parser = new JSONParser();
        JSONObject jsonObj = (JSONObject)parser.parse(jsonResponse);
        String receiptData;
        StringBuilder receiptBuilder = new StringBuilder();
        // Get available pages
        System.out.println("Available pages: " + jsonObj.get("AvailablePages"));

        // get an array from the JSON object
        JSONArray text= (JSONArray)jsonObj.get("OCRText");

        // For zonal OCR: OCRText[z][p]    z - zone, p - pages
        for(int i=0; i<text.size(); i++)
        {
            System.out.println(" "+ text.get(i));
            receiptBuilder.append(text.get(i));
        }
        receiptData = receiptBuilder.toString();
        return receiptData;
    }

}

class NamedEntityExtractor
{
    public static ArrayList<String> extractFoodData(String receiptData)
    {
        String jsonFoodString=null;
        String[] foodArray = {"cranberries","elmhurst", "mens", "suits", "Macy's", "Total"};
        JSONObject foodObject = new JSONObject();
        receiptData = receiptData.toLowerCase();
        int j=1;
        ArrayList<String> foodItemsMatched = new ArrayList<>();

        //get restaurant name matches
        ArrayList<String> matchedRestaurants = getRestaurantDataFromRestaurantName(receiptData);

        //for each restaurant name
        for (String matchedRestaurant : matchedRestaurants) {
            //find matching food items

            List<String> menuItems = getMenuItems(matchedRestaurant);

            for (String menuItem : menuItems) {

                if(receiptData.contains(menuItem))
                {
                    Integer key = new Integer(j);
                    foodItemsMatched.add(menuItem);
                    foodObject.put(key, menuItem);
                    j++;
                }
            }

        }
//
//        //get food item matches
//        for(int i=0;i<foodArray.length;i++)
//        {
//            if(receiptData.contains(foodArray[i]))
//            {
//                Integer key = new Integer(j);
//                foodObject.put(key, foodArray[i]);
//                j++;
//            }
//        }

        return foodItemsMatched;
        //jsonFoodString = foodObject.toJSONString();
        //return jsonFoodString;
    }
}