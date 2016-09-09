package com.rgk.RgkQappsFolder;

import java.util.List;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.Intent.ShortcutIconResource;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Parcelable;
import android.widget.Toast;

public class CreatShortcut {
	final String INSTALL_SHORTCUT_ACTION="com.android.launcher.action.INSTALL_SHORTCUT";

	public boolean addShortcut(Context context, String pakName) {
		if (pakName==null) {
			return false;
		}
	    String title = "unknown";
	    String mainAct = null;
	    int iconIdentifier = 0;
	    PackageManager pkgMag = context.getPackageManager();
	    Intent queryIntent = new Intent(Intent.ACTION_MAIN, null);
	    queryIntent.addCategory(Intent.CATEGORY_LAUNCHER);
	    List<ResolveInfo> list = pkgMag.queryIntentActivities(queryIntent,
	            PackageManager.GET_ACTIVITIES);
	    for (int i = 0; i < list.size(); i++) {
	        ResolveInfo info = list.get(i);
	        if (info.activityInfo.packageName.equals(pakName)) {
	            title = info.loadLabel(pkgMag).toString();
	            mainAct = info.activityInfo.name;
	            iconIdentifier = info.activityInfo.applicationInfo.icon;
	            break;
	        }
	    }
	    if (mainAct == null) {
	        return false;
	    }
	    Intent shortcutIntent = new Intent(INSTALL_SHORTCUT_ACTION);
	    shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, title);
	    shortcutIntent.putExtra("duplicate", true);
	    ComponentName comp = new ComponentName(pakName, mainAct);
	    shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT,
	            new Intent(Intent.ACTION_MAIN).setComponent(comp));
	    Context pkgContext = null;  
	    if (context.getPackageName().equals(pakName)) {
	        pkgContext = context;  
	    } else {  
	        try {  
	            pkgContext = context.createPackageContext(pakName, Context.CONTEXT_IGNORE_SECURITY  
	                    | Context.CONTEXT_INCLUDE_CODE);
	        } catch (NameNotFoundException e) {
	            e.printStackTrace();
	        }
	    }  
	    if (pkgContext != null) {
	        ShortcutIconResource iconRes = ShortcutIconResource.fromContext(pkgContext,
	                iconIdentifier);
	        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconRes);
	    }  
	    context.sendBroadcast(shortcutIntent);
	    Toast.makeText(context, context.getString(R.string.shortcut_string), Toast.LENGTH_SHORT).show();
	    return true;
	}
	public void createShortCutByiteninfo(Context context,ItemInfo itemInfo,int iconID,String titleString){
	    //creat shortout Intent
	    Intent shortcutintent = new Intent(INSTALL_SHORTCUT_ACTION);
	    shortcutintent.putExtra("duplicate", false);
	    //the title
	    shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_NAME, titleString);
	    //the icon
	    //Parcelable icon = (Parcelable) itemInfo.getIcon();
	    Parcelable icon =  ShortcutIconResource.fromContext(context,iconID);;
	    shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);

	    shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, itemInfo.getIntent());
	    //sendBroadcast
	    context.sendBroadcast(shortcutintent);
	    Toast.makeText(context, context.getString(R.string.shortcut_string), Toast.LENGTH_SHORT).show();
	}
}
