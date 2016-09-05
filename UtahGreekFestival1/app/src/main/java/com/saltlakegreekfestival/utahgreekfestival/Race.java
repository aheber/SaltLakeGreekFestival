package com.saltlakegreekfestival.utahgreekfestival;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.aquifyre.saltlakegreekfestival.R;

public class Race extends SherlockFragment{
	
	TextView registrationlink;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.race,container,false);
		registrationlink = (TextView) v.findViewById(R.id.registrationlink);
		
		registrationlink.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent RunnerCard = new Intent(Intent.ACTION_VIEW,Uri.parse("https://www.runnercard.com/roadrace/public/raceGroup/975244"));
				startActivity(RunnerCard);
			}
			});
		
		return v;
		
		
	}

	
	
	
}

