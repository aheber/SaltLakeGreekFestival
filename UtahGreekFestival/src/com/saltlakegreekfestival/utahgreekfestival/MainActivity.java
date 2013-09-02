package com.saltlakegreekfestival.utahgreekfestival;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class MainActivity extends BaseActivity {

	private Fragment mContent;
	private String TAG = "MainActivity";
	// private static String[] adverts= null;
	private static final int SCROLL_FREQUENCY = 10000;
	private static float AD_METRIC = 0.09259259259259f;
	private static final String AWS_URL = "http://slcgreekfestival.s3-website-us-west-2.amazonaws.com/";
	private ArrayList<Advertisement> advertisements;
	private Context mContext;
	/**
	 * The pager widget, which handles animation and allows swiping horizontally
	 * to access previous and next wizard steps.
	 */
	private ViewPager mPager;
	/**
	 * The pager adapter, which provides the pages to the view pager widget.
	 */
	private PagerAdapter mPagerAdapter;

	public MainActivity() {
		super(R.string.greek_festival);
	}

	Handler handler = new Handler();
	Runnable runnable = new Runnable() {
		public void run() {
			rotatePager();
		}
	};

	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		finish();
	}

	/** Called when the activity is first created. */

	public void rotatePager() {
		int curPge = mPager.getCurrentItem();
		int nextPge = 0;
		if (curPge >= advertisements.size() - 1)
			nextPge = 0;
		else
			nextPge = curPge + 1;
		mPager.setCurrentItem(nextPge, false);
		handler.postDelayed(runnable, SCROLL_FREQUENCY);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		handler.removeCallbacks(runnable);
		super.onPause();
	}

	@Override
	public void onBackPressed() {
		Log.v(TAG,
				"Menu is showing: "
						+ String.valueOf(getSlidingMenu().isMenuShowing()));
		if (this.getSlidingMenu().isMenuShowing())
			super.onBackPressed();
		else
			toggle();
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (getSlidingMenu().isMenuShowing()) {
				finish();
				return true;
			}
		}
		return super.onKeyUp(keyCode, event);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// set the Above View
		if (savedInstanceState != null)
			mContent = getSupportFragmentManager().getFragment(
					savedInstanceState, "mContent");
		if (mContent == null)
			mContent = new Schedule(); // Open to Schedule fragment
		mContext = (Context)this;
		FileDownloader fd = new FileDownloader();
		fd.execute("sponsors.xml");

		fd = new FileDownloader();
		fd.execute("food.xml");
		// set the Above View
		setContentView(R.layout.content_frame);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.content_frame, mContent).commit();

		// set the Behind View
		setBehindContentView(R.layout.menu_frame);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.menu_frame, new SlideoutMenu()).commit();

		// customize the SlidingMenu
		getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		setupAdPager();
	}

	private void setupAdPager() {

		try {
			advertisements = buildAds();
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// adverts = this.getResources().getStringArray(R.array.advetisements);
		// Instantiate a ViewPager and a PagerAdapter.
		mPager = (ViewPager) findViewById(R.id.pager);
		mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
		mPager.setAdapter(mPagerAdapter);
		mPager.setOffscreenPageLimit(advertisements.size() - 1);
		mPager.setPageMargin(0);
		// runnable.run();
		DisplayMetrics metrics = getResources().getDisplayMetrics();
		LayoutParams lp = mPager.getLayoutParams();

		lp.height = (int) Math.ceil(metrics.widthPixels * AD_METRIC);
		FrameLayout fl = (FrameLayout) findViewById(R.id.content_frame);
		RelativeLayout.LayoutParams flparams = (RelativeLayout.LayoutParams) fl
				.getLayoutParams();
		flparams.bottomMargin = (int) Math
				.ceil(metrics.widthPixels * AD_METRIC);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		getSupportFragmentManager().putFragment(outState, "mContent", mContent);
	}

	@Override
	protected void onResume() {
		// handler.removeCallbacks(runnable);
		runnable.run();
		super.onResume();
	}

	public void switchContent(Fragment fragment) {
		mContent = fragment;
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.content_frame, fragment).commit();
		getSupportFragmentManager().executePendingTransactions();
		getSlidingMenu().showContent();
	}

	private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
		public ScreenSlidePagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			AdFragment frag = new AdFragment();
			frag.setUrl(advertisements.get(position).getUrl());
			File adImage = new File(mContext.getFilesDir(),advertisements.get(position).getName()+".png");
			InputStream is = null;
			if(adImage.exists())
				frag.setDrawableFile(adImage);
			else {
				FileDownloader fd = new FileDownloader(frag);
				fd.execute(advertisements.get(position).getName()+".png");
				frag.setDrawable(advertisements.get(position).getImage());
			}
			
			return frag;
		}

		@Override
		public int getCount() {
			return advertisements.size();
		}

	}

	private ArrayList<Advertisement> buildAds() throws XmlPullParserException,
			IOException {
		ArrayList<Advertisement> ads = new ArrayList<Advertisement>();
		XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
		factory.setNamespaceAware(true);
		XmlPullParser xpp = factory.newPullParser();
		File sponsorsDL = new File(this.getFilesDir(),"sponsors.xml");
		InputStream is = null;
		if(sponsorsDL.exists())
			is = new FileInputStream(sponsorsDL);
		else
			is = getResources().openRawResource(R.raw.sponsors);
		xpp.setInput(is, "utf-8");
		// check state
		int eventType;
		String tagName = "";
		eventType = xpp.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			// instead of the following if/else if lines
			// you should custom parse your xml
			if (eventType == XmlPullParser.START_DOCUMENT) {
			} else if (eventType == XmlPullParser.START_TAG) {
				tagName = xpp.getName();
				if (tagName.equals("sponsor")) {
					Advertisement ad = new Advertisement(xpp.getAttributeValue(
							null, "image"));
					ad.setImage(getResources().getIdentifier(
							xpp.getAttributeValue(null, "image").toLowerCase(),
							"drawable", getPackageName()));
					ad.setUrl(xpp.getAttributeValue(null, "url"));
					ad.setText(xpp.nextText());
					ads.add(ad);
				}
			} else if (eventType == XmlPullParser.END_TAG) {
			} else if (eventType == XmlPullParser.TEXT) {
			}
			eventType = xpp.next();
		}
		if(is != null)
			is.close();
		return ads;
	}

	class Advertisement {
		String name;
		int image;
		String url;
		String text;

		public Advertisement(String name) {
			this.name = name;
		}

		public Advertisement(String name, int image) {
			this.image = image;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public void setImage(int image) {
			this.image = image;
		}

		public int getImage() {
			return image;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public String getUrl() {
			return url;
		}

		public void setText(String text) {
			this.text = text;
		}

		public String getText() {
			return text;
		}
	}

	private class FileDownloader extends AsyncTask<String, Integer, String> {
		ImageView image = null;
		AdFragment adFrag = null;
		
		public FileDownloader(){
		}
		public FileDownloader(ImageView image){
			this.image = image;
		}

		public FileDownloader(AdFragment adFrag){
			this.adFrag = adFrag;
		}
		
		@Override
		protected void onPreExecute() {
			Log.i("Async-Example", "onPreExecute Called");

		}

		@Override
		protected void onPostExecute(String result) {
			Log.i("Async-Example", "onPostExecute Called");

			File file = new File(mContext.getFilesDir(),result);
			if (result.equals("sponsors.xml"))
				setupAdPager();
			if(adFrag != null && file.exists()){
				adFrag.setDrawableFile(file);
				image = adFrag.getImageView();
				if(image != null)
					image.setImageBitmap(BitmapFactory.decodeFile(file.getPath()));
			}
		}

		private String downloadFile(String files) {
			// initilize the default HTTP client object
			final DefaultHttpClient client = new DefaultHttpClient();
			// forming a HttpGet request
			final HttpGet getRequest = new HttpGet(AWS_URL + files);
			try {

				HttpResponse response = client.execute(getRequest);

				// check 200 OK for success
				final int statusCode = response.getStatusLine().getStatusCode();

				if (statusCode != HttpStatus.SC_OK) {
					Log.w("Async-Example", "Error " + statusCode
							+ " while retrieving file from " + AWS_URL + files);
					return null;

				}

				Log.w("Async-Example", "Great! " + statusCode
						+ " while retrieving file from " + AWS_URL + files);

				final HttpEntity entity = response.getEntity();
				if (entity != null) {
					InputStream inputStream = null;
					FileOutputStream fos = null;
					byte[] bytes = new byte[512];
					try {
						// getting contents from the stream
						// inputStream = entity.getContent();
						BufferedHttpEntity buf = new BufferedHttpEntity(entity);

						inputStream = buf.getContent();
						fos = openFileOutput(files, Context.MODE_PRIVATE);
						buf.writeTo(fos);
						while (buf.isStreaming()) {
							// fos.write(inputStream.read(bytes));
							buf.writeTo(fos);
						}
					} finally {
						if (fos != null) {
							fos.close();
						}
						if (inputStream != null) {
							inputStream.close();
						}
						entity.consumeContent();
					}
				}
			} catch (Exception e) {
				// You Could provide a more explicit error message for
				// IOException
				getRequest.abort();
				Log.e("Async-Example",
						"Something went wrong while"
								+ " retrieving bitmap from " + AWS_URL
								+ files + e.toString());
			}
			return files;
		}

		@Override
		protected String doInBackground(String... params) {
			Log.d("Async-Example", "Starting Download");
			downloadFile(params[0]);
			Log.d("Async-Example", "Download Finished");
			return params[0];

		}
	}

}