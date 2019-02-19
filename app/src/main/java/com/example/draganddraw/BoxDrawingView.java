package com.example.draganddraw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;

public class BoxDrawingView extends View {
    private static final String TAG = "BoxDrawingView";
    private static final String KEY_SUPER_STATE = "super state";
    private static final String KEY_CURRENT_BOX = "current box";
    private static final String KEY_BOXES = "boxes";
    private Box mCurrentBox;
    private List<Box> mBoxes = new ArrayList<>();
    private Paint mBoxPaint;
    private Paint mBackgroundPaint;
    private int mFirstId = -1;

    // Used when creating the view in code
    public BoxDrawingView(Context context) {
        this(context, null);
    }

    // Used when inflating the view from XML
    public BoxDrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // Paint the boxes a nice semitransparent red (ARGB)
        mBoxPaint = new Paint();
        mBoxPaint.setColor(0x22ff0000);
        // Paint the background off-white
        mBackgroundPaint = new Paint();
        mBackgroundPaint.setColor(0xfff8efe0);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int pointerCount = event.getPointerCount();
        if (pointerCount > 2 || (mCurrentBox != null && mCurrentBox.isAngleSet())) {
            return true;
        }
        PointF current;
        int actionMasked = event.getActionMasked();
        switch (actionMasked) {
            case MotionEvent.ACTION_DOWN:
                int pointerIndex = event.getActionIndex();
                current = new PointF(event.getX(pointerIndex), event.getY(pointerIndex));
                mFirstId = event.getPointerId(pointerIndex);
                mCurrentBox = new Box(current);
                mBoxes.add(mCurrentBox);
                break;
            case MotionEvent.ACTION_MOVE:
                if (mCurrentBox == null) {
                    return true;
                }
                for (int index = 0; index < pointerCount; index++) {
                    int id = event.getPointerId(index);
                    current = new PointF(event.getX(index), event.getY(index));
                    if (id == mFirstId) {
                        mCurrentBox.setCurrent(current);
                        invalidate();
                    } else {
                        double height = Math.abs(current.y - mCurrentBox.getOrigin().y);
                        double hypLength = Math.hypot(
                                current.x - mCurrentBox.getOrigin().x,
                                current.y - mCurrentBox.getOrigin().y
                        );
                        double angle = 180 * Math.asin(height / hypLength) / Math.PI;
                        mCurrentBox.setAngle(angle);
                        invalidate();
                    }
                }
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                break;
            case MotionEvent.ACTION_POINTER_UP:
                if (mCurrentBox != null) {
                    mCurrentBox.setAngleSet(true);
                    mCurrentBox = null;
                }
                break;
            default:
                mCurrentBox = null;
                break;
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // Fill the background
        canvas.drawPaint(mBackgroundPaint);
        for (Box box : mBoxes) {
            double angle = box.getAngle();
            canvas.rotate((float) angle, box.getOrigin().x, box.getOrigin().y);
            float left = Math.min(box.getOrigin().x, box.getCurrent().x);
            float right = Math.max(box.getOrigin().x, box.getCurrent().x);
            float top = Math.min(box.getOrigin().y, box.getCurrent().y);
            float bottom = Math.max(box.getOrigin().y, box.getCurrent().y);
            canvas.drawRect(left, top, right, bottom, mBoxPaint);
            canvas.rotate((float) -angle, box.getOrigin().x, box.getOrigin().y);
        }
    }

    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle outState = new Bundle();
        outState.putParcelable(KEY_SUPER_STATE, super.onSaveInstanceState());
        outState.putParcelable(KEY_CURRENT_BOX, mCurrentBox);
        outState.putParcelableArrayList(KEY_BOXES, (ArrayList<? extends Parcelable>) mBoxes);
        return outState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        Bundle inState = (Bundle) state;
        super.onRestoreInstanceState(inState.getParcelable(KEY_SUPER_STATE));
        mCurrentBox = inState.getParcelable(KEY_CURRENT_BOX);
        mBoxes = inState.getParcelableArrayList(KEY_BOXES);
    }
}
