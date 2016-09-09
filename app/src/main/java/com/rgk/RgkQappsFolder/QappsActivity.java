package com.rgk.RgkQappsFolder;

import java.util.Arrays;
import java.util.List;

import android.os.Bundle;
import android.util.Log;

public class QappsActivity extends BaseActivity {
	 final String TAG="RgkQappsFolderLOG";
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.i(TAG,"QappsActivity + onCreate");
		mPackages = (List<String>) Arrays.asList(getResources().getStringArray(
				R.array.folder_qapps));
		mDrawable = getResources().getDrawable(R.drawable.ic_title_qapps);
		mTitle = getResources().getString(R.string.qapps_activity_title);
		super.onCreate(savedInstanceState);
	}
}