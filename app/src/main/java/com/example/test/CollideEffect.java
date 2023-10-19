package com.example.test;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class CollideEffect {

    private final FrameLayout container;
    private final Handler handler = new Handler();

    public CollideEffect(Context context, FrameLayout container){
        this.container = container;
    }

    public void collideEffect(int x, int y, Drawable drawable,long duration){
        ImageView imageView = new ImageView(container.getContext());
        imageView.setImageDrawable(drawable);

        int width = 350;
        int height = 350;

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                width, // エフェクトの幅
                height // エフェクトの高さ
        );

        // エフェクトの中心位置を設定
        params.leftMargin = x - (width / 2);
        params.topMargin = y - (height / 2);

        imageView.setLayoutParams(params);

        container.addView(imageView);

        // フェードアウトアニメーションを追加
        AlphaAnimation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setDuration(duration); // フェードアウトにかける時間
        imageView.startAnimation(fadeOut);

        handler.postDelayed(()-> hideEffect(imageView),duration);
    }

    public void hideEffect(ImageView imageView) {
        container.removeView(imageView);
    }

}
