package com.rgk.RgkQappsFolder;

import android.content.Intent;
import android.graphics.drawable.Drawable;

public class ItemInfo {

	private String title;
	private String packName;
	public String getPackName() {
		return packName;
	}

	public void setPackName(String packName) {
		this.packName = packName;
	}

	private Drawable icon;
	private Intent intent;

	public ItemInfo() {
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Drawable getIcon() {
		return icon;
	}

	public void setIcon(Drawable icon) {
		this.icon = icon;
	}

	public Intent getIntent() {
		return intent;
	}

	public void setIntent(Intent intent) {
		this.intent = intent;
	}
}