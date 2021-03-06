package com.saltlakegreekfestival.utahgreekfestival;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import android.app.Fragment;
import android.app.ListFragment;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.aquifyre.saltlakegreekfestival.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

public class SlideoutMenu extends ListFragment {

	MarkerOptions[] mos;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.list, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		String[] colors = getResources().getStringArray(R.array.color_names);
		MenuAdapter colorAdapter = new MenuAdapter(getActivity(), 
				R.layout.menu_list_item, colors);
		setListAdapter(colorAdapter);
	}

	@Override
	public void onListItemClick(ListView lv, View v, int position, long id) {
		Fragment newContent = null;
		switch (position) {
		case 0:
		    newContent = new Schedule();
			break;
		//case 1:
			//newContent = buildMap();
			//break;
		case 1:
			newContent = new Food();
			break;
		case 2:
			newContent = new Coupons();
			break;
		case 3: 
			newContent = new Race();
			break;
		}
		if (newContent != null)
			switchFragment(newContent);
	}

	// the meat of switching the above fragment
	private void switchFragment(Fragment fragment) {
		if (getActivity() == null)
			return;
		MainActivity fca = (MainActivity) getActivity();
		fca.switchContent(fragment);
	}

	private Fragment buildMap() {
		GoogleMapOptions mGMapOpts = new GoogleMapOptions();
		TypedValue tempVal = new TypedValue();
		getResources().getValue(R.dimen.cameraStartLat, tempVal, true);
		float cameraStartLat = tempVal.getFloat();
		getResources().getValue(R.dimen.cameraStartLng, tempVal, true);
		float cameraStartLng = tempVal.getFloat();
		getResources().getValue(R.dimen.cameraStartZoom, tempVal, true);
		float cameraStartZoom = tempVal.getFloat();
		mGMapOpts.camera(CameraPosition.fromLatLngZoom(new LatLng(cameraStartLat, cameraStartLng), cameraStartZoom));
		
		MapFragment myMap = MapFragment.newInstance(mGMapOpts);
		//pre-commit map to allow us to capture it and add markers.
		switchFragment(myMap);
		GoogleMap map = myMap.getMap();
		Gson gson = new Gson();
		BufferedReader br = new BufferedReader(new InputStreamReader(getResources().openRawResource(R.raw.festivalmap)));
		//only load json maps the first time
		if(mos==null)
			mos = gson.fromJson(br,MarkerOptions[].class);
		for(int i=0;i < mos.length; i++){
			map.addMarker(mos[i]);
		}
		return myMap;
	}
}