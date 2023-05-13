package com.efunhub.ticktok.services;


import com.efunhub.ticktok.retrofit.BaseServiceResponseModel;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface UpdateProfileWithoutPicService {

    @POST("Update-Profile-User")
    @Multipart
    Call<BaseServiceResponseModel> updateProfilewithoutpic(
                                                 @Part("user_id") RequestBody id,
                                                 @Part("mobile") RequestBody mobile_number,
                                                 @Part("name") RequestBody name,
                                                 @Part("email") RequestBody email,
                                                 @Part("district") RequestBody district,
                                                 @Part("state") RequestBody state,
                                                 @Part("country") RequestBody country,
                                                 @Part("insta_id") RequestBody insta_id,
                                                 @Part("facebook_id") RequestBody facebook_id,
                                                 @Part("youtube_id") RequestBody youtube_id,
                                                 @Part("other") RequestBody other
                                                 );
}
