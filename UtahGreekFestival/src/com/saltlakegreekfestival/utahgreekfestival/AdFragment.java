package com.saltlakegreekfestival.utahgreekfestival;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class AdFragment extends Fragment {
	
	Drawable myImage;
	//int myImageId = R.drawable.roofers;
	int myImageId = 0;
	String TAG = "AdFragment";
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.adfragmentlayout, null);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Log.v(TAG,"Created ImageId: "+String.valueOf(myImageId));
		this.myImage = this.getActivity().getResources().getDrawable(myImageId);
		ImageView image = (ImageView)this.getView().findViewById(R.id.advertimage);
		image.setImageDrawable(this.myImage);
	}

	public AdFragment() {
		
	}
	
	public void setDrawable(Drawable myImage){
		//ImageView image = (ImageView)myLayout.findViewById(R.id.advertimage);
		//image.setImageDrawable(myImage);
	}
	
	public void setDrawable(int myImage){
		Log.v(TAG,"Image Id:"+String.valueOf(myImage));
		this.myImageId = myImage;

		Log.v(TAG,"Image Id:"+String.valueOf(this.myImageId));
	}
	
	public Drawable getDrawable(){
		return this.myImage;
	}
	
	
	

}
