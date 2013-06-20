package com.saltlakegreekfestival.utahgreekfestival;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

/**
 * Pieced together from: Android samples:
 * com.example.android.apis.view.ExpandableList1
 * http://androidword.blogspot.com/2012/01/how-to-use-expandablelistview.html
 * http://stackoverflow.com/questions/6938560/android-fragments-setcontentview-
 * alternative
 * http://stackoverflow.com/questions/6495898/findviewbyid-in-fragment-android
 */
public class Schedule extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.schedule, null);
		ExpandableListView elv = (ExpandableListView) v
				.findViewById(R.id.thursched);
		elv.setAdapter(new SavedTabsListAdapter());
		return v;
	}

	public class SavedTabsListAdapter extends BaseExpandableListAdapter {

		private ArrayList<String> sched_days = new ArrayList<String>();

		private ArrayList<ArrayList<String>> children = new ArrayList<ArrayList<String>>();

		public SavedTabsListAdapter() {

			try {
				//parse the raw.schedule.xml file and populate the ArrayLists
				buildEventLists();
			} catch (XmlPullParserException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		@Override
		public int getGroupCount() {
			return sched_days.size();
		}

		@Override
		public int getChildrenCount(int i) {
			return children.get(i).size();
		}

		@Override
		public Object getGroup(int i) {
			return sched_days.get(i);
		}

		@Override
		public Object getChild(int i, int i1) {
			return children.get(i).get(i1);
		}

		@Override
		public long getGroupId(int i) {
			return i;
		}

		@Override
		public long getChildId(int i, int i1) {
			return i1;
		}

		@Override
		public boolean hasStableIds() {
			return true;
		}

		@Override
		public View getGroupView(int i, boolean b, View view,
				ViewGroup viewGroup) {
			LayoutInflater inflater = LayoutInflater.from(getActivity());
	        View eventday = inflater.inflate(R.layout.parentrow, null);
	        TextView textday = (TextView)eventday.findViewById(R.id.eventtext);
			textday.setText(getGroup(i).toString());
			return eventday;
		}

		@Override
		public View getChildView(int i, int i1, boolean b, View view,
				ViewGroup viewGroup) {
			TextView textView = new TextView(Schedule.this.getActivity());
			textView.setText(getChild(i, i1).toString());
			return textView;
		}

		@Override
		public boolean isChildSelectable(int i, int i1) {
			return true;
		}
		
		private void buildEventLists() throws XmlPullParserException, IOException{
			XmlPullParserFactory factory = XmlPullParserFactory
					.newInstance();
			factory.setNamespaceAware(true);
			XmlPullParser xpp = factory.newPullParser();
			InputStream is = getResources().openRawResource(R.raw.schedule);
			xpp.setInput(is, "utf-8");
			// check state
			int eventType;
			int arrayNum = 0;
			String tagName = "";
			eventType = xpp.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				// instead of the following if/else if lines
				// you should custom parse your xml
				if (eventType == XmlPullParser.START_DOCUMENT) {
					Log.d("SCHED", "Start document");
				} else if (eventType == XmlPullParser.START_TAG) {
					tagName = xpp.getName();
					Log.d("SCHED", "Start tag " + tagName);
					if (tagName.equals("day")) {
						sched_days.add(xpp.getAttributeValue(null, "name"));
						arrayNum++;
					} else if (tagName.equals("event")) {
						ArrayList<String> myList;
						if (children.size() >= arrayNum + 1) {
							myList = children.get(arrayNum - 1);
							myList.add(xpp.getAttributeValue(null, "name"));
							children.set(arrayNum - 1, myList);
						} else {
							myList = new ArrayList<String>();
							myList.add(xpp.getAttributeValue(null, "name"));
							children.add(myList);
						}
					}
				} else if (eventType == XmlPullParser.END_TAG) {
					Log.d("SCHED", "End tag " + xpp.getName());
				} else if (eventType == XmlPullParser.TEXT) {
					Log.d("SCHED", "Text " + xpp.getText());
				}
				eventType = xpp.next();
			}			
		}
	}
}