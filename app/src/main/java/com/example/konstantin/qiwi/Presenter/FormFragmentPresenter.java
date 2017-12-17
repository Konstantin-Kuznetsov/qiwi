package com.example.konstantin.qiwi.Presenter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.konstantin.qiwi.Dagger.DependencyInjector;
import com.example.konstantin.qiwi.Model.DataManager;
import com.example.konstantin.qiwi.POJO.Content;
import com.example.konstantin.qiwi.POJO.Element;
import com.example.konstantin.qiwi.R;
import com.example.konstantin.qiwi.UI.FormFragment;

import java.lang.ref.WeakReference;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.ResponseBody;

/**
 *  Презентер для фрагмента с формой.
 *
 * Created by Konstantin on 17.12.2017.
 */

public class FormFragmentPresenter {

    @Inject DataManager dataManager;
    @Inject Context context;

    private WeakReference<FormFragment> bindedView;
    private final String TAG = "qiwi_test_task";

    private RecyclerView recyclerForm;

    private List<Element> test; // тест как распарсится

    public FormFragmentPresenter() {
        DependencyInjector.getComponent().inject(this);
    }

    // загрузка данных с описанием UI
    public void getUIData() {
        dataManager.getFormData(getDataObserver());
    }


    // Прикрепление и открепление активити в зависимости от ЖЦ
    public void attachView(@NonNull FormFragment view) {
        bindedView = new WeakReference<FormFragment>(view);
        if (bindedView.get() != null) {
            recyclerForm = bindedView.get().getView().findViewById(R.id.form_validation_recycler);
        }
    }

    public void detachView() {
        bindedView = null;
    }

    // Observer данных для герерации UI
    private Observer<List<Element>> getDataObserver() {
        return new Observer<List<Element>>() {
            @Override
            public void onSubscribe(Disposable d) {
                //errorText.setVisibility(View.GONE);
                //reloadButton.setVisibility(View.GONE);

                Log.i(TAG, context.getString(R.string.onSubscribe_UIData));
            }

            @Override
            public void onNext(List<Element> elements) {
                // обновление списка новостей в UI
                if (bindedView != null) {
                    recyclerForm.setVisibility(View.VISIBLE);

                    // TODO: формируем и отображаем UI в RecyclerView
                    Log.i(TAG, context.getString(R.string.onNext_UIData));

                    test = elements;
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, e.getLocalizedMessage());
                if (bindedView != null && bindedView.get().getView() != null) {
                    //recyclerForm.setVisibility(View.INVISIBLE);

                    // TODO: скрываем RecyclerView, показываем ошибку и кнопку перезагрузки

                    //errorText.setVisibility(View.VISIBLE);
                    //errorText.setText(e.getLocalizedMessage());
                    //reloadButton.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onComplete() {
                Log.i(TAG, context.getString(R.string.onComplete_UIData));
            }
        };
    }
}
