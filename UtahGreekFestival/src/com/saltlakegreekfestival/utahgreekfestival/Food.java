package com.saltlakegreekfestival.utahgreekfestival;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;

public class Food extends SherlockFragment implements OnItemClickListener {

	// references to our images
	private Integer[] mThumbIds = { R.drawable.sample_2,
			R.drawable.sample_3, R.drawable.sample_4, R.drawable.sample_5,
			R.drawable.sample_6, R.drawable.sample_7, R.drawable.sample_0,
			R.drawable.sample_1, R.drawable.sample_2, R.drawable.sample_3,
			R.drawable.sample_4, R.drawable.sample_5, R.drawable.sample_6,
			R.drawable.sample_7, R.drawable.sample_0, R.drawable.sample_1,
			R.drawable.sample_2, R.drawable.sample_3, R.drawable.sample_4,
			R.drawable.sample_5, R.drawable.sample_6, R.drawable.sample_7, 
			R.drawable.sample_0, R.drawable.sample_1,
			R.drawable.sample_2, R.drawable.sample_3, R.drawable.sample_4,
			R.drawable.sample_5, R.drawable.sample_6, R.drawable.sample_7, 
			R.drawable.sample_0, R.drawable.sample_1,
			R.drawable.sample_2, R.drawable.sample_3, R.drawable.sample_4,
			R.drawable.sample_5, R.drawable.sample_6, R.drawable.sample_7 };
			
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.foodgrid,container,false);
		GridView gridview = (GridView) v.findViewById(R.id.gridview);
		gridview.setAdapter(new ImageAdapter(getSherlockActivity()));

		gridview.setOnItemClickListener(this);
		return v;
	}

	public class ImageAdapter extends BaseAdapter {
		private Context mContext;

		public ImageAdapter(Context c) {
			mContext = c;
		}

		public int getCount() {
			return mThumbIds.length;
		}

		public Object getItem(int position) {
			return null;
		}

		public long getItemId(int position) {
			return 0;
		}

		// create a new ImageView for each item referenced by the Adapter
		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView imageView;
			if (convertView == null) { // if it's not recycled, initialize some
										// attributes
				imageView = new ImageView(mContext);
				imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
				imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
				imageView.setPadding(8, 8, 8, 8);
			} else {
				imageView = (ImageView) convertView;
			}

			imageView.setImageResource(mThumbIds[position]);
			return imageView;
		}


	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		AlertDialog.Builder ad = new AlertDialog.Builder(getSherlockActivity());
		LayoutInflater i = getSherlockActivity().getLayoutInflater();
		View vw = i.inflate(R.layout.foodinfo,null);
		TextView name = (TextView)vw.findViewById(R.id.foodname);
		TextView price = (TextView)vw.findViewById(R.id.price);
		TextView loc = (TextView)vw.findViewById(R.id.location);
		ImageView img = (ImageView)vw.findViewById(R.id.foodimage);
		name.setText(mThumbIds[position]);
		price.setText(mThumbIds[position]);
		loc.setText(mThumbIds[position]);
		img.setImageResource(mThumbIds[position]);
		ad.setView(vw);
		ad.show();
	}
}