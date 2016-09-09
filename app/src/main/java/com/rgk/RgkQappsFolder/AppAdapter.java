package com.rgk.RgkQappsFolder;

import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AppAdapter extends BaseAdapter {

	private List<ItemInfo> mlistAppInfo = null;
	static String paknameString=null;

	LayoutInflater infater = null;
	private Context mContext;
	private CreatShortcut  creatShortcut=null;

	public AppAdapter(Context context, List<ItemInfo> apps) {
		infater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mContext = context;
		mlistAppInfo = apps;
		creatShortcut =new CreatShortcut();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mlistAppInfo.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mlistAppInfo.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertview, ViewGroup arg2) {
		View view = null;
		ViewHolder holder = null;
		if (convertview == null || convertview.getTag() == null) {
			view = infater.inflate(R.layout.item, null);
			holder = new ViewHolder(view);
			view.setTag(holder);
		} else {
			view = convertview;
			holder = (ViewHolder) convertview.getTag();
		}
		final ItemInfo appInfo = (ItemInfo) getItem(position);
		holder.icon.setImageDrawable(appInfo.getIcon());
		holder.title.setText(appInfo.getTitle());

		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mContext.startActivity(appInfo.getIntent());
			}
		});
		view.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View arg0) {
				// TODO Auto-generated method stub
				
				paknameString=appInfo.getPackName();
				return true;
			}
		});
		return view;
	}
	
	class ViewHolder {
		ImageView icon;
		TextView title;

		public ViewHolder(View view) {
			this.icon = (ImageView) view.findViewById(R.id.icon);
			this.title = (TextView) view.findViewById(R.id.title);
		}
	}
}