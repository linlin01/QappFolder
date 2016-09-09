package com.rgk.RgkQappsFolder;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class DragGridView extends GridView{

	private long dragResponseMS = 500;
	private boolean isDrag = false;
	private boolean ChangeTitle=false;
	
	private int mDownX;
	private int mDownY;
	private int moveX;
	private int moveY;
	private int mDragPosition;
	private View mStartDragItemView = null;
	private ImageView mDragImageView;
	private WindowManager mWindowManager;
	private WindowManager.LayoutParams mWindowLayoutParams;
	private Bitmap mDragBitmap;
	private int mPoint2ItemTop ; 
	private int mPoint2ItemLeft;
	private int mOffset2Top;
	private int mOffset2Left;
	private int mStatusHeight;
	private Context mContext=null;
	
	private CreatShortcut  creatShortcut=null;
	
	public DragGridView(Context context) {
		this(context, null);
	}
	
	public DragGridView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public DragGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		mStatusHeight = getStatusHeight(context); 
		mContext=context;
	}

	private Handler mHandler = new Handler();
	
	private Runnable mLongClickRunnable = new Runnable() {

		@Override
		public void run() {
			mStartDragItemView = getChildAt(mDragPosition - getFirstVisiblePosition());
			mPoint2ItemTop = mDownY - mStartDragItemView.getTop();
			mPoint2ItemLeft = mDownX - mStartDragItemView.getLeft();
			mStartDragItemView.setDrawingCacheEnabled(true);
			mDragBitmap = Bitmap.createBitmap(mStartDragItemView.getDrawingCache());
			mStartDragItemView.destroyDrawingCache();
			isDrag = true; 
			//mStartDragItemView.setVisibility(View.INVISIBLE);
			createDragImage(mDragBitmap, mDownX, mDownY);
		}
	};
	public void setDragResponseMS(long dragResponseMS) {
		this.dragResponseMS = dragResponseMS;
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		switch(ev.getAction()){
		case MotionEvent.ACTION_DOWN:
			
			mDownX = (int) ev.getX();
			mDownY = (int) ev.getY();
			mDragPosition = pointToPosition(mDownX, mDownY);
			
			if(mDragPosition == AdapterView.INVALID_POSITION){
				return super.dispatchTouchEvent(ev);
			}
			mHandler.postDelayed(mLongClickRunnable, dragResponseMS);

			mOffset2Top = (int) (ev.getRawY() - mDownY);
			mOffset2Left = (int) (ev.getRawX() - mDownX);

			break;
		case MotionEvent.ACTION_MOVE:
			if(isDrag){
				moveX = (int)ev.getX();
				moveY = (int) ev.getY();
				onDragItem(moveX, moveY);
				if(!isTouchInItem(mStartDragItemView, moveX, moveY)){
					mHandler.removeCallbacks(mLongClickRunnable);
				}
				if (moveY<BaseActivity.title.getHeight()) {
					if (ChangeTitle) {
						BaseActivity.title.setTextColor(Color.rgb(255, 0, 0));
						ChangeTitle=!ChangeTitle;
					}
				}else if (!ChangeTitle) {
					if (BaseActivity.title!=null) {
						BaseActivity.title.setCompoundDrawables(null, null, null, null);
						BaseActivity.title.setText(R.string.creat_shortcut_string);
						BaseActivity.title.setTextColor(Color.rgb(255, 255, 255));
					}
					ChangeTitle=!ChangeTitle;
				}
			}
			break;
		case MotionEvent.ACTION_UP:
			if(isDrag){
				onStopDrag();
				isDrag = false;
			}
			if(moveY<BaseActivity.title.getHeight() &&
					BaseActivity.title.getText().equals(mContext.getResources().getString(R.string.creat_shortcut_string)) ){
				// creat short
				creatShortcut =new CreatShortcut();
				ItemInfo  itemInfo = BaseActivity.mlistAppInfo.get(mDragPosition - getFirstVisiblePosition());
				if(itemInfo.getTitle().equals("Voice Search")){
					creatShortcut.createShortCutByiteninfo(mContext,itemInfo, R.drawable.ic_launcher_voicesearch,itemInfo.getTitle());
				}else if(itemInfo.getTitle().equals("Google")){
					creatShortcut.createShortCutByiteninfo(mContext,itemInfo, R.drawable.ic_launcher_google_search,"Google");
				} else if(itemInfo.getTitle().equals("Google Settings")){
					creatShortcut.createShortCutByiteninfo(mContext,itemInfo, R.drawable.google_settings_icon,itemInfo.getTitle());
				} else{
					creatShortcut.addShortcut(getContext(), AppAdapter.paknameString);
				}
			}
			BaseActivity.title.setCompoundDrawables(BaseActivity.mDrawable, null, null, null);
			BaseActivity.title.setText(BaseActivity.mTitle);
			BaseActivity.title.setTextColor(Color.rgb(255, 255, 255));
			ChangeTitle=false;
			mHandler.removeCallbacks(mLongClickRunnable);
			break;
		}
		return super.dispatchTouchEvent(ev);
	}
	
	private boolean isTouchInItem(View dragView, int x, int y){
		if(dragView==null) return false;
		int leftOffset = dragView.getLeft();
		int topOffset = dragView.getTop();
		if(x < leftOffset || x > leftOffset + dragView.getWidth()){
			return false;
		}
		
		if(y < topOffset || y > topOffset + dragView.getHeight()){
			return false;
		}
		
		return true;
	}
	
	private void createDragImage(Bitmap bitmap, int downX , int downY){
		mWindowLayoutParams = new WindowManager.LayoutParams();
		mWindowLayoutParams.format = PixelFormat.TRANSLUCENT; 
		mWindowLayoutParams.gravity = Gravity.TOP | Gravity.LEFT;
		mWindowLayoutParams.x = downX - mPoint2ItemLeft + mOffset2Left;
		mWindowLayoutParams.y = downY - mPoint2ItemTop + mOffset2Top - mStatusHeight;
		mWindowLayoutParams.alpha = 0.8f; 
		mWindowLayoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;  
		mWindowLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;  
		mWindowLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE  
	                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE ;
		  
		mDragImageView = new ImageView(getContext());  
		mDragImageView.setImageBitmap(bitmap);  
		mWindowManager.addView(mDragImageView, mWindowLayoutParams);  
	}

	private void removeDragImage(){
		if(mDragImageView != null){
			mWindowManager.removeView(mDragImageView);
			mDragImageView = null;
		}
	}

	private void onDragItem(int moveX, int moveY){
		mWindowLayoutParams.x = moveX - mPoint2ItemLeft + mOffset2Left;
		mWindowLayoutParams.y = moveY - mPoint2ItemTop + mOffset2Top - mStatusHeight;
		mWindowManager.updateViewLayout(mDragImageView, mWindowLayoutParams); 
		
	}

	private void onStopDrag(){
		getChildAt(mDragPosition - getFirstVisiblePosition()).setVisibility(View.VISIBLE);
		removeDragImage();
	}

	private static int getStatusHeight(Context context){
        int statusHeight = 0;
        Rect localRect = new Rect();
        ((Activity) context).getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
        statusHeight = localRect.top;
        if (0 == statusHeight){
            Class<?> localClass;
            try {
                localClass = Class.forName("com.android.internal.R$dimen");
                Object localObject = localClass.newInstance();
                int i5 = Integer.parseInt(localClass.getField("status_bar_height").get(localObject).toString());
                statusHeight = context.getResources().getDimensionPixelSize(i5);
            } catch (Exception e) {
                e.printStackTrace();
            } 
        }
        return statusHeight;
    }
}
