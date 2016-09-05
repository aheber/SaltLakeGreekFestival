package com.saltlakegreekfestival.utahgreekfestival;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aquifyre.saltlakegreekfestival.R;

public class Coupons extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.coupon,container,false);

		return v;
	}
	
}

