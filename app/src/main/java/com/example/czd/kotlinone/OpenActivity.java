package com.example.czd.kotlinone;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;
import android.widget.Toolbar;

public class OpenActivity extends AppCompatActivity {
    View decorView;
    float downX, downY;
    float screenWidth, screenHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open);

        // setContentView(View view)方法会把view放到bgLayout中。
        //setContentView(R.layout.layout_second);

        // 获得decorView
        decorView = getWindow().getDecorView();

        // 获得手机屏幕的宽度和高度，单位像素
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenWidth = metrics.widthPixels;
        screenHeight = metrics.heightPixels;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            //设置修改状态栏
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //设置状态栏的颜色，和你的app主题或者标题栏颜色设置一致就ok了
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

    }


    /**
     * 通过重写该方法，对触摸事件进行处理
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            downX = event.getX();

        }else if(event.getAction() == MotionEvent.ACTION_MOVE){
            float moveDistanceX = event.getX() - downX;
            if(moveDistanceX > 0){
                decorView.setX(moveDistanceX);
            }

        }else if(event.getAction() == MotionEvent.ACTION_UP){
            float moveDistanceX = event.getX() - downX;
            if (moveDistanceX>0) {
                if (moveDistanceX > screenWidth / 3) {
                    // 如果滑动的距离超过了手机屏幕的一半, 滑动处屏幕后再结束当前Activity
                    continueMove(moveDistanceX);
                } else {
                    // 如果滑动距离没有超过一半, 往回滑动
                    rebackToLeft(moveDistanceX);
                }
            }
        }
        return super.onTouchEvent(event);
    }

    /**
     * 从当前位置一直往右滑动到消失。
     * 这里使用了属性动画。
     */
    private void continueMove(float moveDistanceX){
        // 从当前位置移动到右侧。
        ValueAnimator anim = ValueAnimator.ofFloat(moveDistanceX, screenWidth);
        anim.setDuration(200); // 一秒的时间结束, 为了简单这里固定为1秒
        anim.start();

        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // 位移
                float x = (float) (animation.getAnimatedValue());
                decorView.setX(x);
            }
        });

        // 动画结束时结束当前Activity
        anim.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationEnd(Animator animation) {
                finish();
                overridePendingTransition(0, 0);
            }

        });
    }

    /**
     * Activity被滑动到中途时，滑回去~
     */
    private void rebackToLeft(float moveDistanceX){
        ObjectAnimator.ofFloat(decorView, "X", moveDistanceX, 0).setDuration(300).start();
    }



}
