package com.example.test;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class TapEffect{
    private final Context context;
    private final ViewGroup parentView;

    public TapEffect(Context context, ViewGroup parentView){
        this.context = context;
        this.parentView = parentView;
    }

    public void show(float x, float y){
        // エフェクト用のImageViewを作成
        ImageView effectView = new ImageView(context);
        Drawable effectDrawable = context.getResources().getDrawable(R.drawable.tap_effect, context.getTheme()); // エフェクトの画像を設定
        effectView.setX(x-130);
        effectView.setY(y-140);
        effectView.setImageDrawable(effectDrawable);

        // エフェクトの位置を設定
        int initialSize = 250;
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(initialSize, initialSize);
        effectView.setLayoutParams(layoutParams);

        // エフェクトを追加
        parentView.addView(effectView);

        // アニメーションの設定
        int finalSize = 200; // 最終的なサイズを設定
        ValueAnimator widthAnimator = ValueAnimator.ofInt(initialSize, finalSize);
        ValueAnimator heightAnimator = ValueAnimator.ofInt(initialSize, finalSize);

        widthAnimator.addUpdateListener(valueAnimator -> {
            int value = (int) valueAnimator.getAnimatedValue();
            ViewGroup.LayoutParams layoutParams1 = effectView.getLayoutParams();
            layoutParams1.width = value;
            effectView.setLayoutParams(layoutParams1);
        });

        heightAnimator.addUpdateListener(valueAnimator -> {
            int value = (int) valueAnimator.getAnimatedValue();
            ViewGroup.LayoutParams layoutParams12 = effectView.getLayoutParams();
            layoutParams12.height = value;
            effectView.setLayoutParams(layoutParams12);
        });

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(widthAnimator, heightAnimator);
        animatorSet.setDuration(300); // アニメーションの時間（ミリ秒）
        animatorSet.start();

// エフェクトのアニメーション（例：フェードアウト）        // エフェクトにアニメーションを適用
//        effectView.startAnimation(animatorSet);
        effectView.animate().alpha(0).setDuration(300).withEndAction(() -> {
            // エフェクトをコンテナから削除
            parentView.removeView(effectView);
        });
    }

}

