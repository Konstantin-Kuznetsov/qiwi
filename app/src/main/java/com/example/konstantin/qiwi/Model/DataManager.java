package com.example.konstantin.qiwi.Model;

import android.content.Context;
import android.util.Log;

import com.example.konstantin.qiwi.Dagger.DependencyInjector;
import com.example.konstantin.qiwi.POJO.Content;
import com.example.konstantin.qiwi.POJO.Element;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
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
import okhttp3.ResponseBody;


/**
 *  Получает данные от сервера и возвращает в виде Observable презентеру
 *
 * Created by Konstantin on 13.12.2017.
 */

public class DataManager {

    @Inject Context context;
    @Inject QiwiBackend.QiwiInterface api;
    @Inject Gson gson;

    private static final String TAG = "qiwi_test_task";

    OkHttpClient client;

    public DataManager() {
        DependencyInjector.getComponent().inject(this);
        client = new OkHttpClient();
    }

    // подписка переданного Observer на данные с сервера
    public void getFormData(Observer<List<Element>> formDataObserver) {
        try {
            getElementsObservable()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(formDataObserver);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private Observable<List<Element>> getElementsObservable() throws IOException {
        String response = downloadFile("https://w.qiwi.com/mobile/form/form.json");

        return Observable.just(parseJSON(response));
    }


    private String downloadFile(String urlToDownload) throws IOException {

        Request request = new Request.Builder()
                .url(urlToDownload)
                .build();

        return client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });

    }



    private List<Element> parseJSON(String jsonString) {

        List<Element> allElements = new ArrayList<>();

        //gson = new GsonBuilder().create();
        Content content = gson.fromJson(jsonString, Content.class);

        // добавляем к списку все элементы кроме первого служебного
        allElements.addAll(content.getElements());
        allElements.remove(0);

        return allElements;
    }


//    private Observable<Content> getElementsObservable() {
//
//        Observable<Content> contentObservable = api.getFormStructure();
//
//        //Observable<List<Element>> elementsListObservable = contentObservable
//                //.concatMapIterable(Content::getElements)
//                //.map(Content::getElements);
//                //.skip(1); // пропуск первого Element со служебной информацией
//
//        //return elementsListObservable;
//        return contentObservable;
//    }


    //private Content content; // содержимое json

    //private static final String QIWI_URL = "https://w.qiwi.com/mobile/form/form.json";

    // список ВСЕХ элементов(описание View) из переданного json
    //private List<Element> allElements = new ArrayList<>();



//    public Observable<List<Element>> getData() {
//
//        Observable<List<Element>> listObservable;
//
//        try {
//            String jsonString = new String(getUrlBytes(QIWI_URL));
//
//            // преобразование JSON -> POJO и обертка в Observable
//
//            listObservable = Observable.just(parseJSON(jsonString))
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread());
//            //
//        } catch (IOException io) {
//            Log.e(TAG, "Ошибка ввода/вывода ", io);
//        }
//
//    }

//    private byte[] getUrlBytes(String url) throws IOException {
//
//        URL qiwiUrl = new URL(url);
//        HttpURLConnection connection = (HttpURLConnection) qiwiUrl.openConnection();
//
//        try {
//            ByteArrayOutputStream out = new ByteArrayOutputStream();
//            InputStream in = connection.getInputStream();
//
//            // если с подключением все не ОК, формируем и пробрасываем ошибку
//            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
//                throw new IOException(connection.getResponseMessage() + ": with " + qiwiUrl);
//            }
//
//            // читаем из потока блоками по 1024 байта
//            int bytesRead = 0;
//            byte[] buffer = new byte[1024];
//            while ((bytesRead = in.read(buffer)) > 0) {
//                out.write(buffer, 0, bytesRead);
//            }
//            out.close();
//            return out.toByteArray(); // метод возвращает поток, преобразованный в массив байтов
//        } finally {
//            connection.disconnect(); // освобождение ресурса
//        }
//
//    }
//
//    private List<Element> parseJSON(String jsonString) {
//        Gson gson = new GsonBuilder().create();
//        Content content = gson.fromJson(jsonString, Content.class);
//
//        // добавляем к списку все элементы кроме первого служебного
//        allElements.addAll(content.getElements());
//        allElements.remove(0);
//
//        return allElements;
//    }
}
