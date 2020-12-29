package xyz.developerbab.smartvoteapp.singleton;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import xyz.developerbab.smartvoteapp.interfaces.RESTApiInterface;

public class RESTCl {

    private static final String BASE_URL = "http://vote.developerbab.xyz/api/";
    private static RESTCl apiClient;
    private static Retrofit retrofit;

    public RESTCl() {
        retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static synchronized RESTCl getInstance() {
        if (apiClient == null) {
            apiClient = new RESTCl();
        }
        return apiClient;
    }

    public RESTApiInterface getApi() {
        return retrofit.create(RESTApiInterface.class);
    }
}