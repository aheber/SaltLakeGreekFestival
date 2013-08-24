package com.saltlakegreekfestival.utahgreekfestival;

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
import android.widget.RelativeLayout;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class MainActivity extends BaseActivity {

	private Fragment mContent;
	private String TAG = "MainActivity";
    private static String[] adverts= null;
    private static final int SCROLL_FREQUENCY = 10000;
    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
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

    /** Called when the activity is first created. */


      public void rotatePager()
      {
          int curPge = mPager.getCurrentItem();
          int nextPge = 0;
          if(curPge >= adverts.length-1)
        	  nextPge = 0;
    	  else 
    		  nextPge = curPge+1;
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
		Log.v(TAG,"Menu is showing: "+String.valueOf(getSlidingMenu().isMenuShowing()));
		if(this.getSlidingMenu().isMenuShowing())
			super.onBackPressed();
		else 
			toggle();
	}
	
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event)
	{
	    if(keyCode == KeyEvent.KEYCODE_BACK)
	    {
	        if(getSlidingMenu().isMenuShowing())
	        {
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
			mContent = getSupportFragmentManager().getFragment(savedInstanceState, "mContent");
		if (mContent == null)
			mContent = new Schedule(); //Open to Schedule fragment	

		// set the Above View
		setContentView(R.layout.content_frame);
		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.content_frame, mContent)
		.commit();

		// set the Behind View
		setBehindContentView(R.layout.menu_frame);
		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.menu_frame, new ColorMenuFragment())
		.commit();

		// customize the SlidingMenu
		getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		
		adverts = this.getResources().getStringArray(R.array.advetisements);
        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.setOffscreenPageLimit(adverts.length-1);
        mPager.setPageMargin(0);
        //runnable.run();
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        LayoutParams lp = mPager.getLayoutParams();
        
        lp.height = (int) Math.max(metrics.widthPixels*0.10416666666667, 1);
        FrameLayout fl = (FrameLayout) findViewById(R.id.content_frame);
        RelativeLayout.LayoutParams flparams = (RelativeLayout.LayoutParams)fl.getLayoutParams();
        flparams.bottomMargin=(int) Math.max(metrics.widthPixels*0.10416666666667, 1);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		getSupportFragmentManager().putFragment(outState, "mContent", mContent);
	}
	
	

	@Override
	protected void onResume() {
		//handler.removeCallbacks(runnable);
        runnable.run();
		super.onResume();
	}


	public void switchContent(Fragment fragment) {
		mContent = fragment;
		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.content_frame, fragment)
		.commit();
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
            int id = getResources().getIdentifier(adverts[position], "drawable", getPackageName());
            frag.setDrawable(id);
        	return frag;
        }

        @Override
        public int getCount() {
            return adverts.length;
        }
        
        
    }
}