package com.example.konstantin.qiwi.UI;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.konstantin.qiwi.Dagger.DependencyInjector;
import com.example.konstantin.qiwi.Presenter.FormFragmentPresenter;
import com.example.konstantin.qiwi.R;

import javax.inject.Inject;

/**
 * Created by Konstantin on 08.12.2017.
 */

public class FormFragment extends Fragment {

    @Inject FormFragmentPresenter presenter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_form, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        DependencyInjector.getComponent().inject(this);

        presenter.attachView(this);
        presenter.getUIData();
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.attachView(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.detachView();
    }

    @Override
    public void onDestroy() {
        presenter.clearCompositeDisposable();
        super.onDestroy();
    }
}
