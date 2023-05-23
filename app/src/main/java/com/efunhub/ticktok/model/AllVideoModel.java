package com.efunhub.ticktok.model;

import com.efunhub.ticktok.retrofit.BaseServiceResponseModel;

import java.util.ArrayList;
import java.util.List;

public  class AllVideoModel extends BaseServiceResponseModel {

    private ArrayList<Data> data;

    public ArrayList<Data> getData() {
        return data;
    }

    public void setData(ArrayList<Data> data) {
        this.data = data;
    }

    public static class Data {
        private int self_like;
        private int total_shares;
        private int total_views;
        private int total_likes;
        private int total_comments;
        private String created_at;
        private String updated_at;
        private String video_caption;
        private String visibility;
        private String video;
        private String _id;
        private String user_id;
        private String name;
        private String profile_img;
        private String point;

        private String campaign_auto_id;

        private String campaign_user_auto_id;

        private String user_auto_id;
        
        private String campaign_name;

        private String isVideo;

        private String campaign_type;

        private String type_of_campaign;
        private String videos;

        private String type;

        private String links;

        private String Sikipable;

        public String getSikipable() {
            return Sikipable;
        }

        public void setSikipable(String sikipable) {
            Sikipable = sikipable;
        }

        public String getLinks() {
            return links;
        }

        public void setLinks(String links) {
            this.links= links;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type= type;
        }

        public String getCampaign_auto_id() {
            return campaign_auto_id;
        }

        public void setCampaign_auto_id(String campaign_auto_id_id) {
            this.campaign_auto_id= campaign_auto_id_id;
        }

        public String getCampaign_user_auto_id() {
            return campaign_user_auto_id;
        }

        public void setCampaign_user_auto_id(String campaign_user_auto_id) {
            this.campaign_user_auto_id = campaign_user_auto_id;
        }

        public String getType_of_campaign() {
            return type_of_campaign;
        }

        public void setType_of_campaign(String type_of_campaign) {
            this.type_of_campaign = type_of_campaign;
        }

        public String getUser_auto_id() {
            return user_auto_id;
        }

        public void setUser_auto_id(String user_auto_id) {
            this.user_auto_id = user_auto_id;
        }


        public String getCampaign_name() {
            return campaign_name;
        }

        public void setCampaign_name(String campaign_name) {
            this.campaign_name = campaign_name;
        }

        public String getIsVideo() {
            return isVideo;
        }

        public void setIsVideo(String isVideo) {
            this.isVideo = isVideo;
        }


        public String getCampaign_type() {
            return campaign_type;
        }

        public void setCampaign_type(String campaign_type) {
            this.campaign_type = campaign_type;
        }

        public String getcVideos() {
            return videos;
        }

        public void setcVideos(String videos) {
            this.videos = videos;
        }


        public String getPoint() {
            return point;
        }

        public void setPoint(String point) {
            this.point = point;
        }

        public int getTotal_comments() {
            return total_comments;
        }

        public void setTotal_comments(int total_comments) {
            this.total_comments = total_comments;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getProfile_image() {
            return profile_img;
        }

        public void setProfile_image(String profile_image) {
            this.profile_img = profile_image;
        }


        public int getSelf_like() {
            return self_like;
        }

        public void setSelf_like(int self_like) {
            this.self_like = self_like;
        }

        public int getTotal_shares() {
            return total_shares;
        }

        public void setTotal_shares(int total_shares) {
            this.total_shares = total_shares;
        }

        public int getTotal_views() {
            return total_views;
        }

        public void setTotal_views(int total_views) {
            this.total_views = total_views;
        }

        public int getTotal_likes() {
            return total_likes;
        }

        public void setTotal_likes(int total_likes) {
            this.total_likes = total_likes;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }

        public String getVideo_caption() {
            return video_caption;
        }

        public void setVideo_caption(String video_caption) {
            this.video_caption = video_caption;
        }

        public String getVisibility() {
            return visibility;
        }

        public void setVisibility(String visibility) {
            this.visibility = visibility;
        }

        public String getVideo() {
            return video;
        }

        public void setVideo(String video) {
            this.video = video;
        }

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }
    }

}
