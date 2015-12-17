package OCR;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ScanReceipts {

	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		String license_code = "7E5C0FAB-2F70-48C3-BC07-29756AF14DFA";
		String user_name =  "KUSHWANTH";
		
		//Supporting only English Language Receipts
		String ocrURL = "http://www.ocrwebservice.com/restservices/processDocument?gettext=true";
		String filePath = "C:\\Users\\ASHASHANTHARAM\\Desktop\\IMAG0681.jpg";
		
		try
		{
			byte[] fileContent = Files.readAllBytes(Paths.get(filePath));
			URL url;
			HttpURLConnection connection;
			url = new URL(ocrURL);
			connection = (HttpURLConnection)url.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Authorization", "Basic " + Base64.getEncoder().encodeToString((user_name + ":" + license_code).getBytes()));
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("Content-Length", Integer.toString(fileContent.length));
			OutputStream stream = connection.getOutputStream();
			
			//Making the POST Request
			stream.write(fileContent);
			stream.close();
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
				System.out.println(receiptData);
				String jsonFood = NamedEntityExtractor.extractFoodData(receiptData);
				System.out.println("Food items converted into json string is " + jsonFood);
			}
			else if (httpCode == HttpURLConnection.HTTP_UNAUTHORIZED)
			{
				System.out.println("OCR Error Message: Unauthorizied request");
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
