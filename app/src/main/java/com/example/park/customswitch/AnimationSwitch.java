package com.example.park.customswitch;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.ImageView;

/**
 * Created by park on 2015-06-03.
 */
public class AnimationSwitch extends FrameLayout {
    private static int SPEED = 5;
    private boolean isDrag = false;
    private ImageView mSwitchIv;
    private FrameLayout.LayoutParams mSwitchParam;
    private long mTouchTime;

    private OnSwitchChangeListener mOnSwitchChangeListener;

    private enum SwitchStatus {
        ON(0), OFF(1);
        private int value;

        SwitchStatus(int value) {
            this.value = value;
        }
    }

    public AnimationSwitch(Context context) {
        super(context);
        if(!isInEditMode()) {
            init(context);
        }
    }

    public AnimationSwitch(Context context, AttributeSet attrs) {
        super(context, attrs);
        if(!isInEditMode()) {
            init(context);
        }
    }

    public AnimationSwitch(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if(!isInEditMode()) {
            init(context);
        }
    }

    private void init(Context context) {
        mSwitchIv = new Switch(context);

        mSwitchParam = new FrameLayout.LayoutParams(getResources().getDimensionPixelSize(R.dimen.dp_24), getResources().getDimensionPixelSize(R.dimen.dp_24));
        mSwitchParam.gravity = Gravity.CENTER_VERTICAL;
        mSwitchIv.setLayoutParams(mSwitchParam);

        setSwitchBackgroundRes(R.drawable.switch_bg);

        setSwitchRes(R.drawable.switch_off);

        addView(mSwitchIv);
    }

    /**
     * Switch BackGround 이미지 변경
     * @param resId
     */
    public void setSwitchBackgroundRes(int resId) {
        setBackgroundResource(resId);
    }

    /**
     * Switch 이미지 변경
     * @param resId
     */
    public void setSwitchRes(int resId) {
        mSwitchIv.setBackgroundResource(resId);
    }

    /**
     * Switch Change Listener 등록
     * @param onSwitchChangeListener
     */
    public void setOnSwitchChangeListener(OnSwitchChangeListener onSwitchChangeListener) {
        this.mOnSwitchChangeListener = onSwitchChangeListener;
    }

    /**
     * Switch Size 크기 변경
     * @param width
     * @param height
     */
    public void setSwitchSize(int width, int height) {
        mSwitchParam.width = width;
        mSwitchParam.height = height;

        mSwitchIv.setLayoutParams(mSwitchParam);
    }

    /**
     * Switch X 위치 변경
     * API11 이전은 setLayoutParams을 사용
     * @param x
     */
    private void setSwitchX(float x) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
            mSwitchIv.setX(x);
        }else{
            mSwitchParam.leftMargin = (int) x;
            mSwitchIv.setLayoutParams(mSwitchParam);
        }

        if (x >= (getWidth() - mSwitchIv.getWidth()) / 2) {
            setSwitchRes(R.drawable.switch_on);
        }else {
            setSwitchRes(R.drawable.switch_off);
        }
    }

    private float getSwitchX() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
            return mSwitchIv.getX();
        }else{
            return mSwitchParam.leftMargin;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mTouchTime = System.currentTimeMillis();
                isDrag = false;
                break;
            case MotionEvent.ACTION_UP:
                if (isDrag) {
                    if(getSwitchX() >= (getWidth() - mSwitchIv.getWidth()) / 2) {
                        sendHadleMsg(SwitchStatus.ON);
                    } else {
                        sendHadleMsg(SwitchStatus.OFF);
                    }
                }else {
                    if (getSwitchX() == 0) {
                        sendHadleMsg(SwitchStatus.ON);
                    } else  {
                        sendHadleMsg(SwitchStatus.OFF);
                    }
                }

                break;
            case MotionEvent.ACTION_MOVE:
                if (System.currentTimeMillis() > mTouchTime + 100) {
                    isDrag = true;

                    if (event.getX() > (mSwitchIv.getWidth() / 2) && (event.getX() - (mSwitchIv.getWidth() / 2) < getWidth() - mSwitchIv.getWidth())) {
                        setSwitchX(event.getX() - (mSwitchIv.getWidth() / 2));
                    } else if (event.getX() < (mSwitchIv.getWidth() / 2)) {
                        setSwitchX(0);
                    } else {
                        setSwitchX(getWidth() - mSwitchIv.getWidth());
                    }
                }
                break;
        }
        return true;
    }

    private void sendHadleMsg(SwitchStatus switchStatus) {
        Message message = Message.obtain();
        message.obj = switchStatus;
        mSwitchHandler.sendMessage(message);
    }

    /**
     * Switch Animation Handler
     */
    Handler mSwitchHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            SwitchStatus switchStatus = (SwitchStatus) msg.obj;
            switch (switchStatus) {
                case OFF :
                    if (getSwitchX() > 0) {
                        setSwitchX(getSwitchX() - SPEED);
                        sendHadleMsg(switchStatus);
                    } else {
                        setSwitchX(0);

                        if (mOnSwitchChangeListener != null) {
                            mOnSwitchChangeListener.onSwitchChange(false);
                        }
                    }
                    break;
                case ON :
                    if (getSwitchX() <= getWidth() - mSwitchIv.getWidth()) {
                        setSwitchX(getSwitchX() + SPEED);
                        sendHadleMsg(switchStatus);
                    } else {
                        setSwitchX(getWidth() - mSwitchIv.getWidth());
                        if (mOnSwitchChangeListener != null) {
                            mOnSwitchChangeListener.onSwitchChange(true);
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    };
    /**
     * Switch ImageView use Class
     */
    class Switch extends ImageView {
        public Switch(Context context) {
            super(context);
        }
    }

    public interface OnSwitchChangeListener {
        void onSwitchChange(boolean isSwitch);
    }
}
