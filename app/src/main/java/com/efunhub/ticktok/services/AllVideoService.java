package com.efunhub.ticktok.services;



import com.efunhub.ticktok.model.AllVideoModel;
import com.efunhub.ticktok.model.RegistrationModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface AllVideoService {
    //////////////////////////////all Videos Api Call////////////////////////
    @POST("all-videos")
    @FormUrlEncoded
    Call<AllVideoModel> allVideos(@Field("login_user_id") String login_user_id,@Field("limit") int limit,@Field("index")int index);
}
