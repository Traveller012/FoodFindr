package OCR;

import org.json.simple.JSONObject;

public class NamedEntityExtractor
{
	public static String extractFoodData(String receiptData)
	{
		String jsonFoodString=null;
		String[] foodArray = {"cranberries","elmhurst"};
		JSONObject foodObject = new JSONObject();
		receiptData = receiptData.toLowerCase();
		int j=1;
		for(int i=0;i<foodArray.length;i++)
		{
			if(receiptData.contains(foodArray[i]))
			{
				Integer key = new Integer(j);
				foodObject.put(key, foodArray[i]);
				j++;
			}
		}
		
		jsonFoodString = foodObject.toJSONString();
		return jsonFoodString;
	}

}
