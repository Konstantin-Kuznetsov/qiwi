package com.example.konstantin.qiwi.UI;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.konstantin.qiwi.R;

public class FormActivity extends AppCompatActivity {

    private FragmentManager fm;
    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        fm = getSupportFragmentManager();
        fragment = fm.findFragmentById(R.id.fragment_container);

        // если фрагмент еще не существует - создаем новый
        // иначе - загружаем уже подготовленный
        if (fragment == null) {
            fragment = createFormFragment();

            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }
    }

    private Fragment createFormFragment() {
        return new FormFragment();
    }
}
