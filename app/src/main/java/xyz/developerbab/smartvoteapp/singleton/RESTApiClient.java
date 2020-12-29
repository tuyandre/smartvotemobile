package xyz.developerbab.smartvoteapp.singleton;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import xyz.developerbab.smartvoteapp.interfaces.RESTApiInterface;

public class RESTApiClient {

    private static final String BASE_URL = "http://nida.developerbab.xyz/api/";
    private static RESTApiClient apiClient;
    private static Retrofit retrofit;

    public RESTApiClient() {
        retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static synchronized RESTApiClient getInstance() {
        if (apiClient == null) {
            apiClient = new RESTApiClient();
        }
        return apiClient;
    }

    public RESTApiInterface getApi() {
        return retrofit.create(RESTApiInterface.class);
    }
}