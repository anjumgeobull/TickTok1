package com.efunhub.ticktok.CampaignModel;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CampaignModelData {
    @SerializedName("status")
    private int status;

    @SerializedName("msg")
    private String msg;

    @SerializedName("data")
    private List<Data> dataList;

    public int getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public List<Data> getDataList() {
        return dataList;
    }

    public static class Data {
        @SerializedName("_id")
        private String id;

        @SerializedName("user_auto_id")
        private String userAutoId;

        @SerializedName("isVideo")
        private String isVideo;

        @SerializedName("frequency")
        private String frequency;

        @SerializedName("campaign_name")
        private String campaignName;

        @SerializedName("campaign_type")
        private String campaignType;

        @SerializedName("campaign_limit")
        private String campaignLimit;

        @SerializedName("start_date")
        private String startDate;

        @SerializedName("end_date")
        private String endDate;

        @SerializedName("contry")
        private String country;

        @SerializedName("state")
        private String state;

        @SerializedName("area")
        private String area;

        @SerializedName("gender")
        private String gender;

        @SerializedName("age")
        private String age;

        @SerializedName("links")
        private String links;

        @SerializedName("status")
        private String status;

        @SerializedName("isImage")
        private String isImage;

        @SerializedName("type_of_campaign")
        private String typeOfCampaign;

        @SerializedName("images")
        private String images;

        @SerializedName("videos")
        private String videos;

        @SerializedName("updated_at")
        private String updatedAt;

        @SerializedName("created_at")
        private String createdAt;

        @SerializedName("Sikipable")
        private String sikipable;

        // Generate getters for all the fields

        public String getId() {
            return id;
        }

        public String getUserAutoId() {
            return userAutoId;
        }

        public String getIsVideo() {
            return isVideo;
        }

        public String getFrequency() {
            return frequency;
        }

        public String getCampaignName() {
            return campaignName;
        }

        public String getCampaignType() {
            return campaignType;
        }

        public String getCampaignLimit() {
            return campaignLimit;
        }

        public String getStartDate() {
            return startDate;
        }

        public String getEndDate() {
            return endDate;
        }

        public String getCountry() {
            return country;
        }

        public String getState() {
            return state;
        }

        public String getArea() {
            return area;
        }

        public String getGender() {
            return gender;
        }

        public String getAge() {
            return age;
        }

        public String getLinks() {
            return links;
        }

        public String getStatus() {
            return status;
        }

        public String getIsImage() {
            return isImage;
        }

        public String getTypeOfCampaign() {
            return typeOfCampaign;
        }

        public String getImages() {
            return images;
        }

        public String getVideos() {
            return videos;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public String getSikipable() {
            return sikipable;
        }
    }
}
