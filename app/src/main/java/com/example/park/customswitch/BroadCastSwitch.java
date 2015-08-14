package com.example.park.customswitch;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.ImageView;

@SuppressLint("NewApi")
@SuppressWarnings("deprecation")
public class BroadCastSwitch extends FrameLayout{
	private Drawable mSwitchTrack, mLeftThum, mRightThum, mLeftBackThum, mRightBackThum;
	private ImageView mThumbImageView;
	private ImageView mTrackImageView;
	private OnSwitchChangeListener changeListener;
	private boolean isSwitchOn = true;
	public BroadCastSwitch(Context context) {
		this(context, null);
	}

	public BroadCastSwitch(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public BroadCastSwitch(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		if(!isInEditMode()){
			init(context, attrs);
		}
	}
	
	private void init(Context context, AttributeSet attrs){
//		TypedArray typedArray = null;
//      try {
//      	typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.BroadCastSwitch);
//      	mSwitchTrack = typedArray.getDrawable(R.styleable.BroadCastSwitch_track);
//      	mLeftThum = typedArray.getDrawable(R.styleable.BroadCastSwitch_leftThumb);
//      	mRightThum = typedArray.getDrawable(R.styleable.BroadCastSwitch_rightThumb);
//      	mLeftBackThum = typedArray.getDrawable(R.styleable.BroadCastSwitch_leftBackThumb);
//      	mRightBackThum = typedArray.getDrawable(R.styleable.BroadCastSwitch_rightBackThumb);
//      } finally {
//          if (typedArray != null) {
//          	typedArray.recycle(); // ensure this is always called
//          }
//      }
      
		mSwitchTrack = context.getResources().getDrawable(R.drawable.toggle_bg);
		mLeftThum = context.getResources().getDrawable(R.drawable.audio_press);
		mLeftBackThum = context.getResources().getDrawable(R.drawable.audio_nor);
		mRightThum = context.getResources().getDrawable(R.drawable.tv_press);
		mRightBackThum = context.getResources().getDrawable(R.drawable.tv_nor);
		
        if(mSwitchTrack != null){
        	if(Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN){
        		setBackground(mSwitchTrack);
        	}else{
        		setBackgroundDrawable(mSwitchTrack);
        	}
        }
        
		LayoutParams params = new LayoutParams((int)getResources().getDimension(R.dimen.gameinfo_broadcast_switch_thumb_width), (int)getResources().getDimension(R.dimen.gameinfo_broadcast_switch_thumb_height));

		LayoutParams params1 = new LayoutParams((int)getResources().getDimension(R.dimen.gameinfo_broadcast_switch_thumb_width), (int)getResources().getDimension(R.dimen.gameinfo_broadcast_switch_thumb_height));
		params1.gravity = Gravity.RIGHT;
		
		//Track이미지
		mTrackImageView = new TrackImageView(context);
		mTrackImageView.setLayoutParams(params1);
		addView(mTrackImageView);
				
		//Thumb이미지
		mThumbImageView = new ThumImageView(context);
		mThumbImageView.setLayoutParams(params);
		addView(mThumbImageView);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(event.getX() - (mThumbImageView.getWidth()/2) < 0 || (event.getX() - (mThumbImageView.getWidth()/2)) > getWidth() - mThumbImageView.getWidth()){
			if(event.getX() - (mThumbImageView.getWidth()/2) < 0 ){
				if(mLeftThum != null){
					if(Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN){
						mThumbImageView.setBackground(mLeftThum);
			        }else{
			        	mThumbImageView.setBackgroundDrawable(mLeftThum);
			        }
				}
				mThumbImageView.setX(0);
				
				if(mRightBackThum != null){
					if(Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN){
						mTrackImageView.setBackground(mRightBackThum);
			        }else{
			        	mTrackImageView.setBackgroundDrawable(mRightBackThum);
			        }
				}
				mTrackImageView.setX(getWidth() - mTrackImageView.getWidth());
				if(!isSwitchOn){
					isSwitchOn = true;
					if (changeListener != null) {
						changeListener.onSwitchChange(isSwitchOn);
					}
				}
			}else{
				if(mRightThum != null){
					if(Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN){
						mThumbImageView.setBackground(mRightThum);
					}else{
						mThumbImageView.setBackgroundDrawable(mRightThum);
					}
				}
				mThumbImageView.setX(getWidth() - mThumbImageView.getWidth());
				
				if(mLeftBackThum != null){
					if(Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN){
						mTrackImageView.setBackground(mLeftBackThum);
			        }else{
			        	mTrackImageView.setBackgroundDrawable(mLeftBackThum);
			        }
				}
				mTrackImageView.setX(0);
				if(isSwitchOn){
					isSwitchOn = false;
					if (changeListener != null) {
						changeListener.onSwitchChange(isSwitchOn);
					}
				}
			}
			
			return true;
		}
		
		switch(event.getAction()){
		case MotionEvent.ACTION_CANCEL : 
		case MotionEvent.ACTION_UP :
			if(event.getX() < getWidth()/2){
				if(mLeftThum != null){
					if(Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN){
						mThumbImageView.setBackground(mLeftThum);
					}else{
						mThumbImageView.setBackgroundDrawable(mLeftThum);
					}
				}
				mThumbImageView.setX(0);
				
				if(mRightBackThum != null){
					if(Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN){
						mTrackImageView.setBackground(mRightBackThum);
			        }else{
			        	mTrackImageView.setBackgroundDrawable(mRightBackThum);
			        }
				}
				mTrackImageView.setX(getWidth() - mTrackImageView.getWidth());
				if(!isSwitchOn){
					isSwitchOn = true;
					if (changeListener != null) {
						changeListener.onSwitchChange(isSwitchOn);
					}
				}
			}else{
				if(mRightThum != null){
					if(Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN){
						mThumbImageView.setBackground(mRightThum);
					}else{
						mThumbImageView.setBackgroundDrawable(mRightThum);
					}
				}
				mThumbImageView.setX(getWidth() - mThumbImageView.getWidth());
				
				if(mLeftBackThum != null){
					if(Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN){
						mTrackImageView.setBackground(mLeftBackThum);
			        }else{
			        	mTrackImageView.setBackgroundDrawable(mLeftBackThum);
			        }
				}
				mTrackImageView.setX(0);
				if(isSwitchOn){
					isSwitchOn = false;
					if (changeListener != null) {
						changeListener.onSwitchChange(isSwitchOn);
					}
				}
			}
			break;
		case MotionEvent.ACTION_MOVE :
			mThumbImageView.setX(event.getX() - (mThumbImageView.getWidth()/2));
			break;
		}
		return true;
	}
	
	public interface OnSwitchChangeListener{
		public void onSwitchChange(boolean isOn);
	}
	
	class ThumImageView extends ImageView{
		
		public ThumImageView(Context context) {
			super(context);
			if(!isInEditMode()){
				init(context);
			}
		}

		public ThumImageView(Context context, AttributeSet attrs, int defStyle) {
			super(context, attrs, defStyle);
			if(!isInEditMode()){
				init(context);
			}
		}

		public ThumImageView(Context context, AttributeSet attrs) {
			super(context, attrs);
			if(!isInEditMode()){
				init(context);
			}
		}
		
		private void init(Context context){
			setBackgroundResource(R.drawable.tv_press);
		}
	}
	
	public void setOnSwitChangeListener(OnSwitchChangeListener listener){
		changeListener = listener;
	}
	
	class TrackImageView extends ImageView{
		
		public TrackImageView(Context context) {
			super(context);
			if(!isInEditMode()){
				init(context);
			}
		}

		public TrackImageView(Context context, AttributeSet attrs, int defStyle) {
			super(context, attrs, defStyle);
			if(!isInEditMode()){
				init(context);
			}
		}

		public TrackImageView(Context context, AttributeSet attrs) {
			super(context, attrs);
			if(!isInEditMode()){
				init(context);
			}
		}
		
		private void init(Context context){
			setBackgroundResource(R.drawable.audio_nor);
		}
	}
}
