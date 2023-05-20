package com.efunhub.ticktok.services;

import com.efunhub.ticktok.model.Click_Video_Model;
import com.efunhub.ticktok.model.LikeVideo_Model.Like_Video_Model;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface Video_Click_Service {
    @POST("add_counts")
    @FormUrlEncoded
    Call<Click_Video_Model> click_video_model(@Field("id") String id,
                                              @Field("user_auto_id") String user_auto_id,
                                              @Field("campaign_auto_id") String campaign_auto_id,
                                              @Field("campaign_user_auto_id") String campaign_user_auto_id,
                                              @Field("video_auto_id") String video_auto_id,
                                              @Field("click") String click,
                                              @Field("view") String view,
                                              @Field("impresson") String impresson);
}
