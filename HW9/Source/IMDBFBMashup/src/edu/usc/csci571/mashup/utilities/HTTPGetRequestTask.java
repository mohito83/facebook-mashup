/**
 * 
 */
package edu.usc.csci571.mashup.utilities;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.AsyncTask;
import edu.usc.csci571.mashup.activities.DisplayResultAcitvity;
import edu.usc.csci571.mashup.activities.IMDBResultArrayAdapter;
import edu.usc.csci571.mashup.parser.IMDBRecords;
import edu.usc.csci571.mashup.parser.JSONParser;

/**
 * This class performs the asynchronous HTTP GET operation 
 * @author mohit.aggarwal
 *
 */
public class HTTPGetRequestTask extends AsyncTask<String, Void, AsyncData> {
	
	private IMDBResultArrayAdapter adapter;
	private DisplayResultAcitvity activity;
	private ProgressDialog progressBar;
	
	
	public HTTPGetRequestTask(DisplayResultAcitvity displayResultAcitvity, IMDBResultArrayAdapter adapter){
		this.adapter = adapter;
		this.activity = displayResultAcitvity;
	}

	@Override
	protected AsyncData doInBackground(String... arg0) {
		String urlStr = arg0[0];
		//System.out.println("Received URL"+urlStr);
		IMDBRecords records = null;
		try {
			URL url = new URL(urlStr);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			int status = con.getResponseCode();
			if(status == 200){
				records = JSONParser.parseData(con.getInputStream());
				records.setStatus(status);
			} else {
				records = new IMDBRecords();
				String errorMsg = con.getResponseMessage();
				records.setErrorMsg(errorMsg);
				records.setStatus(status);
			}
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return records;
	}
	
	protected void onPostExecute(AsyncData data) {
		IMDBRecords records = (IMDBRecords)data;
		if(records!=null && records.getStatus() == 200 && records.getRecordsList().size()>0){
			adapter.updateResults(records);
		} else {
			String message = UIConstants.KEY_NO_RESULTS;
			
			AlertDialog.Builder builder = new AlertDialog.Builder(activity);
			builder.setPositiveButton("OK", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					activity.finish();
				}
			});
			
			if(records.getStatus()!=200){
				 message = records.getErrorMsg();
			 }
			
			builder.setMessage(message);
			
			AlertDialog dialog = builder.create();
			dialog.show();
		}
		progressBar.dismiss();
	}

	protected void onPreExecute(){
		progressBar = ProgressDialog.show(activity, "", UIConstants.KEY_LOADING_BAR);
		
	}
}
