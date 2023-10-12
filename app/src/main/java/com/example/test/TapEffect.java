package com.example.test;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class TapEffect{
    private Context context;
    private ViewGroup parentView;

    public TapEffect(Context context, ViewGroup parentView){
        this.context = context;
        this.parentView = parentView;
    }

    public void show(float x, float y){
        // エフェクト用のImageViewを作成
        ImageView effectView = new ImageView(context);
        Drawable effectDrawable = context.getResources().getDrawable(R.drawable.tap_effect); // エフェクトの画像を設定
        effectView.setX(x-40);
        effectView.setY(y-80);
        effectView.setImageDrawable(effectDrawable);

        // エフェクトの位置を設定
        int initialSize = 200;
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(initialSize, initialSize);
        effectView.setLayoutParams(layoutParams);

        // エフェクトを追加
        parentView.addView(effectView);

        // アニメーションの設定
        int finalSize = 100; // 最終的なサイズを設定
        ValueAnimator widthAnimator = ValueAnimator.ofInt(initialSize, finalSize);
        ValueAnimator heightAnimator = ValueAnimator.ofInt(initialSize, finalSize);

        widthAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int value = (int) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = effectView.getLayoutParams();
                layoutParams.width = value;
                effectView.setLayoutParams(layoutParams);
            }
        });

        heightAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int value = (int) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = effectView.getLayoutParams();
                layoutParams.height = value;
                effectView.setLayoutParams(layoutParams);
            }
        });

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(widthAnimator, heightAnimator);
        animatorSet.setDuration(300); // アニメーションの時間（ミリ秒）
        animatorSet.start();

// エフェクトのアニメーション（例：フェードアウト）        // エフェクトにアニメーションを適用
//        effectView.startAnimation(animatorSet);
        effectView.animate().alpha(0).setDuration(300).withEndAction(new Runnable() {
            @Override
            public void run() {
                // エフェクトをコンテナから削除
                parentView.removeView(effectView);
            }
        });
    }

}

