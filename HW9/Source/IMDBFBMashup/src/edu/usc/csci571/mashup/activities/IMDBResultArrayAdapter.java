/**
 * 
 */
package edu.usc.csci571.mashup.activities;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import edu.usc.csci571.mashup.parser.IMDBRecord;
import edu.usc.csci571.mashup.parser.IMDBRecords;
import edu.usc.csci571.mashup.utilities.AsyncData;

/**
 * @author mohit.aggarwal
 *
 */
public class IMDBResultArrayAdapter extends BaseAdapter {
	
	private final Context context;
	private List<IMDBRecord> values;
	@SuppressLint("UseSparseArrays")
	private Map<Integer, Bitmap> imgMap = new HashMap<Integer, Bitmap>();
	private final LayoutInflater rowItemInflater;

	/**
	 * 
	 * @param context
	 * @param textViewResourceId
	 * @param objects
	 */
	public IMDBResultArrayAdapter(Context context) {
		super();
		this.context = context;
		this.values = new ArrayList<IMDBRecord>();
		this.rowItemInflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = rowItemInflater.inflate(R.layout.list_item, parent, false);
		rowView.setBackgroundColor(Color.BLACK);
		
		IMDBRecord record = values.get(position);
		TextView titleTextView = (TextView) rowView.findViewById(R.id.movieTitle);
		String title = record.getTitle() +" ("+ record.getYear()+")";
		titleTextView.setText(title);
		
		TextView ratingsTextView = (TextView) rowView.findViewById(R.id.movieRating);
		String ratings = "Rating: "+record.getRatings();
		ratingsTextView.setText(ratings);
		
		ImageView imgView = (ImageView) rowView.findViewById(R.id.coverImage);
		ImageDownloaderTask task = new ImageDownloaderTask(imgView);
		task.execute(record.getCover(),position+"");
		
		return rowView;
	}

	@Override
	public int getCount() {
		return values.size();
	}

	@Override
	public Object getItem(int position) {
		return values.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void updateResults(AsyncData records){
		this.values = ((IMDBRecords)records).getRecordsList();
		//to trigger the rendering of the list items
		notifyDataSetChanged();
	}

	/**
	 * @return the imgMap
	 */
	public Map<Integer, Bitmap> getImgMap() {
		return imgMap;
	}
	
	private class ImageDownloaderTask extends AsyncTask<String, Void, Bitmap>
	{
		private final WeakReference<ImageView> imageViewReference;
		
		public ImageDownloaderTask(ImageView view){
			imageViewReference = new WeakReference<ImageView>(view);
		}

		@Override
		protected Bitmap doInBackground(String... params) {
			Bitmap image = null;
			try {
				URL url = new URL(params[0]);
				InputStream content = (InputStream)url.getContent();
				//Drawable drawable = Drawable.createFromStream(content, "src");
				//imgMap.put(Integer.parseInt(params[1]), drawable);
				//record.setDrawable(drawable);
				//imgView.setImageDrawable(drawable);
				image = BitmapFactory.decodeStream(content);
				imgMap.put(Integer.parseInt(params[1]), image);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return image;
		}
		
		@Override
		protected void onPostExecute(Bitmap bitmap) {
	        if (isCancelled()) {
	            bitmap = null;
	        }

	        if (imageViewReference != null) {
	            ImageView imageView = imageViewReference.get();
	            if (imageView != null) {
	                imageView.setImageBitmap(bitmap);
	            }
	        }
	    }
		
	}
}
