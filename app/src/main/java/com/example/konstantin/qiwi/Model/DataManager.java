package com.example.konstantin.qiwi.Model;

import android.content.Context;
import android.util.Log;

import com.example.konstantin.qiwi.Dagger.DependencyInjector;
import com.example.konstantin.qiwi.POJO.Element;
import com.example.konstantin.qiwi.POJO.ResponseJSON;
import com.example.konstantin.qiwi.R;
import com.google.gson.Gson;


import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 *  Получает данные от сервера и возвращает в виде Observable презентеру
 *
 * Created by Konstantin on 13.12.2017.
 */

public class DataManager {

    @Inject Context context;
    @Inject Gson gson;
    @Inject OkHttpClient okHttpClient;

    private static final String TAG = "qiwi_test_task";

    public DataManager() {
        DependencyInjector.getComponent().inject(this);
    }

    // подписка переданного Observer на данные с сервера
    public void getFormData(Observer<List<Element>> formDataObserver) {

        Request request = new Request.Builder()
                .url("https://w.qiwi.com/mobile/form/form.json")
                .build();

        // асинхронно качаем файл с сервера
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, context.getString(R.string.file_loader_bad) + e.getLocalizedMessage());
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {

                    Log.i(TAG, context.getString(R.string.file_loader_ok));
                    final String resp = response.body().string();

                    // подписываем переданный Observable на разобранный json
                    Observable.just(parseJSON(resp)) // парсер
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(formDataObserver);
                }
            }
            });
    }

    private List<Element> parseJSON(String jsonString) {

        List<Element> allElements;

        ResponseJSON responseJSON = gson.fromJson(jsonString, ResponseJSON.class);

        // добавляем к списку все элементы кроме первого служебного
        allElements = responseJSON.getContent().getElements();
        allElements.remove(0);

        return allElements;
    }
}
