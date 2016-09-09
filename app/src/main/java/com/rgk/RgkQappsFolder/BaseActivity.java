package com.rgk.RgkQappsFolder;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class BaseActivity extends Activity {

	protected List<String> mPackages;
	protected static Drawable mDrawable;
	protected static String mTitle;
	static ArrayList<ItemInfo> mlistAppInfo = new ArrayList<ItemInfo>();
	static TextView  title=null;
	final String TAG="RgkQappsFolderLOG";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.i(TAG,"BaseActivity + onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		title = (TextView) findViewById(R.id.maintitle);
		if(mDrawable!=null)
		mDrawable.setBounds(0, 0, mDrawable.getMinimumWidth(), mDrawable.getMinimumHeight());
		title.setText(mTitle);
		title.setCompoundDrawables(mDrawable, null, null, null);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.i(TAG,"BaseActivity + onResume");
		initInfo();
		DragGridView gridview = (DragGridView) findViewById(R.id.gridview);
		AppAdapter adapter = new AppAdapter(this, mlistAppInfo);
		gridview.setAdapter(adapter);
	}
	private void initInfo() {
		Log.i(TAG,"initInfo");
		if (mPackages == null) {
			return;
		}
		mlistAppInfo.clear();
		final PackageManager pm = getPackageManager();
		final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
		mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		List<ResolveInfo> apps = pm.queryIntentActivities(mainIntent, 0);
		for (ResolveInfo app : apps) {
			if (mPackages.contains(app.activityInfo.packageName)) {
				Log.i(TAG,"initInfo + app.activityInfo.packageName:"+app.activityInfo.packageName);
				ItemInfo itemInfo = new ItemInfo();
				itemInfo.setPackName(app.activityInfo.packageName);
				itemInfo.setTitle((String) app.loadLabel(pm));
				itemInfo.setIcon(app.loadIcon(pm));
				itemInfo.setIntent(new Intent().setComponent(new ComponentName(
						app.activityInfo.packageName, app.activityInfo.name)));
				mlistAppInfo.add(itemInfo);
			}
		}
	}
}
