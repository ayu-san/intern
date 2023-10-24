package game.intern.test;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class CollideEffect {

    private final FrameLayout container;
    private final Handler handler = new Handler();

    public CollideEffect(FrameLayout container){
        this.container = container;
    }

    public void collideEffect(int x, int y, Drawable drawable,int width,int height,long duration){
        ImageView imageView = new ImageView(container.getContext());
        imageView.setImageDrawable(drawable);

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
        imageView.setAlpha(1.0f);
        ValueAnimator fadeOut = ObjectAnimator.ofFloat(imageView, "alpha", 1.0f, 0.0f);
        fadeOut.setDuration(duration);
        fadeOut.start();

        handler.postDelayed(()-> hideEffect(imageView),duration);
    }

    public void hideEffect(ImageView imageView) {
        container.removeView(imageView);
    }

}
