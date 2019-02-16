package com.example.draganddraw;

import androidx.fragment.app.Fragment;

import android.os.Bundle;

public class DragAndDrawActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return DragAndDrawFragment.newInstance();
    }
}
