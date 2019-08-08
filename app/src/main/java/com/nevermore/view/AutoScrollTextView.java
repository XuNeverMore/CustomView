package com.nevermore.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.List;

/**
 * Create on 2019/8/8 16:10
 *
 * @author XuChuanting
 */
public class AutoScrollTextView extends View {

    private List<String> mSequenceList;
    private Paint mPaint;
    private int mCurrentIndex = -1;
    private long mInterval = 2000;
    private float mProgress;
    private ValueAnimator mValueAnimator;
    private Runnable mRunnableUpdate = new Runnable() {
        @Override
        public void run() {
            if (mSequenceList == null || mSequenceList.size() == 0) {
                return;
            }
            mCurrentIndex++;
            startOffsetAnim();
            postDelayed(this, mInterval);
        }
    };

    public AutoScrollTextView(Context context) {
        super(context);
        init();
    }

    public AutoScrollTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void startOffsetAnim() {
        if (mValueAnimator != null && mValueAnimator.isRunning()) {
            mValueAnimator.end();
        }
        mValueAnimator = ValueAnimator.ofFloat(0, 1);
        mValueAnimator.setDuration(mInterval / 2);
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mProgress = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        mValueAnimator.start();
    }

    private void init() {

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.BLACK);
        mPaint.setTextSize(48);

    }

    public List<String> getSequenceList() {
        return mSequenceList;
    }

    public void setSequenceList(List<String> sequenceList) {
        mSequenceList = sequenceList;
        mCurrentIndex = -1;
        mProgress = 0;
        post(mRunnableUpdate);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        removeCallbacks(mRunnableUpdate);
        if (mValueAnimator != null && mValueAnimator.isRunning()) {
            mValueAnimator.cancel();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {

        if (mSequenceList == null) {
            return;
        }
        int size = mSequenceList.size();
        if (size == 0) {
            return;
        }
        int measuredHeight = getMeasuredHeight();

        canvas.save();
        canvas.translate(0, mProgress * measuredHeight);

        Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
        float y = measuredHeight / 2f - (fontMetrics.descent + fontMetrics.ascent) / 2f;

        int preIndex = (mCurrentIndex - 1) % size;
        if (preIndex >= 0) {
            CharSequence charSequenceNext = mSequenceList.get(preIndex);
            canvas.drawText(charSequenceNext, 0, charSequenceNext.length(), 0, y, mPaint);
        }

        int currentTextIndex = mCurrentIndex % size;
        if (currentTextIndex >= 0) {
            CharSequence charSequence = mSequenceList.get(currentTextIndex);
            canvas.drawText(charSequence, 0, charSequence.length(), 0, y - measuredHeight, mPaint);
        }

        canvas.restore();
    }


    public int getCurrentTextIndex() {
        if (mSequenceList == null || mSequenceList.size() == 0) {
            return -1;
        }
        return mCurrentIndex % mSequenceList.size();
    }

    public String getCurrentText() {
        int currentTextIndex = getCurrentTextIndex();
        if (currentTextIndex == -1) {
            return null;
        }
        return mSequenceList.get(currentTextIndex);
    }

}
