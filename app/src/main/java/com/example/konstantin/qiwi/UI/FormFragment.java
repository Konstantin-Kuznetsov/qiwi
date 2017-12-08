package com.example.konstantin.qiwi.UI;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.konstantin.qiwi.R;

/**
 * Created by Konstantin on 08.12.2017.
 */

public class FormFragment extends Fragment {

    private final String TAG = "qiwi_form_validator";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "New form fragment has been created.");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_form, container, false);

        return view;
    }
}
