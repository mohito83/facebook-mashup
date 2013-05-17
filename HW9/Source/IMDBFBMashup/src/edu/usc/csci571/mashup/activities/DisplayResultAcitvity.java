package edu.usc.csci571.mashup.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import edu.usc.csci571.mashup.parser.IMDBRecord;
import edu.usc.csci571.mashup.utilities.HTTPGetRequestTask;
import edu.usc.csci571.mashup.utilities.UIConstants;

public class DisplayResultAcitvity extends ListActivity {

	private IMDBResultArrayAdapter adapter = null;
	private HTTPGetRequestTask task = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_result_acitvity);
		adapter = new IMDBResultArrayAdapter(this);
		setListAdapter(adapter);

		task = new HTTPGetRequestTask(this, adapter);
		String urlStr = (String) getIntent().getExtras().get(
				UIConstants.KEY_INTENT_ARGUEMENT);
		task.execute(urlStr);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater()
				.inflate(R.menu.activity_display_result_acitvity, menu);
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


	protected void onListItemClick(ListView l, View v, int position, long id) {
		final IMDBRecord record = (IMDBRecord) adapter.getItem(position);
		Bitmap drawable = adapter.getImgMap().get(position);
		
		launchPostToFBDialog(record,drawable);
		
	}

	private void launchPostToFBDialog(final IMDBRecord record, Bitmap drawable) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		
		LayoutInflater inflater = this.getLayoutInflater();
		
		View view = inflater.inflate(R.layout.main_details_dialog, null);
		ImageView img = (ImageView)view.findViewById(R.id.coverImage);
		//img.setImageDrawable(drawable);
		img.setImageBitmap(drawable);
		
		TextView name = (TextView)view.findViewById(R.id.name);
		String nameStr = "Name: "+record.getTitle();
		name.setText(nameStr);
		
		TextView year = (TextView) view.findViewById(R.id.year);
		String yearStr = "Year: "+ record.getYear();
		year.setText(yearStr);
		
		TextView director = (TextView) view.findViewById(R.id.director);
		String directorStr = "Director: "+ record.getDirectors();
		director.setText(directorStr);
		
		TextView ratings = (TextView) view.findViewById(R.id.rating);
		String ratingStr = "Rating: "+record.getRatings();
		ratings.setText(ratingStr);
		
		Button button = (Button) view.findViewById(R.id.postToFbButton);
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(DisplayResultAcitvity.this,PostToFacebookActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable(UIConstants.KEY_INTENT_IMDBRECORD_ARGUEMENT, record);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
		
		
		builder.setView(view);
		builder.show();
	}

}