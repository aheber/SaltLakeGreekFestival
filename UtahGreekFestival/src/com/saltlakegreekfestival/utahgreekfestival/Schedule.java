package com.saltlakegreekfestival.utahgreekfestival;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockListFragment;

public class Schedule extends SherlockFragment {
	String TAG = this.getTag();
	ActionBar mActionBar;

	private ArrayList<FestivalDay> festival = new ArrayList<FestivalDay>();
	private ArrayList<ActionBar.Tab> tabs = new ArrayList<ActionBar.Tab>();
	private ArrayList<Day> days = new ArrayList<Day>();

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		// Cleanup action bar on fragment exit
		if (mActionBar != null) {
			mActionBar.removeAllTabs();
			mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		}
		Log.d("Schedule","onStop");
	}

	public void buildTabs() {

		Log.d("Schedule","buildTabs");
		mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		for (FestivalDay fd : festival) {
			ActionBar.Tab tab = mActionBar.newTab().setText(fd.getName());
			Day day = new Day();
			day.setName(fd.getName());
			day.setEvents(fd.getEvents());
			tab.setTabListener(new MyTabsListener(day));
			mActionBar.addTab(tab);
			tabs.add(tab);
			days.add(day);
		}
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.d("Schedule","onResume");
		mActionBar.removeAllTabs();
		try {
			festival = buildEventLists();
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		buildTabs();
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		Log.d("Schedule","onCreateView");
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.fragment_pager, container, false);
		try {
			festival = buildEventLists();
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// ActionBar gets initiated
		mActionBar = getSherlockActivity().getSupportActionBar();
		if(mActionBar.getTabCount() == 0)
			buildTabs();
		return v;
	}

	public static class Day extends SherlockListFragment {
		int mNum;
		String myName;
		ArrayList<FestivalEvent> events = null;

		public void setName(String name) {
			myName = name;
		}

		public void setEvents(ArrayList<FestivalEvent> events) {
			this.events = events;

			Log.d("Day","setEvents");
		}

		public String getName() {
			return myName;
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {

			Log.d("Day","onCreateView");
			View v = inflater.inflate(R.layout.scheduletest, container, false);
			View tv = new TextView(this.getSherlockActivity());
			((TextView) tv).setText("Fragment #1 - " + myName);
			Schedule sched = new Schedule();
			if(events == null)
				Log.e("Day","EVENTS IS NULL!!!!!");
			DayListAdapter dla = sched.new DayListAdapter(
					getSherlockActivity(), R.layout.childrow, events);
			this.setListAdapter(dla);
			return v;
		}

		@Override
		public void onListItemClick(ListView l, View v, int position, long id) {
			super.onListItemClick(l, v, position, id);
			/*
			AlertDialog.Builder ad = new AlertDialog.Builder(getSherlockActivity());
			LayoutInflater i = getSherlockActivity().getLayoutInflater();
			View vw = i.inflate(R.layout.schedule_info,null);
			TextView title = (TextView)vw.findViewById(R.id.event_title);
			TextView time = (TextView)vw.findViewById(R.id.event_time);
			TextView loc = (TextView)vw.findViewById(R.id.event_loc);
			TextView desc = (TextView)vw.findViewById(R.id.event_desc);
			title.setText(events.get(position).getTitle());
			time.setText(events.get(position).getTime());
			loc.setText(events.get(position).getLocation());
			desc.setText(events.get(position).getDescription());
			ad.setView(vw);
			ad.show();
			*/
		}
		
		
	}

	class MyTabsListener implements ActionBar.TabListener {
		public Fragment fragment;

		public MyTabsListener(Fragment fragment) {
			this.fragment = fragment;
		}

		@Override
		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			// TODO Auto-generated method stub
			if (fragment == null) {
				Log.v(TAG, "fragment is null");
			}

			if (ft == null) {
				Log.v(TAG, "fragment TRANSACTION is null");
			}

			ft.replace(R.id.fragment_container, fragment);
		}

		@Override
		public void onTabUnselected(Tab tab, FragmentTransaction ft) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onTabReselected(Tab tab, FragmentTransaction ft) {
			// TODO Auto-generated method stub

		}

	}

	private ArrayList<FestivalDay> buildEventLists()
			throws XmlPullParserException, IOException {
		ArrayList<FestivalDay> festDays = new ArrayList<FestivalDay>();
		XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
		factory.setNamespaceAware(true);
		XmlPullParser xpp = factory.newPullParser();
		InputStream is = getResources().openRawResource(R.raw.schedule);
		xpp.setInput(is, "utf-8");
		// check state
		int eventType;
		int arrayNum = 0;
		String tagName = "";
		eventType = xpp.getEventType();
		FestivalDay fd = null;
		while (eventType != XmlPullParser.END_DOCUMENT) {
			// instead of the following if/else if lines
			// you should custom parse your xml
			if (eventType == XmlPullParser.START_DOCUMENT) {
			} else if (eventType == XmlPullParser.START_TAG) {
				tagName = xpp.getName();
				if (tagName.equals("day")) {
					fd = new FestivalDay(xpp.getAttributeValue(null, "name"));
					// festival.add(xpp.getAttributeValue(null, "name"));
					arrayNum++;
				} else if (tagName.equals("event")) {
					FestivalEvent fe = new FestivalEvent();
					fe.setTitle(xpp.getAttributeValue(null, "name"));
					fe.setTime(xpp.getAttributeValue(null, "time"));
					fe.setLocation(xpp.getAttributeValue(null, "location"));
					fe.setDescription(xpp.nextText());
					fd.addEvent(fe);
				}
			} else if (eventType == XmlPullParser.END_TAG) {
				if (xpp.getName().equals("day")) {
					festDays.add(fd);
				}
			} else if (eventType == XmlPullParser.TEXT) {
			}
			eventType = xpp.next();
		}
		return festDays;
	}

	class FestivalDay {

		String name;
		String date;
		String startTime;
		String stopTime;

		ArrayList<FestivalEvent> events = new ArrayList<FestivalEvent>();

		public FestivalDay(String name) {
			this.name = name;
		}

		public FestivalDay(String name, String date) {
			this.name = name;
			this.date = date;
		}

		public FestivalDay(String name, String date, String startTime,
				String stopTime) {
			this.name = name;
			this.date = date;
			this.startTime = startTime;
			this.stopTime = stopTime;
		}

		public String getName() {
			return name;
		}

		public void addEvent(String name) {
			events.add(new FestivalEvent(name));
		}

		public void addEvent(FestivalEvent fe) {
			events.add(fe);
		}

		public ArrayList<FestivalEvent> getEvents() {
			return events;
		}
	}

	private class FestivalEvent {

		String title;
		String time;
		String location;
		String description;

		public FestivalEvent(){
			
		}
		
		public FestivalEvent(String name) {
			this.title = name;
		}

		public void setTitle(String title){
			this.title = title;
		}
		public String getTitle() {
			return title;
		}
		
		public void setTime(String time){
			this.time = time;
		}
		
		public String getTime() {
			return time;
		}
		
		public void setLocation(String location){
			this.location = location;
		}
		
		public String getLocation(){
			return location;
		}
		
		public void setDescription(String description){
			this.description = description;
		}
		
		public String getDescription(){
			return description;
		}
	}

	public class DayListAdapter extends ArrayAdapter<FestivalEvent> {

		private ArrayList<FestivalEvent> events = null;
		private int layoutResourceId = 0;
		private Context mContext = null;

		public DayListAdapter(Context context, int layoutResourceId,
				ArrayList<FestivalEvent> objects) {
			super(context, layoutResourceId, objects);
			events = objects;
			// TODO Auto-generated constructor stub
			this.mContext = context;
			this.layoutResourceId = layoutResourceId;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View row = convertView;
			Holder holder = null;

			if (row == null) {
				LayoutInflater inflater = ((Activity) mContext)
						.getLayoutInflater();
				row = inflater.inflate(layoutResourceId, parent, false);

				holder = new Holder();
				holder.menuTitle = (TextView) row
						.findViewById(R.id.eventlisttext);
				row.setTag(holder);
			} else {
				holder = (Holder) row.getTag();
			}

			holder.menuTitle.setText(events.get(position).getTime()+" - "+events.get(position).getTitle());
			/*
			 * int iconId =
			 * mContext.getResources().getIdentifier(events[position
			 * ].getName().toLowerCase(), "drawable",
			 * mContext.getPackageName()); Drawable d = null; if(iconId == 0)
			 * Log
			 * .e(TAG,"Unable to find drawable resource named "+events[position
			 * ].getName().toLowerCase()); else{ d =
			 * mContext.getResources().getDrawable(iconId);
			 * holder.menuIcon.setImageDrawable(d); }
			 */
			return row;
		}

		class Holder {
			TextView menuTitle;
		}
	}
}