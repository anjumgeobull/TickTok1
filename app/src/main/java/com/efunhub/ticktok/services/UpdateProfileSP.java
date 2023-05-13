package com.efunhub.ticktok.services;

import android.content.Context;

import com.efunhub.ticktok.retrofit.APICallback;
import com.efunhub.ticktok.retrofit.APIServiceFactory;
import com.efunhub.ticktok.retrofit.BaseServiceResponseModel;
import com.efunhub.ticktok.retrofit.ErrorUtils;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateProfileSP {
    private  final UpdateProfileService updateProfileService;

    public UpdateProfileSP(Context context) {
        updateProfileService = APIServiceFactory.createService(UpdateProfileService.class, context);
    }

    public  void CallUpdateProfile(RequestBody id, RequestBody mobile_number,
                                   RequestBody name, RequestBody email, MultipartBody.Part profile_picture, RequestBody district, RequestBody state,
                                   RequestBody country, RequestBody insta_id, RequestBody facebook_id, RequestBody youtube_id,
                                   RequestBody other, final APICallback apiCallback)
    {
        Call<BaseServiceResponseModel> call = null;
        call = updateProfileService.updateProfile(id,mobile_number,name,email,profile_picture,district,state,country,insta_id,facebook_id,youtube_id,other);
        String url = call.request().url().toString();

        call.enqueue(new Callback<BaseServiceResponseModel>() {
            @Override
            public void onResponse(Call<BaseServiceResponseModel> call, Response<BaseServiceResponseModel> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getStatus() == 1) {
                    apiCallback.onSuccess(response.body());
                }
                else  if (response.isSuccessful() && response.body() != null && response.body().getStatus() == 0) {
                    apiCallback.onSuccess(response.body());
                }else {
                    BaseServiceResponseModel model = ErrorUtils.parseError(response);
                    apiCallback.onFailure(model, response.errorBody());
                    // apiCallback.onFailure(response.body(), response.errorBody());
                }
            }
            @Override
            public void onFailure(Call<BaseServiceResponseModel> call, Throwable t) {
                apiCallback.onFailure(null, null);
            }
        });
    }
}
