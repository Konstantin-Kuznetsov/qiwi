package com.example.konstantin.qiwi.Model;

import android.util.Log;

import com.example.konstantin.qiwi.POJO.Content;
import com.example.konstantin.qiwi.POJO.Element;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 *  Получает данные от сервера и возвращает в виде Observable
 *
 * Created by Konstantin on 13.12.2017.
 */

public class DataResolver {

    private static final String QIWI_URL = "https://w.qiwi.com/mobile/form/form.json";
    private static final String TAG = "qiwi_test_task";

    private Content content; // содержимое json

    // список ВСЕХ элементов(описание View) из переданного json
    private List<Element> allElements = new ArrayList<>();

    public Observable<List<Element>> getData() {

        Observable<List<Element>> listObservable;

        try {
            String jsonString = new String(getUrlBytes(QIWI_URL));

            // преобразование JSON -> POJO и обертка в Observable

            listObservable = Observable.just(parseJSON(jsonString))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
            //
        } catch (IOException io) {
            Log.e(TAG, "Ошибка ввода/вывода ", io);
        }

    }

    private byte[] getUrlBytes(String url) throws IOException {

        URL qiwiUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) qiwiUrl.openConnection();

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();

            // если с подключением все не ОК, формируем и пробрасываем ошибку
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(connection.getResponseMessage() + ": with " + qiwiUrl);
            }

            // читаем из потока блоками по 1024 байта
            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray(); // метод возвращает поток, преобразованный в массив байтов
        } finally {
            connection.disconnect(); // освобождение ресурса
        }

    }

    private List<Element> parseJSON(String jsonString) {
        Gson gson = new GsonBuilder().create();
        Content content = gson.fromJson(jsonString, Content.class);

        // добавляем к списку все элементы кроме первого служебного
        allElements.addAll(content.getElements());
        allElements.remove(0);

        return allElements;
    }
}
