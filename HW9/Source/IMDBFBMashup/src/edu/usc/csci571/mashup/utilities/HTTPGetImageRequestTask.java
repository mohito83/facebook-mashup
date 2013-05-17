/**
 * 
 */
package edu.usc.csci571.mashup.utilities;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import edu.usc.csci571.mashup.activities.IMDBResultArrayAdapter;

/**
 * @author mohit.aggarwal
 *
 */
public class HTTPGetImageRequestTask extends HTTPGetRequestTask {
	
	private ImageView imageView = null;
	
	/**
	 * @param adapter
	 */
	public HTTPGetImageRequestTask(IMDBResultArrayAdapter adapter) {
		super(null, adapter);
	}
	
	@Override
	protected AsyncData doInBackground(String... arg0) {
		String urlStr = arg0[0];
		try {
			URL url = new URL(urlStr);
			InputStream content = (InputStream)url.getContent();
			Drawable drawable = Drawable.createFromStream(content, "src");
			imageView.setImageDrawable(drawable);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @return the imageView
	 */
	public ImageView getImageView() {
		return imageView;
	}

	/**
	 * @param imageView the imageView to set
	 */
	public void setImageView(ImageView imageView) {
		this.imageView = imageView;
	}

}
