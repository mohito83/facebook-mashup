package edu.usc.csci571.mashup.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;

import edu.usc.csci571.mashup.parser.IMDBRecord;
import edu.usc.csci571.mashup.utilities.UIConstants;

public class PostToFacebookActivity extends Activity {

	Facebook facebook = new Facebook("304842176291237");
	AsyncFacebookRunner mAsyncRunner = new AsyncFacebookRunner(facebook);
	private SharedPreferences mPrefs;

	@SuppressLint("WorldWriteableFiles")
	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_facebook);
		
		/*
         * Get existing access_token if any
         */
        mPrefs = getSharedPreferences(UIConstants.KEY_FB_CRED, MODE_WORLD_WRITEABLE);//getPreferences(MODE_PRIVATE);
        String access_token = mPrefs.getString("access_token", null);
        long expires = mPrefs.getLong("access_expires", 0);
        if(access_token != null) {
            facebook.setAccessToken(access_token);
        }
        if(expires != 0) {
            facebook.setAccessExpires(expires);
        }
        
        //Post IMDBrecord information on the Facebook feed
        Bundle bundle = getIntent().getExtras();
        final IMDBRecord record = (IMDBRecord)bundle.get(UIConstants.KEY_INTENT_IMDBRECORD_ARGUEMENT);
        
        /*
         * Only call authorize if the access_token has expired.
         */
        if(!facebook.isSessionValid()) {
        	facebook.authorize(this, new String[] {}, new DialogListener() {
                @Override
                public void onComplete(Bundle values) {
                    SharedPreferences.Editor editor = mPrefs.edit();
                    editor.putString("access_token", facebook.getAccessToken());
                    editor.putLong("access_expires", facebook.getAccessExpires());
                    editor.commit();
                    
                    //post to FB after user autherization completes
                    postToFB(record);
                }
    
                @Override
                public void onFacebookError(FacebookError error) {
                	System.out.println("Error in authorization!! error=="+error.getMessage());
                }
    
                @Override
                public void onError(DialogError e) {}
    
                @Override
                public void onCancel() {
                	finish();
                }
            });
        } else {
        	postToFB(record);
        }

	}
	
	/**
	 * This method takes the IMDBRecord pbject nd use it populate the facebook feed dialog
	 * @param record
	 */
	private void postToFB(IMDBRecord record){
        //prepare the parameters to send
        Bundle params = new Bundle();
        
        String title = record.getTitle() +" ("+ record.getYear()+")";
        params.putString(UIConstants.KEY_FEED_NAME, title);
        params.putString(UIConstants.KEY_FEED_CAPTION, UIConstants.VALUE_FEED_CAPTION);
        params.putString(UIConstants.KEY_FEED_PICTURE, record.getCover());
        
        String description = title+" released in "+record.getYear()+" has a rating of "+record.getRatings();
        params.putString(UIConstants.KEY_FEED_DESCRIPTION, description);
        
        String rlink = record.getDetails();
        params.putString(UIConstants.KEY_FEED_LINK, rlink);
        
        String reviews = rlink+"reviews";
        String properties = "{\"Look at user reviews\":{text:\"here\",href:\""+reviews+"\"}} ";
        System.out.println("Properties!! "+properties);
        params.putString(UIConstants.KEY_FEED_PROPERTIES, properties);
        
      //post on user's wall.
        facebook.dialog(PostToFacebookActivity.this, "feed", params,new DialogListener() {
			
			@Override
			public void onFacebookError(FacebookError e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onError(DialogError e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onComplete(Bundle values) {
				// TODO Auto-generated method stub
				finish();
			}
			
			@Override
			public void onCancel() {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

	@SuppressWarnings("deprecation")
	@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        facebook.authorizeCallback(requestCode, resultCode, data);
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_facebook, menu);
		return true;
	}
	
	@SuppressWarnings("deprecation")
	public void onResume() {    
        super.onResume();
        facebook.extendAccessTokenIfNeeded(this, null);
    }

}
