package xyz.developerbab.smartvoteapp.interfaces;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface RESTApiInterface {

    // create user
    @POST("vote/registration/check")
    Call<ResponseBody> checknid(@Body Map<String, String> obj);


    // register for voting
    @POST("population/register")
    Call<ResponseBody> registerforvote(@Body Map<String, String> obj);

    // register for voting
    @POST("population/login")
    Call<ResponseBody> loginforvote(@Body Map<String, String> obj);

    //getupcomingelection
    @POST("mobile/get/candidate")
    Call<String> getallupcomingel();

    // vote
    @POST("mobile/post/voting")
    Call<ResponseBody> vote(@Body Map<String, String> obj);

}
