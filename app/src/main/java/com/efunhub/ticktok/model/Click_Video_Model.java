
package com.efunhub.ticktok.model;

import com.efunhub.ticktok.retrofit.BaseServiceResponseModel;

import java.util.ArrayList;


public class Click_Video_Model extends BaseServiceResponseModel {
    private ArrayList<Click_Video_Model.Data> data;

    public ArrayList<Data> getData() {
        return data;
    }

    public void setData(ArrayList<Data> data) {
        this.data = data;
    }

    public static class Data {
        private String id;
        private String user_auto_id;
        private String campaign_auto_id;
        private String campaign_user_auto_id;
        private String video_auto_id;
        private String click;
        private String view;
        private String impresson;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUser_auto_id() {
            return user_auto_id;
        }

        public void setUser_auto_id(String user_auto_id) {
            this.user_auto_id = user_auto_id;
        }

        public String getCampaign_auto_id() {
            return campaign_auto_id;
        }

        public void setCampaign_auto_id(String campaign_auto_id) {
            this.campaign_auto_id = campaign_auto_id;
        }

        public String getCampaign_user_auto_id() {
            return campaign_user_auto_id;
        }

        public void setCampaign_user_auto_id(String campaign_user_auto_id) {
            this.campaign_user_auto_id = campaign_user_auto_id;
        }

        public String getVideo_auto_id() {
            return video_auto_id;
        }

        public void setVideo_auto_id(String video_auto_id) {
            this.video_auto_id = video_auto_id;
        }

        public String getClick() {
            return click;
        }

        public void setClick(String click) {
            this.click = click;
        }

        public String getView() {
            return view;
        }

        public void setView(String view) {
            this.view = view;
        }

        public String getImpresson() {
            return impresson;
        }

        public void setImpresson(String impresson) {
            this.impresson = impresson;
        }

    }
}

