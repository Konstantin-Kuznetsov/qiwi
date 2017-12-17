package com.example.konstantin.qiwi.Model;

import com.example.konstantin.qiwi.POJO.Content;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;

/**
 * Created by Konstantin on 17.12.2017.
 */

public class QiwiBackend {
    public interface QiwiInterface {
        @GET("form/form.json") // запрос структуры формы для отображения UI
        Observable<Content> getFormStructure();
    }
}
