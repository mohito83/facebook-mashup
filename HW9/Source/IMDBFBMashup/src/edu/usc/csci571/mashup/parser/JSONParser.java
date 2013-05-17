/**
 * 
 */
package edu.usc.csci571.mashup.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class contains methods that parses the JSON string received from the
 * sever
 * 
 * @author mohit.aggarwal
 * 
 */
public class JSONParser {

	/**
	 * This method reads the inputstream from the response object and convert it
	 * into JSON object for further processing.
	 * 
	 * @param in
	 * @return
	 */
	public static IMDBRecords parseData(InputStream in) {
		IMDBRecords records = null;
		StringBuffer buffer = new StringBuffer();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(in));
			String line = "";
			while ((line = reader.readLine()) != null) {
				buffer.append(line);
			}
			JSONObject jsonObject = new JSONObject(buffer.toString());
			records = parseJSON(jsonObject);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException je) {
			je.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return records;
	}

	/**
	 * This method parses the JSON object and populates the IMDB records
	 * 
	 * @param jsonObject
	 * @return
	 * @throws JSONException 
	 */
	private static IMDBRecords parseJSON(JSONObject jsonObject) throws JSONException {
		JSONObject results = jsonObject.getJSONObject("results");
		JSONArray jsonArray = results.getJSONArray("result");
		IMDBRecords records = new IMDBRecords();
		for(int i=0; i<jsonArray.length(); i++){
			JSONObject obj = jsonArray.getJSONObject(i);
			IMDBRecord rec = new IMDBRecord();
			rec.setCover(obj.getString("cover"));
			rec.setTitle(obj.getString("title"));
			rec.setYear(obj.getString("year"));
			rec.setDirectors(obj.getString("director"));
			rec.setRatings(obj.getString("rating"));
			rec.setDetails(obj.getString("details"));
			records.setRecord(rec);
		}
		return records;
	}
}
