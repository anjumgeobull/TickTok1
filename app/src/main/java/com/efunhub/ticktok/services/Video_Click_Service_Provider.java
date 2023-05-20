package com.efunhub.ticktok.services;

import android.content.Context;

import com.efunhub.ticktok.model.Click_Video_Model;
import com.efunhub.ticktok.model.LikeVideo_Model.Like_Video_Model;
import com.efunhub.ticktok.retrofit.APICallback;
import com.efunhub.ticktok.retrofit.APIServiceFactory;
import com.efunhub.ticktok.retrofit.BaseServiceResponseModel;
import com.efunhub.ticktok.retrofit.ErrorUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Video_Click_Service_Provider {
    private  final Video_Click_Service click_service;

    public Video_Click_Service_Provider(Context context) {
        click_service = APIServiceFactory.createService(Video_Click_Service.class, context);
    }

    public  void CallGetAllVideoLikes(String id,String user_id,String campaign_auto_id, String campaign_user_auto_id,String video_auto_id,String click,String view,String impression, final APICallback apiCallback)
    {
        //System.out.println("param"+user_id+"  "+video_id);
        Call<Click_Video_Model> call = null;
        call = click_service.click_video_model(id,user_id, campaign_auto_id, campaign_user_auto_id,video_auto_id,click,view,impression);
        String url = call.request().url().toString();

        call.enqueue(new Callback<Click_Video_Model>() {
            @Override
            public void onResponse(Call<Click_Video_Model> call, Response<Click_Video_Model> response) {
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
            public void onFailure(Call<Click_Video_Model> call, Throwable t) {
                apiCallback.onFailure(null, null);
            }
        });
    }
}
