package edu.usc.csci571.mashup.activities;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import edu.usc.csci571.mashup.utilities.UIConstants;

/**
 * This class the main activity class which starts the IMDB facebook mashup
 * andriod app
 * 
 * @author mohit.aggarwal
 * 
 */
public class SearchMovieActivity extends Activity {

	private String mediaType = null;
	private String title = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_movie);

		// Add OnItemSelectedListener on the SpinnerView
		Spinner spinner = (Spinner) findViewById(R.id.spinner1);
		spinner.setOnItemSelectedListener(new SpinnerSelectionListener());
		
		String [] spin_arry = getResources().getStringArray(R.array.media_type_title);        
		CustomArrayAdapter<CharSequence> mAdapter = new CustomArrayAdapter<CharSequence>(this, spin_arry);
		spinner.setAdapter(mAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_seach_movie, menu);
		return true;
	}
	
	@SuppressLint("WorldWriteableFiles")
	public boolean onOptionsItemSelected(MenuItem item) {
		SharedPreferences mPrefs = getSharedPreferences(UIConstants.KEY_FB_CRED, MODE_WORLD_WRITEABLE);
		
		 String access_token = mPrefs.getString("access_token", null);
	        long expires = mPrefs.getLong("access_expires", 0);
	        System.out.println("access_toekn="+access_token+" expires="+expires);
		SharedPreferences.Editor editor = mPrefs.edit();
        editor.putString("access_token", null);
        editor.putLong("access_expires", 0);
        editor.commit();
		return true;
	 }

	// this method is called when "Search" button is called.
	public void sendMessage(View view) {
		EditText textWidget = (EditText) findViewById(R.id.editText1);
		title = textWidget.getText().toString();
		
		if(title!=null && title.trim().length()>0){
			// prepare HTTP get request
			try {
				title = URLEncoder.encode(title, "UTF-8");
				//mediaType = URLEncoder.encode(mediaType, "UTF-8");
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
			String urlStr = "http://cs-server.usc.edu:10138/fbImdbMash/fbImdbMash?Title="
					+ title + "&mediaType=" + mediaType;
			//System.out.println("URL==" + urlStr);
			
			//send this URL to the another activity to handle the HTTP Get request
			Intent intent = new Intent(this,DisplayResultAcitvity.class);
			intent.putExtra(UIConstants.KEY_INTENT_ARGUEMENT, urlStr);
			startActivity(intent);
		} else {
			//show warning toast
			Context context = getApplicationContext();
			CharSequence message = UIConstants.WARNING_MESSAGE;
			
			Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
			toast.show();
		}
		
	}

	/**
	 * On item selected listener for the Spinner widget
	 * 
	 * @author mohit.aggarwal
	 * 
	 */
	private class SpinnerSelectionListener implements OnItemSelectedListener {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			//((TextView) arg0.getChildAt(0)).setTextColor(Color.BLACK);
			mediaType = arg0.getItemAtPosition(arg2).toString();
			mediaType = mediaType.replace(' ', '+');
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// mediaType = "All Types";
		}

	}
	
	
	/**
	 * This class isa custom array adapter for the spinner to populate and render it content
	 * @author mohit.aggarwal
	 *
	 * @param <T>
	 */
	private class CustomArrayAdapter<T> extends ArrayAdapter<T>	{
		
		T[] objects;
	    public CustomArrayAdapter(Context ctx, T [] objects) {
	        super(ctx, R.layout.custom_spinner, objects);
	        this.objects = objects;
	    }

	    //other constructors

	    @Override
	    public View getDropDownView(int position, View convertView, ViewGroup parent) {
	        View view = super.getView(position, convertView, parent);

	        //we know that simple_spinner_item has android.R.id.text1 TextView:         

	        /* if(isDroidX) {*/
	            TextView text = (TextView)view.findViewById(R.id.mytext1);
	            text.setText((CharSequence)objects[position]);
	            text.setTextColor(Color.BLACK);//choose your color :)         
	        /*}*/

	        return view;

	    }
	}
}
