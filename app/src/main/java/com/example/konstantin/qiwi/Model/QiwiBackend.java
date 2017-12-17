package com.example.konstantin.qiwi.Model;

import com.example.konstantin.qiwi.POJO.Content;
import com.example.konstantin.qiwi.POJO.Element;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Konstantin on 17.12.2017.
 */

public class QiwiBackend {
    public interface QiwiInterface {
        @GET("form/form.json") // запрос структуры формы для отображения UI
        Call<ResponseBody> getFormStructure();
    }
}
