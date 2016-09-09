package com.rgk.RgkQappsFolder;

import java.util.Arrays;
import java.util.List;

import com.rgk.RgkQappsFolder.R;

import android.os.Bundle;

public class GoogleActivity extends BaseActivity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		mPackages = (List<String>) Arrays.asList(getResources().getStringArray(
				R.array.folder_google));
		mDrawable = null;
		mTitle = getResources().getString(R.string.google_activity_name);
		super.onCreate(savedInstanceState);
	}
}