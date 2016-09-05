package com.saltlakegreekfestival.utahgreekfestival;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.aquifyre.saltlakegreekfestival.R;

public class MenuAdapter extends ArrayAdapter<String> {

	Context mContext;
	int layoutResourceId = 0;
	String[] data = null;
	static String TAG = "MenuAdapter";

	public MenuAdapter(Context context, int layoutResourceId,
			String[] data) {
		super(context, layoutResourceId, data);
		this.layoutResourceId = layoutResourceId;
		this.mContext = context;
		this.data = data;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		Holder holder = null;

		if (row == null) {
			LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);

			holder = new Holder();
			holder.menuTitle = (TextView) row.findViewById(R.id.menutitle);
			holder.menuIcon = (ImageView) row.findViewById(R.id.menuicon);
			row.setTag(holder);
		} else {
			holder = (Holder) row.getTag();
		}

		holder.menuTitle.setText(data[position]);
		int iconId = mContext.getResources().getIdentifier(data[position].toLowerCase(), "drawable", mContext.getPackageName());
		Drawable d = null;
		if(iconId == 0)
			Log.e(TAG,"Unable to find drawable resource named "+data[position].toLowerCase());
		else{
			d = mContext.getResources().getDrawable(iconId);
			holder.menuIcon.setImageDrawable(d);
		}
		return row;
	}

	static class Holder {
		TextView menuTitle;
		ImageView menuIcon;
	}

}
