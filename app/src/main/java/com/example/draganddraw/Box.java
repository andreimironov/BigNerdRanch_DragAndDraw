package com.example.draganddraw;

import android.graphics.PointF;
import android.os.Parcel;
import android.os.Parcelable;

public class Box implements Parcelable {
    private PointF mOrigin;
    private PointF mCurrent;
    private double mAngle = 0;
    private boolean isAngleSet = false;

    public Box(PointF origin) {
        mOrigin = origin;
        mCurrent = origin;
    }

    public void setAngle(double angle) {
        mAngle = angle;
    }

    public double getAngle() {
        return mAngle;
    }

    public boolean isAngleSet() {
        return isAngleSet;
    }

    public void setAngleSet(boolean angleSet) {
        this.isAngleSet = angleSet;
    }

    public PointF getCurrent() {
        return mCurrent;
    }

    public void setCurrent(PointF current) {
        mCurrent = current;
    }

    public PointF getOrigin() {
        return mOrigin;
    }

    protected Box(Parcel in) {
        mOrigin = in.readParcelable(PointF.class.getClassLoader());
        mCurrent = in.readParcelable(PointF.class.getClassLoader());
    }

    public static final Creator<Box> CREATOR = new Creator<Box>() {
        @Override
        public Box createFromParcel(Parcel in) {
            return new Box(in);
        }

        @Override
        public Box[] newArray(int size) {
            return new Box[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(mOrigin, flags);
        dest.writeParcelable(mCurrent, flags);
    }
}
